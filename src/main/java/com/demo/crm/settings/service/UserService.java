package com.demo.crm.settings.service;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

}
