package com.yuanyuan.redissondemo.service;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author FAYUAN.PENG
 * @version \$Id: DistributedLocksTemplateTest.java,  2021-06-11 18:05 FAYUAN.PENG Exp $$
 */
@SpringBootTest
class DistributedLocksTemplateTest {

    @Autowired
    private DistributedLocksTemplate template;

    private int count;

    @Test
    void lock() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> template.lock("lock_test", 10, lock -> count++), "thread-a").start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> template.lock("lock_test", 10, lock -> count++), "thread-b").start();
        }

        ThreadUtil.sleep(3, TimeUnit.SECONDS);
        System.out.println(count);
    }
}