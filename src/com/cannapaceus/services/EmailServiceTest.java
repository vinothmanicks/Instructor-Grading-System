package com.cannapaceus.services;

import org.junit.jupiter.api.Test;
import java.util.*;


class EmailServiceTest {

    @Test
    void sendEmailTest() {
        // Add your own email and password for now
        //EmailService emailService = new EmailService("", "");
        List<String> lTo = new ArrayList<String>();
        lTo.add("vm0025@uah.edu");
        lTo.add("nab0016@uah.edu");
        lTo.add("cjg0019@uah.edu");
        lTo.add("jes0030@uah.edu");
        //emailService.SendEmail(lTo, "[CS 499] Test Email with Attachment");
    }
}