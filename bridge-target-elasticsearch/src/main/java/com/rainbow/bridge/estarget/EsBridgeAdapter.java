package com.rainbow.bridge.estarget;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.service.SourceService;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.biz.service.TopicService;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.estarget.param.EsParam;
import com.rainbow.bridge.estarget.service.EsRequestDto;
import com.rainbow.bridge.estarget.service.EsService;
import com.rainbow.bridge.estarget.service.EsServiceImpl;
import com.rainbow.bridge.estarget.utils.EsFieldUtils;
import com.rainbow.bridge.estarget.utils.JdbcUtil;
import com.rainbow.bridge.targetcore.adapter.BridgeAdapter;
import com.rainbow.bridge.targetcore.model.Param;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * 目标源类型es的 具体执行适配器
 *
 * @author gujiachun
 */
public class EsBridgeAdapter implements BridgeAdapter<RestHighLevelClient> {

    protected static final Logger logger = LoggerFactory.getLogger(EsBridgeAdapter.class);

//    private final static String ADD_SCRIPT = "if (ctx.op == 'create'){ctx._source=params.data} else {if (ctx._source.%s == null) {ctx._source.%s = [params.%s]} else {ctx._source.%s.add(params.%s)}}";
//    private final static String REMOVE_SCRIPT = "ctx._source.%s.remove(ctx._source.%s.indexOf(params.%s))";

    @Override
    public void execute(RestHighLevelClient client, EventEnum event, Param param) throws Exception {
        if (param == null) {
            return;
        }

        switch (event){
            case insert:
            case update:
                upsertCommand(client,param);
                break;
            case delete:
                deleteCommand(client, param);
                break;
            default:
                break;
        }
    }

    @Override
    public void execute(RestHighLevelClient client, EventEnum event, List<Param> params) throws Exception {

    }

    private DataSource buildSourceDataSource(String taskId){
        SourceDsCache sourceDsCache = SpringUtil.getBean(SourceDsCache.class);
        DataSource dataSource = sourceDsCache.get(taskId);
        if (dataSource != null){
            return dataSource;
        }

        TaskService taskService = SpringUtil.getBean(TaskService.class);
        SyncTaskEntity syncTaskEntity = taskService.getById(taskId);
        Integer basicTopicId = syncTaskEntity.getBasicTopicId();

        TopicService topicService = SpringUtil.getBean(TopicService.class);
        BasicTopicEntity basicTopicEntity = topicService.getById(basicTopicId);
        Integer sourceId = basicTopicEntity.getSourceId();

        SourceService sourceService = SpringUtil.getBean(SourceService.class);
        BasicSourceEntity basicSourceEntity = sourceService.getById(sourceId);

        Properties properties = PropertiesUtil.stringToProperties(basicSourceEntity.getProps());
        try {
            DataSource ds = DruidDataSourceFactory.createDataSource(properties);
            sourceDsCache.add(taskId,ds);
            return ds;
        } catch (Exception e) {
            logger.error("初始DataSource发生异常，源数据源:{},异常:{}",basicSourceEntity.getSourceName(),e.getMessage());
        }
        return null;
    }

//    private void partUpdateCommand(RestHighLevelClient client,EsParam esParam,EventEnum event){
//        //部分更新
//        partArrayUpdate(esParam,client);
//    }
//
//    private void partDeleteCommand(RestHighLevelClient client,EsParam esParam,EventEnum event){
//        //部分删除
//        partArrayDelete(esParam,client);
//    }

    private void upsertCommand(RestHighLevelClient client,Param param) throws SQLException {

        EsParam esParam = (EsParam) param;

        Map<String, String> fieldTypeMap = PropertiesUtil.stringToMap(esParam.getFieldType(), CommonCons.split);

        Map<String, Object> esMap = new HashMap<>(500);

        //canal同步字段模式
        if (CommonCons.EsIndexType.part_type.equals(esParam.getIndexType())){
            //从canal数据库同步过来的DB数据
            Map<String, Object> partMap = esParam.getPartMap();
            coverDb2EsMap(fieldTypeMap, partMap, esMap);
            syncOneData(client,esParam,esMap);
        }
        //执行sql模式
        else {
            DataSource dataSource = buildSourceDataSource(param.getTaskId());
            List<Map<String, Object>> results = JdbcUtil.select(dataSource, param);

            if (results == null || results.isEmpty()){
                //如果sql执行为空，es索引属性清空
                if (StringUtils.isNotBlank(esParam.getSqlNullField())){
                    String[] esNullFields = esParam.getSqlNullField().split(",");
//                    Map<String, Object> esNullMap = new HashMap<>(10);
                    for (String esNullField : esNullFields){
                        esMap.put(esNullField,null);
                    }
                    execEs(client,esParam,esMap);
                }
                return;
            }

            //遍历返回的数据
            for (Map<String, Object> dbMap : results){
                esMap.clear();
                // 1.同步mysql中指定字段到es里面，2.特殊字段类型转换
                coverDb2EsMap(fieldTypeMap, dbMap, esMap);
                syncOneData(client,esParam,esMap);
            }
        }
    }

