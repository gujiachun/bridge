package com.rainbow.bridge.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.dto.SourceTableDto;
import com.rainbow.bridge.biz.dto.query.SourceTableQueryDto;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.entity.BasicSourceTableEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author gujiachun
 */
public interface SourceTableService extends IService<BasicSourceTableEntity> {

    IPage<SourceTableDto> querySourceTable(SourceTableQueryDto sourceTableQueryDto);

    void deleteBySourceId(Integer sourceId);
}
