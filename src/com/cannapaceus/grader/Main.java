/**
 * @author - Charles Grady, James Shelton, Nicholas Bryant, Vinoth Kumar Manickavasagam
 */
package com.cannapaceus.grader;

import java.util.*;
import com.cannapaceus.services.*;
import java.time.*;

public class Main {

    /**
     * The program execution starts here
     * @param args
     */

    public static void main (String[] args) {
        // Do not remove below print statement
        System.out.println("Instructor Grading Program");

        DBService db = DBService.getInstance();

        String user;
        String pass;

        do {
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter database username: ");
            user = reader.next();
            System.out.print("Enter database password: ");
            pass = reader.next();
        } while (!db.loginDB(user, pass));


        Term teTest = new Term(2019, eSeason.FALL);
        System.out.println(db.storeTerm(teTest));

        Course coTest = new Course("Prob and Stats", "MA 385", "Mathematics");
        System.out.println(db.storeCourse(coTest, teTest.getDBID()));

        Category catBlank = new Category("Homework", 0.15f);
        System.out.println(db.storeCategory(catBlank, coTest.getDBID()));

        Student stuBobby = new Student("BobbyJ","Miller","00451J","bjm0001@UAH.EDU");
        System.out.println(db.storeStudent(stuBobby, coTest.getDBID()));

        LocalDate dueDateMondaiIchi = LocalDate.of(2019, 02, 20);
        LocalDate assignedDateMondaiIchi = LocalDate.of(2019, 02, 27);
        Assignment aAssignment = new Assignment("Mondai-ichi",dueDateMondaiIchi,assignedDateMondaiIchi,false,100,catBlank,0.30f);
        System.out.println(db.storeAssignment(aAssignment, coTest.getDBID()));

        Grade gBobbyAssGrade = new Grade(20,stuBobby,aAssignment);
        System.out.println(db.storeGrade(gBobbyAssGrade, coTest.getDBID()));

        aAssignment.addGrade(gBobbyAssGrade);
        stuBobby.addGrade(gBobbyAssGrade);

        System.out.println(stuBobby.GenerateStudentReport());

        ArrayList<Term> lTerms;
        Course coTest2 = null;


        lTerms = db.retrieveTerms();
        if (!lTerms.isEmpty()) {

            if (!lTerms.get(lTerms.size() - 1).getCourses().isEmpty()) {
                coTest2 = lTerms.get(lTerms.size() - 1).getCourses().get(0);

                coTest2 = db.retrieveCourseData(coTest2.getDBID());

                System.out.println(coTest2.getCourseName());

                for(Category catTemp : coTest2.getlCategories()) {
                    System.out.println(catTemp.getWeight());
                }

                for(Assignment asnTemp : coTest2.getlAssignments()) {
                    System.out.println(asnTemp.getAssignmentName());
                }

                for(Student stuTemp : coTest2.getlStudents()) {
                    System.out.println(stuTemp.getLastName());
                }

                for(Grade graTemp : coTest2.getlGrades()) {
                    System.out.println(graTemp.getGrade());
                }
            }
        }

        ArrayList<Grade> gHolder = aAssignment.getGrades();

        System.out.println(gHolder.get(0).getGrade());
    }

}
