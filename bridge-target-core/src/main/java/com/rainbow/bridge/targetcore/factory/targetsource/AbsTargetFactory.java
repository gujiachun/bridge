package com.rainbow.bridge.targetcore.factory.targetsource;

import com.rainbow.bridge.targetcore.model.TargetConn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 抽象目标源链接工厂
 * @author gujiachun
 */
public abstract class AbsTargetFactory<T> implements TargetFactory<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbsTargetFactory.class);

    protected Map<Integer, T> targetMap;

    /**
     * 目标数据源类型
     * */
    protected String targetType;

    public AbsTargetFactory(String targetType){
        this.targetType = targetType;
    }

    @Override
    public void build(List<TargetConn> targetCons) {
        for (TargetConn targetConn : targetCons){
            initTarget(targetConn);
        }
    }

    @Override
    public void build(String taskId) {
        List<TargetConn> targetCons = getTargetConnByTaskId(taskId);
        if (targetCons == null || targetCons.isEmpty()){
            return;
        }
        for (TargetConn targetConn : targetCons){
            initTarget(targetConn);
        }
    }

    @Override
    public void initTarget(TargetConn targetConn) {
        if (targetMap == null){
            targetMap = new ConcurrentHashMap<>(10);
        }

        if (targetMap.containsKey(targetConn.getTargetId())){
            return;
        }
        logger.info("********************目标源 链接对象 初始化开始 targetId:{}···********************",targetConn.getTargetId());
        T o = initTargetConn(targetConn);
        logger.info("********************目标源 链接对象 初始化结束 targetId:{}···********************",targetConn.getTargetId());
        if (o == null){
            return;
        }
        targetMap.put(targetConn.getTargetId(), o);
    }


    @Override
    public T getTarget(Integer targetId) {
        if (targetMap == null){
            return null;
        }

        return targetMap.get(targetId);
    }

    @Override
    public void removeTarget(String taskId) {
        if (targetMap == null){
            return;
        }

        List<Integer> targetIds = getTargetsByTaskId(taskId);
        if (CollectionUtils.isEmpty(targetIds)){
            return;
        }

        for (Integer targetId : targetIds){
            if (targetMap.containsKey(targetId)){
                Object target = targetMap.get(targetId);
                logger.info("释放target client ,targetId:{}",targetId);
                targetMap.remove(targetId);
                target = null;
            }
        }
    }

    @Override
    public void release() {
        if (targetMap == null){
            return;
        }
        targetMap.clear();
    }

    @PreDestroy
    public void destroy(){
        release();
    }


    @Override
    public boolean support(String targetType) {
        if (this.targetType.equals(targetType)){
            return true;
        }
        return false;
    }

    /**
     * 根据任务id，获取 需要同步到哪些目标源targetId列表
     *@author gujiachun
     *@date 2021/11/17 5:49 下午
     *@param taskId
     *@return java.util.List<java.lang.Integer>
    */
    protected abstract List<Integer> getTargetsByTaskId(String taskId);

    /**
     * 根据任务id，获取 需要同步到哪些目标源target的链接配置
     *@author gujiachun
     *@date 2021/11/19 1:18 下午
     *@param taskId
     *@return java.util.List<com.rainbow.bridge.targetcore.adapter.model.target.TargetConn>
    */
    protected abstract List<TargetConn> getTargetConnByTaskId(String taskId);

    /**
     * 返回目标源链接对象
     *@author gujiachun
     *@date 2021/11/17 5:39 下午
     *@param targetConn 链接字符串 对象
     *@return java.lang.Object 返回的链接对象 如：DataSource、RedisService等
    */
    protected abstract T initTargetConn(TargetConn targetConn);
}
