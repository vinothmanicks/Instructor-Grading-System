/**
 * @author - Vinoth Kumar Manickavasagam
 */

package com.cannapaceus.services;

import com.cannapaceus.grader.*;

import java.util.ArrayList;
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

    private String errMessage = null;

    // Variable to store session
    Session session;
    // Variable to store session properties
    private Properties props;

    private OSService osService;
    private PDFService pdfService;

    /**
     * Singleton Class
     */

    private static EmailService instance = null;

    public static EmailService getInstance() {
        if (instance == null)
            instance = new EmailService(null, null);
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

        osService = OSService.getInstance();
        pdfService = PDFService.getInstance();
    }

    //Getters and setters

    public String getsSender() {
        return sSender;
    }

    public void setsSender(String sSender) {
        this.sSender = sSender;
    }

    public String getsFromAddress() {
        return sFromAddress;
    }

    public void setsFromAddress(String sSender) {
        this.sFromAddress = sSender;
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sFromAddress, sPassword);
            }
        });
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sFromAddress, sPassword);
            }
        });
    }

    // Methods

    /**
     * Constructs the email with an attachment and sends it to the recipient.
     * @param student The student to which you want to send the email.
     * @param course The course in which the student you want to send an email to is in.
     */
    private void SendEmail(Student student, Course course) {

        String sToAddress = student.getStudentEmail();
        String sSubjectText = "Grade Report for " + course.getCourseName();
        String sMessageText = "Hello,\n\n" +
                              "Find your grade report attached to this email.\n" +
                              "To open the password protected file, use your Student ID.\n\n" +
                              "Your Professor";

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sFromAddress));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sToAddress));

            // Set Subject: header field
            message.setSubject(sSubjectText);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(sMessageText);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();

            //Create and add pdf
            String filename = pdfService.printGrades(student, course, true, true);
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            // Delete PDF
            osService.delete(filename);
            errMessage = null;
        } catch (AuthenticationFailedException e) {
            errMessage = "Email or password incorrect. Check the settings page to ensure you entered them correctly.";
        } catch (javax.mail.internet.AddressException e) {
            errMessage = "Set your email and password in the settings first.";
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

    /**
     * Sends email to all students in a course.
     * @param course The course for which you want to email grade reports to
     * @return
     */
    public boolean email(Course course) {
        ArrayList<Student> lStudents = course.getlStudents();

        for (Student student : lStudents) {
            SendEmail(student, course);
            if (errMessage != null)
                return false;
        }

        return true;
    }

    /**
     * Sends email to a student in a course
     * @param student The student to which you want to send the email.
     * @param course The course in which the student you want to send an email to is in.
     * @return
     */
    public boolean email(Student student, Course course) {
        SendEmail(student, course);
        if (errMessage != null)
            return false;
        return true;
    }

    public String getErrorMessage() {
        return this.errMessage;
    }
}