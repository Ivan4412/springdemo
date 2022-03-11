package com.yjs.saToken;

import cn.dev33.satoken.SaManager;
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
public class SaSsoClientApplication {
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(SaSsoClientApplication.class, args);
        System.out.println("\nSa-Token-SSO Client端启动成功");
    }
}