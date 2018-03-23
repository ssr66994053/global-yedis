/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-31 09:47 创建
 *
 */
package com.global.frame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

/**
 * @author aleishus@yiji.com
 */
public class SpringTest {

    public static void main(String... args){

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml") ;
        RedisTemplate template = (RedisTemplate)context.getBean("redisTemplate") ;

         ValueOperations va = template.opsForValue() ;
         HashOperations hash =  template.opsForHash() ;
         ListOperations list = template.opsForList() ;
         SetOperations set = template.opsForSet() ;

        va.set("sfsf","asfaf");
        System.out.println(va.get("sfsf")) ;
        template.delete("ssss");
        Testobj o = (Testobj)context.getBean("ooo") ;
        o.setO(100);
        o.getO() ;
        o.getO() ;

        template.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.info() ;

            }
        }) ;



    }
}
