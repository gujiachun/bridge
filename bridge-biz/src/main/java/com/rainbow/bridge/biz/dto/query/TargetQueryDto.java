package com.rainbow.bridge.biz.dto.query;

import com.rainbow.bridge.core.PageParam;
import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class TargetQueryDto extends PageParam {

    private String name;

    private String env;

}
