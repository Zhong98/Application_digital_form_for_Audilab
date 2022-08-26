package com.example.demo.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchCentreCode {
    public static int searchCentreCode(String codePostal,String centre){
        int code=0;
        String sql1 = "select `Code` from cosium where CP=? and Nom=?";
        if (!codePostal.isEmpty()) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                connection = JDBCUtils.getConnection();
                preparedStatement = connection.prepareStatement(sql1);
                int CP = Integer.parseInt(codePostal);
                preparedStatement.setInt(1, CP);
                preparedStatement.setString(2, centre);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    code = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return code;
    }
}
