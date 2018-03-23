/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-03-11 15:44 创建
 *
 */
package com.global.framework.yedis.session;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

/**
 * @author aleishus@yiji.com
 */

public class YedisHttpSessionConfiguration  extends RedisHttpSessionConfiguration{

    @Resource(name = "sessionRedisTemplate")
    private RedisTemplate redisTemplate ;

    @Override
    @Bean
    public RedisTemplate<String, ExpiringSession> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        return redisTemplate ;
    }
}
