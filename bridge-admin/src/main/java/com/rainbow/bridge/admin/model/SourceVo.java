package com.rainbow.bridge.admin.model;

import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class SourceVo {

    private Integer id;

    private String sourceName;

    private String dbName;

    private String env;

    private String props;

    private String remark;
}
