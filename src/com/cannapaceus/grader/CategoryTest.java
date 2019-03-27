package com.cannapaceus.grader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        category = new Category("Test Category", 1);
    }

    @Test
    void getAndSetName() {
        assertEquals("Test Category", category.getName());
        category.setCategoryName("New Test Category");
        assertEquals("New Test Category", category.getName());
    }

    @Test
    void getAndSetWeight() {
        assertEquals(1, category.getWeight());
        category.setWeight(20);
        assertEquals(20, category.getWeight());
        assertNotEquals(1, category.getWeight());
    }

    @Test
    void addAndGetAssignments() {
        assignment = new Assignment("Test Assignment", LocalDate.of(2019, 03, 25), LocalDate.of(2019, 03, 5), false, 100, category, 5);
        category.addAssignment(assignment);
        assignment = new Assignment("New Test Assignment", LocalDate.of(2019, 03, 25), LocalDate.of(2209, 03, 25), true, 250, category, 9);
        category.addAssignment(assignment);

        ArrayList<Assignment> lAssignments = category.getAssignments();

        assertEquals("Test Assignment", lAssignments.get(0).getAssignmentName());
        assertEquals("New Test Assignment", lAssignments.get(1).getAssignmentName());

        assertEquals(false, lAssignments.get(0).getDroppedAssignment());
        assertEquals(true, lAssignments.get(1).getDroppedAssignment());

        assertEquals(100, lAssignments.get(0).getMaxScore());
        assertEquals(250, lAssignments.get(1).getMaxScore());

        assertEquals(5, lAssignments.get(0).getWeight());
        assertEquals(9, lAssignments.get(1).getWeight());

    }

    @Test
    void removeAssigment() {
        addAndGetAssignments();

        category.removeAssigment(category.getAssignments().get(0));

        ArrayList<Assignment> lAssignments = category.getAssignments();

        assertNotEquals("Test Assignment", lAssignments.get(0).getAssignmentName());
        assertEquals("New Test Assignment", lAssignments.get(0).getAssignmentName());

        assertNotEquals(false, lAssignments.get(0).getDroppedAssignment());
        assertEquals(true, lAssignments.get(0).getDroppedAssignment());

        assertNotEquals(100, lAssignments.get(0).getMaxScore());
        assertEquals(250, lAssignments.get(0).getMaxScore());

        assertNotEquals(5, lAssignments.get(0).getWeight());
        assertEquals(9, lAssignments.get(0).getWeight());
    }

    @Test
    void getAndSetCategoryName() {
        assertEquals("Test Category", category.getName());
        category.setCategoryName("New Test Category");
        assertEquals("New Test Category", category.getName());
    }

    @Test
    void compareTo() {
        assertEquals(0, category.compareTo(new Category("Test Category", 1)));
        assertNotEquals(0, category.compareTo(new Category("New Test Category", 25)));
    }
}