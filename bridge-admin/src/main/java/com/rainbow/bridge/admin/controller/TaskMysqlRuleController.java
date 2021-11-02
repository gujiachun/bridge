package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainbow.bridge.admin.model.TaskMysqlRuleVo;
import com.rainbow.bridge.admin.model.query.TaskMysqlRuleQueryVo;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.biz.service.TaskService;
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
public class TaskMysqlRuleController {

    private final TaskMysqlRuleService taskMysqlRuleService;

    @GetMapping("/task/rule/mysql")
    public Result<PageResult<SyncTaskRuleMysqlEntity>> getList(TaskMysqlRuleQueryVo vo){
        LambdaQueryWrapper<SyncTaskRuleMysqlEntity> queryWrapper = Wrappers.lambdaQuery(SyncTaskRuleMysqlEntity.class);
        queryWrapper.eq(StringUtils.isNotEmpty(vo.getTaskId()), SyncTaskRuleMysqlEntity::getTaskId, vo.getTaskId());
        queryWrapper.eq(vo.getStatus() !=null && vo.getStatus() != -1, SyncTaskRuleMysqlEntity::getStatus, vo.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getTargetTable()), SyncTaskRuleMysqlEntity::getTargetTable, vo.getTargetTable());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getSourceTable()), SyncTaskRuleMysqlEntity::getSourceTable, vo.getSourceTable());
        IPage<SyncTaskRuleMysqlEntity> page = taskMysqlRuleService.page(new Page<>(vo.getCurrentPage(), vo.getPageSize()), queryWrapper);

        return Result.success(new PageResult<>(page.getTotal(), BeanUtil.copyList(page.getRecords(), SyncTaskRuleMysqlEntity.class)));
    }

    @PostMapping("/task/rule/mysql")
    public Result<Boolean> add(@RequestBody TaskMysqlRuleVo vo){
        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setStatus(StatusEnum.audit.getStatus());
        entity.setSyncColumns(vo.getSyncColumns());
        entity.setSyncPks(vo.getSyncPks());
        entity.setTargetDb(vo.getTargetDb());
        entity.setTargetId(vo.getTargetId());
        entity.setTargetTable(vo.getTargetTable());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);

        return Result.success(taskMysqlRuleService.save(entity));
    }

    @PutMapping("/task/rule/mysql")
    public Result<Boolean> update(@RequestBody TaskMysqlRuleVo vo){

        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setSyncColumns(vo.getSyncColumns());
        entity.setSyncPks(vo.getSyncPks());
        entity.setTargetDb(vo.getTargetDb());
        entity.setTargetId(vo.getTargetId());
        entity.setTargetTable(vo.getTargetTable());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskMysqlRuleService.updateById(entity));
    }

    @DeleteMapping("/task/rule/mysql/{id}")
    public Result<Boolean> delete(@PathVariable("id") String id){
        SyncTaskRuleMysqlEntity entity = taskMysqlRuleService.getById(id);
        if (entity == null){
            return Result.success(Boolean.TRUE);
        }
        if (entity.getStatus().equals(StatusEnum.valid.getStatus())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"已发布的任务规则无法删除");
        }

        return Result.success(taskMysqlRuleService.removeById(id));
    }

    @PostMapping("/task/rule/mysql/{id}")
    public Result<Boolean> updateStatus(@PathVariable("id") Integer id,@RequestParam("status") Integer status){
        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        entity.setId(id);
        entity.setStatus(status);
        Date date = new Date();
        entity.setUpdatedTime(date);

        return Result.success(taskMysqlRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/mysql/insert")
    public Result<Boolean> updateInsertRule(@RequestBody TaskMysqlRuleVo vo){

        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        Date date = new Date();
        entity.setInsertSourceConditionFilter(vo.getInsertSourceConditionFilter());
        entity.setInsertTargetOriginCol(vo.getInsertTargetOriginCol());
        entity.setInsertTargetPks(vo.getInsertTargetPks());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskMysqlRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/mysql/update")
    public Result<Boolean> updateUpdatedRule(@RequestBody TaskMysqlRuleVo vo){

        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        Date date = new Date();
        entity.setUpdateNewConditionFilter(vo.getUpdateNewConditionFilter());
        entity.setUpdateSourceConditionFilter(vo.getUpdateSourceConditionFilter());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskMysqlRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/mysql/delete")
    public Result<Boolean> updateDeletedRule(@RequestBody TaskMysqlRuleVo vo){

        SyncTaskRuleMysqlEntity entity = new SyncTaskRuleMysqlEntity();
        Date date = new Date();
        entity.setDeleteSourceConditionFilter(vo.getDeleteSourceConditionFilter());
        entity.setDeleteStrategy(vo.getDeleteStrategy());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskMysqlRuleService.updateById(entity));
    }
}
