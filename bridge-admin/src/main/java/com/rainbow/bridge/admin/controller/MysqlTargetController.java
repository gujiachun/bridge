package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
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
public class MysqlTargetController {

    private final MysqlTargetService mysqlTargetService;

    @GetMapping("/target/mysql")
    public Result<PageResult<MysqlTargetDto>> getList(TargetQueryDto dto){
        IPage<MysqlTargetDto> query = mysqlTargetService.query(dto);
        return Result.success(new PageResult<>(query.getTotal(), query.getRecords()));
    }

    @PostMapping("/target/mysql")
    public Result<Boolean> add(@RequestBody MysqlTargetDto dto){
        return Result.success(mysqlTargetService.add(dto));
    }

    @PutMapping("/target/mysql")
    public Result<Boolean> update(@RequestBody MysqlTargetDto dto){
        return Result.success(mysqlTargetService.update(dto));
    }

    @DeleteMapping("/target/mysql/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(mysqlTargetService.remove(id));
    }
}
