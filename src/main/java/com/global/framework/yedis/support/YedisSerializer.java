/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-12 14:30 创建
 *
 */
package com.global.framework.yedis.support;

import com.global.framework.yedis.YedisUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author aleishus@yiji.com
 */
public class YedisSerializer<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(T t) throws SerializationException {
       return YedisUtil.serialize(t) ;
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return (T)YedisUtil.deserialize(bytes);
    }
}
