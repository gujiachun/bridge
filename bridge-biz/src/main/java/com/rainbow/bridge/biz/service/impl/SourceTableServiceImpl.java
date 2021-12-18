package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.dto.SourceTableDto;
import com.rainbow.bridge.biz.dto.query.SourceTableQueryDto;
import com.rainbow.bridge.biz.entity.BasicSourceTableEntity;
import com.rainbow.bridge.biz.mapper.BasicSourceTableMapper;
import com.rainbow.bridge.biz.service.SourceTableService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class SourceTableServiceImpl extends ServiceImpl<BasicSourceTableMapper, BasicSourceTableEntity> implements SourceTableService {

    @Override
    public IPage<SourceTableDto> querySourceTable(SourceTableQueryDto sourceTableQueryDto) {
        IPage<SourceTableDto> page = new Page<>(sourceTableQueryDto.getCurrentPage(), sourceTableQueryDto.getPageSize());
        return baseMapper.querySourceTable(page,sourceTableQueryDto);
    }

    @Override
    public void deleteBySourceId(Integer sourceId) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_id",sourceId);

        baseMapper.delete(queryWrapper);
    }
}
