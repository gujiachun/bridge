package com.rainbow.bridge.admin.controller;

import com.rainbow.bridge.admin.model.MqVo;
import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.service.MqService;
import com.rainbow.bridge.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MqController {

    private final MqService mqService;

    @GetMapping("/mq")
    public Result<List<BasicMqEntity>> getList(){
        return Result.success(mqService.list());
    }

    @PostMapping("/mq")
    public Result<Boolean> add(@RequestBody MqVo vo){
        BasicMqEntity entity = new BasicMqEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setMqType(vo.getMqType());
        entity.setRemark(vo.getRemark());
        entity.setServers(vo.getServers());
        return Result.success(mqService.save(entity));
    }

    @PutMapping("/mq")
    public Result<Boolean> update(@RequestBody MqVo vo){

        BasicMqEntity entity = new BasicMqEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setMqType(vo.getMqType());
        entity.setRemark(vo.getRemark());
        entity.setServers(vo.getServers());
        entity.setId(vo.getId());

        return Result.success(mqService.updateById(entity));
    }

    @DeleteMapping("/mq/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(mqService.removeById(id));
    }

}
