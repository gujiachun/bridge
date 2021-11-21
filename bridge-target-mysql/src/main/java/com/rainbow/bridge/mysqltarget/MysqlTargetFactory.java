package com.rainbow.bridge.mysqltarget;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.targetcore.factory.targetsource.AbsTargetFactory;
import com.rainbow.bridge.targetcore.model.TargetConn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * mysql目标源类型 构建工厂
 * @author gujiachun
 */
public class MysqlTargetFactory extends AbsTargetFactory<DataSource> {

    private IService mysqlTaskRuleService;

    private IService mysqlTargetService;

    public MysqlTargetFactory(IService mysqlTaskRuleService, IService mysqlTargetService ,String targetType){
        super(targetType);
        this.mysqlTaskRuleService = mysqlTaskRuleService;
        this.mysqlTargetService = mysqlTargetService;
    }

    @Override
    protected DataSource initTargetConn(TargetConn targetConn) {
        Properties properties = PropertiesUtil.stringToProperties(targetConn.getProps());
        try {
            DataSource ds = DruidDataSourceFactory.createDataSource(properties);
            AbsTargetFactory.logger.info("初始DataSource成功,目标数据源:{}",targetConn.getTargetId());
            return ds;
        } catch (Exception e) {
            AbsTargetFactory.logger.error("初始DataSource发生异常，目标数据源:{},异常:{}",targetConn.getTargetId(),e.getMessage());
        }
        return null;
    }

    @Override
    protected List<Integer> getTargetsByTaskId(String taskId) {
        QueryWrapper<SyncTaskRuleMysqlEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntityList = mysqlTaskRuleService.list(wrapper);

        if (CollectionUtils.isEmpty(syncTaskRuleMysqlEntityList)){
            return null;
        }

        List<Integer> targetIds = new ArrayList<>();
        for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : syncTaskRuleMysqlEntityList){
            //排除重复的 targetId
            if (!targetIds.contains(syncTaskRuleMysqlEntity.getTargetId())){
                targetIds.add(syncTaskRuleMysqlEntity.getTargetId());
                AbsTargetFactory.logger.info("任务id:{},需要同步到 目标源targetId:{}",taskId,syncTaskRuleMysqlEntity.getTargetId());
            }
        }

        return targetIds;
    }

    @Override
    protected List<TargetConn> getTargetConnByTaskId(String taskId) {
        List<Integer> targetIds = getTargetsByTaskId(taskId);

        //没有找到 任务规则
        if (targetIds == null || targetIds.isEmpty()){
            return null;
        }

        List<TargetConn> targetCons = new ArrayList<>();

        for (Integer targetId : targetIds){
            QueryWrapper<SyncMysqlTargetEntity> targetWrapper = new QueryWrapper<>();
            targetWrapper.eq("target_id",targetId);
            SyncMysqlTargetEntity syncMysqlTargetEntity = (SyncMysqlTargetEntity)mysqlTargetService.getOne(targetWrapper);

            if (syncMysqlTargetEntity == null || StringUtils.isBlank(syncMysqlTargetEntity.getProps())){
                AbsTargetFactory.logger.error("目标源targetId:{} 没有找到 链接字符串属性 props");
                continue;
            }

            TargetConn targetConn = new TargetConn();
            targetConn.setTargetId(targetId);
            targetConn.setProps(syncMysqlTargetEntity.getProps());

            targetCons.add(targetConn);
        }

        return targetCons;
    }
}
