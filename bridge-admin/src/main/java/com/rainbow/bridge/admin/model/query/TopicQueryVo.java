package com.rainbow.bridge.admin.model.query;

import com.rainbow.bridge.core.PageParam;
import lombok.Data;

/**
 * @author gujiachun
 */
@Data
public class TopicQueryVo extends PageParam {

    private String topic;

    private String env;

}
