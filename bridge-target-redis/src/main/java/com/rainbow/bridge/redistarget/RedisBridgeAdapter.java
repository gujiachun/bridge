package com.rainbow.bridge.redistarget;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.rainbow.bridge.redistarget.constant.RedisCons;
import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.redistarget.client.RedisService;
import com.rainbow.bridge.redistarget.param.RedisParam;
import com.rainbow.bridge.targetcore.adapter.BridgeAdapter;
import com.rainbow.bridge.targetcore.model.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 目标源类型redis的 具体执行适配器
 *
 * @author gujiachun
 */
public class RedisBridgeAdapter implements BridgeAdapter<RedisService> {

    protected static final Logger logger = LoggerFactory.getLogger(RedisBridgeAdapter.class);

    @Override
    public void execute(RedisService client, EventEnum event, Param param) throws Exception {
        actionCommand(client,param);
    }

    @Override
    public void execute(RedisService client, EventEnum event, List<Param> params) throws Exception {

    }

    private void actionCommand(RedisService redisService,Param param) {
        if (param == null) {
            return;
        }

        RedisParam redisParam = (RedisParam) param;

        Long et = null;
        if (redisParam.getExpireTime() != null) {
            et = redisParam.getExpireTime();
        } else if (redisParam.getFixedTime() != null) {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = new Date();
            Calendar date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
            try {
                Date endDate = dft.parse(dft.format(date.getTime()) + " " + redisParam.getFixedTime().toString());
                et = DateUtil.between(endDate, beginDate, DateUnit.SECOND, true);
            } catch (ParseException e) {
                logger.error("固定过期时间 处理异常:{}", e.getMessage());
            }
        }

        String key = redisParam.getKey();
        String value = redisParam.getValue();
        String field = redisParam.getField();

        switch (redisParam.getCommand()) {
            case RedisCons
                    .set:
                if (et == null) {
                    redisService.set(key, value);
                } else {
                    redisService.set(key, value, et.intValue());
                }
                break;
            case RedisCons
                    .delete:
                redisService.delete(key);
                break;
            case RedisCons
                    .hset:
                redisService.hset(key, field, value);
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCons
                    .hmset:
                redisService.hmset(key, JSON.parseObject(value, HashMap.class));
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCons
                    .incr:
                redisService.inCr(key);
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCons
                    .delhKeys:
                redisService.delhKeys(key, field.split(","));
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
        }
    }
}
