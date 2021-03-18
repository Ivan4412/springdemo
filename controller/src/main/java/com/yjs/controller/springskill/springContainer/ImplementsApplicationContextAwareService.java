package com.yjs.controller.springskill.springContainer;

import com.yjs.domain.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


/**
 * author : yjs
 * createTime : 2021/3/3
 * description :
 * 2.实现ApplicationContextAware接口
 * 实现ApplicationContextAware接口，然后重写setApplicationContext方法，也能从该方法中获取到spring容器对象。
 * version : 1.0
 */
@Service
public class ImplementsApplicationContextAwareService implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void add() {
        User user = (User) applicationContext.getBean("user");
    }
}