package com.yjs.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FeignProviderApplication {
    private static Logger logger = LoggerFactory.getLogger("startup info");

    public static void main(String[] args) {
        SpringApplication.run(FeignProviderApplication.class, args);
        logger.info("---------!!!--服务端启动完成--!!!---------");
        while(1==1){

        }
    }
}