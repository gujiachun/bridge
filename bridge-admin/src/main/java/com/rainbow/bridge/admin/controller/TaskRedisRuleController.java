package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainbow.bridge.admin.model.TaskMysqlRuleVo;
import com.rainbow.bridge.admin.model.TaskRedisRuleVo;
import com.rainbow.bridge.admin.model.query.TaskMysqlRuleQueryVo;
import com.rainbow.bridge.admin.model.query.TaskRedisRuleQueryVo;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import com.rainbow.bridge.core.PageResult;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TaskRedisRuleController {

    private final TaskRedisRuleService taskRedisRuleService;

    @GetMapping("/task/rule/redis")
    public Result<PageResult<SyncTaskRuleRedisEntity>> getList(TaskRedisRuleQueryVo vo){
        LambdaQueryWrapper<SyncTaskRuleRedisEntity> queryWrapper = Wrappers.lambdaQuery(SyncTaskRuleRedisEntity.class);
        queryWrapper.eq(StringUtils.isNotEmpty(vo.getTaskId()), SyncTaskRuleRedisEntity::getTaskId, vo.getTaskId());
        queryWrapper.eq(vo.getStatus() !=null && vo.getStatus() != -1, SyncTaskRuleRedisEntity::getStatus, vo.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getTargetId()), SyncTaskRuleRedisEntity::getTargetId, vo.getTargetId());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getSourceTable()), SyncTaskRuleRedisEntity::getSourceTable, vo.getSourceTable());
        IPage<SyncTaskRuleRedisEntity> page = taskRedisRuleService.page(new Page<>(vo.getCurrentPage(), vo.getPageSize()), queryWrapper);

        return Result.success(new PageResult<>(page.getTotal(), BeanUtil.copyList(page.getRecords(), SyncTaskRuleRedisEntity.class)));
    }

    @PostMapping("/task/rule/redis")
    public Result<Boolean> add(@RequestBody TaskRedisRuleVo vo){
        SyncTaskRuleRedisEntity entity = new SyncTaskRuleRedisEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setStatus(StatusEnum.audit.getStatus());
        entity.setCommand(vo.getCommand());
        entity.setExpireTime(vo.getExpireTime());
        entity.setFixedTime(vo.getFixedTime());
        entity.setTargetId(vo.getTargetId());
        entity.setFieldFormat(vo.getFieldFormat());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);
        entity.setValueFormat(vo.getValueFormat());
        entity.setKeyFormat(vo.getKeyFormat());

        return Result.success(taskRedisRuleService.save(entity));
    }

    @PutMapping("/task/rule/redis")
    public Result<Boolean> update(@RequestBody TaskRedisRuleVo vo){

        SyncTaskRuleRedisEntity entity = new SyncTaskRuleRedisEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setCommand(vo.getCommand());
        entity.setExpireTime(vo.getExpireTime());
        entity.setFixedTime(vo.getFixedTime());
        entity.setTargetId(vo.getTargetId());
        entity.setFieldFormat(vo.getFieldFormat());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);
        entity.setValueFormat(vo.getValueFormat());
        entity.setKeyFormat(vo.getKeyFormat());
        entity.setId(vo.getId());

        return Result.success(taskRedisRuleService.updateById(entity));
    }

    @DeleteMapping("/task/rule/redis/{id}")
    public Result<Boolean> delete(@PathVariable("id") String id){
        SyncTaskRuleRedisEntity entity = taskRedisRuleService.getById(id);
        if (entity == null){
            return Result.success(Boolean.TRUE);
        }
        if (entity.getStatus().equals(StatusEnum.valid.getStatus())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"已发布的任务规则无法删除");
        }

        return Result.success(taskRedisRuleService.removeById(id));
    }

    @PostMapping("/task/rule/redis/{id}")
    public Result<Boolean> updateStatus(@PathVariable("id") Integer id,@RequestParam("status") Integer status){
        SyncTaskRuleRedisEntity entity = new SyncTaskRuleRedisEntity();
        entity.setId(id);
        entity.setStatus(status);
        Date date = new Date();
        entity.setUpdatedTime(date);

        return Result.success(taskRedisRuleService.updateById(entity));
    }

}
