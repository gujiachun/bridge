package com.rainbow.bridge.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.SourceTableDto;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.SourceTableQueryDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.entity.BasicSourceTableEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 源表 Mapper 接口
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
public interface BasicSourceTableMapper extends BaseMapper<BasicSourceTableEntity> {
    IPage<SourceTableDto> querySourceTable(IPage<SourceTableDto> page, @Param("model") SourceTableQueryDto sourceTableQueryDto);
}
