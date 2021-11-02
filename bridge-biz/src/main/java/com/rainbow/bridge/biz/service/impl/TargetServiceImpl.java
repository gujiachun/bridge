package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import com.rainbow.bridge.biz.mapper.SyncTargetMapper;
import com.rainbow.bridge.biz.service.TargetService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TargetServiceImpl extends ServiceImpl<SyncTargetMapper, SyncTargetEntity> implements TargetService {
}
