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
@TableName("basic_source_table")
@ApiModel(value="BasicSourceTableEntity对象", description="监听的源库中的表信息")
public class BasicSourceTableEntity extends BridgeEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "源id")
    private Integer sourceId;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "描述")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "BasicSourceTableEntity{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", tableName='" + tableName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
