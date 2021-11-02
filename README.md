# 彩虹桥

## 前言

现在很多公司都用到微服务架构，在微服务架构中会延伸出几个问题

### 冗余问题

如下图![https://gitee.com/gujiachun/redis-plus/blob/master/冗余图.png]()

上图中订单库中的表，冗余了用户的姓名以及商品名称。这个主要目的就是为了订单业务中，快速的查询出用户名、商品名；防止跨库查询。但是有时候业务是需要 冗余的数据，要和源数据保持一致，即更新了用户姓名，订单中的用户姓名也要更新。这个就是冗余导致数据不一致的问题

### 缓存问题

如下图![https://gitee.com/gujiachun/redis-plus/blob/master/商品缓存.png]()

上图中评价服务需要拿到商品信息，为了减少和商品服务的调用频次，有时候需要把商品信息缓存到redis中；这个时候也会出现商品信息更新了，那redis中的缓存 没法即时更新

### ES同步

在有些复杂查询场景中，有时候我们需要把mysql中的数据同步到es中，那我们如何方便有效的进行同步呢？

## 目标

彩虹桥（rainbow bridge）是基于Canal Server的一个***客户端转换平台***，即订阅Canal Server监听到的mysql的binlog日志，产生Topic消息（支持rocketmq、kafka），rainbow bridge是Canal Server的topic 和 其他目标源的**桥梁**，可以让业务自行定义如何同步到目标源中。

**实现目标：**

```
1、支持多种目标源，已支持mysql、redis、即将支持（es、rocketmq）等

2、任务执行的高可用、负载均衡

3、任务在线实时更新，无须重启任务实例

4、实例执行任务自动伸缩

5、任务执行方式支持同步、异步

6、灵活配置任务规则，目标零代码实现业务功能

7、有研发能力的团队可在起基础上，方便的扩展
```

## 架构

![https://gitee.com/gujiachun/redis-plus/blob/master/彩虹桥架构图.png]()

彩虹桥（rainbow bridge）可以理解为canal的**客户端平台**，以任务的方式执行业务需求，***为了保证高可用、负载均衡，引入了集群设计以及zookeeper做实例协调***。

## 部署















