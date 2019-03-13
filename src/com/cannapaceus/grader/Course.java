package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Collections;

public class Course {

    private String sCourseName = null; // Course name
    private String sCourseID = null; // Course ID
    private String sDepartment = null; // Optional

    private ArrayList<Student> lStudents; // List of students in a course
    private ArrayList<Assignment> lAssignments; // List of assignments in a course
    private ArrayList<Category> lCategories; // List of assignment categories
    private ArrayList<Grade> lGrades; // List of all grades for the course
    private ArrayList<Float> lAverageGrades; // List of student average grades for the course

    private boolean bArchived; // Archived flag
    private Statistics stCourseStats; // Course statistics object

    public Course(String sCourseName, String sCourseID, String sDepartment) { // Class constructor
        this.setCourseName(sCourseName);
        this.setCourseID(sCourseID);
        this.setDepartment(sDepartment); // Optional
        this.lStudents = new ArrayList<Student>();
        this.lAssignments = new ArrayList<Assignment>();
        this.lCategories = new ArrayList<Category>();
        this.lGrades = new ArrayList<Grade>();
        this.lAverageGrades = new ArrayList<Float>();

        this.bArchived = false;
        this.stCourseStats = new Statistics();
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
        new Grade(newGrade.getGrade(), newGrade.getStudentCopy(), newGrade.getAssignmentCopy());
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

    public void PopulateAverages(ArrayList<Student> lStudents) // Populates or repopulates list of student average grades
    {
        this.lAverageGrades.clear();
        for (int a = 0; a < lStudents.size(); a++) {
            this.lAverageGrades.add(lStudents.get(a).getAverageGrade());
        }
    }

    public void calculateStats()
    {
        float mean;
        float median;
        float mode;
        float standardDev;

        float tempSum = 0;
        for (int a = 0; a < this.lAverageGrades.size(); a++) {
            tempSum += this.lAverageGrades.get(a);
        }
        mean = tempSum / this.lAverageGrades.size();

        int medianMarker = Math.round(this.lAverageGrades.size()/2);
        median = this.lAverageGrades.get(medianMarker);

        float maxValue = 0;
        int maxCount = 0;

        for (int i = 0; i < this.lAverageGrades.size(); ++i) {
            int count = 0;
            for (int j = 0; j < this.lAverageGrades.size(); ++j) {
                if (this.lAverageGrades.get(j) == this.lAverageGrades.get(i)) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = this.lAverageGrades.get(i);
            }
        }
        mode = maxValue;

        float fCalculationValue = 0;
        for (Float fAverageGrade:this.lAverageGrades)
        {
            float theGrade = fAverageGrade;
            fCalculationValue += Math.multiplyExact((long)(theGrade - median),(long)(theGrade - median));
        }

        fCalculationValue = (float)Math.sqrt((double)(fCalculationValue/this.lAverageGrades.size()));
        standardDev = fCalculationValue;

        this.stCourseStats.setMean(mean);
        //System.out.printf("%.1f\n", this.stCourseStats.getMean());
        this.stCourseStats.setMedian(median);
        //System.out.printf("%.1f\n", this.stCourseStats.getMedian());
        this.stCourseStats.setMode(mode);
        //System.out.printf("%.1f\n", this.stCourseStats.getMode());
        this.stCourseStats.setStandardDev(standardDev);
        //System.out.printf("%.3f\n", this.stCourseStats.getStandardDev());
    }

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
