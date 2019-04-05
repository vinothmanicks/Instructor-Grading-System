package com.cannapaceus.services;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EmailTester {

    public static void main(String args[])
    {
        ArrayList<String> recipiants = new ArrayList<String>();
        String toEmail = new String("bigboi@mailinator.com");
        String myEmail = new String( "ChubbaDubba420@gmail.com");
        recipiants.add(toEmail);
        EmailService es = new EmailService(myEmail,"N000000!");
        es.setsMessageText("Find your grade attached.\n Thanks,\nYour Prof");
        es.SendEmail(recipiants,"Not important");
    }


}
