package com.demo.crm.workbench.web.controller;


import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.settings.service.impl.UserServiceImpl;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.PrintJson;
import com.demo.crm.utils.ServiceFactory;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.service.ActivityService;
import com.demo.crm.workbench.service.impl.ActivityServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    private ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
    private UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入市场活动控制器：ActivityController");
        String servletPath = req.getServletPath();

        if("/workbench/activity/getUserList.do".equals(servletPath)){
            getUserList(req,resp);

        }else if("/workbench/activity/save.do".equals(servletPath)){
            save(req,resp);

        }else if("/workbench/activity/pageList.do".equals(servletPath)){
            pageList(req,resp);

        }else if("/workbench/activity/delete.do".equals(servletPath)){
            delete(req,resp);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(servletPath)){
            getUserListAndActivity(req,resp);
        } else if("/workbench/activity/update.do".equals(servletPath)){
            update(req,resp);
        }else if("/workbench/activity/detail.do".equals(servletPath)){
            detail(req,resp);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(servletPath)){
            getRemarkListByAid(req,resp);
        }else if("/workbench/activity/deleteRemark.do".equals(servletPath)){
            deleteRemark(req,resp);
        } else if("/workbench/activity/saveRemark.do".equals(servletPath)){
            saveRemark(req,resp);
        }else if("/workbench/activity/updateRemark.do".equals(servletPath)){
            updateRemark(req,resp);
        }
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入获取用户列表方法");


        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入模态窗口的保存操作方法");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setName(name);
        activity.setCost(cost);
        activity.setCreateBy(createBy);
        activity.setOwner(owner);
        activity.setCreateTime(createTime);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setId(id);
        activity.setDescription(description);





        boolean flag = activityService.save(activity);

        PrintJson.printJsonFlag(resp,flag);



    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入分页方法");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);        //每页展现
        int skipCount = (pageNo-1)*pageSize;                //略过几条
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String endDate = req.getParameter("endDate");
        String startDate = req.getParameter("startDate");

        Map<String, Object> map = new HashMap();
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        map.put("name",name);
        map.put("owner",owner);
        map.put("endDate",endDate);
        map.put("startDate",startDate);

        PaginationVO<Activity> paginationVO = activityService.pageList(map);
        PrintJson.printJsonObj(resp,paginationVO);

    }

    private void delete(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入删除市场活动方法");
        String[] ids = req.getParameterValues("id");
        System.out.println(ids);

        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入到修改页面展示数据的方法");
        String id = req.getParameter("id");

        Map<String,Object> map =  activityService.getUserListAndActivity(id);       //List Activity
        PrintJson.printJsonObj(resp,map);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入市场活动修改的方法");

        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        String id = req.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setName(name);
        activity.setCost(cost);
        activity.setEditBy(editBy);
        activity.setOwner(owner);
        activity.setEditTime(editTime);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setId(id);
        activity.setDescription(description);

        boolean flag = activityService.update(activity);

        PrintJson.printJsonFlag(resp,flag);

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入跳转到相信信息页的方法");
        String id = req.getParameter("id");

        Activity activity = activityService.detail(id);
        req.setAttribute("activity",activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);

    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入根据市场活动ID获取备注信息列表方法");
        String activityId = req.getParameter("activityId");
        List<ActivityRemark> list = activityService.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(resp,list);
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入删除备注信息的方法");
        String id = req.getParameter("id");
        boolean flag =  activityService.deleteRemark(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入保存备注信息的方法");
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String uuid = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setNoteContent(noteContent);
        activityRemark.setActivityId(activityId);
        activityRemark.setId(uuid);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);

        boolean flag =  activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(resp,map);

    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入修复备注信息的方法");
        String noteContent = req.getParameter("noteContent");
        String id = req.getParameter("id");
        String editFlag = "1";
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) req.getSession().getAttribute("user")).getName();
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setEditFlag(editFlag);
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditTime(editTime);
        boolean flag =  activityService.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(resp,map);
    }

}
