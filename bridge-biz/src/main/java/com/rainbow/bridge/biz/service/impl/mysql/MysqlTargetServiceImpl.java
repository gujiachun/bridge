package com.rainbow.bridge.biz.service.impl.mysql;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.*;
import com.rainbow.bridge.biz.entity.mysql.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.mapper.mysql.SyncMysqlTargetMapper;
import com.rainbow.bridge.biz.service.*;
import com.rainbow.bridge.biz.service.mysql.MysqlTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author gujiachun
 */
@Service
public class MysqlTargetServiceImpl extends ServiceImpl<SyncMysqlTargetMapper, SyncMysqlTargetEntity> implements MysqlTargetService {

    @Autowired
    private TargetService targetService;

    @Override
    public SyncMysqlTargetEntity getSyncMysqlTargetByTargetId(Integer targetId) {

        QueryWrapper<SyncMysqlTargetEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id",targetId);

        return getOne(wrapper);
    }

    @Override
    public IPage<MysqlTargetDto> query(TargetQueryDto targetQueryDto) {
        IPage<MysqlTargetDto> page = new Page<>(targetQueryDto.getCurrentPage(), targetQueryDto.getPageSize());
        return baseMapper.queryMysqlTarget(page, targetQueryDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(MysqlTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        targetService.save(entity);

        SyncMysqlTargetEntity syncMysqlTargetEntity = new SyncMysqlTargetEntity();
        syncMysqlTargetEntity.setTargetId(entity.getId());
        syncMysqlTargetEntity.setDbName(dto.getDbName());
        syncMysqlTargetEntity.setProps(dto.getProps());
        syncMysqlTargetEntity.setCreatedTime(date);
        syncMysqlTargetEntity.setUpdatedTime(date);

        save(syncMysqlTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(MysqlTargetDto dto) {
        SyncTargetEntity entity = new SyncTargetEntity();
        entity.setEnv(dto.getEnv());
        entity.setName(dto.getName());
        entity.setRemark(dto.getRemark());
        entity.setType(dto.getType());
        entity.setId(dto.getTargetId());
        Date date = new Date();
        entity.setUpdatedTime(date);
        targetService.updateById(entity);

        SyncMysqlTargetEntity syncMysqlTargetEntity = new SyncMysqlTargetEntity();
        syncMysqlTargetEntity.setTargetId(dto.getTargetId());
        syncMysqlTargetEntity.setId(dto.getId());
        syncMysqlTargetEntity.setDbName(dto.getDbName());
        syncMysqlTargetEntity.setProps(dto.getProps());
        syncMysqlTargetEntity.setUpdatedTime(date);

        updateById(syncMysqlTargetEntity);

        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(Integer id) {
        SyncMysqlTargetEntity syncMysqlTargetEntity = getById(id);
        removeById(id);
        targetService.removeById(syncMysqlTargetEntity.getTargetId());
        return Boolean.TRUE;
    }


}
