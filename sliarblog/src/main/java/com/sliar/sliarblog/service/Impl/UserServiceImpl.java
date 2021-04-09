package com.sliar.sliarblog.service.Impl;

import com.sliar.sliarblog.dao.UserRepository;
import com.sliar.sliarblog.entity.User;
import com.sliar.sliarblog.service.UserService;
import com.sliar.sliarblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
