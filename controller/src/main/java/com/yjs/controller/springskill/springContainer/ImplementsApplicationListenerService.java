package com.yjs.controller.springskill.springContainer;

import com.yjs.domain.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;


/**
 * author : yjs
 * createTime : 2021/3/3
 * description :
 * 3.实现ApplicationListener接口
 * 实现ApplicationListener接口，需要注意的是该接口接收的泛型是ContextRefreshedEvent类，
 * 然后重写onApplicationEvent方法，也能从该方法中获取到spring容器对象。
 * version : 1.0
 */
@Service
public class ImplementsApplicationListenerService implements ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
    }

    public void add() {
        User user = (User) applicationContext.getBean("user");
    }
}