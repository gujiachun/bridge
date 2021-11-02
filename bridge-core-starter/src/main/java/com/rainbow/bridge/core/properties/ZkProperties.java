//package com.rainbow.bridge.core.properties;
//
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
///**
// * @author gujiachun
// */
//@ConfigurationProperties(ZkProperties.PREFIX)
//public class ZkProperties {
//
//    public static final String PREFIX = "bridge-zk";
//
//    /**
//     * zk的链接
//     */
//    private String connStr;
//
//    /**
//     * 根节点
//     */
//    private String rootPath;
//
//    /**
//     * 是否开启监听 rootPath
//     */
//    private Boolean watched = false;
//
//    public String getConnStr() {
//        return connStr;
//    }
//
//    public void setConnStr(String connStr) {
//        this.connStr = connStr;
//    }
//
//    public String getRootPath() {
//        return rootPath;
//    }
//
//    public void setRootPath(String rootPath) {
//        this.rootPath = rootPath;
//    }
//
//    public Boolean getWatched() {
//        return watched;
//    }
//
//    public void setWatched(Boolean watched) {
//        this.watched = watched;
//    }
//}
