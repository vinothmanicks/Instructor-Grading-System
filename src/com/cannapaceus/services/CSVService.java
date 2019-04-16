package com.cannapaceus.services;

import com.cannapaceus.grader.*;
import com.opencsv.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            OSService osService = OSService.getInstance();

            // create FileWriter object with file as parameter
            String filename = Today() + "_" + course.getCourseName() + "_" + term.getYear() + term.getSeason() + ".csv";
            FileWriter outputfile = new FileWriter(osService.getDesktopDirectoryPath() + filename);

            // create CSVWriter object filewriter object as parameter
            writer = new CSVWriter(outputfile,',',writer.NO_QUOTE_CHARACTER);


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

            CSVWriter_Categories(course);
            CSVWriter_Assignment(course.getlAssignments());
            CSVWriter_Student(course.getlStudents());
            CSVWriter_Grades(course);


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

            String catName;
            String weightCust;
            if(lAssignments.get(i).getCategoryReference() == null)
            {
                catName = "Uncategorized";
            }
            else
            {
                catName = lAssignments.get(i).getCategoryReference().getName();
            }

            if(lAssignments.get(i).getWeight() == null)
            {
                weightCust = "";
            }
            else
            {
                weightCust = String.valueOf(lAssignments.get(i).getWeight());
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            String[] data = {lAssignments.get(i).getAssignmentName(), lAssignments.get(i).getDueDate().format(formatter).replace("-", "/"), lAssignments.get(i).getAssignedDate().format(formatter).replace("-", "/"), String.valueOf(lAssignments.get(i).getDroppedAssignment()), String.valueOf(lAssignments.get(i).getMaxScore()), catName, weightCust};
            writer.writeNext(data);
        }

        String[] footer = {"___END___"};
        writer.writeNext(footer);
        writer.writeNext(emptyLine);
    }

    private void CSVWriter_Grades(Course cCourse)
    {
        String[] dataType = {"DataType", "Grade"};
        writer.writeNext(dataType);

        String[] emptyLine = {" "};
        writer.writeNext(emptyLine);

        String[] gradeScale = {"GradeScale",String.valueOf(cCourse.getScale())};
        writer.writeNext(gradeScale);

        ArrayList<String> header = new ArrayList<>();
        header.add("Student ID");
        for (Assignment aAssignment: cCourse.getlAssignments())
        {
            header.add(aAssignment.getAssignmentName());
        }
        String[] aheader = new String[header.size()];
        aheader = header.toArray(aheader);
        writer.writeNext(aheader);

        for(Student stuStudent: cCourse.getlStudents()) {
            ArrayList<String> dataList = new ArrayList<>();
            dataList.add(stuStudent.getStudentID());
            for (Assignment aAssignment : cCourse.getlAssignments()) {
                for (Grade gGrade:aAssignment.getGrades()) {
                    if(gGrade.getStudentReference().getStudentID().equals(stuStudent.getStudentID()))
                    {
                        dataList.add(String.valueOf(gGrade.getGrade()));
                        break;
                    }
                }
            }
            String[] data = new String[dataList.size()];
            data = dataList.toArray(data);
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

    private void CSVWriter_Categories(Course coCourse) {
        String[] dataType = {"DataType", "Category"};
        writer.writeNext(dataType);

        String[] emptyLine = {" "};
        writer.writeNext(emptyLine);

        String[] header = {"Category Name", "Weight", "Dropped Grades"};
        writer.writeNext(header);

        int iSize = coCourse.getlCategories().size();

        for(int i = 0; i < iSize; i++) {
            String[] data = {coCourse.getlCategories().get(i).getName(), String.valueOf(coCourse.getlCategories().get(i).getWeight()),String.valueOf(coCourse.getlCategories().get(i).getDropped())};
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

    public Course ImportCSV(String csvFilePath) {
        String line = "";
        String cvsSplitBy = ",";
        Course importedCourse = new Course("","","");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            while ((line = br.readLine()) != null)
            {
                String[] lineElements= line.split(",");

                if(lineElements[0] != null) {
                    switch (lineElements[0]) {
                        case "FileType":
                            /*if (lineElements[1] != "Course") {
                                System.out.println("Invalid file imported");
                                return null;
                            }*/
                            break;
                        case "CourseName":
                            importedCourse.setCourseName(lineElements[1]);
                            break;
                        case "CourseID":
                            importedCourse.setCourseID(lineElements[1]);
                            break;
                        case "DepartmentName":
                            importedCourse.setDepartment(lineElements[1]);
                            break;
                        case "DataType":
                            switch (lineElements[1])
                            {
                                case "Category":
                                    while(((line = br.readLine()) != null)&&(!line.contains("_END_")))
                                    {
                                        lineElements = line.split(",");
                                        if((lineElements[0].equals(" "))||(lineElements[0].trim().equals("Category Name")))
                                        {
                                            continue;
                                        }
                                        Category newCat = new Category(lineElements[0],Float.valueOf(lineElements[1]), Integer.valueOf(lineElements[2]));
                                        importedCourse.addCategory(newCat);
                                    }
                                    break;
                                case "Assignment":
                                    while(((line = br.readLine()) != null)&&(!line.contains("_END_")))
                                    {
                                        lineElements = line.split(",");
                                        if((lineElements[0].equals(" "))||(lineElements[0].trim().equals("Assignment Name")))
                                        {
                                            continue;
                                        }
                                        Assignment addedAssignment = new Assignment("",null,null,false,100,null,new Float(100));
                                        addedAssignment.setAssignmentName(lineElements[0]);
                                        String[] dateSections = lineElements[1].split("/");
                                        LocalDate dueDate = LocalDate.of(Integer.valueOf(dateSections[2]),Integer.valueOf(dateSections[1]),Integer.valueOf(dateSections[0]));
                                        addedAssignment.setDueDate(dueDate);
                                        dateSections = lineElements[2].split("/");
                                        LocalDate assignedDate = LocalDate.of(Integer.valueOf(dateSections[2]),Integer.valueOf(dateSections[1]),Integer.valueOf(dateSections[0]));
                                        addedAssignment.setAssignedDate(assignedDate);
                                        addedAssignment.setDroppedAssignment(Boolean.valueOf(lineElements[3]));
                                        addedAssignment.setMaxScore(Float.valueOf(lineElements[4]));
                                        for (Category cat:importedCourse.getlCategories())
                                        {
                                            if(cat.getName().equals(lineElements[5]))
                                            {
                                                addedAssignment.setCategory(cat);
                                                break;
                                            }
                                        }
                                        if(lineElements.length <= 6)
                                        {
                                            addedAssignment.setWeight(null);
                                        }
                                        else
                                        {
                                            addedAssignment.setWeight(Float.valueOf(lineElements[6]));
                                        }
                                        importedCourse.addAssignment(addedAssignment);
                                    }
                                    break;
                                case "Student":
                                    while(((line = br.readLine()) != null)&&(!line.contains("_END_")))
                                    {
                                        lineElements = line.split(",");
                                        if((lineElements[0].equals(" "))||(lineElements[0].trim().equals("First MI Name")))
                                        {
                                            continue;
                                        }
                                        Student addedStudent = new Student("","","","");
                                        addedStudent.setFirstMIName(lineElements[0]);
                                        addedStudent.setLastName(lineElements[1]);
                                        addedStudent.setStudentID(lineElements[2]);
                                        addedStudent.setStudentID(lineElements[3]);
                                        importedCourse.addStudent(addedStudent);
                                    }
                                    break;
                                case "Grade":
                                    while(((line = br.readLine()) != null)&&(!line.contains("_END_")))
                                    {
                                        String[] studentGrades = line.split(",");
                                        if((lineElements[0].equals(" "))||(lineElements[0].trim().equals("Student ID")))
                                        {
                                            continue;
                                        }
                                        if(lineElements[0].equals("GradeScale"))
                                        {
                                            importedCourse.setScale(Float.valueOf(lineElements[1]));
                                        }
                                        for (Student stuStudent:importedCourse.getlStudents()) {
                                            if(stuStudent.getStudentID().equals(lineElements[0].trim()))
                                            for (Assignment aAssignment:importedCourse.getlAssignments()) {
                                                int size = importedCourse.getlAssignments().size();
                                                for(int i = 1; i < size; i++) {
                                                    if(studentGrades[i].trim().equals(""))
                                                    {
                                                        break;
                                                    }
                                                    Grade gGrade = new Grade(Float.valueOf(studentGrades[i]), stuStudent, aAssignment);
                                                    aAssignment.addGrade(gGrade);
                                                    stuStudent.addGrade(gGrade);
                                                    importedCourse.addGrade(gGrade);
                                                }
                                            }
                                        }

                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }


            }

            return importedCourse;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
