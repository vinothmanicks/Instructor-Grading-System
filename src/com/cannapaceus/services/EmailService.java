/**
 * @author - Vinoth Kumar Manickavasagam
 */

package com.cannapaceus.services;

import com.cannapaceus.grader.*;

import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    // Variable to store session
    Session session;
    // Variable to store session properties
    private Properties props;

    /**
     * Singleton Class
     */

    private static EmailService instance = null;

    public static EmailService getInstance() {
        //if (instance == null)
            //instance = new EmailService();
        return instance;
    }

    public static EmailService getInstance(String sFromAddress, String sPassword) {
        if (instance == null)
            instance = new EmailService(sFromAddress, sPassword);
        return instance;
    }

    //Constructor
    /**
     * @param sFromAddress
     * @param sPassword
     */
    private EmailService(String sFromAddress, String sPassword) {
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
        session = Session.getInstance(props,new javax.mail.Authenticator() {
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

    // Methods

    /**
     * @param lTo
     * @param sSubject
     */
    private void SendEmail(String lTo, String sSubject, String sMessageText) {

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sFromAddress));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(lTo));

            // Set Subject: header field
            message.setSubject(sSubject);

            // Now set the actual message
            message.setText(sMessageText);

            // Send message
            Transport.send(message);

            //System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
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

    public boolean email(Course course) {
        ArrayList<Student> lStudents = course.getlStudents();
        String sSubjectText = "Grade Report for " + course.getCourseName();

        for (Student student : lStudents) {
            String sMessageText = student.GenerateStudentReport();
            SendEmail(student.getStudentEmail(), sSubjectText, sMessageText);
        }

        return true;
    }

    public boolean email(Student student, Course course) {
        String sSubjectText = "Grade Report for " + course.getCourseName();
        String sMessageText = student.GenerateStudentReport();
        SendEmail(student.getStudentEmail(), sSubjectText, sMessageText);

        return true;
    }
}