package com.yjs.feign.githubdemo;

import com.alibaba.fastjson.JSON;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import lombok.Data;

import java.util.List;

/**
 * Demo
 *
 * @author yjs
 * @date 2023/7/4
 */
interface GitHub {

    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repository);

    @Data
    class Contributor {
        String login;
        int contributions;
    }
}

public class Demo {
    public static void main(String[] args) {
        GitHub github = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GitHub.class, "https://api.github.com");

        /* The owner and repository parameters will be used to expand the owner and repo expressions
         * defined in the RequestLine.
         *
         * the resulting uri will be https://api.github.com/repos/OpenFeign/feign/contributors
         */
        List<GitHub.Contributor> list = github.contributors("OpenFeign", "feign");
        System.out.println(JSON.toJSONString(list));
    }
}