package com.rainbow.bridge.admin.model.query;

import com.rainbow.bridge.core.PageParam;
import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class TaskEsRuleQueryVo extends PageParam {

    private String taskId;

    private String sourceTable;

    private Integer status;

}
