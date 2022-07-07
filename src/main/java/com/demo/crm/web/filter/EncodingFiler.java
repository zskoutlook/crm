package com.demo.crm.web.filter;

import jakarta.servlet.*;

import java.io.IOException;

public class EncodingFiler implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入过滤器：中文编码过滤");
        servletRequest.setCharacterEncoding("UTF-8");    //过滤post请求中文参数乱码
        servletResponse.setContentType("text/html;charset=utf-8");  //过滤响应流中文乱码

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
