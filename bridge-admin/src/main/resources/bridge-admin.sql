/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : rainbow_bridge

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 06/11/2021 15:30:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for basic_cluster
-- ----------------------------
DROP TABLE IF EXISTS `basic_cluster`;
CREATE TABLE `basic_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '集群名称',
  `code` varchar(50) DEFAULT NULL COMMENT '集群代码，唯一',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='集群';

-- ----------------------------
-- Records of basic_cluster
-- ----------------------------
BEGIN;
INSERT INTO `basic_cluster` VALUES (1, '集群1', 'c1', NULL, 'dev', NULL, '2021-10-12 20:34:40');
COMMIT;

-- ----------------------------
-- Table structure for basic_mq
-- ----------------------------
DROP TABLE IF EXISTS `basic_mq`;
CREATE TABLE `basic_mq` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT 'mq名称',
  `mq_type` varchar(20) DEFAULT NULL COMMENT 'mq类型 kafka、rocketmq',
  `servers` varchar(255) DEFAULT NULL COMMENT '链接服务器地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='mq关联基础表';

-- ----------------------------
-- Records of basic_mq
-- ----------------------------
BEGIN;
INSERT INTO `basic_mq` VALUES (1, 'mq1', 'rocketmq', '172.16.112.130:9876', 'canal server的rocketmq', 'dev');
INSERT INTO `basic_mq` VALUES (4, '11', 'rocketmq', '111', '1', 'dev');
COMMIT;

-- ----------------------------
-- Table structure for basic_namespace
-- ----------------------------
DROP TABLE IF EXISTS `basic_namespace`;
CREATE TABLE `basic_namespace` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码 唯一',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='环境命名空间';

-- ----------------------------
-- Records of basic_namespace
-- ----------------------------
BEGIN;
INSERT INTO `basic_namespace` VALUES (1, '开发环境', 'dev', '2021-10-08 19:47:44', '2021-10-08 19:47:46');
INSERT INTO `basic_namespace` VALUES (2, '测试环境', 'test', '2021-10-08 19:48:00', '2021-10-08 19:48:02');
INSERT INTO `basic_namespace` VALUES (3, '生产环境', 'prod', '2021-10-08 19:48:17', '2021-10-08 19:48:20');
COMMIT;

-- ----------------------------
-- Table structure for basic_topic
-- ----------------------------
DROP TABLE IF EXISTS `basic_topic`;
CREATE TABLE `basic_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic` varchar(50) DEFAULT NULL COMMENT '主题topic',
  `mq_id` int(11) DEFAULT NULL COMMENT 'basic_mq的主键',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `sync_db` varchar(50) DEFAULT NULL COMMENT '同步库名',
  `sync_table` varchar(3000) DEFAULT NULL COMMENT '同步表',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='topic关联基础表';

-- ----------------------------
-- Records of basic_topic
-- ----------------------------
BEGIN;
INSERT INTO `basic_topic` VALUES (1, 'topic1', 1, NULL, 'test', 'goods,t_users', 'dev');
COMMIT;

-- ----------------------------
-- Table structure for basic_zk
-- ----------------------------
DROP TABLE IF EXISTS `basic_zk`;
CREATE TABLE `basic_zk` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `servers` varchar(255) DEFAULT NULL COMMENT 'server地址',
  `root_path` varchar(50) DEFAULT NULL COMMENT 'zk根节点名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='zookeeper基础关联表';

-- ----------------------------
-- Records of basic_zk
-- ----------------------------
BEGIN;
INSERT INTO `basic_zk` VALUES (1, 'zk', '127.0.0.1:2181', '/bridge', '', 'dev', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sync_mysql_target
-- ----------------------------
DROP TABLE IF EXISTS `sync_mysql_target`;
CREATE TABLE `sync_mysql_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `target_id` int(11) DEFAULT NULL COMMENT '目标数据源ID',
  `db_name` varchar(50) DEFAULT NULL COMMENT '数据库名',
  `props` varchar(1000) DEFAULT NULL COMMENT '数据源配置格式（k1=v1;k2=v2）',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='同步目标数据源Mysql';

