package com.rainbow.bridge.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步任务 Mapper 接口
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
public interface SyncTaskMapper extends BaseMapper<SyncTaskEntity> {
    IPage<TaskBizDto> queryTask(IPage<TaskBizDto> page, @Param("model") TaskQueryDto taskQueryDto);
}
