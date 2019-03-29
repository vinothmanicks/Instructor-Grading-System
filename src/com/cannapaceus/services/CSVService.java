package com.cannapaceus.services;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Student;
import com.cannapaceus.grader.Term;

import com.opencsv.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSVService {

    private CSVWriter writer;

    public void ExportCSV(List<String[]> data){
        try {
            // create FileWriter object with file as parameter
            String filename = Today() + ".csv";
            FileWriter outputfile = new FileWriter(filename);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // create a List which contains String array

            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ExportCSV(Term term){
        try {
            // create FileWriter object with file as parameter
            String filename = Today() + "_" + term.getYear() + term.getSeason() + ".csv";
            FileWriter outputfile = new FileWriter(filename);

            // create CSVWriter object filewriter object as parameter
            writer = new CSVWriter(outputfile);

            //Write CSV document styling
            String[] fileType = {"FileType", "Term"};
            writer.writeNext(fileType);

            String[] courseName = {"Year", String.valueOf(term.getYear())};
            writer.writeNext(courseName);

            String[] courseID = {"Season", term.getSeason().toString()};
            writer.writeNext(courseID);

            String[] status = {"Archived", String.valueOf(term.getArchivedStatus())};
            writer.writeNext(status);

            String[] emptyLine = {" "};
            writer.writeNext(emptyLine);

            String[] header = {"Course Name", "Course ID", "Department"};
            writer.writeNext(header);

            ArrayList<Course> lCourses = term.getCourses();
            int iSize = lCourses.size();

            for(int i = 0; i < iSize; i++) {
                String[] data = {lCourses.get(i).getCourseName(), lCourses.get(i).getCourseID(), lCourses.get(i).getDepartment()};
                writer.writeNext(data);
            }

            String[] footer = {"___END___"};
            writer.writeNext(footer);
            writer.writeNext(emptyLine);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ExportCSV(Course course, Term term){
        try {
            // create FileWriter object with file as parameter
            String filename = Today() + "_" + course.getCourseName() + "_" + term.getYear() + term.getSeason() + ".csv";
            FileWriter outputfile = new FileWriter(filename);

            // create CSVWriter object filewriter object as parameter
            writer = new CSVWriter(outputfile);

            //Write CSV document styling
            String[] fileType = {"FileType", "Course"};
            writer.writeNext(fileType);

            String[] courseName = {"CourseName", course.getCourseName()};
            writer.writeNext(courseName);

            String[] courseID = {"CourseID", course.getCourseID()};
            writer.writeNext(courseID);

            String[] departmentName = {"DepartmentName", course.getDepartment()};
            writer.writeNext(departmentName);

            String[] emptyLine = {" "};
            writer.writeNext(emptyLine);

            CSVWriter_Assignment(course.getlAssignments());
            CSVWriter_Student(course.getlStudents());

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CSVWriter_Assignment(ArrayList<Assignment> lAssignments) {
        String[] dataType = {"DataType", "Assignment"};
        writer.writeNext(dataType);

        String[] emptyLine = {" "};
        writer.writeNext(emptyLine);

        String[] header = {"Assignment Name", "Due Date", "Assigned Date", "Dropped", "Max Score", "Category", "Weight"};
        writer.writeNext(header);

        int iSize = lAssignments.size();

        for(int i = 0; i < iSize; i++) {
            String[] data = {lAssignments.get(i).getAssignmentName(), lAssignments.get(i).getDueDate().toString(), lAssignments.get(i).getAssignmentName().toString(), String.valueOf(lAssignments.get(i).getDroppedAssignment()), String.valueOf(lAssignments.get(i).getMaxScore()), lAssignments.get(i).getCategoryCopy().getName(), String.valueOf(lAssignments.get(i).getWeight())};
            writer.writeNext(data);
        }

        String[] footer = {"___END___"};
        writer.writeNext(footer);
        writer.writeNext(emptyLine);
    }

    private void CSVWriter_Student(ArrayList<Student> lStudents) {
        String[] dataType = {"DataType", "Student"};
        writer.writeNext(dataType);

        String[] emptyLine = {" "};
        writer.writeNext(emptyLine);

        String[] header = {"First MI Name", "Last Name", "Student ID", "Student Email"};
        writer.writeNext(header);

        int iSize = lStudents.size();

        for(int i = 0; i < iSize; i++) {
            String[] data = {lStudents.get(i).getFirstMIName(), lStudents.get(i).getLastName(), lStudents.get(i).getStudentID(), lStudents.get(i).getStudentEmail()};
            writer.writeNext(data);
        }

        String[] footer = {"___END___"};
        writer.writeNext(footer);
        writer.writeNext(emptyLine);
    }

    /*
    public void ExportCSV() {
        java.sql.ResultSet myResultSet = getResultSetFromSomewhere();
        writer.writeAll(myResultSet, includeHeaders); //writer is instance of CSVWriter
    }*/

    public void ImportCSV() {

    }

    public String Today() {
        String pattern = "yyyyMMdd_HHmmss";

        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date according to the chosen pattern
        DateFormat df = new SimpleDateFormat(pattern);

        // Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String todayAsString = df.format(today);

        return todayAsString;
    }
}
