package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    public static final long BEGIN_TIMESTAMP = 1735689600L;
    public static final int COUNT_BITS = 32;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public long nextId(String keyPrefix){
        // 1 生成时间戳（用当前时间戳减去开始的 得到的秒数就是）
        LocalDateTime now = LocalDateTime.now();
        long nowSecond= now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2 生成序列号
        //2.1 获取当前日期 精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //2.2 自增长
        long count = stringRedisTemplate.opsForValue().increment("irc:" + keyPrefix + ":" + date);

        // 3 拼接并返回

        return timestamp << COUNT_BITS | count;
    }

}
