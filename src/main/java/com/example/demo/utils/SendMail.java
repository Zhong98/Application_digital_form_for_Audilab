package com.example.demo.utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SendMail {

    public static void sendHtmlEmail(String date, String technicien, String centreMail, String technicienMail, String filePath) {
        MimeMessage message;
        Session session;
        Transport transport = null;

        String mailHost = "";
        String sender_username = "";
        String sender_password = "";

        Properties properties = new Properties();

        InputStream in = SendMail.class.getClassLoader().getResourceAsStream("mail.properties");
        try {
            properties.load(in);
            mailHost = properties.getProperty("mail.host");
            sender_username = properties.getProperty("mail.username");
            sender_password = properties.getProperty("mail.password");
        } catch (IOException e) {
            e.printStackTrace();
        }

        session = Session.getInstance(properties);
        message = new MimeMessage(session);


        try {
            // Expéditeur
            InternetAddress from = new InternetAddress(sender_username);
            message.setFrom(from);

            // Destinataires
            InternetAddress toCentre = new InternetAddress(centreMail);
            InternetAddress toTechnicien = new InternetAddress(technicienMail);
            InternetAddress[] receivers=new InternetAddress[]{toCentre,toTechnicien};

            message.setRecipients(Message.RecipientType.TO,receivers);

            // objet
            String subject="Compte rendu d'intervention du"+date;
            message.setSubject(subject);

            // L'objet multipart, la texte, la pièce-jointe sont ajoutées dans ce objet
            Multipart multipart = new MimeMultipart();

            // texte
            BodyPart contentPart = new MimeBodyPart();
            String sendHtml="Voici en Pièce jointe le Compte rendu d'intervention du "+date+" faite par "+technicien;
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            // Ajouter la pièce-jointe
            contentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(new File(filePath));

            contentPart.setDataHandler(new DataHandler(source));
            //Nom de la pièce-jointe
            contentPart.setFileName("Fiche intervention "+date+".pdf");
            multipart.addBodyPart(contentPart);


            // Mettre l'objet multipart dans le message
            message.setContent(multipart);
            // Enregistrer les changements
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp vérifications
            transport.connect(mailHost, sender_username, sender_password);
            // Envoyer
            transport.sendMessage(message, message.getAllRecipients());

            System.out.println("send success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
