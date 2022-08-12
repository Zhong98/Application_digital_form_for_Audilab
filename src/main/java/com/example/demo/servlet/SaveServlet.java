package com.example.demo.servlet;

import com.example.demo.utils.CreateCSV;
import com.example.demo.utils.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/saveServlet")
public class SaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();



        String id = req.getParameter("idFiche");
        String societe = req.getParameter("societe");
        String centre = req.getParameter("centre");
        String technicien = req.getParameter("technicien");
        String date = req.getParameter("date");
        String timeArrive = req.getParameter("time_arrive");
        String timeDepart = req.getParameter("time_depart");
        //String[] actions = req.getParameterValues("actionAdded");

        System.out.println(societe);
        //if (req.getParameter("signTech")!=null && req.getParameter("signClient")!=null){
            if (!societe.equals("--Merci de selectionner une societe--")) {
                if (!centre.isEmpty()) {
                    if (!technicien.isEmpty()) {
                        if (!date.isEmpty()) {
                            if (!timeArrive.isEmpty()) {
                                if (!timeDepart.isEmpty()) {
                                    int rest;
                                    if (req.getParameter("rest").isEmpty()) {
                                        rest = 0;
                                    } else {
                                        rest = Integer.parseInt(req.getParameter("rest"));
                                    }

                                    //calculer le prix
                                    //Prix Intervention
                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                                    Date arriveTime;
                                    Date departTime;
                                    try {
                                        arriveTime = simpleDateFormat1.parse(timeArrive);
                                        departTime = simpleDateFormat1.parse(timeDepart);
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    long arrive = arriveTime.getTime();
                                    long depart = departTime.getTime();
                                    int minutes = (int) ((depart - arrive) / (1000 * 60));
                                    int hours;
                                    if (rest != 0) {
                                        hours = (minutes - rest) / 60;
                                    } else {
                                        hours = minutes / 60;
                                    }
                                    int priceInter;
                                    if (hours >= 5) {
                                        priceInter = 500;
                                    } else {
                                        if (minutes % 60 != 0) {
                                            hours++;
                                        }
                                        priceInter = 100 * hours;
                                    }


                                    //Créer csv file
                                    int qte;
                                    int priceEta = 0;
                                    int code = 0;
                                    String strQte = req.getParameter("qte");
                                    if (strQte.isEmpty()) {
                                        qte = 1;
                                    } else {
                                        qte = Integer.parseInt(strQte);
                                    }
                                    String codePostal = req.getParameter("codePostal");
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
                                    CreateCSV.createCSV(societe, date, qte, priceInter, priceEta, code);


                                    //actions
                                    String actMsg = "";


                                    //domain
                                    String domain = req.getParameter("domain");
                                    if (domain.equals("-- Merci de selectionner votre poste --")) {
                                        domain = "";
                                    }

                                    //satisfaction
                                    int satisfaction = 0;
                                    if (!req.getParameter("satisfaction").isEmpty()) {
                                        satisfaction = Integer.parseInt(req.getParameter("satisfaction"));
                                    }

                                    //img signature
                                    Blob signatureClient= null;
                                    Blob signatureTechnicien=null;
                                    try {
                                        signatureClient = new SerialBlob(req.getParameter("signClient").getBytes("GBK"));
                                        signatureTechnicien = new SerialBlob(req.getParameter("signTech").getBytes("GBK"));
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    //date
                                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                    Date parse;
                                    try {
                                        parse = simpleDateFormat2.parse(date);
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    java.sql.Date sqlDate = new java.sql.Date(parse.getTime());


                                    Connection connection = null;
                                    PreparedStatement preparedStatement = null;
                                    try {
                                        connection = JDBCUtils.getConnection();
                                        String sql = "insert into fiches(id,Societe,Centre,Nom_technicien,`Date`,Heure_arr,Heure_dep,Pause,PrixInter,Actions,`domain`,`Sign_client(base64)`,`Sign_tech(base64)`,satisfaction) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                        preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setInt(1, Integer.parseInt(id));
                                        preparedStatement.setString(2, societe);
                                        preparedStatement.setString(3, centre);
                                        preparedStatement.setString(4, technicien);
                                        preparedStatement.setDate(5, sqlDate);
                                        preparedStatement.setString(6, timeArrive);
                                        preparedStatement.setString(7, timeDepart);
                                        preparedStatement.setInt(8, rest);
                                        preparedStatement.setInt(9, priceInter);
                                        preparedStatement.setString(10, actMsg);
                                        preparedStatement.setString(11, domain);
                                        preparedStatement.setBlob(12, signatureClient);
                                        preparedStatement.setBlob(13, signatureTechnicien);
                                        preparedStatement.setInt(14, satisfaction);
                                        preparedStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    } finally {
                                        try {
                                            preparedStatement.close();
                                            connection.close();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    Cookie cookie = new Cookie("save", "OK");
                                    resp.addCookie(cookie);
                                    cookie.setMaxAge(10);
                                    resp.sendRedirect("http://10.37.15.110:8080/content.html");
                                } else {
                                    writer.write("<h1>Merci de entre l'heure départ</h1>");
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    resp.sendRedirect("http://10.37.15.110:8080/content.html");
                                }
                            } else {
                                writer.write("<h1>Merci de entre l'heure arrivée</h1>");
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                resp.sendRedirect("http://10.37.15.110:8080/content.html");
                            }
                        } else {
                            writer.write("<h1>Merci de selectionner la date</h1>");
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            resp.sendRedirect("http://10.37.15.110:8080/content.html");
                        }
                    } else {
                        writer.write("<h1>Merci de selectionner un technicien</h1>");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        resp.sendRedirect("http://10.37.15.110:8080/content.html");
                    }
                } else {
                    writer.write("<h1>Merci de selectionner un centre</h1>");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    resp.sendRedirect("http://10.37.15.110:8080/content.html");
                }
            } else {
                resp.getWriter().print(1);
            }
        }
    //}
}
