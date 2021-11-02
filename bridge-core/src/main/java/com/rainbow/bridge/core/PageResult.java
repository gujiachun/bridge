package com.rainbow.bridge.core;

import java.util.List;

/**
 * @author fonlin
 * @date 2020/5/27
 */
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页，从1开始
     * */
    private Integer currentPage;

    /**
     * 每页大小
     * */
    private Integer pageSize;

    /**
     * 当前页数据集
     */
    private List<T> records;

    /**
     * 额外数据
     */
    private Object data;

    public PageResult() {
    }

    public PageResult(Long total, List<T> records) {
        this(total, records, null,1,20);
    }

    public PageResult(Long total, List<T> records, Object data) {
        this(total,records,data,1,20);
    }

    public PageResult(Long total, List<T> records, Object data,Integer currentPage,Integer pageSize) {
        this.total = total;
        this.records = records;
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public static <T> PageResultBuilder<T> builder() {
        return new PageResultBuilder<T>();
    }

    public static class PageResultBuilder<T> {
        private Long total;

        private List<T> records;

        private Object data;

        /**
         * 当前页，从1开始
         * */
        private Integer currentPage;

        /**
         * 每页大小
         * */
        private Integer pageSize;

        PageResultBuilder() {
        }

        public PageResultBuilder<T> total(final Long total) {
            this.total = total;
            return this;
        }

        public PageResultBuilder<T> pageSize(final Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PageResultBuilder<T> currentPage(final Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public PageResultBuilder<T> records(final List<T> records) {
            this.records = records;
            return this;
        }

        public PageResultBuilder<T> data(final Object data) {
            this.data = data;
            return this;
        }

        public PageResult<T> build() {
            return new PageResult<T>(this.total, this.records, this.data,this.currentPage,this.pageSize);
        }
    }
}
