package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Collections;

public class Course implements Comparable<Course> {

    //Long to hold the course's ID from the database
    private long lDBID = 0;

    private String sCourseName = null; // Course name
    private String sCourseID = null; // Course ID
    private String sDepartment = null; // Optional

    private ArrayList<Student> lStudents; // List of student objects
    private ArrayList<Assignment> lAssignments; // List of assignment objects
    private ArrayList<Category> lCategories; // List of category objects
    private ArrayList<Grade> lGrades; // List of grade objects

    private boolean bArchived; // Archived flag
    private Statistics stCourseStats; // Course statistics struct

    public Course(String sCourseName, String sCourseID, String sDepartment) { // Class constructor
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

    public void addStudent(Student newStudent) { // Add a student to a course
        new Student(newStudent);
        this.lStudents.add(newStudent);
        Collections.sort(this.lStudents);
    }

    public void addAssignment(Assignment newAssignment) { // Add an assignment to a course
        new Assignment(newAssignment);
        this.lAssignments.add(newAssignment);
    }

    public void addCategory(Category newCategory) { // Add a grading category to a course
        new Category(newCategory);
        this.lCategories.add(newCategory);
    }

    public void addGrade(Grade newGrade) { // Add a student's grade to a course
        this.lGrades.add(newGrade);
    }

    public void removeStudent(Student targetStudent) // Remove a student from a course
    {
        this.lStudents.remove(targetStudent);
    }

    public void removeAssignment(Assignment targetAssignment) // Remove an assignment from a course
    {
        this.lAssignments.remove(targetAssignment);
    }

    public void removeCategory(Category targetCategory) // Remove a grading category from a course
    {
        this.lCategories.add(targetCategory);
    }

    public void removeGrade(Grade targetGrade) // Remove a student's grade from a course
    {
        this.lGrades.remove(targetGrade);
    }

    public void Archive()
    {
        this.bArchived = true;
    }

    public void calculateStats(){}

    // private Assignment generateAssignment(String sCourseName, )
    // Honors requirement

    // Get functions
    /**
     * Getter for a copy of the course's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

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

    public ArrayList<Student> getlStudents() {
        return lStudents;
    }

    public ArrayList<Assignment> getlAssignments() {
        return lAssignments;
    }

    public ArrayList<Category> getlCategories() {
        return lCategories;
    }

    public ArrayList<Grade> getlGrades() {
        return lGrades;
    }

    // Set functions
    /**
     * Setter for the course's ID from the database
     * @param lDBID ID of the course from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

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

    public int compareTo(Course c) {
        return this.getCourseName().compareTo(c.getCourseName());
    }
}
