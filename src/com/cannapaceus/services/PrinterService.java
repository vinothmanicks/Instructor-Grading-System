package com.cannapaceus.services;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PrinterService {

    private static PrinterService instance = null;

    public static PrinterService getInstance() {
        if (instance == null)
            instance = new PrinterService();

        return instance;
    }

    private void startPrintJob(String filename){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Doc doc = new SimpleDoc(inputStream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        attributeSet.add(new Copies(1));

        DocPrintJob job = new PrinterJob.();
        job.print();
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print(attributeSet);
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
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

    /*int[] pageBreaks;  // array of page break line positions.

    /* Synthesise some sample lines of text
    String[] data;

    /*public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {

        Font font = new Font("Serif", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();

        if (pageBreaks == null) {
            int linesPerPage = (int)(pf.getImageableHeight()/lineHeight);
            int numBreaks = (data.length-1)/linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b=0; b<numBreaks; b++) {
                pageBreaks[b] = (b+1)*linesPerPage;
            }
        }

        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         * Since we are drawing text we
         */
        //Graphics2D g2d = (Graphics2D)g;
        //g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         *
        int y = 0;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length)
                ? data.length : pageBreaks[pageIndex];
        for (int line=start; line<end; line++) {
            y += lineHeight;
            g.drawString(data[line], 0, y);
        }

        /* tell the caller that this page is part of the printed document *
        return PAGE_EXISTS;
    }

    public void startPrintJob() {
        PrinterJob job = PrinterJob.getPrinterJob();
        //job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                // The job did not successfully complete
            }
        }
    }

    public void printList(Course course) {
        pageBreaks = null;

        ArrayList<Student> lStudents = course.getlStudents();

        int iSize =  lStudents.size();
        data = new String[iSize];

        for(int i = 0; i < iSize; i++) {
            data[i] = lStudents.get(i).getFirstMIName() + "\t" + lStudents.get(i).getLastName() + "\t" + lStudents.get(i).getStudentID();
        }

        startPrintJob();
    }

    public void printGrades(Course course)
    {
        pageBreaks = null;

        ArrayList<Student> lStudents = course.getlStudents();
        int iSize = lStudents.size();
        data = new String[iSize];

        int iIterator = 0;

        for(Student student : lStudents) {
            data[iIterator] = student.GenerateStudentReportString();
            ++iIterator;
        }

        startPrintJob();
    }*/
}