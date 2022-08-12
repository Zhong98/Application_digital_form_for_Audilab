package com.example.demo.servlet;

import com.example.demo.utils.JDBCUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/loginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        Connection connection;
        String username=req.getParameter("username");
        String password = req.getParameter("password");
        if (!password.isEmpty() && !username.isEmpty()){
            String sql="select COUNT(*) from users where username=? and `password`=?";
            RequestDispatcher requestDispatcher;
            try {
                connection = JDBCUtils.getConnection();
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,password);
                ResultSet resultSet = preparedStatement.executeQuery();
                int num=0;
                if (resultSet.next()){
                    num=resultSet.getInt(1);
                }

                if (num==0){
                    requestDispatcher= req.getRequestDispatcher("/loginFailed");
                }else {
                    requestDispatcher = req.getRequestDispatcher("/loginSucceed");
                }
                requestDispatcher.forward(req,resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }
}