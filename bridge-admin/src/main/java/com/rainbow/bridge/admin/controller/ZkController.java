package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.model.ZkVo;
import com.rainbow.bridge.biz.entity.BasicZkEntity;
import com.rainbow.bridge.biz.service.ZkService;
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
public class ZkController {

    private final ZkService zkService;

    @GetMapping("/zk")
    public Result<List<BasicZkEntity>> getList(){
        return Result.success(zkService.list());
    }

    @PostMapping("/zk")
    public Result<Boolean> add(@RequestBody ZkVo vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",vo.getEnv());
        BasicZkEntity one = zkService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"zk在每个命名空间中只允许一个");
        }

        BasicZkEntity entity = new BasicZkEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setRootPath(vo.getRootPath());
        entity.setRemark(vo.getRemark());
        entity.setServers(vo.getServers());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        return Result.success(zkService.save(entity));
    }

    @PutMapping("/zk")
    public Result<Boolean> update(@RequestBody ZkVo vo){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",vo.getEnv());
        BasicZkEntity one = zkService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(vo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"zk在每个命名空间中只允许一个");
        }

        BasicZkEntity entity = new BasicZkEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setRootPath(vo.getRootPath());
        entity.setRemark(vo.getRemark());
        entity.setServers(vo.getServers());
        entity.setId(vo.getId());
        entity.setUpdatedTime(new Date());

        return Result.success(zkService.updateById(entity));
    }

    @DeleteMapping("/zk/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(zkService.removeById(id));
    }

}
