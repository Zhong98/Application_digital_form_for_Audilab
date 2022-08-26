package com.example.demo.servlet;

import com.example.demo.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sun.misc.BASE64Decoder;

@WebServlet("/saveServlet")
public class SaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");

        String id = req.getParameter("idFiche");
        String societe = req.getParameter("societe");
        String centre = req.getParameter("centre");
        String technicien = req.getParameter("technicien");
        String date = req.getParameter("date");
        String timeArrive = req.getParameter("time_arrive");
        String timeDepart = req.getParameter("time_depart");
        String actions = req.getParameter("actions");
        String articles = req.getParameter("articlesAdded");

            if (!societe.equals("--Merci de selectionner une societe--")) {
                if (!centre.equals("--Merci de selectionner un centre--")) {
                    if (!technicien.equals("--Merci de selectionner un technicien--")) {
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
                                    int priceInter= CalculatePrice.calculatePrice(timeArrive,timeDepart,rest);


                                    //Créer le fichier csv
                                    String codePostal = req.getParameter("codePostal");
                                    int qte;
                                    int priceEta = 0;
                                    int code = SearchCentreCode.searchCentreCode(codePostal,centre); //Chercher le code cosium de centre
                                    String strQte = req.getParameter("qte");
                                    if (strQte.isEmpty()) {
                                        qte = 1;
                                    } else {
                                        qte = Integer.parseInt(strQte);
                                    }
                                    CreateCSV.createCSV(societe, date, qte, priceInter, priceEta, code);

                                    //images de signature
                                    BASE64Decoder decoder = new BASE64Decoder();
                                    String imgClient="";
                                    String imgTech="";
                                    try
                                    {
                                        //Base64 decode
                                        byte[] b1 = decoder.decodeBuffer(req.getParameter("signClient"));
                                        byte[] b2 = decoder.decodeBuffer(req.getParameter("signTech"));
                                        for(int i=0;i<b1.length;++i)
                                        {
                                            if(b1[i]<0)
                                            {//Modifier les données incorrectes
                                                b1[i]+=256;
                                            }
                                        }
                                        for(int i=0;i<b2.length;++i)
                                        {
                                            if(b2[i]<0)
                                            {
                                                b2[i]+=256;
                                            }
                                        }
                                        //Créer les images en jpg
                                        imgClient = "C:\\test\\img\\Signature_Client\\signClient_"+id+".jpg";
                                        imgTech = "C:\\test\\img\\Signature_Technicien\\signTech_"+id+".jpg";
                                        OutputStream out1 = new FileOutputStream(imgClient);
                                        OutputStream out2 = new FileOutputStream(imgTech);
                                        out1.write(b1);
                                        out2.write(b2);
                                        out1.flush();
                                        out2.flush();
                                        out1.close();
                                        out2.close();
                                    }
                                    catch (Exception e)
                                    {
                                        System.out.println(e.getMessage());
                                    }


                                    //Qualité
                                    String domain = new String(req.getParameter("domain").getBytes(),"UTF-8");
                                    if (domain.equals("-- Merci de selectionner votre poste --")) {
                                        domain = "";
                                    }

                                    //Créer un fichier en PDF
                                    String pause;
                                    if (rest==0){
                                        pause="";
                                    }else {
                                        pause=rest+"";
                                    }
                                    String filePath=CreatePDF.createPDF(technicien,societe,centre,date,timeArrive,timeDepart,pause,actions,articles,priceInter+" €",domain,id,imgClient,imgTech,qte+"");

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


                                    //Envoi de Mail
                                    /*String centreMail=SearchMail.getCentreMail(centre);
                                    String technicienMail=SearchMail.getTechMail(technicien);*/
                                    SendMail.sendHtmlEmail(date,technicien,"980819213zzx@gmail.com","zixiao.zhong98@gmail.com",filePath);


                                    Connection connection = null;
                                    PreparedStatement preparedStatement = null;
                                    try {
                                        connection = JDBCUtils.getConnection();
                                        String sql = "update fiches set Societe=?,Centre=?,Nom_technicien=?,`Date`=?,Heure_arr=?,Heure_dep=?,Pause=?,PrixInter=?,Actions=?,Refacturer=?,`domain`=?,`Sign_client(blob)`=?,`Sign_tech(blob)`=?,satisfaction=? where id=?";
                                        preparedStatement = connection.prepareStatement(sql);
                                        preparedStatement.setString(1, societe);
                                        preparedStatement.setString(2, centre);
                                        preparedStatement.setString(3, technicien);
                                        preparedStatement.setDate(4, sqlDate);
                                        preparedStatement.setString(5, timeArrive);
                                        preparedStatement.setString(6, timeDepart);
                                        preparedStatement.setInt(7, rest);
                                        preparedStatement.setInt(8, priceInter);
                                        preparedStatement.setString(9, actions);
                                        preparedStatement.setString(10, articles);
                                        preparedStatement.setString(11, domain);
                                        preparedStatement.setBlob(12, signatureClient);
                                        preparedStatement.setBlob(13, signatureTechnicien);
                                        preparedStatement.setInt(14, satisfaction);
                                        preparedStatement.setInt(15, Integer.parseInt(id));
                                        preparedStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        System.out.println(e.getMessage());
                                    } finally {
                                        try {
                                            preparedStatement.close();
                                            connection.close();
                                        } catch (SQLException e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                    Cookie cookie = new Cookie("save", "OK");
                                    resp.addCookie(cookie);
                                } else {
                                    resp.getWriter().print(6);
                                }
                            } else {
                                resp.getWriter().print(5);
                            }
                        } else {
                            resp.getWriter().print(4);
                        }
                    } else {
                        resp.getWriter().print(3);
                    }
                } else {
                    resp.getWriter().print(2);
                }
            } else {
                resp.getWriter().print(1);
            }
        }
    //}
}
