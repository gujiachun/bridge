package com.rainbow.bridge.server.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.core.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 * @author gujiachun
 */
public class PageUtil {

    /**
     * 分页组装,内存分页
     *@author gujiachun
     *@date 2021/7/20 9:19 上午
     *@param list 所有的对象列表
     *@param pageIndex 页码 从1开始
     *@param pageSize 每页 数量
     *@return java.util.List<T>
    */
    public static <T> List<T> page(List<T> list, int pageIndex, int pageSize) {
        int total = list.size();
        int leftIndex = (pageIndex - 1) * pageSize;
        boolean covered = total - leftIndex > 0;
        int rightIndex = pageIndex * pageSize;
        if (!covered) {
            return new ArrayList();
        } else {
            return rightIndex <= total ? list.subList(leftIndex, rightIndex) : list.subList(leftIndex, total);
        }
    }

    /***
     * 组装为自身框架的PageResult对象
     * mybatis plus 的page 对象转成 pateo的page对象
     * @param page mybatis plus的分页对象
     * @param <T> 分页实体
     * @return 博泰的分页对象
     */
    public static <T> PageResult<T> convertPageResult(IPage<T> page){
        PageResult<T> pageResult=new PageResult();
        pageResult.setCurrentPage((int)page.getCurrent());
        pageResult.setPageSize((int)page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }
}
