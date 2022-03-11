package com.yjs.saToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2022/1/27
 * @description :
 */
@SpringBootApplication
public class SaSsoClient2Application {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(SaSsoClient2Application.class, args);
        System.out.println("\nSa-Token-SSO Client端启动成功");
    }
}