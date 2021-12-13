package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.mapper.BasicSourceMapper;
import com.rainbow.bridge.biz.mapper.BasicTopicMapper;
import com.rainbow.bridge.biz.service.SourceService;
import com.rainbow.bridge.biz.service.TopicService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class SourceServiceImpl extends ServiceImpl<BasicSourceMapper, BasicSourceEntity> implements SourceService {
}
