/**
 * @author - Charles Grady, James Shelton, Nicholas Bryant, Vinoth Kumar Manickavasagam
 */
package com.cannapaceus.grader;

import java.util.*;
import java.time.*;

public class Main {

    /**
     * The program execution starts here
     * @param args
     */

    public static void main (String[] args) {
        // Do not remove below print statement
        System.out.println("Instructor Grading Program");

        DBService db = new DBService();
        db.loginDB();

        Course coTest = new Course("Prob and Stats", "MA 385", "Mathematics");
        Student stuBobby = new Student("BobbyJ","Miller","00451J","bjm0001@UAH.EDU");
        Category catBlank = new Category(null);
        LocalDate dueDateMondaiIchi = LocalDate.of(2019, 02, 20);
        LocalDate assignedDateMondaiIchi = LocalDate.of(2019, 02, 27);
        Assignment aAssignment = new Assignment("Mondai-ichi",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,catBlank,0.30f);
        Grade gBobbyAssGrade = new Grade(20,stuBobby,aAssignment);
        aAssignment.addGrade(gBobbyAssGrade);

        db.StoreCourse(coTest, 0);
        db.StoreCategory(catBlank, coTest.getDBID());
        db.StoreAssignment(aAssignment, coTest.getDBID());
        db.StoreStudent(stuBobby, coTest.getDBID());
        db.StoreGrade(gBobbyAssGrade, coTest.getDBID());

        ArrayList<Grade> gHolder = aAssignment.getGrades();

        System.out.println(gHolder.get(0).getGrade());
    }

}
