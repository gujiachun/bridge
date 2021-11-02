package com.rainbow.bridge.admin.model;

import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class TopicVo {

    private Integer id;

    private String topic;

    private String env;

    private Integer mqId;

    private String remark;

    private String syncDb;

    private String syncTable;
}
