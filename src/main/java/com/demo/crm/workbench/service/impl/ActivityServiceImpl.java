package com.demo.crm.workbench.service.impl;

import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
}
