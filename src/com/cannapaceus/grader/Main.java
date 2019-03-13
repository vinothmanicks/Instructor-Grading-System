/**
 * @author - Charles Grady, James Shelton, Nicholas Bryant, Vinoth Kumar Manickavasagam
 */
package com.cannapaceus.grader;

import java.util.*;

public class Main {

    /**
     * The program execution starts here
     * @param args
     */

    public static void main (String[] args) {
        // Do not remove below print statement
        System.out.println("Instructor Grading Program");
/*
        DBService db = new DBService();
        db.loginDB();
*/
        Course newCourse = new Course("testCourse", "testID", "testDepartment");
        Student stuAlice = new Student("Alice", "Cain", "abc0013", "abc0013@uah.edu");
        newCourse.addStudent(stuAlice);
        Student stuBob = new Student("Bob", "Bobson", "bbb0011", "bbb0011@uah.edu");
        newCourse.addStudent(stuBob);
        Category cat1 = new Category("onlyCategory", 1.0f);
        Date dueDateMondaiIchi = new Date(20022019);
        Date assignedDateMondaiIchi = new Date(27022019);
        Assignment assignment1 = new Assignment("A1",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,cat1,1.0f);
        Assignment assignment2 = new Assignment("A2",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,cat1,1.0f);
        Assignment assignment3 = new Assignment("A3",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,cat1,1.0f);
        newCourse.addAssignment(assignment1);
        newCourse.addAssignment(assignment2);
        newCourse.addAssignment(assignment3);
        Grade Grade1 = new Grade(100, stuAlice, assignment1);
        newCourse.addGrade(Grade1);
        stuAlice.addGrade(Grade1);
        Grade Grade2 = new Grade(75, stuAlice, assignment2);
        newCourse.addGrade(Grade2);
        stuAlice.addGrade(Grade2);
        Grade Grade3 = new Grade(85, stuAlice, assignment3);
        newCourse.addGrade(Grade3);
        stuAlice.addGrade(Grade3);
        stuAlice.setAverageGrade(stuAlice.getGrades());
        Grade Grade4 = new Grade(99, stuBob, assignment1);
        newCourse.addGrade(Grade4);
        stuBob.addGrade(Grade4);
        Grade Grade5 = new Grade(88, stuBob, assignment2);
        newCourse.addGrade(Grade5);
        stuBob.addGrade(Grade5);
        Grade Grade6 = new Grade(77, stuBob, assignment3);
        newCourse.addGrade(Grade6);
        stuBob.addGrade(Grade6);
        stuBob.setAverageGrade(stuBob.getGrades());
        newCourse.PopulateAverages(newCourse.getlStudents());
        newCourse.calculateStats();
    }

}
