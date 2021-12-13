package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.entity.BasicSourceTableEntity;
import com.rainbow.bridge.biz.mapper.BasicSourceMapper;
import com.rainbow.bridge.biz.mapper.BasicSourceTableMapper;
import com.rainbow.bridge.biz.service.SourceService;
import com.rainbow.bridge.biz.service.SourceTableService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class SourceTableServiceImpl extends ServiceImpl<BasicSourceTableMapper, BasicSourceTableEntity> implements SourceTableService {
}
