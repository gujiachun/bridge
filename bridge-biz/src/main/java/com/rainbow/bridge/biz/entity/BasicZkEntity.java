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
 * zookeeper基础关联表
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("basic_zk")
@ApiModel(value="BasicZkEntity对象", description="zookeeper基础关联表")
public class BasicZkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "server地址")
    private String servers;

    @ApiModelProperty(value = "zk根节点名称")
    private String rootPath;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "环境代码")
    private String env;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
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

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String toString() {
        return "BasicZkEntity{" +
            "id=" + id +
            ", name=" + name +
            ", servers=" + servers +
            ", rootPath=" + rootPath +
            ", remark=" + remark +
            ", env=" + env +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
        "}";
    }
}
