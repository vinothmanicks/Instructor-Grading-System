package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Collections;

public class Course {

    private String sCourseName; // Course name
    private String sCourseID; // Course ID
    private String sDepartment; // Optional

    private ArrayList<Student> lStudents; // List of student objects
    private ArrayList<Assignment> lAssignments; // List of assignment objects
    private ArrayList<Category> lCategories; // List of category objects
    private ArrayList<Grade> lGrades; // List of grade objects

    private boolean bArchived; // Archived flag
    private Statistics stCourseStats; // Course statistics struct

    public void Course(String sCourseName, String sCourseID, String sDepartment) { // Class constructor
        this.setCourseName(sCourseName);
        this.setCourseID(sCourseID);
        this.setDepartment(sDepartment); // Optional
        this.lStudents = new ArrayList<Student>();
        this.lAssignments = new ArrayList<Assignment>();
        this.lCategories = new ArrayList<Category>();
        this.lGrades = new ArrayList<Grade>();

        this.bArchived = false;
        this.stCourseStats = null;
    }

    private void addStudent(Student newStudent) { // Add a student to a course
        new Student(newStudent);
        this.lStudents.add(newStudent);
        //Collections.sort(lStudents, Student.getLastName().CASE_INSENSITIVE_ORDER); //Error
    }

    private void addAssignment(Assignment newAssignment) { // Add an assignment to a course
        new Assignment(newAssignment);
        this.lAssignments.add(newAssignment);
    }

    private void addCategory(Category newCategory) { // Add a grading category to a course
        new Category(newCategory);
        this.lCategories.add(newCategory);
    }

    private void addGrade(Grade newGrade) { // Add a student's grade to a course
        new Grade(newGrade.getGrade(), newGrade.getStudentCopy(), newGrade.getAssignmentCopy());
        this.lGrades.add(newGrade);
    }

    private void removeStudent(Student targetStudent) // Remove a student from a course
    {
        this.lStudents.remove(targetStudent);
    }

    private void removeAssignment(Assignment targetAssignment) // Remove an assignment from a course
    {
        this.lAssignments.remove(targetAssignment);
    }

    private void removeCategory(Category targetCategory) // Remove a grading category from a course
    {
        this.lCategories.add(targetCategory);
    }

    private void removeGrade(Grade targetGrade) // Remove a student's grade from a course
    {
        this.lGrades.remove(targetGrade);
    }

    private void calculateStats(){}

    // private Assignment generateAssignment(String sCourseName, )
    // Honors requirement

    // Get functions
    public String getCourseName()
    {
        String sCourseNameCopy = this.sCourseName;
        return sCourseNameCopy;
    }

    public String getCourseID()
    {
        String sCourseIDCopy = this.sCourseID;
        return sCourseIDCopy;
    }

    public String getDepartment()
    {
        String sDepartmentCopy = this.sDepartment;
        return sDepartmentCopy;
    }

    // Set functions
    public void setCourseName(String CourseName)
    {
        this.sCourseName = CourseName;
    }

    public void setCourseID(String CourseID)
    {
        this.sCourseID = CourseID;
    }

    public void setDepartment(String Department)
    {
        this.sDepartment = Department;
    }
}
