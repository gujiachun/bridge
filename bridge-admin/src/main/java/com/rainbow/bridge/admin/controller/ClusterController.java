package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.model.ClusterVo;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.service.ClusterService;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ClusterController {

    private final ClusterService clusterService;

    @GetMapping("/cluster")
    public Result<List<BasicClusterEntity>> getList(){
        return Result.success(clusterService.list());
    }

    @GetMapping("/cluster/env")
    public Result<List<BasicClusterEntity>> getListByEnv(@RequestParam("env") String env){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",env);

        return Result.success(clusterService.list(queryWrapper));
    }

    @PostMapping("/cluster")
    public Result<Boolean> add(@RequestBody ClusterVo vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",vo.getCode());
        BasicClusterEntity one = clusterService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"集群代码必须全局唯一");
        }

        BasicClusterEntity entity = new BasicClusterEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setCode(vo.getCode());
        entity.setRemark(vo.getRemark());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        return Result.success(clusterService.save(entity));
    }

    @PutMapping("/cluster")
    public Result<Boolean> update(@RequestBody ClusterVo vo){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",vo.getCode());
        BasicClusterEntity one = clusterService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(vo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"集群代码必须全局唯一");
        }

        BasicClusterEntity entity = new BasicClusterEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setCode(vo.getCode());
        entity.setRemark(vo.getRemark());
        entity.setId(vo.getId());
        entity.setUpdatedTime(new Date());

        return Result.success(clusterService.updateById(entity));
    }

    @DeleteMapping("/cluster/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(clusterService.removeById(id));
    }

}
