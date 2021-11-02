package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.SyncTargetEntity;
import com.rainbow.bridge.biz.service.TargetService;
import com.rainbow.bridge.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TargetController {

    private final TargetService targetService;

    @GetMapping("/target/{type}/{env}")
    public Result<List<SyncTargetEntity>> getListByType(@PathVariable("type") String type,
                                                        @PathVariable("env") String env){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type",type);
        queryWrapper.eq("env",env);

        return Result.success(targetService.list(queryWrapper));
    }

}
