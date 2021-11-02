package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicNamespaceEntity;
import com.rainbow.bridge.biz.mapper.BasicNamespaceMapper;
import com.rainbow.bridge.biz.service.NameSpaceService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class NameSpaceServiceImpl extends ServiceImpl<BasicNamespaceMapper, BasicNamespaceEntity> implements NameSpaceService {
}
