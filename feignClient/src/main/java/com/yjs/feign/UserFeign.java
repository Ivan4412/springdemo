package com.yjs.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @FeignClient 一般采用服务名进行命名
 * name：指定FeignClient的名称，如果项目使用了Ribbon，name属性会作为微服务的名称，用于服务发现
 * url: url一般用于调试，可以手动指定@FeignClient调用的地址
 * @RequestMapping 主要用于feign框架拼接传递url，弥补了Ribbon的url需要手动拼接的缺陷
 * @PathVariable("id") 当路径为restful风格时路径传参方式
 * @RequestParam("id") 当路径为？id=250 时传参方式
 * @RequestBody User user 当路径为对象时采用的传参方式，（集合、数组等都属于对象）
 */

/**
 * UserFeign
 *
 * @author yjs
 * @date 2023/7/4
 */

@FeignClient("feign-provider")
@RequestMapping("/provider")
public interface UserFeign {

    @RequestMapping("/getUserById/{id}")
    String getUserById(@PathVariable("id") Integer id);
}
