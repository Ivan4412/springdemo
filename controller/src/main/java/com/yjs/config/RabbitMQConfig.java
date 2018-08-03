package com.yjs.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * author : yjs
 * createTime : 2018/8/3
 * description :
 * version : 1.0
 */
@Configuration
public class RabbitMQConfig {


    @Autowired
    private  Config config;

    @Bean
    public  ConnectionFactory getConnectionFactory() throws IOException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //定义连接地址
        factory.setHost(config.getRabbitmqHost());
        //定义端口
        factory.setPort(config.getRabbitmqPort());
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost(config.getRabbitmqVirtualhost());
        factory.setUsername(config.getRabbitmqUsername());
        factory.setPassword(config.getRabbitmqPassword());

        return factory;
    }
}
