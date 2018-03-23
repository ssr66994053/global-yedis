/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2015-01-12 04:51 创建
 *
 */
package com.global.frame;

import org.springframework.cache.annotation.Cacheable;

/**
 * @author aleishus@yiji.com
 */
public class Testobj {


    private Object o ;

    @Cacheable(value = "test")
    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
