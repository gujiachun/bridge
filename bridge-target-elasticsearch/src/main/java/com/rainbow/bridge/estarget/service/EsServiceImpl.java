package com.rainbow.bridge.estarget.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author gujiachun
 */
public class EsServiceImpl implements EsService {

    protected static final Logger logger = LoggerFactory.getLogger(EsServiceImpl.class);

    private RestHighLevelClient restHighLevelClient;

    public EsServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public void add(EsRequestDto reqDto) {
        IndexRequest indexRequest = new IndexRequest(reqDto.getIndex());
        // 规则，put /wf_index/_doc/1
        indexRequest.id(reqDto.getId());
        indexRequest.timeout(TimeValue.timeValueSeconds(5));
        // 如果不是父文档则需要指定routing
        if (!reqDto.isParentFlag()) {
            indexRequest.routing(reqDto.getRouting());
        }

        indexRequest.create(false);
        String source = JSONObject.toJSONString(reqDto.getData(), SerializerFeature.WriteMapNullValue);
        logger.info("需要添加到es里面的数据为：{}-----{}", source, indexRequest);
        //将我们数据放入请求,不过滤对象中的null值
        indexRequest.source(source, XContentType.JSON);
        try {
            //客户端发送请求
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            logger.info("es添加数据成功***{}***", indexResponse.toString());
        } catch (IOException ioException) {
            logger.error("！！！es新增数据发生异常！！！" + ioException.getMessage());
        } catch (Exception e) {
            logger.error("！！！es新增数据发生未知异常！！！" + e.getMessage());
        }
    }

    @Override
    public void delete(String index, String id) {
        //参数为索引名，可以不指定，可以一个，可以多个
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        // 更新时版本冲突
        request.setConflicts("proceed");
        // 设置查询条件，第一个参数是字段名，第二个参数是字段的值
        request.setQuery(new TermQueryBuilder("_id", id));
        // 更新最大文档数
        request.setMaxDocs(10);
        // 最大重试次数
        request.setMaxRetries(10);
        // 批次大小
        request.setBatchSize(1000);
        // 并行
        request.setSlices(2);
        // 使用滚动参数来控制“搜索上下文”存活的时间
        request.setScroll(TimeValue.timeValueMinutes(10));
        // 超时
        request.setTimeout(TimeValue.timeValueMinutes(5));
        // 刷新索引
        request.setRefresh(true);
        try {
            logger.info("开始删除es的数据--id={},index={}", id, index);
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            logger.info("SUCCESS===========es删除数据成功----id={}----response：{}", id, response.toString());
        } catch (IOException e) {
            logger.error("ERROR！！！es删除数据发生异常！！！");
        }
    }

    @Override
    public void upsert(EsRequestDto reqDto) {
        logger.info("\n");
        UpdateRequest updateRequest = new UpdateRequest(reqDto.getIndex(), reqDto.getId());
        String source = JSONObject.toJSONString(reqDto.getData(), SerializerFeature.WriteMapNullValue);
        logger.info("更新的es数据为：{}", source);
        updateRequest.doc(source, XContentType.JSON);
        updateRequest.docAsUpsert(true);
        // 如果不是父文档则需要指定routing
        if (!reqDto.isParentFlag()) {
            updateRequest.routing(reqDto.getRouting());
        }
        updateRequest.retryOnConflict(20);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            logger.info("SUCCESS==============es修改数据成功updateResponse：{}=====reqVo：{}", updateResponse.toString(), reqDto);
        } catch (IOException ioException) {
            logger.error("ERROR！！！es修改数据发生异常！！！{}======={}", ioException.getMessage(), reqDto);
        } catch (Exception e) {
            logger.error("ERROR！！！发生了未知异常！！！{}======={}", e.getMessage(), reqDto);
        } finally {
        }
        logger.info("\n");
    }

    @Override
    public void updateByScript(String index, String id, String scriptStr, Map<String, Object> paramMap) {
        logger.info("\n");
        UpdateRequest updateRequest = new UpdateRequest(index, id);
        Script script = new Script(ScriptType.INLINE, "painless", scriptStr, paramMap);
        updateRequest.script(script);
        updateRequest.retryOnConflict(20);
        updateRequest.upsert();
        updateRequest.scriptedUpsert(true);
        try {
            //处理
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException ioException) {
            logger.error("ERROR！！！script-es修改数据发生异常！！！{}，脚本为：{}====数据为：{}", ioException.getMessage(), scriptStr, JSONObject.toJSONString(paramMap));
        } catch (Exception e) {
            logger.error("ERROR！！！异常！！！{}，脚本为={}，数据为={}", e.getMessage(), scriptStr, JSONObject.toJSONString(paramMap));
        }
        logger.info("\n");
    }
}
