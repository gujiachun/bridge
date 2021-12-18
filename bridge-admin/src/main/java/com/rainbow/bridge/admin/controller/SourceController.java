package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.model.SourceVo;
import com.rainbow.bridge.biz.entity.BasicSourceEntity;
import com.rainbow.bridge.biz.service.SourceService;
import com.rainbow.bridge.biz.service.SourceTableService;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SourceController {

    private static final Logger logger = LoggerFactory.getLogger(SourceController.class);

    private final SourceService sourceService;

    private final SourceTableService sourceTableService;

    @GetMapping("/source")
    public Result<List<BasicSourceEntity>> getList(){
        return Result.success(sourceService.list());
    }

    @GetMapping("/source/query/env")
    public Result<List<BasicSourceEntity>> getListByEnv(@RequestParam("env") String env){
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(env)){
            queryWrapper.eq("env",env);
        }

        return Result.success(sourceService.list(queryWrapper));
    }

    @GetMapping("/source/{id}")
    public Result<BasicSourceEntity> getById(@PathVariable("id") Integer id){
        return Result.success(sourceService.getById(id));
    }

    @PostMapping("/source")
    public Result<Boolean> add(@RequestBody SourceVo vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_name",vo.getSourceName());
        BasicSourceEntity one = sourceService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"源名称必须全局唯一");
        }

        BasicSourceEntity entity = new BasicSourceEntity();
        entity.setDbName(vo.getDbName());
        entity.setEnv(vo.getEnv());
        entity.setSourceName(vo.getSourceName());
        entity.setProps(vo.getProps());
        entity.setRemark(vo.getRemark());

        return Result.success(sourceService.save(entity));
    }

    @PutMapping("/source")
    public Result<Boolean> update(@RequestBody SourceVo vo){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("source_name",vo.getSourceName());
        BasicSourceEntity one = sourceService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(vo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"源名称必须全局唯一");
        }

        BasicSourceEntity entity = new BasicSourceEntity();
        entity.setDbName(vo.getDbName());
        entity.setEnv(vo.getEnv());
        entity.setSourceName(vo.getSourceName());
        entity.setProps(vo.getProps());
        entity.setRemark(vo.getRemark());
        entity.setId(vo.getId());

        return Result.success(sourceService.updateById(entity));
    }

    @DeleteMapping("/source/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        sourceTableService.deleteBySourceId(id);
        return Result.success(sourceService.removeById(id));
    }

}
