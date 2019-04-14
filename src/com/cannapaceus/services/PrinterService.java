package com.cannapaceus.services;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.xml.ws.Service;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PrinterService {

    private static PrinterService instance = null;

    public static PrinterService getInstance() {
        if (instance == null)
            instance = new PrinterService();

        return instance;
    }

    private void startPrintJob(String filename){

        PDDocument pdf = null;

        try {
            pdf = PDDocument.load(new File(filename));
            PrinterJob job = PrinterJob.getPrinterJob();

            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            PrintService selectedPrintService = ServiceUI.printDialog(null, 150, 150, printServices, defaultPrintService, null, attributeSet);

            if(selectedPrintService!=null) {
                DocPrintJob docPrintJob = selectedPrintService.createPrintJob();

                job.setPrintService(docPrintJob.getPrintService());
                job.setPageable(new PDFPageable(pdf));

                job.print(attributeSet);
            }

            //'top': The top input tray in the printer.
            //'middle': The middle input tray in the printer.
            //'bottom': The bottom input tray in the printer.
            //'envelope': The envelope input tray in the printer.
            //'manual': The manual feed input tray in the printer.
            //'large-capacity': The large capacity input tray in the printer.
            //'main': The main input tray
            //'side': The side input tray

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public void printList(Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printList(course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    public void printGrades(Student student, Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGrades(student, course, false, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    public void printGrades(Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGrades(course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }
}