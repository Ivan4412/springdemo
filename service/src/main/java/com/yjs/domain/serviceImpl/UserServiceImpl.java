package com.yjs.domain.serviceImpl;

import com.yjs.domain.entity.User;
import com.yjs.domain.mapper.UserMapper;
import com.yjs.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserMapper userMapper;

	@Override
	public User getUserById(Long id) {
		return userMapper.getUserById(id);
	}

	@Override
	public List<User> getUserList() {
		return userMapper.getUserList();
	}

	@Override
	public int add(User user) {
		return userMapper.add(user);
	}

	@Override
	public int update(Long id, User user) {
		return userMapper.update(id, user);
	}

	@Override
	public int delete(Long id) {
		return userMapper.delete(id);
	}
}