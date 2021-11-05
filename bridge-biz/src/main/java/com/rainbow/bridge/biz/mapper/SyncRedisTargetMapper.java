package com.rainbow.bridge.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.RedisTargetDto;
import com.rainbow.bridge.biz.dto.TargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncRedisTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步目标数据源Mysql Mapper 接口
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
public interface SyncRedisTargetMapper extends BaseMapper<SyncRedisTargetEntity> {

    IPage<RedisTargetDto> queryRedisTarget(IPage<RedisTargetDto> page, @Param("model") TargetQueryDto targetQueryDto);
}
