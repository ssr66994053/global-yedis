/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-03-11 16:00 创建
 *
 */
package com.global.framework.yedis.session;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.global.framework.yedis.support.YedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

/**
 * @author aleishus@yiji.com
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 6000)
@Import(RedisHttpSessionConfiguration.class)
public class ConnectionFactoryConfig {

    @Resource(name = "sessionConnectionFactory")
    private YedisConnectionFactory sessionConnectionFactory ;

    @Bean
    public RedisConnectionFactory connectionFactory(){
        return  sessionConnectionFactory ;
    }
}
