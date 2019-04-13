package com.cannapaceus.services;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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

    public String printList(Course course) {
        OSService osService = OSService.getInstance();
        ArrayList<Student> lStudents = course.getlStudents();

        int iSize =  lStudents.size();
        data = new String[iSize];

        for(int i = 0; i < iSize; i++) {
            data[i] = lStudents.get(i).getFirstMIName() + "    " + lStudents.get(i).getLastName() + "    " + lStudents.get(i).getStudentID();
        }

        String filename = osService.getDesktopDirectoryPath() + "StudentList_" + course.getCourseName() + ".pdf";

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 720);
            for (String text : data) {
                contentStream.showText(text);
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

    public String printGrades(Course course, Boolean bEncrypt) {
        OSService osService = OSService.getInstance();
        ArrayList<Student> lStudents = course.getlStudents();
        String filename;

        if (bEncrypt) {
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

    public String printGrades(Student student, Course course, Boolean bEncrypt) {
        OSService osService = OSService.getInstance();
        String filename;

        if (bEncrypt) {
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
