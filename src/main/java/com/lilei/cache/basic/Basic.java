package com.lilei.cache.basic;

/**
 * @ClassName Basic
 * @Description TODO
 * @Author lilei
 * @Date 17/06/2020 22:51
 * @Version 1.0
 **/
public interface Basic {
    String ip = "192.168.168.14";
    int port = 6379;
    String auth = "123456";
    int redPackCount = 1000;

    int threadCount = 20;
    String redPackPoolKey = "redPackPoolKey"; //LIST类型来模拟红包池子
    String redPackDetailListKey = "redPackDetailListKey";//LIST类型，记录所有用户抢红包的详情
    String userIdRecordKey = "userIdRecordKey";//记录已经抢过红包的用户ID,防止重复抢

    /*
     * KEYS[1]:redPackPool //模拟红包池，用来从红包池抢红包
     * KEYS[2]:redPackDetailList：//记录所有用户抢红包的详情
     * KEYS[3]:userIdRecord//记录所有已经抢过红包的用户ID
     * KEYS[4]:userId  //模拟抢红包的用户ID
     */
    String getRedPackScript =
            // 判断是否用户是否已抢过红包 如果已抢过直接返回nil
            "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then \n" +
                    "\t\treturn nil\n" +
                    "else\n" +
                    //从红包池取出一个红包
                    "\tlocal redPack = redis.call('rpop', KEYS[1]);\n" +
                    //判断红包是否为空
                    "\tif redPack then \n" +
                    "\t\tlocal x = cjson.decode(redPack);\n" +
                    "\t\tx['userId'] = KEYS[4];\n" +
                    "\t\tlocal re = cjson.encode(x);\n" +
                    // 记录用户已抢过userIdRecordKey
                    "\t\tredis.call('hset', KEYS[3], KEYS[4], '1');\n" +
                    // 保存用户抢红包详情
                    "\t\tredis.call('lpush', KEYS[2],re);\n" +
                    "\t\treturn re;\n" +
                    "\tend\n" +
                    "end\t\n" +
                    "return nil";
}
