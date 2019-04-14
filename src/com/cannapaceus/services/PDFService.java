package com.cannapaceus.services;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import com.sun.istack.internal.Nullable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.ArrayList;

public class PDFService {

    private static PDFService instance = null;

    public static PDFService getInstance() {
        if (instance == null) {
            instance = new PDFService();
        }
        return instance;
    }

    /* Synthesise some sample lines of text */
    String[] data;

    public String printList(Course course, Boolean bTemp) {
        OSService osService = OSService.getInstance();
        ArrayList<Student> lStudents = course.getlStudents();
        String filename;

        if (bTemp) {
            filename = osService.getTempDirectoryPath() + "Grades_" + course.getCourseName() + ".pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + "Grades_" + course.getCourseName() + ".pdf";

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();

            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 720);

            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Last Name");
            contentStream.newLineAtOffset(160, 0);
            contentStream.showText("First and Middle Name");
            contentStream.newLineAtOffset(160, 0);
            contentStream.showText("Student ID");
            contentStream.newLineAtOffset(-320, 0);
            contentStream.newLine();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            for (Student student : lStudents) {
                contentStream.showText(student.getLastName());
                contentStream.newLineAtOffset(160, 0);
                contentStream.showText(student.getFirstMIName());
                contentStream.newLineAtOffset(160, 0);
                contentStream.showText(student.getStudentID());
                contentStream.newLineAtOffset(-320, 0);
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();

            document.save(filename);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String printGrades(Course course, Boolean bTemp) {
        OSService osService = OSService.getInstance();
        ArrayList<Student> lStudents = course.getlStudents();
        String filename;

        if (bTemp) {
            filename = osService.getTempDirectoryPath() + "Grades_" + course.getCourseName() + ".pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + "Grades_" + course.getCourseName() + ".pdf";

        try {
            PDDocument document = new PDDocument();

            for(Student student : lStudents) {
                PDPage page = new PDPage();
                document.addPage(page);

                String[] data = student.GenerateStudentReportArray();

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 720);

                contentStream.showText("Course Name: " + course.getCourseName());
                contentStream.newLine();
                contentStream.newLine();

                for (String text : data) {
                    contentStream.showText(text);
                    contentStream.newLine();
                }

                contentStream.endText();
                contentStream.close();
            }

            document.save(filename);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String printGrades(Student student, Course course, Boolean bEncrypt, Boolean bTemp) {
        OSService osService = OSService.getInstance();
        String filename;

        if (bEncrypt || bTemp) {
            filename = osService.getTempDirectoryPath() + "Grades_" + course.getCourseName() + "_" + student.getFirstMIName() + student.getLastName() + ".pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + "Grades_" + course.getCourseName() + "_" + student.getFirstMIName() + student.getLastName() + ".pdf";

        try {
            PDDocument document = new PDDocument();

            PDPage page = new PDPage();
            document.addPage(page);

            String[] data = student.GenerateStudentReportArray();

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 720);

            contentStream.showText("Course Name: " + course.getCourseName());
            contentStream.newLine();
            contentStream.newLine();

            for (String text : data) {
                contentStream.showText(text);
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            if (bEncrypt == true) {
                //Creating access permission object
                AccessPermission ap = new AccessPermission();

                //Creating StandardProtectionPolicy object
                StandardProtectionPolicy spp = new StandardProtectionPolicy("1234", student.getStudentID(), ap);

                //Setting the length of the encryption key
                spp.setEncryptionKeyLength(128);

                //Setting the access permissions
                spp.setPermissions(ap);

                //Protecting the document
                document.protect(spp);
            }

            document.save(filename);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }
}
