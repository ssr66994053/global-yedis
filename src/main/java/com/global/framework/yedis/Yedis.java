/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-30 10:01 创建
 *
 */
package com.global.framework.yedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aleishus@yiji.com
 * 定义一些常用的redis命令
 */
public interface Yedis {

    <K> void del(K key) ;

    <K> boolean exists(K key) ;

    <K> long expire(K key, int seconds) ;
    
    <K,V> V get(K key) ;

    <K,V> String set(K key, V value) ;

    <K,V> String setex(K key, int seconds, V value) ;

    <K,V> String psetex(K key, int milliseconds, V value);

    <K,V> long setnx(K key, V value) ;

    <K,V> V getSet(K key, V value) ;

    <K,T> long hdel(K key, T... fields) ;

    <K,T> boolean hexists(K key, T fields) ;

    <K,T,V> V hget(K key, T fields) ;

    <K,T,V> Map<T,V> hgetAll(K key) ;

    <K,T> Set<T> hkeys(K key) ;

    <K,T,V> long hset(K key, T field, V value) ;

    <K,T,V> long hsetnx(K key, T field, V value) ;

    <K,T,V> List<V> hmget(K key, T... fields) ;

    <K,T,V> String hmset(K key, Map<T, V> hash) ;

    <K,V> List<V> hvals(K key) ;

    <K,V> V lpop(K key) ;

    <K,V> long lpush(K key, V... values) ;

    <K,V> long lpushx(K key, V... values) ;

    <K,V> V lindex(K key, long index) ;

    <K> long llen(K key) ;

    <K,V> List<V> lrange(K key, long start, long end) ;

    <K,V> long lrem(K key, long count, V value) ;

    <K,V> String lset(K key, long index, V value) ;

    <K> String ltrim(K key, long start, long end);

    <K,V> V rpop(K key) ;

    <K,V> V rpopLpush(K srcKey, K destKey) ;

    <K,V> long rpush(K key, V... values) ;

    <K,V> long rpushx(K key, V... values) ;









}
