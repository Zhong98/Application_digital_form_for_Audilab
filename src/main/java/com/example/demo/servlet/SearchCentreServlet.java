package com.example.demo.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entities.Centre;
import com.example.demo.utils.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/searchCentre")
public class SearchCentreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Centre> centreList=new ArrayList<>();
        String sql="select Code_Cosium, Code_Postal, Societe from centres";

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection= JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                Centre centre=new Centre();
                centre.setCodeCosium(resultSet.getString(1));
                centre.setCodePostal(resultSet.getInt(2));
                centre.setSociete(resultSet.getString(3));
                centreList.add(centre);
            }

            JSONArray centreJsonArray;
            centreJsonArray = JSONArray.parseArray(JSONObject.toJSONString(centreList));
            resp.getWriter().print(centreJsonArray);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