-- ----------------------------
-- Records of sync_mysql_target
-- ----------------------------
BEGIN;
INSERT INTO `sync_mysql_target` VALUES (1, 1, 'test1', 'driverClassName=com.mysql.cj.jdbc.Driver;maxActive=20;maxPoolPreparedStatementPerConnectionSize=20;minIdle=8;initialSize=8;maxWait=60000;testOnBorrow=false;testOnReturn=false;timeBetweenEvictionRunsMillis=60000;minEvictableIdleTimeMillis=30000;testWhileIdle=true;password=root;url=jdbc:mysql://127.0.0.1:3306/test1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai;username=root;validationQuery=SELECT 1', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sync_redis_target
-- ----------------------------
DROP TABLE IF EXISTS `sync_redis_target`;
CREATE TABLE `sync_redis_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `target_id` int(11) DEFAULT NULL COMMENT '目标数据源ID',
  `mode` int(1) DEFAULT NULL COMMENT 'Redis部署方式，0：单机，1：哨兵，2：集群',
  `props` varchar(1000) DEFAULT NULL COMMENT '配置格式（k1=v1;k2=v2）',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='同步目标数据源Mysql';

-- ----------------------------
-- Records of sync_redis_target
-- ----------------------------
BEGIN;
INSERT INTO `sync_redis_target` VALUES (10, 2, 0, 'redis.single.host=localhost;redis.single.max-active=8;redis.single.max-idle=8;redis.single.min-idle=1;redis.single.database=0;redis.single.max-wait=5000;redis.single.so-timeout=1000;redis.single.port=6379', '2021-10-24 19:13:19', '2021-10-24 19:13:19');
COMMIT;

-- ----------------------------
-- Table structure for sync_target
-- ----------------------------
DROP TABLE IF EXISTS `sync_target`;
CREATE TABLE `sync_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '数据源名称',
  `type` varchar(20) DEFAULT NULL COMMENT '目标数据源类型 mysql、es、redis、rocketmq',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='同步目标数据源';

-- ----------------------------
-- Records of sync_target
-- ----------------------------
BEGIN;
INSERT INTO `sync_target` VALUES (1, '目标mysql1', 'mysql', NULL, 'dev', NULL, NULL);
INSERT INTO `sync_target` VALUES (2, '目标redis2', 'redis', NULL, 'dev', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sync_task
-- ----------------------------
DROP TABLE IF EXISTS `sync_task`;
CREATE TABLE `sync_task` (
  `id` varchar(50) NOT NULL COMMENT '任务主键，也会用来做消费者的groupId',
  `name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `env` varchar(20) DEFAULT NULL COMMENT '环境代码',
  `basic_topic_id` int(11) DEFAULT NULL COMMENT '订阅的主题ID',
  `target_type` varchar(20) DEFAULT NULL COMMENT '目标数据源类型 mysql、es、redis、rocketmq',
  `publish_cluster` varchar(50) DEFAULT NULL COMMENT '发布到哪个集群code中，会部署多个集群实例，每个实例在启动是需要指明集群code；代表归属哪个集群',
  `instance_count` int(1) DEFAULT NULL COMMENT '集群中多少个实例做此任务，暂定一个实例最多可以分配3个任务',
  `async` int(1) DEFAULT '0' COMMENT '是否异步处理消息 0：同步 1：异步',
  `status` int(1) DEFAULT '0' COMMENT '状态 0:未发布 1:发布有效 2：失效',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步任务';

-- ----------------------------
-- Records of sync_task
-- ----------------------------
BEGIN;
INSERT INTO `sync_task` VALUES ('taskid-1', 'task1', 'dev', 1, 'mysql', 'c1', 2, 1, 1, NULL, '2021-10-22 22:54:14');
INSERT INTO `sync_task` VALUES ('taskid-2', 'task2', 'dev', 1, 'redis', 'c1', 2, 1, 1, NULL, '2021-10-22 21:50:57');
INSERT INTO `sync_task` VALUES ('taskid-3', 'task3', 'dev', 1, 'mysql', 'c1', 3, 1, 2, NULL, '2021-10-21 09:19:18');
INSERT INTO `sync_task` VALUES ('taskid-4', 'task4', 'dev', 1, 'mysql', 'c1', 3, 1, 2, NULL, '2021-10-21 09:19:23');
INSERT INTO `sync_task` VALUES ('taskid-5', 'task5', 'dev', 1, 'mysql', 'c1', 3, 1, 2, NULL, '2021-10-21 09:19:12');
INSERT INTO `sync_task` VALUES ('taskid-6', 'task6', 'dev', 1, 'mysql', 'c1', 3, 1, 2, NULL, '2021-10-20 22:09:57');
COMMIT;

-- ----------------------------
-- Table structure for sync_task_rule_mysql
-- ----------------------------
DROP TABLE IF EXISTS `sync_task_rule_mysql`;
CREATE TABLE `sync_task_rule_mysql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` varchar(50) NOT NULL COMMENT '任务id',
  `source_db` varchar(50) DEFAULT NULL COMMENT '源库',
  `source_table` varchar(50) DEFAULT NULL COMMENT '源表',
  `target_id` int(11) DEFAULT NULL COMMENT '目标数据源ID',
  `target_db` varchar(50) DEFAULT NULL COMMENT '目标库',
  `target_table` varchar(50) DEFAULT NULL COMMENT '目标表',
  `sync_pks` varchar(100) DEFAULT NULL COMMENT '同步时对应的关联PK，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)\n新增时：无效\n修改和删除时：当过滤条件',
  `sync_columns` varchar(255) DEFAULT NULL COMMENT '同步列名，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)\n新增时：源和目标对应的列名\n修改时：源和目标对应的列名\n删除时：源和目标对应的列名（如果源的值 和 目标值不一致，就不处理了；如果一致，那么目标的列值 就设置为空）',
  `insert_enable` int(1) DEFAULT NULL COMMENT '是否开启新增同步',
  `insert_target_pks` varchar(100) DEFAULT NULL COMMENT '新增同步时，目标表的主键Id列名，以及生成规则，暂只支持uuid（列名=uuid），如果此值为空 表示新增时 忽略',
  `insert_target_origin_col` varchar(255) DEFAULT NULL COMMENT '新增同步时，目标表区分来源列名，格式( 目标列=指定来源值sync)；目标就是能够区分数据来源；可以为空，这样忽略区分',
  `insert_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '新增过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `update_enable` int(1) DEFAULT NULL COMMENT '是否开启更新同步',
  `update_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '更新过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `update_new_condition_filter` varchar(255) DEFAULT NULL COMMENT '更新新值的过滤条件，表达式；决定要不要同步',
  `delete_enable` int(1) DEFAULT NULL COMMENT '是否开启删除同步',
  `delete_strategy` int(1) DEFAULT NULL COMMENT '删除策略: 0:也删除对应的行，1：只更新对应的值为空（源和目标值不一致，就忽略更新）2：只更新对应的值为空（源和目标值不一致，也强制更新为NULL）',
  `delete_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '删除过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:未发布 1:发布有效 2：失效',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`,`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='任务规则之mysql';

-- ----------------------------
-- Records of sync_task_rule_mysql
-- ----------------------------
BEGIN;
INSERT INTO `sync_task_rule_mysql` VALUES (1, 'taskid-1', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'goods_id;goods_name;create_time', 1, 'pid=uuid', 'source_type=sync1', 'goods_name==\'abc\'', 1, 'price < 30 && is_del==0', 'price > 50 && is_del==0', 1, 1, NULL, 1, NULL, '2021-10-18 21:56:45');
INSERT INTO `sync_task_rule_mysql` VALUES (2, 'taskid-1', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'is_del', 0, NULL, NULL, NULL, 1, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
INSERT INTO `sync_task_rule_mysql` VALUES (3, 'taskid-3', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'create_by_id', 0, NULL, NULL, NULL, 1, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
INSERT INTO `sync_task_rule_mysql` VALUES (4, 'taskid-4', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'create_by_name', 0, NULL, NULL, NULL, 1, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
INSERT INTO `sync_task_rule_mysql` VALUES (5, 'taskid-5', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'update_by_id', 0, '1', '2', '3', 1, NULL, NULL, 0, 0, NULL, 1, NULL, '2021-10-18 22:21:57');
INSERT INTO `sync_task_rule_mysql` VALUES (6, 'taskid-6', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'update_by_name', 0, NULL, NULL, NULL, 1, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
INSERT INTO `sync_task_rule_mysql` VALUES (7, 'taskid-1', 'test', 'goods', 1, 'test1', 't_goods', 'goods_id', 'price;count;status;checked', 1, 'pid=uuid', 'source_type=sync1', NULL, 1, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sync_task_rule_redis
-- ----------------------------
DROP TABLE IF EXISTS `sync_task_rule_redis`;
CREATE TABLE `sync_task_rule_redis` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` varchar(50) NOT NULL COMMENT '任务id',
  `source_db` varchar(50) DEFAULT NULL COMMENT '源库',
  `source_table` varchar(50) DEFAULT NULL COMMENT '源表',
  `target_id` int(11) DEFAULT NULL COMMENT '目标数据源ID',
  `command` varchar(50) DEFAULT NULL COMMENT '执行命令',
  `key_format` varchar(1000) DEFAULT NULL COMMENT 'key格式',
  `field_format` varchar(1000) DEFAULT NULL COMMENT 'field格式',
  `value_format` varchar(1000) DEFAULT NULL COMMENT 'value格式',
  `expire_time` bigint(20) DEFAULT NULL COMMENT '过期时间   秒',
  `fixed_time` time DEFAULT NULL COMMENT '在每天的固定时间过期',
  `insert_enable` int(1) DEFAULT NULL COMMENT '是否开启新增同步',
  `insert_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '新增过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `update_enable` int(1) DEFAULT NULL COMMENT '是否开启更新同步',
  `update_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '更新过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `update_new_condition_filter` varchar(255) DEFAULT NULL COMMENT '更新新值的过滤条件，表达式；决定要不要同步',
  `delete_enable` int(1) DEFAULT NULL COMMENT '是否开启删除同步',
  `delete_source_condition_filter` varchar(255) DEFAULT NULL COMMENT '删除过滤条件，同步的源数据过滤，表达式；决定要不要同步',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:未发布 1:发布有效 2：失效',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`,`task_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='任务规则之redis';

-- ----------------------------
-- Records of sync_task_rule_redis
-- ----------------------------
BEGIN;
INSERT INTO `sync_task_rule_redis` VALUES (9, 'taskid-2', 'test', 'goods', 2, 'set', 'key_${goods_name}', NULL, '${price}', 50, NULL, 1, NULL, 1, NULL, NULL, 0, NULL, 1, '2021-10-30 22:29:10', '2021-10-31 12:28:58');
INSERT INTO `sync_task_rule_redis` VALUES (10, 'taskid-2', 'test', 'goods', 2, 'delete', 'key_${goods_name}', NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, 1, NULL, 1, '2021-10-31 12:28:40', '2021-10-31 12:30:06');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
