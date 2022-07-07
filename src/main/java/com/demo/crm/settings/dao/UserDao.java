package com.demo.crm.settings.dao;

import com.demo.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, Object> map);
}
