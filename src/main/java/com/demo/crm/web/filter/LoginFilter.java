package com.demo.crm.web.filter;

import com.demo.crm.settings.domain.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入过滤器：验证是否登录");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        if("/login.jsp".equals(servletPath) || "/settings/user/login.do".equals(servletPath)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {

            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");
            if(user != null){
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                response.sendRedirect(request.getContextPath()+"/login.jsp");          //重定向到登录页
            }
        }




    }
}
