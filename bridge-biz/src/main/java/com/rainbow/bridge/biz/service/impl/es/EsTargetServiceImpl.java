package com.rainbow.bridge.biz.service.impl.es;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.dto.EsTargetDto;
import com.rainbow.bridge.biz.dto.RedisTargetDto;
import com.rainbow.bridge.biz.dto.TargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import com.rainbow.bridge.biz.entity.es.SyncEsTargetEntity;
import com.rainbow.bridge.biz.entity.redis.SyncRedisTargetEntity;
import com.rainbow.bridge.biz.mapper.es.SyncEsTargetMapper;
import com.rainbow.bridge.biz.mapper.redis.SyncRedisTargetMapper;
import com.rainbow.bridge.biz.service.TargetService;
import com.rainbow.bridge.biz.service.es.EsTargetService;
import com.rainbow.bridge.biz.service.redis.RedisTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author gujiachun
 */
@Service
public class EsTargetServiceImpl extends ServiceImpl<SyncEsTargetMapper, SyncEsTargetEntity> implements EsTargetService {

    @Autowired
    private TargetService targetService;

    @Override
    public SyncEsTargetEntity getSyncEsTargetByTargetId(Integer targetId) {

        QueryWrapper<SyncEsTargetEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id",targetId);

        return getOne(wrapper);
    }

    @Override
    public IPage<EsTargetDto> query(TargetQueryDto targetQueryDto) {
        IPage<EsTargetDto> page = new Page<>(targetQueryDto.getCurrentPage(), targetQueryDto.getPageSize());
        return baseMapper.queryEsTarget(page, targetQueryDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(EsTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        targetService.save(entity);

        SyncEsTargetEntity syncEsTargetEntity = new SyncEsTargetEntity();
        syncEsTargetEntity.setTargetId(entity.getId());
        syncEsTargetEntity.setHosts(dto.getHosts());
        syncEsTargetEntity.setVersion(dto.getVersion());
        syncEsTargetEntity.setProps(dto.getProps());
        syncEsTargetEntity.setCreatedTime(date);
        syncEsTargetEntity.setUpdatedTime(date);

        save(syncEsTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(EsTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        entity.setId(dto.getTargetId());
        Date date = new Date();
        entity.setUpdatedTime(date);
        targetService.updateById(entity);

        SyncEsTargetEntity syncEsTargetEntity = new SyncEsTargetEntity();
        syncEsTargetEntity.setTargetId(dto.getTargetId());
        syncEsTargetEntity.setId(dto.getId());
        syncEsTargetEntity.setHosts(dto.getHosts());
        syncEsTargetEntity.setVersion(dto.getVersion());
        syncEsTargetEntity.setProps(dto.getProps());
        syncEsTargetEntity.setUpdatedTime(date);

        updateById(syncEsTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(Integer id) {
        SyncEsTargetEntity syncEsTargetEntity = getById(id);
        removeById(id);
        targetService.removeById(syncEsTargetEntity.getTargetId());
        return Boolean.TRUE;
    }


}
