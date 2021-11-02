package com.rainbow.bridge.core.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

/**
 * 遍历树的好工具
 *
 * @author fonlin
 * @date 2021/4/1
 */
public class TreeTraversalUtil {

    /**
     * 深度优先遍历，优先 一直打到最终叶子节点
     * // list   节点集合
     * // root   根节点
     * // TreeTraversal.depthFirst(list, root, new TreeTraversal.ChildrenSupplier<DepartmentTreeDTO>() {
     * //     @Override
     * //     public List<DepartmentTreeDTO> get(List<DepartmentTreeDTO> list, DepartmentTreeDTO parent) {
     * //         // 获取当前节点的子节点
     * //         return list.stream().filter(d -> parent.getId().equals(d.getParentId())).collect(Collectors.toList());
     * //     }
     * // }, new Consumer<DepartmentTreeDTO>() {
     * //     @Override
     * //     public void accept(DepartmentTreeDTO departmentTreeDTO) {
     * //         // 对当前正在遍历的节点做业务消费
     * //         System.out.println("当前节点："+ departmentTreeDTO.getName());
     * //     }
     * // });
     * @param list  所有树节点
     * @param parent    开始遍历的父节点
     * @param childrenSupplier  查询子节点的方法类
     * @param consumer  遍历消费方法
     * @param <T>   节点泛型
     */
    public static <T> void depthFirst(List<T> list, T parent, ChildrenSupplier<T> childrenSupplier, Consumer<T> consumer) {
        consumer.accept(parent);
        List<T> children = childrenSupplier.get(list, parent);
        if (children != null && children.size() > 0) {
            for (T child : children) {
                depthFirst(list, child, childrenSupplier, consumer);
            }
        }
    }


    /**
     * 广度遍历，优先先处理 同一级 处理
     *
     * // list   节点集合
     * // root   根节点
     * // TreeTraversal.depthFirst(list, root, new TreeTraversal.ChildrenSupplier<DepartmentTreeDTO>() {
     * //     @Override
     * //     public List<DepartmentTreeDTO> get(List<DepartmentTreeDTO> list, DepartmentTreeDTO parent) {
     * //         // 获取当前节点的子节点
     * //         return list.stream().filter(d -> parent.getId().equals(d.getParentId())).collect(Collectors.toList());
     * //     }
     * // }, new Consumer<DepartmentTreeDTO>() {
     * //     @Override
     * //     public void accept(DepartmentTreeDTO departmentTreeDTO) {
     * //         // 对当前正在遍历的节点做业务消费
     * //         System.out.println("当前节点："+ departmentTreeDTO.getName());
     * //     }
     * // });
     *
     *@author gujiachun
     *@date 2021/9/7 4:50 下午
     *@param list
     *@param rootNodeSupplier
     *@param childrenSupplier
     *@return void
    */
    public <T> void breadthFirst(List<T> list, RootNodeSupplier<T> rootNodeSupplier, ChildrenSupplier<T> childrenSupplier) {
        Deque<T> deque = new ArrayDeque<>();
        T root = rootNodeSupplier.get(list);
        deque.add(root);
        while (!deque.isEmpty()) {
            // 取出队列第一个
            T node = deque.peekFirst();
            //获得节点的子节点，对于二叉树就是获得节点的左子结点和右子节点
            List<T> children = childrenSupplier.get(list, node);
            if (children != null && !children.isEmpty()) {
                deque.addAll(children);
            }
        }
    }

    public interface RootNodeSupplier<T> {

        T get(List<T> list);
    }

    public interface ChildrenSupplier<T> {

        List<T> get(List<T> list, T parent);
    }
}
