package com.rainbow.bridge.biz.dto.query;

import com.rainbow.bridge.core.PageParam;
import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class SourceTableQueryDto extends PageParam {

    private String tableName;

    private Integer sourceId;
}
