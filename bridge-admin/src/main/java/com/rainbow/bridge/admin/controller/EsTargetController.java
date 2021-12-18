package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.EsTargetDto;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.service.es.EsTargetService;
import com.rainbow.bridge.biz.service.mysql.MysqlTargetService;
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
public class EsTargetController {

    private final EsTargetService esTargetService;

    @GetMapping("/target/es")
    public Result<PageResult<EsTargetDto>> getList(TargetQueryDto dto){
        IPage<EsTargetDto> query = esTargetService.query(dto);
        return Result.success(new PageResult<>(query.getTotal(), query.getRecords()));
    }

    @PostMapping("/target/es")
    public Result<Boolean> add(@RequestBody EsTargetDto dto){
        return Result.success(esTargetService.add(dto));
    }

    @PutMapping("/target/es")
    public Result<Boolean> update(@RequestBody EsTargetDto dto){
        return Result.success(esTargetService.update(dto));
    }

    @DeleteMapping("/target/es/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(esTargetService.remove(id));
    }
}
