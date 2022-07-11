package com.example.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/content.html")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            int cookiesLen=cookies.length;
            boolean hasStatus=false;
            Cookie cookie=null;
            for (int i = 0; i < cookiesLen; i++) {
                if (cookies[i].getName().equals("status")){
                    hasStatus=true;
                    cookie=cookies[i];
                }
            }
            if (hasStatus==false || cookie.getValue().equals("failed")){
                response.sendRedirect("http://10.37.15.110:8080");
                return;
            }
        }else {
            response.sendRedirect("http://10.37.15.110:8080");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
