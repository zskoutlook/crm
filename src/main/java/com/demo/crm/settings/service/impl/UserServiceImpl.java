package com.demo.crm.settings.service.impl;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);


        User user = userDao.login(map);

        if (user == null){
            throw new LoginException("账号或密码错误");
        }
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("IP受限");
        }


        return user;
    }
}
