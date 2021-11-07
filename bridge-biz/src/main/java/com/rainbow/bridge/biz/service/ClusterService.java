package com.rainbow.bridge.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;

import java.util.List;

/**
 * @author gujiachun
 */
public interface ClusterService extends IService<BasicClusterEntity> {

    List<BasicClusterEntity> getListByEnv(String env);

}
