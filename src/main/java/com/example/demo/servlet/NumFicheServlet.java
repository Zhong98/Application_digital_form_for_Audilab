package com.example.demo.servlet;

import com.example.demo.utils.InsertData;
import com.example.demo.utils.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/numFiche")
public class NumFicheServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sql = "select COUNT(*) from fiches";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int num = 0;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            //Créer un record pour occuper une place pour éviter deux fiches qui portent le même numéro
            Cookie[] cookies = req.getCookies();
            if (cookies!=null){
                boolean hasSaved = false;
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("save")){
                        hasSaved=true;
                        break;
                    }
                }
                if (hasSaved){
                    InsertData.insertData(); //Créer une donnée vide occupe une place pour éviter deux fiche ont meme numéro
                }
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                num = resultSet.getInt(1);
            }
            resp.getWriter().print(num);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if (resultSet!=null){
                    resultSet.close();
                }
                if (preparedStatement!=null){
                    preparedStatement.close();
                }
                if (connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
