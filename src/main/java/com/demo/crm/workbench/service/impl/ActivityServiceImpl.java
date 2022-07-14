package com.demo.crm.workbench.service.impl;

import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.domain.User;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ActivityRemarkDao;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = null;
    private ActivityRemarkDao activityRemarkDao = null;
    private UserDao userDao = null;

    @Override
    public boolean save(Activity activity) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

        boolean flag = true;
        int count = activityDao.save(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }


    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

        int total = activityDao.getTotalCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        PaginationVO<Activity> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(dataList);
        return paginationVO;
    }

    @Override
    public boolean delete(String[] ids) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean flag = true;

        //查询除需要删除的备注数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注返回受影响备注条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        //备注：对比
        if(count1 != count2){
            flag = false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        List<User> userList = userDao.getUserList();        //用户列表
        Activity activity = activityDao.getById(id);        //activity
        Map<String,Object> map = new HashMap<>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

        boolean flag = true;
        int count = activityDao.update(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        return activityRemarkDao.getRemarkListByAid(activityId);
    }

    @Override
    public boolean deleteRemark(String id) {
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean flag = true;
        int count =  activityRemarkDao.deleteRemark(id);
        if (count != 1){
            flag =false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = true;
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count =  activityRemarkDao.saveRemark(activityRemark);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = true;
        activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count =  activityRemarkDao.updateRemark(activityRemark);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.getActivityListByClueId(clueId);
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.getActivityListByNameAndNotByClueId(map);
    }

    @Override
    public List<Activity> getActivityListByName(String activityName) {
        activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        return activityDao.getActivityListByName(activityName);
    }
}
