package com.cannapaceus.services;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import com.cannapaceus.grader.Grade;
import javafx.beans.property.StringProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

public class PrinterService implements Printable {

    private static PrinterService instance = null;

    public static PrinterService getInstance() {
        if (instance == null)
            instance = new PrinterService();

        return instance;
    }


    int[] pageBreaks;  // array of page break line positions.

    /* Synthesise some sample lines of text */
    String[] data;

    public int print(Graphics g, PageFormat pf, int pageIndex)
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
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length)
                ? data.length : pageBreaks[pageIndex];
        for (int line=start; line<end; line++) {
            y += lineHeight;
            g.drawString(data[line], 0, y);
        }

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }

    public void startPrintJob() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                /* The job did not successfully complete */
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
        ArrayList<Assignment> lAssignments = course.getlAssignments();



        int iSize =  lStudents.size();
        int jSize =  lAssignments.size();
        data = new String[iSize+1];

        data[0] = "Student ID";
        for(int j = 0; j<jSize; j++)
        {
            data[0] = data[0] + "\t" + lAssignments.get(j).getAssignmentName();
        }

        for(int i = 1; i < iSize; i++) {
            data[i] = lStudents.get(i).getStudentID();
            for(int j = 0; j < jSize; j++) {
                for (Grade gGrade: lAssignments.get(j).getGrades()) {
                    if(gGrade.getStudentCopy().getStudentID().equals(lStudents.get(i).getStudentID()))
                    {
                        data[i] = data[i] + "\t" + gGrade.getGrade();
                    }
                }
            }
        }

        startPrintJob();

    }

}