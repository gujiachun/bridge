package com.rainbow.bridge.estarget.service;

import java.util.Map;

/**
 * @author gujiachun
 */
public interface EsService {

    /**
     * 添加es数据
     * @param reqDto
     */
    void add(EsRequestDto reqDto);

    /**
     * 删除es数据
     * @param index
     * @param id
     */
    void delete(String index, String id);


    /**
     * upsert
     * @param reqDto
     */
    void upsert(EsRequestDto reqDto);

    /**
     * 根据script来更新文档
     * @param scriptStr
     * @param paramMap
     */
    void updateByScript(String index, String id, String scriptStr, Map<String, Object> paramMap);

}
