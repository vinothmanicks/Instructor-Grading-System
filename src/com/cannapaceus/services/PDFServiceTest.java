package com.cannapaceus.services;

import org.junit.jupiter.api.Test;

class PDFServiceTest {

    @Test
    public void printTest() {
        PDFService pdfService = new PDFService();
        pdfService.PrintTest();
    }
}