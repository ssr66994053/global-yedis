/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-11 21:30 创建
 *
 */
package com.global.framework.yedis.support;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Set;

/**
 * @author aleishus@yiji.com
 */
public class YedisCache implements Cache {

    private static final int PAGE_SIZE = 128;
    private final String name;
    @SuppressWarnings("rawtypes") private final RedisTemplate template;
    private final byte[] prefix;
    private final byte[] setName;
    private final byte[] cacheLockName;
    private long WAIT_FOR_LOCK = 300;
    private final long expiration;

    /**
     * Constructs a new <code>YedisCache</code> instance.
     *
     * @param name cache name
     * @param prefix
     * @param template
     * @param expiration
     */
    YedisCache(String name, byte[] prefix, RedisTemplate<? extends Object, ? extends Object> template, long expiration) {

        Assert.hasText(name, "non-empty cache name is required");
        this.name = name;
        this.template = template;
        this.prefix = prefix;
        this.expiration = expiration;

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // name of the set holding the keys
        this.setName = stringSerializer.serialize(name + "~keys");
        this.cacheLockName = stringSerializer.serialize(name + "~lock");
    }

    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc} This implementation simply returns the RedisTemplate used for configuring the cache, giving access to
     * the underlying Redis store.
     */
    public Object getNativeCache() {
        return template;
    }

    public ValueWrapper get(final Object key) {
        return (ValueWrapper) template.execute(new RedisCallback<ValueWrapper>() {

            public ValueWrapper doInRedis(RedisConnection connection) throws DataAccessException {
                waitForLock(connection);
                byte[] bs = connection.get(computeKey(key));
                Object value = template.getValueSerializer() != null ? template.getValueSerializer().deserialize(bs) : bs;
                return (bs == null ? null : new SimpleValueWrapper(value));
            }
        }, true);
    }

    /**
     * Return the value to which this cache maps the specified key, generically specifying a type that return value will
     * be cast to.
     *
     * @param key
     * @param type
     * @return
     * @see DATAREDIS-243
     */
    public <T> T get(Object key, Class<T> type) {

        ValueWrapper wrapper = get(key);
        return wrapper == null ? null : (T) wrapper.get();
    }

    public void put(final Object key, final Object value) {

        final byte[] keyBytes = computeKey(key);
        final byte[] valueBytes = convertToBytesIfNecessary(template.getValueSerializer(), value);

        template.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                waitForLock(connection);

                //connection.multi();

                connection.set(keyBytes, valueBytes);
                connection.zAdd(setName, 0, keyBytes);

                if (expiration > 0) {
                    connection.expire(keyBytes, expiration);
                    // update the expiration of the set of keys as well
                    connection.expire(setName, expiration);
                }
               // connection.exec();

                return null;
            }
        }, true);
    }

    public ValueWrapper putIfAbsent(Object key, final Object value) {

        final byte[] keyBytes = computeKey(key);
        final byte[] valueBytes = convertToBytesIfNecessary(template.getValueSerializer(), value);

        return toWrapper(template.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                waitForLock(connection);

                Object resultValue = value;
                boolean valueWasSet = connection.setNX(keyBytes, valueBytes);
                if (valueWasSet) {
                    connection.zAdd(setName, 0, keyBytes);
                    if (expiration > 0) {
                        connection.expire(keyBytes, expiration);
                        // update the expiration of the set of keys as well
                        connection.expire(setName, expiration);
                    }
                } else {
                    resultValue = deserializeIfNecessary(template.getValueSerializer(), connection.get(keyBytes));
                }

                return resultValue;
            }
        }, true));
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new SimpleValueWrapper(value) : null);
    }

    public void evict(Object key) {
        final byte[] k = computeKey(key);

        template.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(k);
                // remove key from set
                connection.zRem(setName, k);
                return null;
            }
        }, true);
    }

    public void clear() {
        // need to del each key individually
        template.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // another clear is on-going
                if (connection.exists(cacheLockName)) {
                    return null;
                }

                try {
                    connection.set(cacheLockName, cacheLockName);

                    int offset = 0;
                    boolean finished = false;

                    do {
                        // need to paginate the keys
                        Set<byte[]> keys = connection.zRange(setName, (offset) * PAGE_SIZE, (offset + 1) * PAGE_SIZE - 1);
                        finished = keys.size() < PAGE_SIZE;
                        offset++;
                        if (!keys.isEmpty()) {
                            connection.del(keys.toArray(new byte[keys.size()][]));
                        }
                    } while (!finished);

                    connection.del(setName);
                    return null;

                } finally {
                    connection.del(cacheLockName);
                }
            }
        }, true);
    }

    private byte[] computeKey(Object key) {

        byte[] keyBytes = convertToBytesIfNecessary(template.getKeySerializer(), key);

        if (prefix == null || prefix.length == 0) {
            return keyBytes;
        }

        byte[] result = Arrays.copyOf(prefix, prefix.length + keyBytes.length);
        System.arraycopy(keyBytes, 0, result, prefix.length, keyBytes.length);

        return result;
    }

    private boolean waitForLock(RedisConnection connection) {

        boolean retry;
        boolean foundLock = false;
        do {
            retry = false;
            if (connection.exists(cacheLockName)) {
                foundLock = true;
                try {
                    Thread.sleep(WAIT_FOR_LOCK);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                retry = true;
            }
        } while (retry);

        return foundLock;
    }

    private byte[] convertToBytesIfNecessary(RedisSerializer<Object> serializer, Object value) {

        if (value instanceof byte[]) {
            return (byte[]) value;
        }else if(null == serializer ){
            return null ;
        }
        return serializer.serialize(value);
    }

    private Object deserializeIfNecessary(RedisSerializer<byte[]> serializer, byte[] value) {

        if (serializer != null) {
            return serializer.deserialize(value);
        }

        return value;
    }

}
