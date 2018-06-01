package com.yjs.domain.service;

import com.yjs.domain.entity.User;

import java.util.List;

/**
 * Created by lenovo on 2018/6/1.
 */
public interface UserService {

    User getUserById(Long id);

    public List<User> getUserList();

    public int add(User user);

    public int update(Long id, User user);

    public int delete(Long id);
}
