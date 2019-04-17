package com.cannapaceus.services;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class PrinterService {

    /** Singleton Design Pattern**/
    private static PrinterService instance = null;

    public static PrinterService getInstance() {
        if (instance == null)
            instance = new PrinterService();

        return instance;
    }

    /**
     *
     * @param filename
     */
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints list of students in a course.
     * @param course Course for which student list is to be printed.
     */
    public void printList(Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printList(course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    /**
     *
     * @param student
     * @param course
     */
    public void printGrades(Student student, Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGrades(student, course, false, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    /**
     *
     * @param course
     */
    public void printGrades(Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGrades(course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    /**
     * For printing the overall grade report for a course
     * @param course
     */
    public void printGradeBook(Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGradeBook(course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }

    /**
     * For printing the grade report for a specific assignment
     * @param assignment
     * @param course
     */
    public void printGradeBook(Assignment assignment, Course course) {
        OSService osService = OSService.getInstance();
        PDFService pdfService = PDFService.getInstance();

        String filename = pdfService.printGradeBook(assignment, course, true);
        startPrintJob(filename);
        osService.delete(filename);
    }
}