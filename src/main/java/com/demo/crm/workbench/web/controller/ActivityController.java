package com.demo.crm.workbench.web.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入市场活动控制器：ActivityController");
        String servletPath = req.getServletPath();

        if("/workbench/activity/xxx.do".equals(servletPath)){
            //xxx(req,resp);

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }else if("/settings/user/xxx.do".equals(servletPath)){

        }
    }


}
