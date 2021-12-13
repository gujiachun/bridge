package com.rainbow.bridge.biz.mapper.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.biz.dto.EsTargetDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.entity.es.SyncEsTargetEntity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 同步目标数据源Es Mapper 接口
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
public interface SyncEsTargetMapper extends BaseMapper<SyncEsTargetEntity> {

    IPage<EsTargetDto> queryEsTarget(IPage<EsTargetDto> page, @Param("model") TargetQueryDto targetQueryDto);
}
