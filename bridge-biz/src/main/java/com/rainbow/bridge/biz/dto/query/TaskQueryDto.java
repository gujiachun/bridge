package com.rainbow.bridge.biz.dto.query;

import com.rainbow.bridge.core.PageParam;
import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class TaskQueryDto extends PageParam {

    private String name;

    private String env;

    private String targetType;

    private Integer status;
}
