package com.demo.crm.settings.service;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
    List<User> getUserList();
}
