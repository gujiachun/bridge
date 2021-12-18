package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.admin.model.SourceTableVo;
import com.rainbow.bridge.biz.dto.SourceTableDto;
import com.rainbow.bridge.biz.dto.query.SourceTableQueryDto;
import com.rainbow.bridge.biz.entity.BasicSourceTableEntity;
import com.rainbow.bridge.biz.service.SourceTableService;
import com.rainbow.bridge.core.PageResult;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SourceTableController {

    private static final Logger logger = LoggerFactory.getLogger(SourceTableController.class);

    private final SourceTableService sourceTableService;

    @GetMapping("/source-table")
    public Result<List<BasicSourceTableEntity>> getList(){
        return Result.success(sourceTableService.list());
    }

    @GetMapping("/source-table/search")
    public Result<PageResult<SourceTableDto>> getListByEnv(SourceTableQueryDto sourceTableQueryDto){
        IPage<SourceTableDto> sourceTableDtoIPage = sourceTableService.querySourceTable(sourceTableQueryDto);
        return Result.success(new PageResult<>(sourceTableDtoIPage.getTotal(),sourceTableDtoIPage.getRecords()));
    }

    @GetMapping("/source-table/sourceId")
    public Result<List<BasicSourceTableEntity>> getListBySourceId(@RequestParam("sourceId") Integer sourceId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_id",sourceId);
        return Result.success(sourceTableService.list(queryWrapper));
    }

    @PostMapping("/source-table")
    public Result<Boolean> add(@RequestBody SourceTableVo vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_id",vo.getSourceId());
        queryWrapper.eq("table_name",vo.getTableName());
        BasicSourceTableEntity one = sourceTableService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"同一个源下，表名必须唯一");
        }

        BasicSourceTableEntity entity = new BasicSourceTableEntity();
        entity.setSourceId(vo.getSourceId());
        entity.setTableName(vo.getTableName());
        entity.setRemark(vo.getRemark());

        return Result.success(sourceTableService.save(entity));
    }

    @PutMapping("/source-table")
    public Result<Boolean> update(@RequestBody SourceTableVo vo){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_id",vo.getSourceId());
        queryWrapper.eq("table_name",vo.getTableName());
        BasicSourceTableEntity one = sourceTableService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(vo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"同一个源下，表名必须唯一");
        }

        BasicSourceTableEntity entity = new BasicSourceTableEntity();
        entity.setSourceId(vo.getSourceId());
        entity.setTableName(vo.getTableName());
        entity.setRemark(vo.getRemark());
        entity.setId(vo.getId());

        return Result.success(sourceTableService.updateById(entity));
    }

    @DeleteMapping("/source-table/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(sourceTableService.removeById(id));
    }

}
