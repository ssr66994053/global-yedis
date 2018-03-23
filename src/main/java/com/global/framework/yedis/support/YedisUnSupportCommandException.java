/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-10 20:55 创建
 *
 */
package com.global.framework.yedis.support;

/**
 * @author aleishus@yiji.com
 */
public class YedisUnSupportCommandException extends RuntimeException {
    public YedisUnSupportCommandException() {
    }

    public YedisUnSupportCommandException(String message) {
        super(message);
    }

    public YedisUnSupportCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public YedisUnSupportCommandException(Throwable cause) {
        super(cause);
    }

    public YedisUnSupportCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
