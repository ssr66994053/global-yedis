/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhouxi@yiji.com 2014-12-30 19:53 创建
 *
 */
package com.global.frame;

import com.global.framework.yedis.SmartYedisPool;
import com.global.framework.yedis.Yedis;
import com.global.framework.yedis.YedisTemplate;
import com.global.framework.yedis.YedisUtil;
import com.yjf.common.kryo.Kryos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aleishus@yiji.com
 */
public class Test {


   static  class Order{

       private  int x ;
       private  Date timestamp ;
       private  Timestamp getTimestamp ;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

       public Date getTimestamp() {
           return timestamp;
       }

       public void setTimestamp(Date timestamp) {
           this.timestamp = timestamp;
       }

       public Timestamp getGetTimestamp() {
            return getTimestamp;
        }

        public void setGetTimestamp(Timestamp getTimestamp) {
            this.getTimestamp = getTimestamp;
        }
    }


   static class Dto {
      private  int m  ;
        private  List<Order> orders ;

        public int getM() {
            return m;
        }

        public void setM(int m) {
            this.m = m;
        }

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
    }

    public static void  main(String... args){
       /* Test t =new Test();
        SmartYedisPool pool = new SmartYedisPool("192.168.45.10",19000,"namespace") ;
        Yedis yedis = new YedisTemplate(pool) ;
        yedis.set("key1","value1") ;
        yedis.set("keys",new Object()) ;
        yedis.set("keyss", 1) ;
        yedis.hset("key2", "mk1", "mkv1") ;
        yedis.psetex("sss", 1000, "adad") ;*/

        Dto t = new Dto() ;
        t.setM(10);
        Order o = new Order();
        o.setGetTimestamp(new Timestamp(1111111));
        o.setTimestamp(new Timestamp(1212121));
        o.setX(1212);

        Order o1 = new Order();
        o1.setGetTimestamp(new Timestamp(1111111));
        o1.setTimestamp(new Date());
        o1.setX(1212);

        List<Order> ol = new ArrayList<>();
        ol.add(o);
        ol.add(o1) ;

        t.setOrders(ol);

        //Timestamp ttt = new Timestamp(100000000L) ;
       // l.add(ttt);
        //byte[] dm = Kryos.serialize(ol) ;
       // Kryos.deserialize(dm) ;
       byte[] tm = YedisUtil.serialize(ol) ;

       YedisUtil.deserialize(tm) ;
    }
}
