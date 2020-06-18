package com.lilei.cache.redpack;

import com.lilei.cache.basic.Basic;
import com.lilei.cache.utils.JedisUtils;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName GetRedPack
 * @Description TODO
 * @Author lilei
 * @Date 17/06/2020 23:29
 * @Version 1.0
 **/
public class GetRedPack {


    /**
     * 多线程模拟用户抢红包
     */
    public static void getRedPack(){
        // 发枪器
        final CountDownLatch latch = new CountDownLatch(Basic.threadCount);
        for (int i = 0; i < Basic.threadCount; i++) {
            new Thread(()->{
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final  JedisUtils jedis = new JedisUtils(Basic.ip, Basic.port, Basic.auth);
                while (true){
                    String userId = UUID.randomUUID().toString();

                    Object eval = jedis.eval(Basic.getRedPackScript, 4, Basic.redPackPoolKey, Basic.redPackDetailListKey, Basic.userIdRecordKey, userId);
                    if (eval!=null){
                        System.out.println("用户id:" + userId + " 抢红包详情为" + eval);
                    }else{
                        if (jedis.llen(Basic.redPackPoolKey)==0){
                            break;
                        }
                    }
                }
            }).start();
        }
    }
}
