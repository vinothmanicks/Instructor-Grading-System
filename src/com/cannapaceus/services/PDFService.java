package com.cannapaceus.services;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import com.cannapaceus.qbank.Question;
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

    /* Synthesise new page */
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

            int iCounter = 2;
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

    public void printAssignment(Course course, Assignment assignment, ArrayList<Question> selectedQuestions) {
        OSService osService = OSService.getInstance();
        String filename;

        filename = osService.getDesktopDirectoryPath() + course.getCourseName() + "_" + selectedQuestions.get(0).getQuestionAssignmentType().toString() + "_" + assignment.getAssignmentName() + ".pdf";

        try {
            document = new PDDocument();
            GenerateNewPage();

            contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 16);
            contentStream.showText("Course Name: " + course.getCourseName());
            contentStream.newLine();
            contentStream.newLine();
            contentStream.setFont(PDType1Font.TIMES_BOLD, 14);
            contentStream.showText("Assignment Name: " + assignment.getAssignmentName());
            contentStream.newLine();
            contentStream.showText("Due Date: " + assignment.getDueDate());
            contentStream.newLine();
            contentStream.showText("Assigned Date: " + assignment.getAssignedDate());
            contentStream.newLine();
            contentStream.showText("Max Score: " + assignment.getMaxScore());
            contentStream.newLine();

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            int iNumberOfQuestions = 0;
            int iCounter = 6;
            for (Question question : selectedQuestions) {
                ++iNumberOfQuestions;
                contentStream.newLine();
                ++iCounter;
                String[] words = question.getQuestion().split(" ");
                contentStream.showText(iNumberOfQuestions + ". ");
                int iCharacterCounter = 3;

                for (int j = 0; j < words.length; j++) {
                    if(words[j].length() <= 120) {
                        if (iCharacterCounter + words[j].length() > 120) {
                            contentStream.newLine();
                            ++iCounter;
                            iCharacterCounter = 0;
                        }

                        if (iCounter == 47) {
                            GenerateNewPage();
                            iCounter = 0;
                        }

                        contentStream.showText(words[j] + " ");
                        iCharacterCounter = iCharacterCounter + 1 + words[j].length();
                    }

                    else if (words[j].length() > 120) {
                        contentStream.newLine();
                        int iSplitter = 0;
                        while (iSplitter < (words[j].length()/120)) {
                            if (iCounter == 47) {
                                GenerateNewPage();
                                iCounter = 0;
                            }
                            contentStream.showText(words[j].substring(iSplitter*120, iSplitter*120 + 119));
                            contentStream.newLine();
                            ++iSplitter;
                            ++iCounter;
                        }
                        if (iCounter == 47) {
                            GenerateNewPage();
                            iCounter = 0;
                        }
                        contentStream.showText(words[j].substring(iSplitter*120));

                        ++iCounter;
                    }
                }

                if(iCounter == 47){
                    GenerateNewPage();
                    iCounter = 0;
                }

                contentStream.newLine();

                switch (question.getQuestionType()) {
                    case LONGANSWER:
                        for (int i = 0; i < 7; i++) {
                            if(iCounter == 47){
                                GenerateNewPage();
                                iCounter = 0;
                            }
                            contentStream.newLine();
                            ++iCounter;
                        }
                        break;

                    case SHORTANSWER:
                        for (int i = 0; i < 2; i++) {
                            if(iCounter == 47){
                                GenerateNewPage();
                                iCounter = 0;
                            }
                            contentStream.newLine();
                            ++iCounter;
                        }
                        break;

                    case TRUEORFALSE:
                        if(iCounter == 47){
                            GenerateNewPage();
                            iCounter = 0;
                        }
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("a. True");
                        contentStream.newLineAtOffset(220, 0);
                        contentStream.showText("b. False");
                        contentStream.newLineAtOffset(-320, 0);
                        contentStream.newLine();
                        ++iCounter;
                        break;

                    case FILLINTHEBLANK:
                        if(iCounter == 47){
                            GenerateNewPage();
                            iCounter = 0;
                        }
                        contentStream.newLine();
                        ++iCounter;
                        break;

                    case MULTIPLECHOICE:
                        break;

                    default:
                        for (int i = 0; i < 5; i++) {
                            if(iCounter == 47){
                                GenerateNewPage();
                                iCounter = 0;
                            }
                            contentStream.newLine();
                            ++iCounter;
                        }
                        break;
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
    }
}
