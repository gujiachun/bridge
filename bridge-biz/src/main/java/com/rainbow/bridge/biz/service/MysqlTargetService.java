package com.rainbow.bridge.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;

import java.util.List;

public interface MysqlTargetService extends IService<SyncMysqlTargetEntity> {

    SyncMysqlTargetEntity getSyncMysqlTargetByTargetId(Integer targetId);

    IPage<MysqlTargetDto> query(TargetQueryDto targetQueryDto);

    Boolean add(MysqlTargetDto dto);

    Boolean update(MysqlTargetDto dto);

    Boolean remove(Integer id);
}
