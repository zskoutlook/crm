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
import com.demo.crm.workbench.domain.Clue;
import com.demo.crm.workbench.domain.Tran;
import com.demo.crm.workbench.service.ActivityService;
import com.demo.crm.workbench.service.ClueService;
import com.demo.crm.workbench.service.impl.ActivityServiceImpl;
import com.demo.crm.workbench.service.impl.ClueServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    private UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
    private ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
    private ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入线索控制器：ClueController");
        String servletPath = req.getServletPath();

        if("/workbench/clue/getUserList.do".equals(servletPath)){
            getUserList(req,resp);
        }else if("/workbench/clue/save.do".equals(servletPath)){
            save(req,resp);
        }else if("/workbench/clue/pageList.do".equals(servletPath)){
            pageList(req,resp);
        }else if("/workbench/clue/detail.do".equals(servletPath)){
            detail(req,resp);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(servletPath)){
            getActivityListByClueId(req,resp);
        } else if("/workbench/clue/unbund.do".equals(servletPath)){
            unbund(req,resp);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(servletPath)){
            getActivityListByNameAndNotByClueId(req,resp);
        } else if("/workbench/clue/bund.do".equals(servletPath)){
            bund(req,resp);
        }else if("/workbench/clue/getActivityListByName.do".equals(servletPath)){
            getActivityListByName(req,resp);
        } else if("/workbench/clue/convert.do".equals(servletPath)){
            convert(req,resp);
        }
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入获取用户列表方法");
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入创建模态保存方法");
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();;
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        String id = UUIDUtil.getUUID();

        Clue clue = new Clue();
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        clue.setId(id);

        boolean flag =  clueService.save(clue);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("进入分页方法");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);        //每页展现
        int skipCount = (pageNo-1)*pageSize;                //略过几条
        String company = req.getParameter("company");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String source = req.getParameter("source");
        String owner = req.getParameter("owner");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");

        Map<String, Object> map = new HashMap();
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        map.put("company",company);
        map.put("owner",owner);
        map.put("fullname",fullname);
        map.put("phone",phone);
        map.put("state",state);
        map.put("mphone",mphone);
        map.put("source",source);

        PaginationVO<Clue> paginationVO = clueService.pageList(map);
        PrintJson.printJsonObj(resp,paginationVO);

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页");
        String id = req.getParameter("id");

        Clue clue = clueService.detail(id);
        req.setAttribute("clue",clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("跳转到根据线索id查询关联的市场活动");
        String clueId = req.getParameter("clueId");

        List<Activity> list = activityService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(resp,list);
    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("接触关联操作");
        String id = req.getParameter("id");
        boolean flag = clueService.unbund(id);
        PrintJson.printJsonFlag(resp,flag);
    }
    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("模糊查市场活动列表");
        String activityName = req.getParameter("activityName");
        String clueId = req.getParameter("clueId");
        Map<String,String> map =  new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> list = activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(resp,list);
    }
    private void bund(HttpServletRequest req, HttpServletResponse resp){
        String clueId = req.getParameter("clueId");
        String[] activityIds = req.getParameterValues("activityId");
        boolean flag = clueService.bund(clueId,activityIds);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("模糊查所有市场活动列表");
        String activityName = req.getParameter("activityName");
        List<Activity> list = activityService.getActivityListByName(activityName);
        PrintJson.printJsonObj(resp,list);
    }
    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("线索转换方法");
        String clueId = req.getParameter("clueId");
        String flag = req.getParameter("flag");
        String creatBy = ((User)req.getSession().getAttribute("user")).getName();
        Tran t = null;

        if("flag".equals(flag)){
            t= new Tran();
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String creatTime = DateTimeUtil.getSysTime();

            t.setName(name);
            t.setActivityId(activityId);
            t.setId(id);
            t.setMoney(money);
            t.setStage(stage);
            t.setExpectedDate(expectedDate);
            t.setCreateBy(creatBy);
            t.setCreateTime(creatTime);
        }

        boolean f = clueService.convert(clueId,t,creatBy);
        if (f){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }

    }
}
