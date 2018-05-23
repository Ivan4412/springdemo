package com.yjs.springdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yjs on 2018/5/23.
 */
@Slf4j
@Configuration
public class JedisConfiguaration {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.pwd}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMaxWaitMillis(20000);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, 20000, "".equals(password) ? null : password);

        log.info("JedisPool注入成功!");
        log.info("redis地址："+host+":"+port);

        return jedisPool;
    }

}
