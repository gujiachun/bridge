package com.rainbow.bridge.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * mq关联基础表
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("basic_mq")
@ApiModel(value="BasicMqEntity对象", description="mq关联基础表")
public class BasicMqEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "mq名称")
    private String name;

    @ApiModelProperty(value = "mq类型 kafka、rocketmq")
    private String mqType;

    @ApiModelProperty(value = "链接服务器地址")
    private String servers;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "环境代码")
    private String env;

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
    public String getMqType() {
        return mqType;
    }

    public void setMqType(String mqType) {
        this.mqType = mqType;
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

    @Override
    public String toString() {
        return "BasicMqEntity{" +
            "id=" + id +
            ", name=" + name +
            ", mqType=" + mqType +
            ", servers=" + servers +
            ", remark=" + remark +
            ", env=" + env +
        "}";
    }
}
