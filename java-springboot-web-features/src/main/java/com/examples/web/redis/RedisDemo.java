package com.examples.web.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

@Component
public class RedisDemo {
    @Resource
    RedisTemplate redisTemplate;
    public void test1(){
//        redisTemplate.execute(new RedisCallback() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                //redisConnection.setNX("test_log".getBytes(), )
//
//                redisConnection.set("lock".getBytes(), "100".getBytes(), Expiration.from(Duration.of(5, TimeUnit.SECONDS)), RedisStringCommands.SetOption.ifAbsent());
//                return null;
//            }
//        })
        boolean t=this.redisTemplate.boundValueOps("lock").setIfAbsent(100, Duration.ofSeconds(5));
        System.out.println(t);
        t=this.redisTemplate.boundValueOps("lock").setIfAbsent(100, Duration.ofSeconds(5));
        System.out.println(t);
    }
}
