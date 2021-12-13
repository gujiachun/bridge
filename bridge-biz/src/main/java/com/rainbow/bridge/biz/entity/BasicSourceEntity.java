package com.rainbow.bridge.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 监听的源库信息
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("basic_source")
@ApiModel(value="BasicSourceEntity对象", description="监听的源库信息")
public class BasicSourceEntity extends BridgeEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "源名")
    private String sourceName;

    @ApiModelProperty(value = "数据库名")
    private String dbName;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "数据源配置格式（k1=v1;k2=v2）")
    private String props;

    @ApiModelProperty(value = "环境代码")
    private String env;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "BasicSourceEntity{" +
                "id=" + id +
                ", sourceName='" + sourceName + '\'' +
                ", dbName='" + dbName + '\'' +
                ", remark='" + remark + '\'' +
                ", props='" + props + '\'' +
                ", env='" + env + '\'' +
                '}';
    }
}
