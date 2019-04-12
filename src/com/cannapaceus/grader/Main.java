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


        Term teTest = new Term(2019, eSeason.SPRING);
        //System.out.println(db.storeTerm(teTest));

        Course coTest = new Course("Test Course", "TST 000", "Testing");
        //System.out.println(db.storeCourse(coTest, teTest.getDBID()));

        Category catTests = new Category("Tests", 0.50f);
        coTest.addCategory(catTests);
        //System.out.println(db.storeCategory(catTests, coTest.getDBID()));

        Category catHmwk = new Category("Homework", 0.35f);
        coTest.addCategory(catHmwk);
        //System.out.println(db.storeCategory(catHmwk, coTest.getDBID()));

        Category catOther = new Category("Other", 0.15f);
        coTest.addCategory(catOther);
        //System.out.println(db.storeCategory(catOther, coTest.getDBID()));

        Student stuOne = new Student("Student","One","id001","id001@email.address");
        coTest.addStudent(stuOne);
        //System.out.println(db.storeStudent(stuOne, coTest.getDBID()));

        Student stuTwo = new Student("Student","Two","id002","id002@email.address");
        coTest.addStudent(stuTwo);
        //System.out.println(db.storeStudent(stuTwo, coTest.getDBID()));

        Student stuThree = new Student("Student","Three","id003","id003@email.address");
        coTest.addStudent(stuThree);
        //System.out.println(db.storeStudent(stuThree, coTest.getDBID()));

        LocalDate dueDateTestOne = LocalDate.of(2019, 04, 20);
        LocalDate assignedDateTestOne = LocalDate.of(2019, 04, 03);
        Assignment aTestOne = new Assignment("Test One",dueDateTestOne,assignedDateTestOne,false,100,catTests,catTests.getWeight());
        coTest.addAssignment(aTestOne);
        //System.out.println(db.storeAssignment(aTestOne, coTest.getDBID()));

        LocalDate dueDateHmwkOne = LocalDate.of(2019, 04, 10);
        LocalDate assignedDateHmwkOne = LocalDate.of(2019, 04, 03);
        Assignment aHmwkOne = new Assignment("Homework One",dueDateHmwkOne,assignedDateHmwkOne,false,100,catHmwk,catHmwk.getWeight());
        coTest.addAssignment(aHmwkOne);
        //System.out.println(db.storeAssignment(aHmwkOne, coTest.getDBID()));

        LocalDate dueDatePopQuiz = LocalDate.of(2019, 04, 03);
        LocalDate assignedDatePopQuiz = LocalDate.of(2019, 04, 03);
        Assignment aPopQuiz = new Assignment("Pop Quiz 04/03",dueDatePopQuiz,assignedDatePopQuiz,false,100,catOther,catOther.getWeight());
        coTest.addAssignment(aPopQuiz);
        //System.out.println(db.storeAssignment(aPopQuiz, coTest.getDBID()));

        Grade gTestOneGradeOne = new Grade(90,stuOne,aTestOne);
        //System.out.println(db.storeGrade(gTestOneGradeOne, coTest.getDBID()));

        Grade gTestOneGradeTwo = new Grade(100,stuTwo,aTestOne);
        //System.out.println(db.storeGrade(gTestOneGradeTwo, coTest.getDBID()));

        Grade gTestOneGradeThree = new Grade(99,stuThree,aTestOne);
        //System.out.println(db.storeGrade(gTestOneGradeThree, coTest.getDBID()));

        aTestOne.addGrade(gTestOneGradeOne);
        aTestOne.addGrade(gTestOneGradeTwo);
        aTestOne.addGrade(gTestOneGradeThree);
        stuOne.addGrade(gTestOneGradeOne);
        stuTwo.addGrade(gTestOneGradeTwo);
        stuThree.addGrade(gTestOneGradeThree);
        coTest.addGrade(gTestOneGradeOne);
        coTest.addGrade(gTestOneGradeTwo);
        coTest.addGrade(gTestOneGradeThree);

        //System.out.println(stuOne.GenerateStudentReport());

        stuOne.setAverageGrade(stuOne.getGrades(),0);
        stuTwo.setAverageGrade(stuTwo.getGrades(),0);
        stuThree.setAverageGrade(stuThree.getGrades(),0);
        coTest.PopulateAverages(coTest.getlStudents());
        coTest.calculateStats();

        ArrayList<Term> lTerms;
        Course coTest2;


        lTerms = db.retrieveTerms();
        if (!lTerms.isEmpty()) {

            if (!lTerms.get(lTerms.size() - 1).getCourses().isEmpty()) {
                coTest2 = lTerms.get(lTerms.size() - 1).getCourses().get(0);

                coTest2 = db.retrieveCourseData(coTest2.getDBID());

                System.out.println(coTest2.getCourseName());

                for(Category catTemp : coTest2.getlCategories()) {
                    System.out.println(catTemp.getName() + " " + catTemp.getWeight());
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

        //ArrayList<Grade> gHolder = aTestOne.getGrades();

        //System.out.println(gHolder.get(0).getGrade());
    }

}
