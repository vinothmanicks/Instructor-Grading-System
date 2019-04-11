package com.cannapaceus.grader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    private Assignment assignment;
    private Student student;
    private Grade grade;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Test Category", 1);
        assignment = new Assignment("Test Assignment", LocalDate.of(2019, 03, 25), LocalDate.of(2019, 03, 5), false, 100, category, 5);
        student = new Student("Test", "Student", "25825845", "ts0012@test.edu");
        grade = new Grade(20, student, assignment);
    }

    @Test
    void addAndGetGrade() {
        assignment.addGrade(grade);
        ArrayList<Grade> lgrades = assignment.getGrades();
        assertEquals(lgrades.get(0), grade);
    }

    @Test
    void setAndGetAssignmentName() {
        assertEquals("Test Assignment", assignment.getAssignmentName());
        assignment.setAssignmentName("New Test Assignment");
        assertEquals("New Test Assignment", assignment.getAssignmentName());
    }

    @Test
    void setAndGetDueDate() {
        assertEquals(LocalDate.of(2019, 03, 25), assignment.getDueDate());
        assignment.setDueDate(LocalDate.of(2395, 12, 25));
        assertEquals(LocalDate.of(2395, 12, 25), assignment.getDueDate());
    }

    @Test
    void setAndGetAssignedDate() {
        assertEquals(LocalDate.of(2019, 03, 5), assignment.getAssignedDate());
        assignment.setAssignedDate(LocalDate.of(2395, 12, 25));
        assertEquals(LocalDate.of(2395, 12, 25), assignment.getAssignedDate());
    }

    @Test
    void setAndGetMaxScore() {
        assertEquals(100, assignment.getMaxScore());
        assignment.setMaxScore(250);
        assertEquals(250, assignment.getMaxScore());
        assertNotEquals(100, assignment.getMaxScore());
    }

    @Test
    void setAndGetCategory() {
        assertEquals(0, assignment.getCategoryReference().compareTo(category));
        assignment.setCategory(new Category("New Test Category", 2));
        assertEquals(0, assignment.getCategoryReference().compareTo(new Category("New Test Category", 2)));
        assertNotEquals(0, assignment.getCategoryReference().compareTo(category));
    }

    @Test
    void setAndGetWeight() {
        assertEquals(5, assignment.getWeight());
        assignment.setWeight(10);
        assertEquals(10, assignment.getWeight());
        assertNotEquals(5, assignment.getWeight());
    }

    @Test
    void setAndGetDroppedAssignment() {
        assertEquals(false, assignment.getDroppedAssignment());
        assignment.setDroppedAssignment(true);
        assertEquals(true, assignment.getDroppedAssignment());
        assertNotEquals(false, assignment.getDroppedAssignment());
    }

    @Test
    void compareTo() {
        assertEquals(0, assignment.compareTo(new Assignment("Test Assignment", LocalDate.of(2019, 03, 25), LocalDate.of(2019, 03, 5), false, 100, category, 5)));

        assertNotEquals(0, assignment.compareTo(new Assignment("New Test Assignment", LocalDate.of(2395, 12, 25), LocalDate.of(2395, 12, 25), false, 100, category, 5)));
    }
}