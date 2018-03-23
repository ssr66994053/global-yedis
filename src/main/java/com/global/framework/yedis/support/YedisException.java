/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-10 21:13 创建
 *
 */
package com.global.framework.yedis.support;

/**
 * @author aleishus@yiji.com
 */
public class YedisException extends RuntimeException {
    public YedisException() {
    }

    public YedisException(String message) {
        super(message);
    }

    public YedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public YedisException(Throwable cause) {
        super(cause);
    }

    public YedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
