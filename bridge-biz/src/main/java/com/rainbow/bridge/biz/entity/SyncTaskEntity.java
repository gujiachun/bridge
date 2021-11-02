package com.rainbow.bridge.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 同步任务
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("sync_task")
@ApiModel(value="SyncTaskEntity对象", description="同步任务")
public class SyncTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务主键，也会用来做消费者的groupId")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "环境代码")
    private String env;

    @ApiModelProperty(value = "订阅的主题ID")
    private Integer basicTopicId;

    @ApiModelProperty(value = "目标数据源类型 mysql、es、redis、rocketmq")
    private String targetType;

    @ApiModelProperty(value = "发布到哪个集群中，会部署多个集群实例，每个实例在启动是需要指明是cluster_name；代表归属哪个集群")
    private String publishCluster;

    @ApiModelProperty(value = "集群中多少个实例做此任务，暂定一个实例最多可以分配3个任务")
    private Integer instanceCount;

    @ApiModelProperty(value = "是否异步处理消息 0：同步 1：异步")
    private Integer async;

    @ApiModelProperty(value = "状态 0:未发布 1:发布有效 2：失效")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
    public Integer getBasicTopicId() {
        return basicTopicId;
    }

    public void setBasicTopicId(Integer basicTopicId) {
        this.basicTopicId = basicTopicId;
    }
    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    public String getPublishCluster() {
        return publishCluster;
    }

    public void setPublishCluster(String publishCluster) {
        this.publishCluster = publishCluster;
    }
    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getAsync() {
        return async;
    }

    public void setAsync(Integer async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "SyncTaskEntity{" +
            "id=" + id +
            ", name=" + name +
            ", env=" + env +
            ", basicTopicId=" + basicTopicId +
            ", targetType=" + targetType +
            ", publishCluster=" + publishCluster +
            ", instanceCount=" + instanceCount +
            ", status=" + status +
            ", async=" + async +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
        "}";
    }
}
