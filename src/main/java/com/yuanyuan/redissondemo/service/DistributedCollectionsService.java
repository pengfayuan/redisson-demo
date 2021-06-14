package com.yuanyuan.redissondemo.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yuanyuan.redissondemo.model.User;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.MapOptions;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RDeque;
import org.redisson.api.RList;
import org.redisson.api.RListMultimap;
import org.redisson.api.RListMultimapCache;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapWriter;
import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.EntryRemovedListener;
import org.redisson.api.map.event.EntryUpdatedListener;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FAYUAN.PENG
 * @version \$Id: DistributedCollectionsService.java,  2021-05-19 22:29 FAYUAN.PENG Exp $$
 */
@Service
public class DistributedCollectionsService {

    @Autowired
    private RedissonClient redisson;

    public void testMap() {
        RMap<String, Object> rMap = redisson.getMap("r_map", StringCodec.INSTANCE);
//        User user = new User();
//        user.setId(1);
//        user.setName("yuanyuan");
//        rMap.put("user:169391", JSONUtil.toJsonStr(user));

        // 32248ms
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 1000; i++) {
            rMap.get("user:169391");
        }
        stopWatch.stop();
        System.out.println("cost time: " + stopWatch.getTotalTimeMillis());
    }

    public void testMapCache() {
        RMapCache<String, Object> rMapCache = redisson.getMapCache("r_map_cache", StringCodec.INSTANCE);
        User user = new User();
        user.setId(1);
        user.setName("yuanyuan");

        User user2 = new User();
        user2.setId(2);
        user2.setName("fafa");

        User user3 = new User();
        user3.setId(3);
        user3.setName("chenchen");
        rMapCache.put("user:169391", JSONUtil.toJsonStr(user), 10, TimeUnit.SECONDS);
        rMapCache.put("user:816", JSONUtil.toJsonStr(user2), 10, TimeUnit.SECONDS);
        rMapCache.put("user:1024", JSONUtil.toJsonStr(user3), 10, TimeUnit.SECONDS);

        Set<String> set = rMapCache.keySet();
        System.out.println(set);

        ThreadUtil.sleep(1, TimeUnit.MINUTES);
    }

    public void testLocalCachedMap() {
        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                .evictionPolicy(EvictionPolicy.NONE)
                .cacheSize(1000)
                .reconnectionStrategy(ReconnectionStrategy.NONE)
                .syncStrategy(SyncStrategy.INVALIDATE)
                .timeToLive(10000);

        RLocalCachedMap rLocalMap = redisson.getLocalCachedMap("r_local_map", StringCodec.INSTANCE, options);
//        User user = new User();
//        user.setId(1);
//        user.setName("yuanyuan");
//        rLocalMap.put("user:169391", JSONUtil.toJsonStr(user));

        // 125
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 1000; i++) {
            rLocalMap.get("user:169391");
        }
        stopWatch.stop();
        System.out.println("cost time: " + stopWatch.getTotalTimeMillis());

    }

    public void testListener() {
        RMapCache<String, Object> rMap = redisson.getMapCache("r_map_listener", StringCodec.INSTANCE);

        rMap.addListener((EntryCreatedListener) event -> System.out.println(event.getKey() + " created"));
        rMap.addListener(
                (EntryUpdatedListener) event -> System.out.println(event.getKey() + " updated, old value:" + event.getOldValue()
                        + ", new value:" + event.getValue()));
        rMap.addListener((EntryRemovedListener) event -> System.out.println(event.getKey() + " remove"));
        rMap.addListener((EntryExpiredListener) event -> System.out.println(event.getKey() + " expired"));

        rMap.put("aaa", "aaa");
        ThreadUtil.sleep(100);

        rMap.replace("aaa", "bbb");
        ThreadUtil.sleep(100);

        rMap.remove("aaa");
        ThreadUtil.sleep(100);

        rMap.put("ddd", "ddd", 1, TimeUnit.SECONDS);

        ThreadUtil.sleep(5, TimeUnit.SECONDS);
    }

    public void testMapOptions() {
        MapOptions mapOptions = MapOptions.defaults().writer(new MyWriter());

        RMap rMap = redisson.getMap("r_map", StringCodec.INSTANCE, mapOptions);

        User user = new User();
        user.setId(1);
        user.setName("yuanyuan");
        rMap.put("user:169391", JSONUtil.toJsonStr(user));

        User user2 = new User();
        user2.setId(2);
        user2.setName("yuanyuan2");
        rMap.put("user:169392", JSONUtil.toJsonStr(user2));

        rMap.remove("user:169391");
    }

    static class MyWriter implements MapWriter {

        @Override
        public void write(Map map) {
            System.out.println(map);
        }

        @Override
        public void delete(Collection keys) {
            System.out.println(keys);

        }
    }

    public void testMultimap() {
        RListMultimap<String, User> listMultimap = redisson.getListMultimap("list_multimap");
        User user = new User();
        user.setId(1);
        user.setName("yuanyuan");

        User user2 = new User();
        user2.setId(2);
        user2.setName("fanfan");

        listMultimap.put("room1", user);
        listMultimap.put("room1", user2);

        System.out.println(listMultimap.get("room1"));

        listMultimap.removeAll("room1");

        listMultimap.put("room1", user);

        System.out.println(listMultimap.get("room1"));
    }

    public void testMultimapCache() {
        RListMultimapCache<Object, Object> listMultimapCache = redisson.getListMultimapCache("list_multimap_cache");
        User user = new User();
        user.setId(1);
        user.setName("yuanyuan");

        listMultimapCache.put("room1", user);
        listMultimapCache.put("room2", user);
        listMultimapCache.expireKey("room1", 5, TimeUnit.SECONDS);

        ThreadUtil.sleep(10, TimeUnit.SECONDS);
        System.out.println(listMultimapCache.get("room1"));
        System.out.println(listMultimapCache.get("room2"));

    }

    public void testRSet() {
        RSet<String> rSet = redisson.getSet("set", StringCodec.INSTANCE);
        rSet.add("111");
        rSet.add("111");
        rSet.add("222");
        rSet.add("333");

        System.out.println(redisson.getSet("set", StringCodec.INSTANCE));
    }

    public void testRList() {
        RList<String> rList = redisson.getList("list", StringCodec.INSTANCE);
        rList.add("111");
        rList.add("111");
        rList.add("222");
        rList.add("333");

        System.out.println(redisson.getList("list", StringCodec.INSTANCE));
    }

    public void testRQueue() {
        RQueue<String> rQueue = redisson.getQueue("queue", StringCodec.INSTANCE);
        rQueue.add("111");
        rQueue.add("222");
        rQueue.add("333");

        System.out.println(redisson.getQueue("queue", StringCodec.INSTANCE));
        System.out.println(rQueue.poll(2));
        System.out.println(redisson.getQueue("queue", StringCodec.INSTANCE));
    }

    public void testDeque() {
        RDeque<Object> rDeque = redisson.getDeque("deque", StringCodec.INSTANCE);
        rDeque.add("111");
        rDeque.add("222");
        rDeque.add("333");

        System.out.println(redisson.getQueue("deque", StringCodec.INSTANCE));
        System.out.println(rDeque.pollFirst());
        System.out.println(rDeque.pollLast());
        System.out.println(redisson.getQueue("deque", StringCodec.INSTANCE));
    }

    public void testRBlockingQueue() throws InterruptedException {
        RBlockingQueue<String> blocking_queue = redisson.getBlockingQueue("blocking_queue", StringCodec.INSTANCE);

        ThreadUtil.execAsync(() -> {
            ThreadUtil.sleep(3, TimeUnit.SECONDS);
            try {
                blocking_queue.put("111");
                blocking_queue.put("222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("take start");
        System.out.println(blocking_queue.take());
        System.out.println("take end");

    }

    public void testDelayedQueue() {
        RDelayedQueue<Object> rDelayedQueue = redisson.getDelayedQueue(redisson.getQueue("delayed_queue", StringCodec.INSTANCE));

        rDelayedQueue.offer("111", 3, TimeUnit.SECONDS);
        rDelayedQueue.offer("222", 3, TimeUnit.SECONDS);

    }

    /**
     * todo 一直报错，待排查
     */
    public void testPriorityQueue() {
        RPriorityQueue<User> priorityQueue = redisson.getPriorityQueue("priority_queue", StringCodec.INSTANCE);
        Comparator<User> userComparator = Comparator.comparingInt(User::getId);
        priorityQueue.trySetComparator(userComparator);

        User user = new User();
        user.setName("yuanyuan");
        user.setId(2);

        User user2 = new User();
        user2.setName("fanfan");
        user2.setId(1);

        User user3 = new User();
        user3.setName("houhou");
        user3.setId(3);

        priorityQueue.add(user);
        priorityQueue.add(user2);
        priorityQueue.add(user3);

        System.out.println(priorityQueue.readAll());
    }

}
