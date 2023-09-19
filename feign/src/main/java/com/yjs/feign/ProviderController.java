package com.yjs.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProviderController
 *
 * @author yjs
 * @date 2023/7/4
 */
@RestController
@RequestMapping("/provider")
public class ProviderController {

    @GetMapping("/getUserById/{id}")
    public String getUserById(@PathVariable Integer id) {
        return "获取用户：" + id;
    }
}