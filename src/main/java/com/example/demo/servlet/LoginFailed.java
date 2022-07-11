package com.example.demo.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/loginFailed")
public class LoginFailed extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        Cookie cookie=null;
        boolean hasStatus=false;
        if(cookies!=null){
            int cookiesLen=cookies.length;
            for (int i = 0; i < cookiesLen; i++) {
                if (cookies[i].getName().equals("status")){
                    hasStatus=true;
                    cookie=cookies[i];
                }
            }
        }
        if (hasStatus){
            cookie.setValue("failed");
        }else {
            cookie=new Cookie("status","failed");
        }
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);
        resp.sendRedirect("http://10.37.15.110:8080");
    }
}
