package com.example.demo.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchMail {
    //Chercher le mail de centre
    public static String getCentreMail(String centre){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        String sql="select Adresse_mail from centres where Code_Cosium=?";
        String centreMail="";

        try {
            connection= JDBCUtils.getConnection();
            preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,centre);
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                centreMail=resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try{
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
        return centreMail;
    }

    //chercher le mail de technicien
    public static String getTechMail(String nomTech){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        String sql="select Adresse_mail from techniciens where Nom=?";
        String techMail="";

        try {
            connection= JDBCUtils.getConnection();
            preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,nomTech);
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                techMail=resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try{
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
        return techMail;
    }
}
