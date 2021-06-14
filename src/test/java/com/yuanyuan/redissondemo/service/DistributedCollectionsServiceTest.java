package com.yuanyuan.redissondemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author FAYUAN.PENG
 * @version \$Id: DistributedCollectionsServiceTest.java,  2021-05-20 17:43 FAYUAN.PENG Exp $$
 */
@SpringBootTest
public class DistributedCollectionsServiceTest {

    @Autowired
    private DistributedCollectionsService service;

    @Test
    void testMap() {
        service.testMap();
    }

    @Test
    void testMapCache() {
        service.testMapCache();
    }

    @Test
    void testLocalCachedMap() {
        service.testLocalCachedMap();
    }

    @Test
    void testListener() {
        service.testListener();
    }

    @Test
    void testMapOptions() {
        service.testMapOptions();
    }

    @Test
    void testMultimap() {
        service.testMultimap();
    }

    @Test
    void testMultimapCache() {
        service.testMultimapCache();
    }

    @Test
    void testRSet() {
        service.testRSet();
    }

    @Test
    void testRList() {
        service.testRList();
    }

    @Test
    void testRQueue() {
        service.testRQueue();
    }

    @Test
    void testDeque() {
        service.testDeque();
    }

    @Test
    void testRBlockingQueue() throws InterruptedException {
        service.testRBlockingQueue();
    }

    @Test
    void testDelayedQueue() {
        service.testDelayedQueue();
    }

    @Test
    void testPriorityQueue() {
        service.testPriorityQueue();
    }
}