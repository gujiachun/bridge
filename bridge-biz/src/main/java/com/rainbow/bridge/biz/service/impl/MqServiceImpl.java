package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.mapper.BasicMqMapper;
import com.rainbow.bridge.biz.service.MqService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class MqServiceImpl extends ServiceImpl<BasicMqMapper, BasicMqEntity> implements MqService {
}
