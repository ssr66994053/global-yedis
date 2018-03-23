/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-19 16:24 创建
 */
package com.global.framework.yedis.support;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author qiubo@yiji.com
 */
public class YedisStringKeySerializer implements RedisSerializer<Object> {
	private final Charset charset = Charset.forName("UTF8");
	
	@Override
	public byte[] serialize(Object o) throws SerializationException {
		if (o == null) {
			return null;
		} else {
			if (o instanceof byte[]) {
				return (byte[]) o;
			}
			return o.toString().getBytes(charset);
		}
	}
	
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		return (bytes == null ? null : new String(bytes, charset));
	}
}
