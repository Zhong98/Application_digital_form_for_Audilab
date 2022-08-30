package com.example.demo.servlet;

import com.example.demo.utils.InsertData;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/loginSucceed")
public class LoginSucceed extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Cookie[] cookies = req.getCookies();
        Cookie cookie=null;
        boolean hasStatus=false;
        if (cookies!=null){
            int cookiesLen=cookies.length;
            for (int i = 0; i < cookiesLen; i++) {
                if (cookies[i].getName().equals("status")){
                    hasStatus=true;
                    cookie=cookies[i];
                }
            }
        }
        if (hasStatus){
            cookie.setValue("succeed");
        }else {
            cookie=new Cookie("status","succeed");
        }

        //clear cookie "save"
        if (cookies!=null){
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("save")){
                    cookies[i].setMaxAge(0);
                    resp.addCookie(cookies[i]);
                    break;
                }
            }
        }
        cookie.setMaxAge(1800);
        resp.addCookie(cookie);

        //Créer un record occupe une place pour éviter deux fiches qui portent le même numéro. 
        InsertData.insertData();

        resp.sendRedirect("http://10.37.15.110:8080/content.html");
    }
}
