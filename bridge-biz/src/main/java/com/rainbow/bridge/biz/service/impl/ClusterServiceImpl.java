package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.mapper.BasicClusterMapper;
import com.rainbow.bridge.biz.service.ClusterService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class ClusterServiceImpl extends ServiceImpl<BasicClusterMapper, BasicClusterEntity> implements ClusterService {
}
