/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-10 20:47 创建
 *
 */
package com.global.framework.yedis.support;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisSentinelConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.RedisClientInfo;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author aleishus@yiji.com
 */
public class YedisConnection extends JedisConnection {

    private byte[] namespace ;

    private final int DEFAULT_BUFFER_SIZE = 64 ;

    private final ThreadLocal<ByteArrayOutputStream> buffer = new ThreadLocal<ByteArrayOutputStream>(){
        @Override
        protected ByteArrayOutputStream initialValue() {
            return new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE) ;
        }
    };

    public YedisConnection(Jedis jedis,String namespace) {
        super(jedis);
        if(null == namespace) {
            throw  new YedisException("namespace must be set.") ;
        }
        this.namespace = namespace.getBytes() ;
    }

    public YedisConnection(Jedis jedis, Pool<Jedis> pool, int dbIndex,String namespace) {
        super(jedis, pool, dbIndex);
        if(null == namespace) {
            throw  new YedisException("namespace must be set.") ;
        }
            this.namespace = namespace.getBytes();

    }

    private byte[] bindNamespace(byte[] rawKey){
        try(ByteArrayOutputStream writer = buffer.get()){
            writer.reset();
            writer.write(namespace);
            writer.write(rawKey);
           return writer.toByteArray() ;
        }
        catch (IOException e){
            throw new YedisException("bindNamespace failed.",e) ;
        }
    }
    @Override
    public void set(byte[] key, byte[] value) {
        super.set(bindNamespace(key), value);
    }


    @Override
    protected DataAccessException convertJedisAccessException(Exception ex) {
        return super.convertJedisAccessException(ex);
    }

    @Override
    public Object execute(String command, byte[]... args) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void close() throws DataAccessException {
        super.close();
    }

    @Override
    public Jedis getNativeConnection() {
        return super.getNativeConnection();
    }

    @Override
    public boolean isClosed() {
        return super.isClosed();
    }

    @Override
    public boolean isQueueing() {
        return super.isQueueing();
    }

    @Override
    public boolean isPipelined() {
        return super.isPipelined();
    }

    @Override
    public void openPipeline() {
        super.openPipeline();
    }

    @Override
    public List<Object> closePipeline() {
        return super.closePipeline();
    }

    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        return super.sort(bindNamespace(key), params);
    }

    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        return super.sort(bindNamespace(key), params, bindNamespace(storeKey));
    }

    @Override
    public Long dbSize() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void flushDb() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void flushAll() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void bgSave() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void bgReWriteAof() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void bgWriteAof() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void save() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public List<String> getConfig(String param) {
        return super.getConfig(param);
    }

    @Override
    public Properties info() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Properties info(String section) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Long lastSave() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void setConfig(String param, String value) {
        super.setConfig(param, value);
    }

    @Override
    public void resetConfigStats() {
        super.resetConfigStats();
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public void shutdown(ShutdownOption option) {
        super.shutdown(option);
    }

    @Override
    public byte[] echo(byte[] message) {
        return super.echo(message);
    }

    @Override
    public String ping() {
        return super.ping();
    }

    @Override
    public Long del(byte[]... keys) {
        Long r = 0L ;
      if(null != keys){
          for(byte[] key :keys){
              r+= super.del(bindNamespace(key)) ;
          }
      }
         return r ;
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public List<Object> exec() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean exists(byte[] key) {
        return super.exists(bindNamespace(key));
    }

    @Override
    public Boolean expire(byte[] key, long seconds) {
        return super.expire(bindNamespace(key), seconds);
    }

    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        return super.expireAt(bindNamespace(key), unixTime);
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void multi() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean persist(byte[] key) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean move(byte[] key, int dbIndex) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public byte[] randomKey() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void rename(byte[] oldName, byte[] newName) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void select(int dbIndex) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Long ttl(byte[] key) {
        return super.ttl(bindNamespace(key));
    }

    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return super.pExpire(bindNamespace(key), millis);
    }

    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return super.pExpireAt(bindNamespace(key), unixTimeInMillis);
    }

    @Override
    public Long pTtl(byte[] key) {
        return super.pTtl(bindNamespace(key));
    }

    @Override
    public byte[] dump(byte[] key) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        super.restore(bindNamespace(key), ttlInMillis, serializedValue);
    }

    @Override
    public DataType type(byte[] key) {
        return super.type(bindNamespace(key));
    }

    @Override
    public void unwatch() {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void watch(byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public byte[] get(byte[] key) {
        return super.get(bindNamespace(key));
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return super.getSet(bindNamespace(key), value);
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return super.append(bindNamespace(key), value);
    }

    @Override
    public List<byte[]> mGet(byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void mSet(Map<byte[], byte[]> tuples) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public void setEx(byte[] key, long time, byte[] value) {
        super.setEx(bindNamespace(key), time, value);
    }

    @Override
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        super.pSetEx(bindNamespace(key), milliseconds, value);
    }

    @Override
    public Boolean setNX(byte[] key, byte[] value) {
        return super.setNX(bindNamespace(key), value);
    }

    @Override
    public byte[] getRange(byte[] key, long start, long end) {
        return super.getRange(bindNamespace(key), start, end);
    }

    @Override
    public Long decr(byte[] key) {
        return super.decr(bindNamespace(key));
    }

    @Override
    public Long decrBy(byte[] key, long value) {
        return super.decrBy(bindNamespace(key), value);
    }

    @Override
    public Long incr(byte[] key) {
        return super.incr(bindNamespace(key));
    }

    @Override
    public Long incrBy(byte[] key, long value) {
        return super.incrBy(bindNamespace(key), value);
    }

    @Override
    public Double incrBy(byte[] key, double value) {
        return super.incrBy(bindNamespace(key), value);
    }

    @Override
    public Boolean getBit(byte[] key, long offset) {
        return super.getBit(bindNamespace(key), offset);
    }

    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {
        return super.setBit(bindNamespace(key), offset, value);
    }

    @Override
    public void setRange(byte[] key, byte[] value, long start) {
        super.setRange(bindNamespace(key), value, start);
    }

    @Override
    public Long strLen(byte[] key) {
        return super.strLen(bindNamespace(key));
    }

    @Override
    public Long bitCount(byte[] key) {
        return super.bitCount(bindNamespace(key));
    }

    @Override
    public Long bitCount(byte[] key, long begin, long end) {
        return super.bitCount(bindNamespace(key), begin, end);
    }

    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Long lPush(byte[] key, byte[]... values) {
        return super.lPush(bindNamespace(key), values);
    }

    @Override
    public Long rPush(byte[] key, byte[]... values) {
        return super.rPush(bindNamespace(key), values);
    }

    @Override
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public byte[] lIndex(byte[] key, long index) {
        return super.lIndex(bindNamespace(key), index);
    }

    @Override
    public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
        return super.lInsert(bindNamespace(key), where, pivot, value);
    }

    @Override
    public Long lLen(byte[] key) {
        return super.lLen(bindNamespace(key));
    }

    @Override
    public byte[] lPop(byte[] key) {
        return super.lPop(bindNamespace(key));
    }

    @Override
    public List<byte[]> lRange(byte[] key, long start, long end) {
        return super.lRange(bindNamespace(key), start, end);
    }

    @Override
    public Long lRem(byte[] key, long count, byte[] value) {
        return super.lRem(bindNamespace(key), count, value);
    }

    @Override
    public void lSet(byte[] key, long index, byte[] value) {
        super.lSet(bindNamespace(key), index, value);
    }

    @Override
    public void lTrim(byte[] key, long start, long end) {
        super.lTrim(bindNamespace(key), start, end);
    }

    @Override
    public byte[] rPop(byte[] key) {
        return super.rPop(bindNamespace(key));
    }

    @Override
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        return super.rPopLPush(bindNamespace(srcKey), bindNamespace(dstKey));
    }

    @Override
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        return super.bRPopLPush(timeout, bindNamespace(srcKey), bindNamespace(dstKey));
    }

    @Override
    public Long lPushX(byte[] key, byte[] value) {
        return super.lPushX(bindNamespace(key), value);
    }

    @Override
    public Long rPushX(byte[] key, byte[] value) {
        return super.rPushX(bindNamespace(key), value);
    }

    @Override
    public Long sAdd(byte[] key, byte[]... values) {
        return super.sAdd(bindNamespace(key), values);
    }

    @Override
    public Long sCard(byte[] key) {
        return super.sCard(bindNamespace(key));
    }

    @Override
    public Set<byte[]> sDiff(byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Set<byte[]> sInter(byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        throw new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean sIsMember(byte[] key, byte[] value) {
        return super.sIsMember(bindNamespace(key), value);
    }

    @Override
    public Set<byte[]> sMembers(byte[] key) {
        return super.sMembers(bindNamespace(key));
    }

    @Override
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        return super.sMove(bindNamespace(srcKey), bindNamespace(destKey), value);
    }

    @Override
    public byte[] sPop(byte[] key) {
        return super.sPop(bindNamespace(key));
    }

    @Override
    public byte[] sRandMember(byte[] key) {
        return super.sRandMember(bindNamespace(key));
    }

    @Override
    public List<byte[]> sRandMember(byte[] key, long count) {
        return super.sRandMember(bindNamespace(key), count);
    }

    @Override
    public Long sRem(byte[] key, byte[]... values) {
        return super.sRem(bindNamespace(key), values);
    }

    @Override
    public Set<byte[]> sUnion(byte[]... keys) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        return super.zAdd(bindNamespace(key), score, value);
    }

    @Override
    public Long zAdd(byte[] key, Set<Tuple> tuples) {
        return super.zAdd(bindNamespace(key), tuples);
    }

    @Override
    public Long zCard(byte[] key) {
        return super.zCard(bindNamespace(key));
    }

    @Override
    public Long zCount(byte[] key, double min, double max) {
        return super.zCount(bindNamespace(key), min, max);
    }

    @Override
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        return super.zIncrBy(bindNamespace(key), increment, value);
    }

    @Override
    public Long zInterStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        return super.zRange(bindNamespace(key), start, end);
    }

    @Override
    public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
        return super.zRangeWithScores(bindNamespace(key), start, end);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return super.zRangeByScore(bindNamespace(key), min, max);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return super.zRangeByScoreWithScores(bindNamespace(key), min, max);
    }

    @Override
    public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        return super.zRevRangeWithScores(bindNamespace(key), start, end);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return super.zRangeByScore(bindNamespace(key), min, max, offset, count);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return super.zRangeByScoreWithScores(bindNamespace(key), min, max, offset, count);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return super.zRevRangeByScore(bindNamespace(key), min, max, offset, count);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return super.zRevRangeByScore(bindNamespace(key), min, max);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return super.zRevRangeByScoreWithScores(bindNamespace(key), min, max, offset, count);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return super.zRevRangeByScoreWithScores(bindNamespace(key), min, max);
    }

    @Override
    public Long zRank(byte[] key, byte[] value) {
        return super.zRank(bindNamespace(key), value);
    }

    @Override
    public Long zRem(byte[] key, byte[]... values) {
        return super.zRem(bindNamespace(key), values);
    }

    @Override
    public Long zRemRange(byte[] key, long start, long end) {
        return super.zRemRange(bindNamespace(key), start, end);
    }

    @Override
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return super.zRemRangeByScore(bindNamespace(key), min, max);
    }

    @Override
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        return super.zRevRange(bindNamespace(key), start, end);
    }

    @Override
    public Long zRevRank(byte[] key, byte[] value) {
        return super.zRevRank(bindNamespace(key), value);
    }

    @Override
    public Double zScore(byte[] key, byte[] value) {
        return super.zScore(bindNamespace(key), value);
    }

    @Override
    public Long zUnionStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        return super.hSet(bindNamespace(key), field, value);
    }

    @Override
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        return super.hSetNX(bindNamespace(key), field, value);
    }

    @Override
    public Long hDel(byte[] key, byte[]... fields) {
        return super.hDel(bindNamespace(key), fields);
    }

    @Override
    public Boolean hExists(byte[] key, byte[] field) {
        return super.hExists(bindNamespace(key), field);
    }

    @Override
    public byte[] hGet(byte[] key, byte[] field) {
        return super.hGet(bindNamespace(key), field);
    }

    @Override
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        return super.hGetAll(bindNamespace(key));
    }

    @Override
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        return super.hIncrBy(bindNamespace(key), field, delta);
    }

    @Override
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        return super.hIncrBy(bindNamespace(key), field, delta);
    }

    @Override
    public Set<byte[]> hKeys(byte[] key) {
        return super.hKeys(bindNamespace(key));
    }

    @Override
    public Long hLen(byte[] key) {
        return super.hLen(bindNamespace(key));
    }

    @Override
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        return super.hMGet(bindNamespace(key), fields);
    }

    @Override
    public void hMSet(byte[] key, Map<byte[], byte[]> tuple) {
        super.hMSet(bindNamespace(key), tuple);
    }

    @Override
    public List<byte[]> hVals(byte[] key) {
        return super.hVals(bindNamespace(key));
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Subscription getSubscription() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public boolean isSubscribed() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void scriptFlush() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void scriptKill() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public String scriptLoad(byte[] script) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public List<Boolean> scriptExists(String... scriptSha1) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Long time() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void killClient(String host, int port) {
        super.killClient(host, port);
    }

    @Override
    public void slaveOf(String host, int port) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public void setClientName(byte[] name) {
        super.setClientName(name);
    }

    @Override
    public String getClientName() {
        return super.getClientName();
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return super.getClientList();
    }

    @Override
    public void slaveOfNoOne() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Cursor<byte[]> scan() {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Cursor<byte[]> scan(ScanOptions options) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Cursor<byte[]> scan(long cursorId, ScanOptions options) {
        throw  new YedisUnSupportCommandException() ;
    }

    @Override
    public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
        return super.zScan(bindNamespace(key), options);
    }

    @Override
    public Cursor<Tuple> zScan(byte[] key, Long cursorId, ScanOptions options) {
        return super.zScan(bindNamespace(key), cursorId, options);
    }

    @Override
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return super.sScan(bindNamespace(key), options);
    }

    @Override
    public Cursor<byte[]> sScan(byte[] key, long cursorId, ScanOptions options) {
        return super.sScan(bindNamespace(key), cursorId, options);
    }

    @Override
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return super.hScan(bindNamespace(key), options);
    }

    @Override
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, long cursorId, ScanOptions options) {
        return super.hScan(bindNamespace(key), cursorId, options);
    }

    @Override
    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        super.setConvertPipelineAndTxResults(convertPipelineAndTxResults);
    }

    @Override
    protected boolean isActive(RedisNode node) {
        return super.isActive(node);
    }

    @Override
    protected JedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        return super.getSentinelConnection(sentinel);
    }

    @Override
    protected Jedis getJedis(RedisNode node) {
        return super.getJedis(node);
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return super.getSentinelConnection();
    }

    @Override
    public void setSentinelConfiguration(RedisSentinelConfiguration sentinelConfiguration) {
        super.setSentinelConfiguration(sentinelConfiguration);
    }

    @Override
    public boolean hasRedisSentinelConfigured() {
        return super.hasRedisSentinelConfigured();
    }

    public byte[] getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace.getBytes();
    }



}
