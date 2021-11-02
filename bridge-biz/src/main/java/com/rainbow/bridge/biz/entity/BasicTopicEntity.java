package com.rainbow.bridge.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * topic关联基础表
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("basic_topic")
@ApiModel(value="BasicTopicEntity对象", description="topic关联基础表")
public class BasicTopicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "主题topic")
    private String topic;

    @ApiModelProperty(value = "basic_mq的主键")
    private Integer mqId;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "同步库名")
    private String syncDb;

    @ApiModelProperty(value = "同步表")
    private String syncTable;

    @ApiModelProperty(value = "环境代码")
    private String env;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public Integer getMqId() {
        return mqId;
    }

    public void setMqId(Integer mqId) {
        this.mqId = mqId;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSyncDb() {
        return syncDb;
    }

    public void setSyncDb(String syncDb) {
        this.syncDb = syncDb;
    }
    public String getSyncTable() {
        return syncTable;
    }

    public void setSyncTable(String syncTable) {
        this.syncTable = syncTable;
    }
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "BasicTopicEntity{" +
            "id=" + id +
            ", topic=" + topic +
            ", mqId=" + mqId +
            ", remark=" + remark +
            ", syncDb=" + syncDb +
            ", syncTable=" + syncTable +
            ", env=" + env +
        "}";
    }
}
