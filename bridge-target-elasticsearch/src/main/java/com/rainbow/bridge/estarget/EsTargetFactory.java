package com.rainbow.bridge.estarget;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import com.rainbow.bridge.biz.entity.es.SyncEsTargetEntity;
import com.rainbow.bridge.biz.entity.es.SyncTaskRuleEsEntity;
import com.rainbow.bridge.biz.entity.mysql.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.mysql.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.estarget.constant.EsPropCons;
import com.rainbow.bridge.estarget.strategy.CustomConnectionKeepAliveStrategy;
import com.rainbow.bridge.targetcore.factory.targetsource.AbsTargetFactory;
import com.rainbow.bridge.targetcore.model.TargetConn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gujiachun
 */
public class EsTargetFactory extends AbsTargetFactory<RestHighLevelClient> {

    private IService esTaskRuleService;

    private IService esTargetService;

    public EsTargetFactory(IService esTaskRuleService, IService esTargetService ,String targetType){
        super(targetType);
        this.esTargetService = esTargetService;
        this.esTaskRuleService = esTaskRuleService;
    }

    @Override
    protected List<Integer> getTargetsByTaskId(String taskId) {
        QueryWrapper<SyncTaskRuleEsEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleEsEntity> syncTaskRuleEsEntityList = esTaskRuleService.list(wrapper);

        if (CollectionUtils.isEmpty(syncTaskRuleEsEntityList)){
            return null;
        }

        List<Integer> targetIds = new ArrayList<>();
        for (SyncTaskRuleEsEntity syncTaskRuleEsEntity : syncTaskRuleEsEntityList){
            //??????????????? targetId
            if (!targetIds.contains(syncTaskRuleEsEntity.getTargetId())){
                targetIds.add(syncTaskRuleEsEntity.getTargetId());
                AbsTargetFactory.logger.info("??????id:{},??????????????? ?????????targetId:{}",taskId,syncTaskRuleEsEntity.getTargetId());
            }
        }

        return targetIds;
    }

    @Override
    protected List<TargetConn> getTargetConnByTaskId(String taskId) {
        List<Integer> targetIds = getTargetsByTaskId(taskId);

        //???????????? ????????????
        if (targetIds == null || targetIds.isEmpty()){
            return null;
        }

        List<TargetConn> targetCons = new ArrayList<>();

        for (Integer targetId : targetIds){
            QueryWrapper<SyncEsTargetEntity> targetWrapper = new QueryWrapper<>();
            targetWrapper.eq("target_id",targetId);
            SyncEsTargetEntity syncEsTargetEntity = (SyncEsTargetEntity)esTargetService.getOne(targetWrapper);

            if (syncEsTargetEntity == null || StringUtils.isBlank(syncEsTargetEntity.getProps())){
                AbsTargetFactory.logger.error("?????????targetId:{} ???????????? ????????????????????? props");
                continue;
            }

            EsTargetConn targetConn = new EsTargetConn();
            targetConn.setTargetId(targetId);
            targetConn.setProps(syncEsTargetEntity.getProps());
            targetConn.setHosts(syncEsTargetEntity.getHosts());
            targetConn.setVersion(syncEsTargetEntity.getVersion());

            targetCons.add(targetConn);
        }

        return targetCons;
    }

    @Override
    protected RestHighLevelClient initTargetConn(TargetConn targetConn) {

        logger.info(">>>>?????????elastic search??????client,???????????????:{}",targetConn.getTargetId());

        Map<String, String> map = PropertiesUtil.stringToMap(targetConn.getProps());
        try {
            EsTargetConn esTargetConn = (EsTargetConn) targetConn;
            if (StringUtils.isBlank(esTargetConn.getHosts())){
                return null;
            }
            //??????host
            String[] urlArr = esTargetConn.getHosts().split(";");
            HttpHost[] httpPostArr = new HttpHost[urlArr.length];
            for (int i = 0; i < urlArr.length; i++) {
                HttpHost httpHost = new HttpHost(urlArr[i].split(":")[0].trim(),
                        Integer.parseInt(urlArr[i].split(":")[1].trim()), "http");
                httpPostArr[i] = httpHost;
            }

            RestClientBuilder builder = RestClient.builder(httpPostArr);

            //??????
            final int connectTimeOut;
            if (StringUtils.isNotBlank(map.get(EsPropCons.connectTimeOut))){
                connectTimeOut = Integer.parseInt(map.get(EsPropCons.connectTimeOut));
            }else{
                connectTimeOut = 3000;
            }

            final int socketTimeOut;
            if (StringUtils.isNotBlank(map.get(EsPropCons.socketTimeOut))){
                socketTimeOut = Integer.parseInt(map.get(EsPropCons.socketTimeOut));
            }else{
                socketTimeOut = 3000;
            }

            final int connectionRequestTimeOut;
            if (StringUtils.isNotBlank(map.get(EsPropCons.connectionRequestTimeOut))){
                connectionRequestTimeOut = Integer.parseInt(map.get(EsPropCons.connectionRequestTimeOut));
            }else{
                connectionRequestTimeOut = 3000;
            }

            // ??????httpclient??????????????????
            builder.setRequestConfigCallback(requestConfigBuilder -> {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            });


            final int maxConnectCount;
            if (StringUtils.isNotBlank(map.get(EsPropCons.maxConnectCount))){
                maxConnectCount = Integer.parseInt(map.get(EsPropCons.maxConnectCount));
            }else{
                maxConnectCount = 100;
            }

            final int maxConnectPerRoute;
            if (StringUtils.isNotBlank(map.get(EsPropCons.maxConnectPerRoute))){
                maxConnectPerRoute = Integer.parseInt(map.get(EsPropCons.maxConnectPerRoute));
            }else{
                maxConnectPerRoute = 10;
            }

            final int keepAliveMinutes;
            if (StringUtils.isNotBlank(map.get(EsPropCons.keepAliveMinutes))){
                keepAliveMinutes = Integer.parseInt(map.get(EsPropCons.keepAliveMinutes));
            }else{
                keepAliveMinutes = 10;
            }

            // ??????httpclient??????
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                // httpclient???????????????
                httpClientBuilder.setMaxConnTotal(maxConnectCount);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                // httpclient????????????
                httpClientBuilder.setKeepAliveStrategy(CustomConnectionKeepAliveStrategy.getInstance(keepAliveMinutes));
                httpClientBuilder.disableAuthCaching();

                //????????????????????? ??? ??????
                if (StringUtils.isNotBlank(map.get(EsPropCons.username))){
                    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(map.get(EsPropCons.username), map.get(EsPropCons.password)));  //es?????????????????????????????????elastic???
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
                return httpClientBuilder;
            });

            return new RestHighLevelClient(builder);

        } catch (Exception e) {
            logger.error("??????elastic search??????client??????????????????????????????:{},??????:{}",targetConn.getTargetId(),e.getMessage());
        }
        return null;
    }
}
