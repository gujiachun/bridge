package com.rainbow.bridge.core;


/**
 * @author fonlin
 * @date 2020/5/12
 */
public class PageParam {

    /**
     * 当前页，从1开始
     * */
    private Integer currentPage;

    /**
     * 每页大小
     * */
    private Integer pageSize;

    /**
     * 排序字段
     * */
    private String sort;

    /**
     * 排序方向
     * */
    private String direction;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
