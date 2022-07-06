package com.demo.crm.settings.service.impl;

import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
