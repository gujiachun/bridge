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
 * 同步目标数据源Mysql
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("sync_mysql_target")
@ApiModel(value="SyncMysqlTargetEntity对象", description="同步目标数据源Mysql")
public class SyncMysqlTargetEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "目标数据源ID")
    private Integer targetId;

    @ApiModelProperty(value = "数据库名")
    private String dbName;

    @ApiModelProperty(value = "数据源配置格式（k1=v1;k2=v2）")
    private String props;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
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

    @Override
    public String toString() {
        return "SyncMysqlTargetEntity{" +
            "id=" + id +
            ", targetId=" + targetId +
            ", dbName=" + dbName +
            ", props=" + props +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
        "}";
    }
}
