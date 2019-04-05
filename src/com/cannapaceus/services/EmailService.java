/**
 * @author - Vinoth Kumar Manickavasagam
 */

package com.cannapaceus.services;

import com.cannapaceus.grader.*;

import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.jfoenix.validation.RegexValidator;

public class EmailService {
    // Variables
    // String to store sender ID
    private String sSender;
    // String to store sender password
    private String sPassword;
    // String to store sender email address
    private String sFromAddress;
    // String to store SMTP server name;
    private String sHost;

    private String sMessageText = "";

    // Variable to store session
    Session session;
    // Variable to store session properties
    private Properties props;


    //Constructor
    /**
     * @param sFromAddress
     * @param sPassword
     */
    public EmailService(String sFromAddress, String sPassword) {
        //this.sSender = sSender;
        this.sPassword = sPassword;

        this.sFromAddress = sFromAddress;

        sHost = "smtp.gmail.com";

        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Get the Session object.
        session = Session.getInstance(props,
        new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sFromAddress, sPassword);
            }
        });
    }

    //Getters and setters

    public String getsSender() {
        return sSender;
    }

    public void setsSender(String sSender) {
        this.sSender = sSender;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public void setsMessageText(String sMessageText){this.sMessageText = sMessageText; }

    // Methods

    /**
     * @param lTo
     * @param sSubject
     */
    public void SendEmail(List<String> lTo, String sSubject) {

        for (String to : lTo) {
            try {
                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(sFromAddress));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

                // Set Subject: header field
                message.setSubject(sSubject);

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText(sMessageText);

                // Create a multipart message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                /*messageBodyPart = new MimeBodyPart();
                String filename = "C:\\Users\\vinot\\Desktop\\TrialDocument.pdf";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts*/
                message.setContent(multipart);


                // Send message
                Transport.send(message);


                System.out.println("Sent message successfully....");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if (mat.matches()) {
           return true;
        }
        return false;
    }
}