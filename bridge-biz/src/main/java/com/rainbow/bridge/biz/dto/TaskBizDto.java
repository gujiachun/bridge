package com.rainbow.bridge.biz.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author gujiachun
 */
@Data
public class TaskBizDto {

    private String id;

    private String name;

    private String env;

    private Integer basicTopicId;

    private String topic;

    private String mqName;

    private String targetType;

    private String publishCluster;

    private String clusterName;

    private Integer instanceCount;

    private Integer activeCount;

    private Integer async;

    private Integer status;

    private Date createdTime;

    private Date updatedTime;
}
