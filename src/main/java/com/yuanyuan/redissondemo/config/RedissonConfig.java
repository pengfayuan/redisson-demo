package com.yuanyuan.redissondemo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FAYUAN.PENG
 * @version \$Id: RedissonConfig.java,  2021-05-14 17:17 FAYUAN.PENG Exp $$
 */
@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String address;
    @Value("${redisson.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单节点模式
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setPassword(password);
        singleServerConfig.setDatabase(3);

        return Redisson.create(config);
    }
}
