package com.cannapaceus.grader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;
    private Student student;
    private Assignment assignment;
    private Category category;
    private Grade grade;

    @BeforeEach
    void setUp() {
        course = new Course("Test Course", "TC 000", "Test Department");
    }

    @Test
    void addAndGetStudent() {
        student = new Student("Test", "Student 0", "1234567890", "ts0000@test.edu");
        course.addStudent(student);
        student = new Student("Test", "Student 1", "1234567891", "ts0001@test.edu");
        course.addStudent(student);
        student = new Student("Test", "Student 2", "1234567892", "ts0002@test.edu");
        course.addStudent(student);

        ArrayList<Student> lStudents = course.getlStudents();

        assertEquals("Test", lStudents.get(0).getFirstMIName());
        assertEquals("Test", lStudents.get(1).getFirstMIName());
        assertEquals("Test", lStudents.get(2).getFirstMIName());

        assertEquals("Student 0", lStudents.get(0).getLastName());
        assertEquals("Student 1", lStudents.get(1).getLastName());
        assertEquals("Student 2", lStudents.get(2).getLastName());
        
        assertEquals("1234567890", lStudents.get(0).getStudentID());
        assertEquals("1234567891", lStudents.get(1).getStudentID());
        assertEquals("1234567892", lStudents.get(2).getStudentID());

        assertEquals("ts0000@test.edu", lStudents.get(0).getStudentEmail());
        assertEquals("ts0001@test.edu", lStudents.get(1).getStudentEmail());
        assertEquals("ts0002@test.edu", lStudents.get(2).getStudentEmail());
    }

    @Test
    void addAndGetAssignment() {
        assignment = new Assignment("Test Assignment 0", LocalDate.of(2019, 03, 25), LocalDate.of(2019, 03, 5), false, 100, new Category("Test Category 0", 1), 5);
        course.addAssignment(assignment);
        assignment = new Assignment("Test Assignment 1", LocalDate.of(2020, 03, 25), LocalDate.of(2020, 03, 5), false, 101, new Category("Test Category 1", 1), 5);
        course.addAssignment(assignment);

        ArrayList<Assignment> lAssignment = course.getlAssignments();

        assertEquals("Test Assignment 0", lAssignment.get(0).getAssignmentName());
        assertEquals("Test Assignment 1", lAssignment.get(1).getAssignmentName());

        assertEquals(LocalDate.of(2019, 03, 25), lAssignment.get(0).getDueDate());
        assertEquals(LocalDate.of(2020, 03, 25), lAssignment.get(1).getDueDate());

        assertEquals(LocalDate.of(2019, 03, 5), lAssignment.get(0).getAssignedDate());
        assertEquals(LocalDate.of(2020, 03, 5), lAssignment.get(1).getAssignedDate());

        assertEquals(100, lAssignment.get(0).getMaxScore());
        assertEquals(101, lAssignment.get(1).getMaxScore());

        assertEquals("Test Category 0", lAssignment.get(0).getCategoryReference().getName());
        assertEquals("Test Category 1", lAssignment.get(1).getCategoryReference().getName());
    }

    @Test
    void addAndGetCategory() {
        category = new Category("Test Category 0", 10);
        course.addCategory(category);
        category = new Category("Test Category 1", 20);
        course.addCategory(category);

        ArrayList<Category> lCategory = course.getlCategories();

        assertEquals("Test Category 0", lCategory.get(0).getName());
        assertEquals("Test Category 1", lCategory.get(1).getName());

        assertEquals(10, lCategory.get(0).getWeight());
        assertEquals(20, lCategory.get(1).getWeight());
    }

    @Test
    void addAndGetGrade() {
        student = new Student("Test", "Student 0", "1234567890", "ts0000@test.edu");
        course.addStudent(student);
        assignment = new Assignment("Test Assignment 1", LocalDate.of(2020, 03, 25), LocalDate.of(2020, 03, 5), false, 101, new Category("Test Category 1", 1), 5);
        course.addAssignment(assignment);

        grade = new Grade(10.5f, student, assignment);
        course.addGrade(grade);

        ArrayList<Grade> lGrades = course.getlGrades();
        assertEquals(10.5f, lGrades.get(0).getGrade());
        assertSame(assignment, lGrades.get(0).getAssignmentReference());
        assertSame(student, lGrades.get(0).getStudentReference());

    }

    @Test
    void removeStudentTest() {
        addAndGetStudent();
        course.removeStudent(student);

        ArrayList<Student> lStudents = course.getlStudents();
        assertEquals(2, lStudents.size());
    }

    @Test
    void removeAssignmentTest() {
        addAndGetAssignment();
        course.removeAssignment(assignment);

        ArrayList<Assignment> lAssignments = course.getlAssignments();
        assertEquals(1, lAssignments.size());
    }

    @Test
    void removeCategoryTest() {
        addAndGetCategory();
        course.removeCategory(category);

        ArrayList<Category> lCategories = course.getlCategories();
        assertEquals(1, lCategories.size());
    }

    @Test
    void removeGradeTest() {
        addAndGetGrade();
        course.removeGrade(grade);

        ArrayList<Grade> lGrades = course.getlGrades();
        assertEquals(true, lGrades.isEmpty());
    }

    @Test
    void archiveTest() {
    }

    @Test
    void populateAveragesTest() {
    }

    @Test
    void calculateStatsTest() {
    }

    @Test
    void getDBIDTest() {
    }

    @Test
    void getCourseNameTest() {
    }

    @Test
    void getCourseIDTest() {
    }

    @Test
    void getDepartmentTest() {
    }

    @Test
    void setDBIDTest() {
    }

    @Test
    void setCourseNameTest() {
    }

    @Test
    void setCourseIDTest() {
    }

    @Test
    void setDepartmentTest() {
    }

    @Test
    void compareTo() {
    }
}