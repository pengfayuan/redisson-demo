package com.yuanyuan.redissondemo.service;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FAYUAN.PENG
 * @version \$Id: DistributedLocksService.java,  2021-06-10 22:30 FAYUAN.PENG Exp $$
 */
@Service
@Slf4j
public class DistributedLocksTemplate {

    private static final Integer MAX_WAIT_TIME = 10;
    private static final Integer MAX_RETRY_COUNT = 10;

    @Autowired
    private RedissonClient redisson;

    public void lock(String lockName, int lockTime, Consumer callBack) {
        int count = 1;
        boolean isAcquired = false;
        RLock rLock = redisson.getLock(lockName);
        while (count < MAX_RETRY_COUNT) {
            try {
                isAcquired = rLock.tryLock(MAX_WAIT_TIME, lockTime, TimeUnit.SECONDS);
                if (isAcquired) {
                    break;
                }

                ThreadUtil.sleep(3, TimeUnit.SECONDS);
                count++;


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isAcquired) {
            callBack.accept(lockName);
            rLock.unlock();
        } else {
            log.warn("get lock failed : {}", MAX_RETRY_COUNT);
        }

    }

}
