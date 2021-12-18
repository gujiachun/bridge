package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainbow.bridge.admin.model.TaskEsRuleVo;
import com.rainbow.bridge.admin.model.TaskMysqlRuleVo;
import com.rainbow.bridge.admin.model.query.TaskEsRuleQueryVo;
import com.rainbow.bridge.biz.entity.es.SyncTaskRuleEsEntity;
import com.rainbow.bridge.biz.entity.mysql.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.service.es.TaskEsRuleService;
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
public class TaskEsRuleController {

    private final TaskEsRuleService taskEsRuleService;

    @GetMapping("/task/rule/es")
    public Result<PageResult<SyncTaskRuleEsEntity>> getList(TaskEsRuleQueryVo vo){
        LambdaQueryWrapper<SyncTaskRuleEsEntity> queryWrapper = Wrappers.lambdaQuery(SyncTaskRuleEsEntity.class);
        queryWrapper.eq(StringUtils.isNotEmpty(vo.getTaskId()), SyncTaskRuleEsEntity::getTaskId, vo.getTaskId());
        queryWrapper.eq(vo.getStatus() !=null && vo.getStatus() != -1, SyncTaskRuleEsEntity::getStatus, vo.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getSourceTable()), SyncTaskRuleEsEntity::getSourceTable, vo.getSourceTable());
        IPage<SyncTaskRuleEsEntity> page = taskEsRuleService.page(new Page<>(vo.getCurrentPage(), vo.getPageSize()), queryWrapper);

        return Result.success(new PageResult<>(page.getTotal(), BeanUtil.copyList(page.getRecords(), SyncTaskRuleEsEntity.class)));
    }

    @PostMapping("/task/rule/es")
    public Result<Boolean> add(@RequestBody TaskEsRuleVo vo){
        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setStatus(StatusEnum.audit.getStatus());
        entity.setTargetId(vo.getTargetId());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);

        entity.setIndexFormat(vo.getIndexFormat());
        entity.setIdFormat(vo.getIdFormat());
        entity.setIndexType(vo.getIndexType());
        entity.setPartFormat(vo.getPartFormat());
        entity.setSqlFormat(vo.getSqlFormat());
        entity.setSqlFieldFormat(vo.getSqlFieldFormat());
        entity.setSqlNullField(vo.getSqlNullField());
        entity.setFieldType(vo.getFieldType());
        entity.setSkipsField(vo.getSkipsField());

        entity.setRelationType(vo.getRelationType());
        entity.setRelationFieldName(vo.getRelationFieldName());
        entity.setRelationJoinName(vo.getRelationJoinName());
        entity.setRelationChildParentFormat(vo.getRelationChildParentFormat());
        entity.setRelationChildRouteFormat(vo.getRelationChildRouteFormat());

        return Result.success(taskEsRuleService.save(entity));
    }

    @PutMapping("/task/rule/es")
    public Result<Boolean> update(@RequestBody TaskEsRuleVo vo){

        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        entity.setDeleteEnable(vo.getDeleteEnable());
        entity.setInsertEnable(vo.getInsertEnable());
        entity.setSourceDb(vo.getSourceDb());
        entity.setSourceTable(vo.getSourceTable());
        Date date = new Date();
        entity.setTargetId(vo.getTargetId());
        entity.setTaskId(vo.getTaskId());
        entity.setUpdateEnable(vo.getUpdateEnable());
        entity.setUpdatedTime(date);

        entity.setIndexFormat(vo.getIndexFormat());
        entity.setIdFormat(vo.getIdFormat());
        entity.setIndexType(vo.getIndexType());
        entity.setPartFormat(vo.getPartFormat());
        entity.setSqlFormat(vo.getSqlFormat());
        entity.setSqlFieldFormat(vo.getSqlFieldFormat());
        entity.setSqlNullField(vo.getSqlNullField());
        entity.setFieldType(vo.getFieldType());
        entity.setSkipsField(vo.getSkipsField());

        entity.setRelationType(vo.getRelationType());
        entity.setRelationFieldName(vo.getRelationFieldName());
        entity.setRelationJoinName(vo.getRelationJoinName());
        entity.setRelationChildParentFormat(vo.getRelationChildParentFormat());
        entity.setRelationChildRouteFormat(vo.getRelationChildRouteFormat());

        entity.setId(vo.getId());

        return Result.success(taskEsRuleService.updateById(entity));
    }

    @DeleteMapping("/task/rule/es/{id}")
    public Result<Boolean> delete(@PathVariable("id") String id){
        SyncTaskRuleEsEntity entity = taskEsRuleService.getById(id);
        if (entity == null){
            return Result.success(Boolean.TRUE);
        }
        if (entity.getStatus().equals(StatusEnum.valid.getStatus())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"已发布的任务规则无法删除");
        }

        return Result.success(taskEsRuleService.removeById(id));
    }

    @PostMapping("/task/rule/es/{id}")
    public Result<Boolean> updateStatus(@PathVariable("id") Integer id,@RequestParam("status") Integer status){
        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        entity.setId(id);
        entity.setStatus(status);
        Date date = new Date();
        entity.setUpdatedTime(date);

        return Result.success(taskEsRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/es/insert")
    public Result<Boolean> updateInsertRule(@RequestBody TaskEsRuleVo vo){

        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        Date date = new Date();
        entity.setInsertSourceConditionFilter(vo.getInsertSourceConditionFilter());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskEsRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/es/update")
    public Result<Boolean> updateUpdatedRule(@RequestBody TaskEsRuleVo vo){

        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        Date date = new Date();
        entity.setUpdateNewConditionFilter(vo.getUpdateNewConditionFilter());
        entity.setUpdateSourceConditionFilter(vo.getUpdateSourceConditionFilter());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskEsRuleService.updateById(entity));
    }

    @PutMapping("/task/rule/es/delete")
    public Result<Boolean> updateDeletedRule(@RequestBody TaskEsRuleVo vo){

        SyncTaskRuleEsEntity entity = new SyncTaskRuleEsEntity();
        Date date = new Date();
        entity.setDeleteSourceConditionFilter(vo.getDeleteSourceConditionFilter());
        entity.setDeleteStrategy(vo.getDeleteStrategy());
        entity.setUpdatedTime(date);
        entity.setId(vo.getId());

        return Result.success(taskEsRuleService.updateById(entity));
    }
}
