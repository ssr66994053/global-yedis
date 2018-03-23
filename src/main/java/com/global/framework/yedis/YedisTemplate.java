/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-29 17:49 创建
 *
 */
package com.global.framework.yedis;

import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * 精简redis命令集，支持常用的，添加namespace支持
 * @author aleishus@yiji.com
 */
public class YedisTemplate  implements Yedis {

    private SmartYedisPool smartYedisPool ;

    private byte[] namespace ;

    public YedisTemplate(SmartYedisPool smartYedisPool) {
        this.smartYedisPool = smartYedisPool;
        namespace = smartYedisPool.getNamespace() ;
    }

    @Override
    public <K> void del(K key) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()){
             jedis.del(nk) ;
        }
    }



    @Override
    public <K> boolean exists(K key) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return jedis.exists(nk) ;
        }
    }

    @Override
    public <K> long expire(K key, int seconds) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
          return jedis.expire(nk,seconds) ;
        }
    }

    @Override
    public <K, V> V get(K key) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            byte[] vb = jedis.get(nk);
            return (V)YedisUtil.deserialize(vb) ;
        }
    }

    @Override
    public <K, V> String set(K key, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.set(nk, vb);
        }
    }

    @Override
    public <K, V> String setex(K key, int seconds, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.setex(nk, seconds, vb);
        }
    }

    @Override
    public <K, V> String psetex(K key, int milliseconds, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.psetex(nk, milliseconds, vb);
        }
    }

    @Override
    public <K, V> long setnx(K key, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return   jedis.setnx(nk, vb) ;
        }
    }

    @Override
    public <K, V> V getSet(K key, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  (V)YedisUtil.deserialize(jedis.getSet(nk, vb));
        }
    }

   /* @Override
    public <K,V> String Mset(Map<K,V> hash) {
         byte[][] kvs = new byte[2*hash.size()][] ;
         int i = 0 ;
         for(Map.Entry<K,V> entry : hash.entrySet()){
             kvs[i] = YedisUtil.yedisKeyWapper(entry.getKey()) ;
             kvs[++i] = YedisUtil.serialize(entry.getValue()) ;
             i++ ;
         }
        jedis.mset(kvs) ;
    }

    @Override
    public <K,V> long Msetnx(Map<K,V> hash) {
        byte[][] kvs = new byte[2*hash.size()][] ;
        int i = 0 ;
        for(Map.Entry<K,V> entry : hash.entrySet()){
            kvs[i] = YedisUtil.yedisKeyWapper(entry.getKey()) ;
            kvs[++i] = YedisUtil.serialize(entry.getValue()) ;
            i++ ;
        }
        jedis.msetnx(kvs) ;
    }

    @Override
    public <K, V> List<V> Mget(K... keys) {
        byte[][] ks = new byte[keys.length][] ;
        for(int i =0 ;i<keys.length;i++){
            ks[i] = YedisUtil.yedisKeyWapper(keys[i]) ;
        }
        List<V> vl  = new ArrayList<>() ;
        List<byte[]> vbl = jedis.mget(ks) ;
        if(null != vbl){
           for(byte[] vb: vbl){
               vl.add((V)YedisUtil.deserialize(vb)) ;
           }
        }
        return  vl ;
    }
*/
    @Override
    public <K, T> long hdel(K key, T... fields) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[][] fbb = new byte[fields.length][] ;
        for(int i= 0;i<fields.length;i++){
            fbb[i] = YedisUtil.serialize(fields[i]) ;
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.hdel(nk,fbb);
        }
    }

    @Override
    public <K, T> boolean hexists(K key, T fields) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] fb  = YedisUtil.serialize(fields) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.hexists(nk, fb) ;
        }
    }

    @Override
    public <K, T, V> V hget(K key, T fields) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] fb  = YedisUtil.serialize(fields) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  (V)YedisUtil.deserialize(jedis.hget(nk, fb));
        }
    }

    @Override
    public <K, T, V> Map<T, V> hgetAll(K key) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            Map<byte[], byte[]> vbb = jedis.hgetAll(nk);
            Map<T, V> vb = new HashMap<>();
            if (null != vbb) {
                for (Map.Entry<byte[], byte[]> entry : vbb.entrySet()) {
                    vb.put((T) YedisUtil.deserialize(entry.getKey()),
                            (V) YedisUtil.deserialize(entry.getValue()));
                }
            }
            return vb;
        }
    }

    @Override
    public <K, T> Set<T> hkeys(K key) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            Set<byte[]> vsb = jedis.hkeys(nk);
            Set<T> vs = new HashSet<>();
            if (vsb != null) {
                for (byte[] vb : vsb) {
                    vs.add((T) YedisUtil.deserialize(vb));
                }
            }
            return vs;
        }
    }

    @Override
    public <K, T, V> long hset(K key, T field, V value) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] fb = YedisUtil.serialize(field) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.hset(nk, fb, vb);
        }
    }

    @Override
    public <K, T, V> long hsetnx(K key, T field, V value) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] fb = YedisUtil.serialize(field) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.hsetnx(nk, fb, vb);
        }
    }

    @Override
    public <K, T, V> List<V> hmget(K key, T... fields) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[][] fbb = new byte[fields.length][] ;
        for(int i = 0;i<fields.length;i++){
            fbb[i] = YedisUtil.serialize(fields[i]) ;
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
            List<byte[]> vlb = jedis.hmget(nk, fbb);
            List<V> vl = new ArrayList<>();
            if (null != vlb) {
                for (byte[] b : vlb) {
                    vl.add((V) YedisUtil.deserialize(b));
                }
            }
            return vl;
        }
    }

    @Override
    public <K, T, V> String hmset(K key, Map<T, V> hash) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        Map<byte[],byte[]> mb = new HashMap<>() ;
        try(Jedis jedis = smartYedisPool.getResource()) {
        for(Map.Entry<T,V> entry : hash.entrySet()){
            mb.put(YedisUtil.serialize(entry.getKey()),
                    YedisUtil.serialize(entry.getValue())) ;
        }
           return jedis.hmset(nk, mb);
        }
    }

    @Override
    public <K, V> List<V> hvals(K key) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            List<byte[]> lvb = jedis.hvals(nk);
            List<V> lv = new ArrayList<>();
            for (byte[] b : lvb) {
                lv.add((V) YedisUtil.deserialize(b));
            }
            return lv;
        }
    }

    @Override
    public <K, V> V lpop(K key) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return (V) YedisUtil.deserialize(jedis.lpop(nk));
        }
    }

    @Override
    public <K, V> long lpush(K key, V... values) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[][] vkb = new byte[values.length][] ;
        for(int i = 0 ;i< values.length;i++){
            vkb[i] = YedisUtil.serialize(values[i]) ;
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.lpush(nk, vkb);
        }
    }

    @Override
    public <K, V> long lpushx(K key, V... values) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[][] vkb = new byte[values.length][] ;
        for(int i = 0 ;i< values.length;i++){
            vkb[i] = YedisUtil.serialize(values[i]) ;
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.lpushx(nk, vkb);
        }
    }

    @Override
    public <K, V> V lindex(K key, long index) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            return (V) YedisUtil.deserialize(jedis.lindex(nk, index));
        }
    }

    @Override
    public <K> long llen(K key) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.llen(nk);
        }
    }

    @Override
    public <K, V> List<V> lrange(K key, long start, long end) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
            List<byte[]> vlb = jedis.lrange(nk, start, end);
            List<V> vl = new ArrayList<>();
            if (null != vlb) {
                for (byte[] b : vlb) {
                    vl.add((V) YedisUtil.deserialize(b));
                }
            }
            return vl;
        }
    }

    @Override
    public <K, V> long lrem(K key, long count, V value) {
        byte[] nk  = YedisUtil.yedisKeyWapper(namespace,key) ;
        byte[] vb = YedisUtil.serialize(value) ;
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.lrem(nk, count, vb);
        }
    }

    @Override
    public <K, V> String lset(K key, long index, V value) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key);
        byte[] vb = YedisUtil.serialize(value);
        try(Jedis jedis = smartYedisPool.getResource()) {
           return jedis.lset(nk, index, vb);
        }
    }

    @Override
    public <K> String ltrim(K key, long start, long end) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key);
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.ltrim(nk, start, end);
        }
    }

    @Override
    public <K, V> V rpop(K key) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key);
        try(Jedis jedis = smartYedisPool.getResource()) {
            return (V) YedisUtil.deserialize(jedis.rpop(nk));
        }
    }

    @Override
    public <K, V> V rpopLpush(K srcKey, K destKey) {
        byte[] sk = YedisUtil.yedisKeyWapper(namespace,srcKey);
        byte[] dk = YedisUtil.yedisKeyWapper(namespace,destKey);
        try(Jedis jedis = smartYedisPool.getResource()) {
            return (V) YedisUtil.deserialize(jedis.rpoplpush(sk, dk));
        }
    }

    @Override
    public <K, V> long rpush(K key, V... values) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key);
        byte[][] vbb = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            vbb[i] = YedisUtil.serialize(values[i]);
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
            return  jedis.rpush(nk, vbb);
        }
    }

    @Override
    public <K, V> long rpushx(K key, V... values) {
        byte[] nk = YedisUtil.yedisKeyWapper(namespace,key);
        byte[][] vbb = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            vbb[i] = YedisUtil.serialize(values[i]);
        }
        try(Jedis jedis = smartYedisPool.getResource()) {
           return  jedis.rpushx(nk, vbb);
        }
    }

    public SmartYedisPool getSmartYedisPool() {
        return smartYedisPool;
    }

    public void setSmartYedisPool(SmartYedisPool smartYedisPool) {
        this.smartYedisPool = smartYedisPool;
    }
}

