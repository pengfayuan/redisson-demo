package com.yuanyuan.redissondemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author FAYUAN.PENG
 * @version \$Id: DistributedObjectsServiceTest.java,  2021-05-15 11:26 FAYUAN.PENG Exp $$
 */
@SpringBootTest
public class DistributedObjectsServiceTest {

    @Autowired
    private DistributedObjectsService service;

    @Test
    void testRKeys() {
        service.testRKeys();
    }

    @Test
    void testRBuckets() {
        service.testRBuckets();
    }

    @Test
    void testRBinaryStream() {
        service.testRBinaryStream();
    }

    @Test
    void testRBitSet() {
        service.testRBitSet();
    }

    @Test
    void testRTopicServer() {
        service.testRTopicServer();
    }

    @Test
    void testRTopicClinet() {
        service.testRTopicClinet();
    }

    @Test
    void testRBloomFilter() {
        service.testRBloomFilter();
    }

    @Test
    void testHyperLogLog() {
        service.testHyperLogLog();
    }

    @Test
    void testLongAdder() {
        service.testLongAdder();
    }
}