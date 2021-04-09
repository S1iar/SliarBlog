package com.sliar.sliarblog.service;

import com.sliar.sliarblog.entity.User;

public interface UserService {
    User checkUser(String username, String password);
}
