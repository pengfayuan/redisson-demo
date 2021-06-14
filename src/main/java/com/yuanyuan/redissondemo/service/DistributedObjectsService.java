package com.yuanyuan.redissondemo.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yuanyuan.redissondemo.model.User;
import java.io.InputStream;
import jodd.util.ThreadUtil;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RBitSet;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RBucket;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RKeys;
import org.redisson.api.RLongAdder;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FAYUAN.PENG
 * @version \$Id: RedissonService.java,  2021-05-15 11:21 FAYUAN.PENG Exp $$
 */
@Service
public class DistributedObjectsService {

    @Autowired
    private RedissonClient client;

    public void testRKeys() {
        RKeys keys = client.getKeys();

        // 获取所有的key
        Iterable<String> allKeys = keys.getKeys();

        // 根据正则查询匹配的key
        Iterable<String> foundedKeys = keys.getKeysByPattern("user*");

        // 删除key
        keys.delete("user:169391");

        // key的数量
        long count = keys.count();

        // 删除过期时间
        keys.clearExpire("user:169391");

        System.out.println("keys count:" + count);
        System.out.println("allKeys:" + allKeys);
    }

    public void testRBuckets() {
        RBucket<Object> bucket = client.getBucket("user:169391", StringCodec.INSTANCE);
//        bucket.set("fayuan");
        System.out.println(bucket.get());
    }

    public void testRBinaryStream() {
        RBinaryStream binaryStream = client.getBinaryStream("bank.png");
//        OutputStream GeoEntry = binaryStream.getOutputStream();
//        byte[] bytes = FileUtil.readBytes("D:\\工作\\crediGo\\需求文档\\图标\\bank\\BB G-01.png");
//        IoUtil.write(outputStream, true, bytes);
        InputStream inputStream = binaryStream.getInputStream();
        FileUtil.writeFromStream(inputStream, "D:\\bank.png");

    }

    public void testRBitSet() {
        RBitSet isVipBitSet = client.getBitSet("isVip");
        RBitSet isManBitSet = client.getBitSet("isMan");
        isVipBitSet.set(169391);
        isVipBitSet.set(169390);
        isManBitSet.set(169391);
        isManBitSet.set(169392);

        isVipBitSet.and("isMan");

        System.out.println("vip且性别是男性：" + isVipBitSet);
    }

    public void testRTopicServer() {
        RTopic rTopic = client.getTopic("order_message", StringCodec.INSTANCE);
        rTopic.addListener(String.class, (channel, message) -> {
            System.out.println("message: " + message);
        });

        ThreadUtil.sleep(100000);
    }

    public void testRTopicClinet() {
        RTopic rTopic = client.getTopic("order_message", StringCodec.INSTANCE);
        User user = new User();
        user.setId(1);
        user.setName("yuanyuan");
        rTopic.publish(JSONUtil.toJsonStr(user));
    }

    public void testRBloomFilter() {
        RBloomFilter<Object> bloomFilterBlackList = client.getBloomFilter("bloom_filter_black_list", StringCodec.INSTANCE);
        bloomFilterBlackList.tryInit(100000L, 0.03);
        bloomFilterBlackList.add("192.168.0.1");
        bloomFilterBlackList.add("192.168.0.2");

        System.out.println(bloomFilterBlackList.contains("192.168.0.1"));
        System.out.println(bloomFilterBlackList.contains("192.168.0.2"));
        System.out.println(bloomFilterBlackList.contains("192.168.0.3"));
    }

    public void testHyperLogLog() {
        RHyperLogLog<Object> homePage = client.getHyperLogLog("home_page");
        homePage.add("169391");
        homePage.add("816");
        homePage.add("72");
        homePage.add("169391");
        System.out.println(homePage.count());
    }

    public void testLongAdder() {
        RLongAdder statementInvoice = client.getLongAdder("statement_invoice");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> statementInvoice.increment()).start();
        }

        ThreadUtil.sleep(1000);
        System.out.println(statementInvoice.sum());
    }
}
