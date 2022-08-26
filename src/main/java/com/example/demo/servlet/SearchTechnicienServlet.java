package com.example.demo.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet("/searchTech")
public class SearchTechnicienServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        ArrayList<String> techniciens=new ArrayList<>();
        try {
            String sql="select Nom from techniciens";
            connection= JDBCUtils.getConnection();
            preparedStatement= connection.prepareStatement(sql);
            resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                techniciens.add(resultSet.getString(1));
            }

            JSONArray technicienArray=JSONArray.parseArray(JSONObject.toJSONString(techniciens));
            resp.getWriter().print(technicienArray);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection!=null){

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
