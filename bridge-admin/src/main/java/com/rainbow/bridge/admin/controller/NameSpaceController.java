package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.model.NsVo;
import com.rainbow.bridge.biz.entity.BasicNamespaceEntity;
import com.rainbow.bridge.biz.service.NameSpaceService;
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
public class NameSpaceController {

    private final NameSpaceService nameSpaceService;

    @GetMapping("/ns")
    public Result<List<BasicNamespaceEntity>> getNameSpaces(){
        return Result.success(nameSpaceService.list());
    }

    @PostMapping("/ns")
    public Result<Boolean> addNameSpace(@RequestBody NsVo nsVo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",nsVo.getEnv());
        BasicNamespaceEntity one = nameSpaceService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"环境代码必须全局唯一");
        }

        BasicNamespaceEntity basicNamespaceEntity = new BasicNamespaceEntity();
        basicNamespaceEntity.setName(nsVo.getName());
        basicNamespaceEntity.setEnv(nsVo.getEnv());
        Date date = new Date();
        basicNamespaceEntity.setCreatedTime(date);
        basicNamespaceEntity.setUpdatedTime(date);
        return Result.success(nameSpaceService.save(basicNamespaceEntity));
    }

    @PutMapping("/ns")
    public Result<Boolean> updateNameSpace(@RequestBody NsVo nsVo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",nsVo.getEnv());
        BasicNamespaceEntity one = nameSpaceService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(nsVo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"环境代码必须全局唯一");
        }
        BasicNamespaceEntity basicNamespaceEntity = new BasicNamespaceEntity();
        basicNamespaceEntity.setName(nsVo.getName());
        basicNamespaceEntity.setEnv(nsVo.getEnv());
        basicNamespaceEntity.setId(nsVo.getId());
        basicNamespaceEntity.setUpdatedTime(new Date());
        return Result.success(nameSpaceService.updateById(basicNamespaceEntity));
    }

    @DeleteMapping("/ns/{id}")
    public Result<Boolean> deleteNameSpace(@PathVariable("id") Integer id){
        return Result.success(nameSpaceService.removeById(id));
    }

}
