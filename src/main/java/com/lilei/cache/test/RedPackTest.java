package com.lilei.cache.test;

import com.lilei.cache.redpack.GenRedPack;
import com.lilei.cache.redpack.GetRedPack;

/**
 * @ClassName RedPackTest
 * @Description TODO
 * @Author lilei
 * @Date 17/06/2020 23:28
 * @Version 1.0
 **/
public class RedPackTest {
    public static void main(String[] args) {
        GenRedPack.genRedPack();//初始化红包

        GetRedPack.getRedPack();//从红包池抢红包
    }
}
