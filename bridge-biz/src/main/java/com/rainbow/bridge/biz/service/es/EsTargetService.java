package com.rainbow.bridge.biz.service.es;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.dto.EsTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.es.SyncEsTargetEntity;

public interface EsTargetService extends IService<SyncEsTargetEntity> {

    SyncEsTargetEntity getSyncEsTargetByTargetId(Integer targetId);

    IPage<EsTargetDto> query(TargetQueryDto targetQueryDto);

    Boolean add(EsTargetDto dto);

    Boolean update(EsTargetDto dto);

    Boolean remove(Integer id);
}
