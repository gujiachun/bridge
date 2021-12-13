package com.rainbow.bridge.biz.service.redis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.dto.RedisTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.redis.SyncRedisTargetEntity;

public interface RedisTargetService extends IService<SyncRedisTargetEntity> {

    SyncRedisTargetEntity getSyncRedisTargetByTargetId(Integer targetId);

    IPage<RedisTargetDto> query(TargetQueryDto targetQueryDto);

    Boolean add(RedisTargetDto dto);

    Boolean update(RedisTargetDto dto);

    Boolean remove(Integer id);
}
