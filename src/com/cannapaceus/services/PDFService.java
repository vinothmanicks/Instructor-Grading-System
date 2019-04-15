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
    PDPageContentStream contentStream = null;
    PDDocument document;

    public static PDFService getInstance() {
        if (instance == null) {
            instance = new PDFService();
        }
        return instance;
    }

    /* Synthesise some sample lines of text */
    private void GenerateNewPage() throws IOException {
        if(contentStream != null) {
            contentStream.endText();
            contentStream.close();
        }

        PDPage page = new PDPage();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 730);
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
    }

    public String printList(Course course, Boolean bTemp) {
        OSService osService = OSService.getInstance();
        ArrayList<Student> lStudents = course.getlStudents();
        String filename;

        if (bTemp) {
            filename = osService.getTempDirectoryPath() + course.getCourseName() + "_StudentList.pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + course.getCourseName() + "_StudentList.pdf";

        try {
            document = new PDDocument();
            GenerateNewPage();

            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.showText("Last Name");
            contentStream.newLineAtOffset(160, 0);
            contentStream.showText("First and Middle Name");
            contentStream.newLineAtOffset(160, 0);
            contentStream.showText("Student ID");
            contentStream.newLineAtOffset(-320, 0);
            contentStream.newLine();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            int iCounter = 1;
            for (Student student : lStudents) {
                contentStream.showText(student.getLastName());
                contentStream.newLineAtOffset(160, 0);
                contentStream.showText(student.getFirstMIName());
                contentStream.newLineAtOffset(160, 0);
                contentStream.showText(student.getStudentID());
                contentStream.newLineAtOffset(-320, 0);
                contentStream.newLine();
                ++iCounter;

                if(iCounter >= 47) {
                    GenerateNewPage();
                    iCounter = 0;
                }
            }
            contentStream.endText();
            contentStream.close();

            document.save(filename);
            document.close();

            contentStream = null;

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
            filename = osService.getTempDirectoryPath() + course.getCourseName() + "_Grades.pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + course.getCourseName() + "_Grades.pdf";

        try {
            document = new PDDocument();

            for(Student student : lStudents) {
                GenerateNewPage();

                String[] data = student.GenerateStudentReportArray();

                contentStream.showText("Course Name: " + course.getCourseName());
                contentStream.newLine();
                contentStream.newLine();

                int iCounter = 3;
                for (String text : data) {
                    contentStream.showText(text);
                    contentStream.newLine();

                    iCounter += 2;

                    if(iCounter>=47) {
                        GenerateNewPage();
                        iCounter = 0;
                    }
                }

                contentStream.endText();
                contentStream.close();
            }

            document.save(filename);
            document.close();

            contentStream = null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String printGrades(Student student, Course course, Boolean bEncrypt, Boolean bTemp) {
        OSService osService = OSService.getInstance();
        String filename;

        if (bEncrypt || bTemp) {
            filename = osService.getTempDirectoryPath() + course.getCourseName() + "_Grades_" + student.getFirstMIName() + "_" + student.getLastName() + ".pdf";
        }
        else
            filename = osService.getDesktopDirectoryPath() + course.getCourseName() + "_Grades_" + student.getFirstMIName() + "_" + student.getLastName() + ".pdf";

        try {
            document = new PDDocument();
            GenerateNewPage();

            String[] data = student.GenerateStudentReportArray();

            contentStream.showText("Course Name: " + course.getCourseName());
            contentStream.newLine();
            contentStream.newLine();

            int iCounter = 3;
            for (String text : data) {
                contentStream.showText(text);
                contentStream.newLine();

                iCounter += 2;

                if(iCounter>=47) {
                    GenerateNewPage();
                    iCounter = 0;
                }
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

            contentStream = null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }
}
