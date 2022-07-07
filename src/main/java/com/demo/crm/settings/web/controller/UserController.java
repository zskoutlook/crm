package com.demo.crm.settings.web.controller;

import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.settings.service.impl.UserServiceImpl;
import com.demo.crm.utils.MD5Util;
import com.demo.crm.utils.PrintJson;
import com.demo.crm.utils.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入用户控制器：UserController");
        String servletPath = req.getServletPath();

        if("/settings/user/login.do".equals(servletPath)){
            login(req,resp);

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp){
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        System.out.println("进入用户控制器：下的登录方法");

        String loginAct = req.getParameter("loginAct");              //例如：zs
        String loginPwd = req.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);                            //例如    202cb962ac59075b964b07152d234b70
        String ip = req.getRemoteAddr();                                //例如    7.0.0.1


        try {
            User user = userService.login(loginAct,loginPwd,ip);
            req.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(resp,true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(resp,map);
        }

    }
}
