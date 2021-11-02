package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.RedisTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncRedisTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import com.rainbow.bridge.biz.mapper.SyncMysqlTargetMapper;
import com.rainbow.bridge.biz.mapper.SyncRedisTargetMapper;
import com.rainbow.bridge.biz.service.MysqlTargetService;
import com.rainbow.bridge.biz.service.RedisTargetService;
import com.rainbow.bridge.biz.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author gujiachun
 */
@Service
public class RedisTargetServiceImpl extends ServiceImpl<SyncRedisTargetMapper, SyncRedisTargetEntity> implements RedisTargetService {

    @Autowired
    private TargetService targetService;

    @Override
    public SyncRedisTargetEntity getSyncRedisTargetByTargetId(Integer targetId) {

        QueryWrapper<SyncRedisTargetEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id",targetId);

        return getOne(wrapper);
    }

    @Override
    public IPage<RedisTargetDto> query(TargetQueryDto targetQueryDto) {
        IPage<RedisTargetDto> page = new Page<>(targetQueryDto.getCurrentPage(), targetQueryDto.getPageSize());
        return baseMapper.query(page, targetQueryDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(RedisTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        targetService.save(entity);

        SyncRedisTargetEntity syncRedisTargetEntity = new SyncRedisTargetEntity();
        syncRedisTargetEntity.setTargetId(entity.getId());
        syncRedisTargetEntity.setMode(dto.getMode());
        syncRedisTargetEntity.setProps(dto.getProps());
        syncRedisTargetEntity.setCreatedTime(date);
        syncRedisTargetEntity.setUpdatedTime(date);

        save(syncRedisTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(RedisTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        entity.setId(dto.getTargetId());
        Date date = new Date();
        entity.setUpdatedTime(date);
        targetService.updateById(entity);

        SyncRedisTargetEntity syncRedisTargetEntity = new SyncRedisTargetEntity();
        syncRedisTargetEntity.setTargetId(dto.getTargetId());
        syncRedisTargetEntity.setId(dto.getId());
        syncRedisTargetEntity.setMode(dto.getMode());
        syncRedisTargetEntity.setProps(dto.getProps());
        syncRedisTargetEntity.setUpdatedTime(date);

        updateById(syncRedisTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(Integer id) {
        SyncRedisTargetEntity syncRedisTargetEntity = getById(id);
        removeById(id);
        targetService.removeById(syncRedisTargetEntity.getTargetId());
        return Boolean.TRUE;
    }


}
