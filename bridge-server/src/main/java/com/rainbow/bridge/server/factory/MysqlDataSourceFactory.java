package com.rainbow.bridge.server.factory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.service.MysqlTargetService;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gujiachun
 */
@Component
public class MysqlDataSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(MysqlDataSourceFactory.class);

    private Map<Integer, DataSource> dataSourceMap;

    public MysqlDataSourceFactory() {
        dataSourceMap = new ConcurrentHashMap<>(10);
    }

    @Autowired
    private MysqlTargetService mysqlTargetService;

    @Autowired
    private TaskMysqlRuleService taskMysqlRuleService;

    @PreDestroy
    public void destroy(){
        dataSourceMap.clear();
    }

    public void buildDataSources(List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntityList){
        for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : syncTaskRuleMysqlEntityList){
            SyncMysqlTargetEntity syncMysqlTargetEntity = mysqlTargetService.getSyncMysqlTargetByTargetId(syncTaskRuleMysqlEntity.getTargetId());
            initTargetDataSource(syncMysqlTargetEntity.getTargetId(),syncMysqlTargetEntity.getProps());
        }
    }

    /**
     * 初始化连接对象
     */
    private void initTargetDataSource(Integer targetId,String props) {
        if (dataSourceMap.containsKey(targetId)){
            return;
        }
        logger.info("====================================================================================================");
        logger.info("********************目标数据源{},初始化中···********************",targetId);
        logger.info("********************目标数据源{},props: {}  ****************",targetId,props);
        Properties properties = PropertiesUtil.stringToProperties(props);
        try {
            DataSource ds = DruidDataSourceFactory.createDataSource(properties);
            dataSourceMap.put(targetId, ds);
            logger.info("初始DataSource成功,目标数据源:{}",targetId);
        } catch (Exception e) {
            logger.error("初始DataSource发生异常，目标数据源:{},异常:{}",targetId,e.getMessage());
        }
        logger.info("====================================================================================================\n");
    }

    public DataSource getDataSource(Integer targetId){
        return dataSourceMap.get(targetId);
    }

    public void removeDataSource(String taskId){
        QueryWrapper<SyncTaskRuleMysqlEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntityList = taskMysqlRuleService.list(wrapper);

        for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : syncTaskRuleMysqlEntityList){
            if (dataSourceMap.containsKey(syncTaskRuleMysqlEntity.getTargetId())){
                DataSource dataSource = dataSourceMap.get(syncTaskRuleMysqlEntity.getTargetId());
                logger.info("释放DataSource targetId:{}",syncTaskRuleMysqlEntity.getTargetId());
                dataSource = null;
                dataSourceMap.remove(syncTaskRuleMysqlEntity.getTargetId());
            }
        }
    }
}
