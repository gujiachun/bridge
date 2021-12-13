package com.rainbow.bridge.estarget.service;

/**
 * 索引 实体
 * @author gujiachun
 */
public class EsRequestDto<T> {

    /** 索引名称 */
    private String index;

    /** es主键 */
    private String id;

    /** 是否父文档,默认是 */
    private boolean parentFlag = true;

    /** 指向父文档的主键id,只有添加 子文档的时候才使用此字段 */
    private String routing;

    /** 更新的数据 */
    private T data;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(boolean parentFlag) {
        this.parentFlag = parentFlag;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
