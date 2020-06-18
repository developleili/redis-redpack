package com.lilei.cache.redpack;

import com.alibaba.fastjson.JSONObject;
import com.lilei.cache.basic.Basic;
import com.lilei.cache.utils.JedisUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName GenRedPack
 * @Description TODO
 * @Author lilei
 * @Date 17/06/2020 23:29
 * @Version 1.0
 **/
public class GenRedPack {



    /**
     * 多线程模拟红包池初始化
     */
    public static void genRedPack(){
        final  JedisUtils jedis = new JedisUtils(Basic.ip, Basic.port, Basic.auth);
        // 发枪器
        final  CountDownLatch latch = new CountDownLatch(Basic.threadCount);
        // 清空所有redis缓存
        jedis.flushall();
        // 每个线程需要初始化多少红包
        int per = Basic.redPackCount/Basic.threadCount;
        for (int i = 0; i < Basic.threadCount; i++) {
            final int page = i;
            new Thread( () -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.getStackTrace();
                }
                JSONObject object = new JSONObject();

                for (int j = page * per; j < (page+1) * per; j++){
                    object.put("id", "rid_" + j);// 红包id
                    object.put("money", j);
                    jedis.lpush(Basic.redPackPoolKey, object.toJSONString());
                }
            }).start();
        }
    }
}
