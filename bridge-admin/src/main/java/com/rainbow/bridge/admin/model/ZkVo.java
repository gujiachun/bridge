package com.rainbow.bridge.admin.model;

import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class ZkVo {

    private Integer id;

    private String name;

    private String env;

    private String servers;

    private String remark;

    private String rootPath;
}
