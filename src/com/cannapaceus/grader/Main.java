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
        Course coTest = new Course("Prob and Stats", "MA 385", "Mathematics");
        Student stuBobby = new Student("BobbyJ","Miller","00451J","bjm0001@UAH.EDU");
        Category catBlank = new Category(null);
        Date dueDateMondaiIchi = new Date(20022019);
        Date assignedDateMondaiIchi = new Date(27022019);
        Assignment aASSignment = new Assignment("Mondai-ichi",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,catBlank,0.30f);
        Grade gBobbyAssGrade = new Grade(20,stuBobby,aASSignment);
        aASSignment.addGrade(gBobbyAssGrade);

        ArrayList<Grade> gHolder = aASSignment.getGrades();

        System.out.println(gHolder.get(0).getGrade());
    }

}
