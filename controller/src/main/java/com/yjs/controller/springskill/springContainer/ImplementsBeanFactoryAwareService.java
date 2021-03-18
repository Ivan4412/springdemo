package com.yjs.controller.springskill.springContainer;

import com.yjs.domain.entity.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;


/**
 * author : yjs
 * createTime : 2021/3/3
 * description :
 * 1.实现BeanFactoryAware接口
 *      实现BeanFactoryAware接口，然后重写setBeanFactory方法，就能从该方法中获取到spring容器对象。
 * version : 1.0
 */
@Service
public class ImplementsBeanFactoryAwareService implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    public void add(){
        User user = (User) beanFactory.getBean("user");
    }
}