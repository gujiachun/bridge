package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.RedisTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.service.redis.RedisTargetService;
import com.rainbow.bridge.core.PageResult;
import com.rainbow.bridge.core.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RedisTargetController {

    private final RedisTargetService redisTargetService;

    @GetMapping("/target/redis")
    public Result<PageResult<RedisTargetDto>> getList(TargetQueryDto dto){
        IPage<RedisTargetDto> query = redisTargetService.query(dto);
        return Result.success(new PageResult<>(query.getTotal(), query.getRecords()));
    }

    @PostMapping("/target/redis")
    public Result<Boolean> add(@RequestBody RedisTargetDto dto){
        return Result.success(redisTargetService.add(dto));
    }

    @PutMapping("/target/redis")
    public Result<Boolean> update(@RequestBody RedisTargetDto dto){
        return Result.success(redisTargetService.update(dto));
    }

    @DeleteMapping("/target/redis/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(redisTargetService.remove(id));
    }
}
