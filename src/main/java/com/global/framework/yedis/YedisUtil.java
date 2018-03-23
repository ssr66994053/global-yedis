/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 * 是
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-29 17:55 创建
 *
 */
package com.global.framework.yedis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yjf.common.kryo.Kryos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author aleishus@yiji.com
 */
public class YedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(YedisUtil.class);


    private static final ThreadLocal<ByteArrayOutputStream> cache = new ThreadLocal<ByteArrayOutputStream>() {
        @Override
        protected ByteArrayOutputStream initialValue() {
            return new ByteArrayOutputStream(YedisConstants.DEFAULT_BUFFER_SIZE) ;
        }
    };



    /**
     * 为key绑定namespace
     * @param key
     * @return
     */
    public static byte[] bindNamespace(byte[] namespace,byte[] key){
        try (ByteArrayOutputStream out = cache.get()){
            out.reset();
            out.write(namespace);
            out.write(key);
            return  out.toByteArray() ;
        }
        catch (IOException e){
             logger.error("error:",e);
            throw new RuntimeException(e) ;
        }
    }
    /**
     * 使用kryo进行序列化
     * @param o
     * @return
     */
    public static byte[] serialize(Object o) {
        Kryo kryo = Kryos.getKryo() ;
        Output output = Kryos.getOutput();
        try {
            kryo.writeClassAndObject(output, o);
            return output.toBytes();
        } finally {
            output.clear();
        }

    }

    /**
     * 使用kryo反序列化
     * @param in
     * @return
     */
    public static Object deserialize(byte[] in) {
        if (in == null) {
            return null;
        }
        Kryo kryo = Kryos.getKryo();
        Input input = new Input();
        input.setBuffer(in);
        return kryo.readClassAndObject(input);
    }

    /**
     * 序列化key，并将key加上namespace
     * @param key
     * @param <K>
     * @return
     */
    public static  <K>  byte[] yedisKeyWapper(byte[] namespace,K key){
        byte[] sk = YedisUtil.serialize(key) ;
        return YedisUtil.bindNamespace(namespace,sk) ;
    }

}
