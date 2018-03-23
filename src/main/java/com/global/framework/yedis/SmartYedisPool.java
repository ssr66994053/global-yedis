/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-24 11:36 创建
 * 能通过配置管理系统hera动态的调整连接池大小
 *
 */
package com.global.framework.yedis;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;
import java.net.URI;

/**
 * @author aleishus@yiji.com
 */
public class SmartYedisPool extends JedisPool {

    private byte[] namespace ;

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host,String namespace) {
        super(poolConfig, host);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(String host, int port,String namespace) {
        super(host, port);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(String host,String namespace) {
        super(host);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(URI uri,String namespace) {
        super(uri);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(URI uri, int timeout,String namespace) {
        super(uri, timeout);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password,String namespace) {
        super(poolConfig, host, port, timeout, password);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host, int port,String namespace) {
        super(poolConfig, host, port);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout,String namespace) {
        super(poolConfig, host, port, timeout);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database,String namespace) {
        super(poolConfig, host, port, timeout, password, database);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName,String namespace) {
        super(poolConfig, host, port, timeout, password, database, clientName);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, URI uri,String namespace) {
        super(poolConfig, uri);
        this.namespace = namespace.getBytes() ;
    }

    public SmartYedisPool(GenericObjectPoolConfig poolConfig, URI uri, int timeout,String namespace) {
        super(poolConfig, uri, timeout);
        this.namespace = namespace.getBytes() ;
    }
    public GenericObjectPool getPool(){
        return  this.internalPool ;
    }

    public byte[] getNamespace() {
        return namespace;
    }

    public void setNamespace(byte[] namespace) {
        this.namespace = namespace;
    }
}
