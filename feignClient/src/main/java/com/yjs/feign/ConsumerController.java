package com.yjs.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConsumerController
 *
 * @author yjs
 * @date 2023/7/4
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/getUserById/{id}")
    public String getUserById(@PathVariable Integer id){
        return userFeign.getUserById(id);
    }
}
