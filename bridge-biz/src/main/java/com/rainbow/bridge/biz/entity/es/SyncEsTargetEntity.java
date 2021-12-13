package com.rainbow.bridge.biz.entity.es;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rainbow.bridge.biz.entity.BridgeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>
 * 同步目标数据源Es
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("sync_es_target")
@ApiModel(value="SyncEsTargetEntity对象", description="同步目标数据源Es")
public class SyncEsTargetEntity extends BridgeEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "目标数据源ID")
    private Integer targetId;

    @ApiModelProperty(value = "链接地址，集群地址；分号隔开")
    private String hosts;

    @ApiModelProperty(value = "es版本")
    private String version;

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

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        return "SyncEsTargetEntity{" +
                "id=" + id +
                ", targetId=" + targetId +
                ", hosts='" + hosts + '\'' +
                ", version='" + version + '\'' +
                ", props='" + props + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
