package com.demo.crm.workbench.web.controller;


import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.settings.service.impl.UserServiceImpl;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.PrintJson;
import com.demo.crm.utils.ServiceFactory;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.CustomerDao;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.domain.Tran;
import com.demo.crm.workbench.domain.TranHistory;
import com.demo.crm.workbench.service.ActivityService;
import com.demo.crm.workbench.service.CustomerService;
import com.demo.crm.workbench.service.TranService;
import com.demo.crm.workbench.service.impl.ActivityServiceImpl;
import com.demo.crm.workbench.service.impl.CustomerServiceImpl;
import com.demo.crm.workbench.service.impl.TranServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    private ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
    private UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
    private CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
    private TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入交易控制器：TranController");
        String servletPath = req.getServletPath();

        if("/workbench/transaction/add.do".equals(servletPath)){
            add(req,resp);
        }else if("/workbench/transaction/getCustomerName.do".equals(servletPath)){
            getCustomerName(req,resp);
        }else if("/workbench/transaction/save.do".equals(servletPath)){
            save(req,resp);
        }else if("/workbench/transaction/detail.do".equals(servletPath)){
            detail(req,resp);
        }else if("/workbench/transaction/getHistoryByTranId.do".equals(servletPath)){
            getHistoryByTranId(req,resp);
        }else if("/workbench/transaction/changeStage.do".equals(servletPath)){
            changeStage(req,resp);
        }else if("/workbench/transaction/getCharts.do".equals(servletPath)){
            getCharts(req,resp);
        }
    }


    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入跳转到交易添加页方法");
        List<User> userList = userService.getUserList();
        req.setAttribute("userList",userList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }
    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入获取客户名称列表（模糊查询）方法");
        String name = req.getParameter("name");
        List<String> list = customerService.getCustomerName(name);
        PrintJson.printJsonObj(resp,list);
    }
    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String money = req.getParameter("money");
        String name = req.getParameter("name");
        String expectedDate = req.getParameter("expectedDate");
        String customerName = req.getParameter("customerName");
        String stage = req.getParameter("stage");
        String type = req.getParameter("type");
        String source = req.getParameter("source");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();;
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        boolean flag = tranService.save(tran,customerName);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到详细信息也");
        String id = req.getParameter("id");
        Tran tran = tranService.detail(id);



        String stage = tran.getStage();
        Map<String,String> map = (Map)this.getServletContext().getAttribute("pMap");
        String s = map.get(stage);

        req.setAttribute("tran",tran);
        req.setAttribute("s",s);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);

    }

    private void getHistoryByTranId(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入根据交易ID获取历史列表方法");
        String id = req.getParameter("tranId");
        List<TranHistory> list= tranService.getHistoryByTranId(id);
        Map<String,String> map = (Map)this.getServletContext().getAttribute("pMap");
        for (TranHistory tranHistory : list) {
            String stage = tranHistory.getStage();
            String s = map.get(stage);
            tranHistory.setPossibility(s);
        }


        PrintJson.printJsonObj(resp,list);

    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行改变阶段操作");
        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editBy = ((User)req.getSession().getAttribute("user")).getName();;
        String editTime = DateTimeUtil.getSysTime();
        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        boolean flag = tranService.changeStage(tran);

        Map<String,String> pMap = (Map)this.getServletContext().getAttribute("pMap");
        tran.setPossibility(pMap.get(stage));


        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);

        PrintJson.printJsonObj(resp,map);

    }

    private void getCharts(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("取交易阶段图表数据方法");
        Map<String,Object> map = tranService.getCharts();
        PrintJson.printJsonObj(resp,map);
    }
}