    //同步一条记录
    private void syncOneData(RestHighLevelClient client,EsParam esParam,Map<String, Object> esMap){
        // 是否配置了父子表关系
        if (esParam.getRelationType() > 0 && StringUtils.isNotBlank(esParam.getRelationFieldName())) {

            Map<String, Object> joinMap = new HashMap<>(10);
            joinMap.put("name",esParam.getRelationJoinName());
            //子文档
            if (CommonCons.EsFieldType.relation_child.equals(esParam.getRelationType())){
                joinMap.put("parent",esParam.getRelationChildParentFormat());
            }
            esMap.put(esParam.getRelationFieldName(), joinMap);
        }

        // 判断是否有需要过滤的字段
        if (StringUtils.isNotBlank(esParam.getSkipsField())) {
            String[] skipsField = esParam.getSkipsField().split(",");
            Iterator<String> skipsIterator = esMap.keySet().iterator();
            while (skipsIterator.hasNext()) {
                String key = skipsIterator.next();
                for (String skipField : skipsField){
                    if (skipField.equals(key)) {
                        skipsIterator.remove();
                        break;
                    }
                }
            }
        }

        execEs(client,esParam,esMap);
    }

    private void execEs(RestHighLevelClient client,EsParam esParam,Map<String, Object> esMap){
        EsRequestDto<Map<String, Object>> requestDto = new EsRequestDto<>();
        requestDto.setData(esMap);
        requestDto.setId(esParam.getIdFormat());
        requestDto.setIndex(esParam.getIndexFormat());
        if (CommonCons.EsFieldType.relation_child.equals(esParam.getRelationType())
                && StringUtils.isNotBlank(esParam.getRelationFieldName())){
            requestDto.setRouting(esParam.getRelationChildRouteFormat());
            requestDto.setParentFlag(false);
        }
        EsService esService = new EsServiceImpl(client);
        esService.upsert(requestDto);
    }

    private void deleteCommand(RestHighLevelClient client,Param param) throws SQLException {
        EsParam esParam = (EsParam) param;
        if (CommonCons.EsFieldType.delete_strategy_0.equals(esParam.getDeleteStrategy())){
            EsServiceImpl esService = new EsServiceImpl(client);
            esService.delete(esParam.getIndexFormat(), esParam.getIdFormat());
        }else if (CommonCons.EsFieldType.delete_strategy_1.equals(esParam.getDeleteStrategy())){
            upsertCommand(client,param);
        }
    }

    /**
     * 数据库的字段类型 转换 es field类型
     * 对一行数据进行转换
     * @param fieldTypeMap 数据库中 定义的 field type对应的值
     * @param dbMap 源数据库中返回的数据
     * @param esMap 转换后es对应的数据
     */
    private void coverDb2EsMap(Map<String, String> fieldTypeMap, Map<String, Object> dbMap, Map<String, Object> esMap) {
        // 判断是否配置特殊类型转换，如果配置了则需要特殊字段类型转换
        for (String col : dbMap.keySet()){
            if (fieldTypeMap != null && fieldTypeMap.containsKey(col)){
                String fieldType = fieldTypeMap.get(col);
                esMap.put(col, EsFieldUtils.convertType(fieldType, dbMap.get(col)));
            }else{
                esMap.put(col, dbMap.get(col));
            }
        }
    }

//    private void partArrayDelete(EsParam esParam, RestHighLevelClient client) {
//
//        Map<String, Object> partMap = esParam.getPartMap();
//        if (MapUtils.isEmpty(partMap)){
//            return;
//        }
//
//        String esIndex = esParam.getIndexFormat();
//        String id = esParam.getIdFormat();
//
//        EsService esService = new EsServiceImpl(client);
//
//        for (String esField : partMap.keySet()){
//            String valueScript = String.format(REMOVE_SCRIPT, esField, esField, esField);
//            Map<String, Object> valueMap = new HashMap<>();
//            valueMap.put(esField, partMap.get(esField));
//            esService.updateByScript(esIndex, id, valueScript, valueMap);
//        }
//
//
//    }
//
//    private void partArrayUpdate(EsParam esParam, RestHighLevelClient client) {
//        Map<String, Object> partMap = esParam.getPartMap();
//        if (MapUtils.isEmpty(partMap)){
//            return;
//        }
//
//        String esIndex = esParam.getIndexFormat();
//        String id = esParam.getIdFormat();
//
//        EsService esService = new EsServiceImpl(client);
//
//        for (String esField : partMap.keySet()){
//            String script = String.format(ADD_SCRIPT, esField, esField, esField, esField, esField);
//
//            Map<String, Object> valueMap = new HashMap<>();
//
//            ///////////////////初始化的数据/////////////////
//            Map<String, Object> data = new HashMap<>();
//            List<Object> dataList = new ArrayList<>(500);
//            dataList.add(partMap.get(esField));
//            data.put(esField, dataList);
//            ///////////////////初始化的数据/////////////////
//
//            valueMap.put("data", data);
//            valueMap.put(esField, partMap.get(esField));
//
//            esService.updateByScript(esIndex, id, script, valueMap);
//        }
//    }

}
