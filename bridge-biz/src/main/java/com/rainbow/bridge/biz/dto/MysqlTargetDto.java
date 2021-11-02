package com.rainbow.bridge.biz.dto;

/**
 * @author gujiachun
 */
public class MysqlTargetDto extends TargetDto {

    private Integer id;

    private String dbName;

    private String props;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
