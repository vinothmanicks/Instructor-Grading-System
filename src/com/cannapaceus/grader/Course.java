package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Course {

    //Long to hold the course's ID from the database
    private long lDBID = 0;

    private int iDropUncategorized = 0;

    private String sCourseName = null; // Course name
    private String sCourseID = null; // Course ID
    private String sDepartment = null; // Optional

    private ArrayList<Student> lStudents; // List of students in a course
    private ArrayList<Assignment> lAssignments; // List of assignments in a course
    private ArrayList<Category> lCategories; // List of assignment categories
    private ArrayList<Grade> lGrades; // List of all grades for the course
    private ArrayList<Float> lScaledGrades; //List of scaled grades, each corresponding to a student's final grade in a course
    private float courseGradeScale; //Float to scale all grades by.

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
        this.lScaledGrades = new ArrayList<Float>();

        this.iDropUncategorized = 0;

        this.bArchived = false;
        this.stCourseStats = new Statistics();
    }

    public void addStudent(Student newStudent) { // Add a student to a course
        new Student(newStudent);
        this.lStudents.add(newStudent);
        Collections.sort(this.lStudents, Student.nameComparator);
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
        this.lCategories.remove(targetCategory);
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
        if (this.lAverageGrades.size() == 0) {
            this.stCourseStats.setMean(0.0f);
            this.stCourseStats.setMedian(0.0f);
            this.stCourseStats.setMode(0.0f);
            this.stCourseStats.setStandardDev(0.0f);
            return;
        }

        float mean;
        float median;
        float mode;
        float standardDev;

        if (this.lAverageGrades.size() == 0) {
            this.stCourseStats.setMean(0.0f);
            this.stCourseStats.setMedian(0.0f);
            this.stCourseStats.setMode(0.0f);
            this.stCourseStats.setStandardDev(0.0f);

            return;
        }

        float tempSum = 0;
        for (int a = 0; a < this.lAverageGrades.size(); a++) {
            tempSum += this.lAverageGrades.get(a);
        }
        mean = tempSum / this.lAverageGrades.size();

        int medianMarker = Math.round(this.lAverageGrades.size()/2.0f) - 1;
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

    public void scaleFinalAverages(float fScaleBy)
    {
        lScaledGrades.clear();

        for (float f:lAverageGrades)
        {
            lScaledGrades.add(f+fScaleBy);
        }
    }



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

    public float getScale()
    {
        return this.courseGradeScale;
    }

    public int getDropUncategorized() {
        return this.iDropUncategorized;
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

    public Statistics getStCourseStats() {
        return stCourseStats;
    }

    public boolean getBArchived() {
        return bArchived;
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

    public ArrayList<Float> getlScaledGrades(){return lScaledGrades;}

    public Statistics getStatistics() {
        Statistics statsCopy = this.stCourseStats;
        return statsCopy;
    }

    // Set functions
    /**
     * Setter for the course's ID from the database
     * @param lDBID ID of the course from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    public void setScale(float scale)
    {
        this.courseGradeScale = scale;
    }

    public void setDropUncategorized(int numDrop)
    {
        this.iDropUncategorized = numDrop;
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

    public void setStCourseStats(Statistics stCourseStats) {
        this.stCourseStats = stCourseStats;
    }

    public void setBArchived(boolean bArchived) {
        this.bArchived = bArchived;
    }

    public int compareTo(Course c) {
        return this.getCourseName().compareTo(c.getCourseName());
    }

    public static Comparator<Course> nameComparator = new Comparator<Course>() {
        @Override
        public int compare(Course c1, Course c2) {
            return c1.getCourseName().toUpperCase().compareTo(c2.getCourseName().toUpperCase());
        }
    };

    public ArrayList<Object> dropGrades() {
        ArrayList<Object> retVal = new ArrayList<>();
        ArrayList<Assignment> noCatAssigns = new ArrayList<>();

        int numToDrop = 0;

        for (Category cat : this.lCategories) {
            numToDrop = cat.getDropped();

            for (Student s : this.lStudents) {
                ArrayList<Grade> gradesInCat = new ArrayList<>();
                for (Grade g : s.getGrades()) {
                    if (!g.getSubmitted()) {
                        continue;
                    }
                    if (g.getAssignmentReference().getCategoryReference() == cat) {
                        gradesInCat.add(g);
                        retVal.add(g);
                        g.setDropped(false);
                    }
                }

                if (numToDrop <= 0) {
                    continue;
                }

                if (numToDrop >= gradesInCat.size()) {
                    for (Grade g : gradesInCat) {
                        g.setDropped(true);
                    }
                } else {
                    Collections.sort(gradesInCat, Grade.scoreComparator);
                    for (int i = 0; i < numToDrop; ++i) {
                        gradesInCat.get(i).setDropped(true);

                        if (!retVal.contains(gradesInCat.get(i).getStudentReference()))
                            retVal.add(gradesInCat.get(i).getStudentReference());

                        if (!retVal.contains(gradesInCat.get(i).getAssignmentReference()))
                            retVal.add(gradesInCat.get(i).getAssignmentReference());
                    }

                    s.setAverageGrade(s.getGrades(), this.getScale());

                    for (Assignment a : this.getlAssignments()) {
                        a.calculateStats();
                    }
                }
            }
        }

        for (Assignment a : this.getlAssignments()) {
            if (a.getCategoryReference() == null) {
                noCatAssigns.add(a);
            }
        }


        numToDrop = this.getDropUncategorized();

        for (Student s : this.lStudents) {
            ArrayList<Grade> gradesInCat = new ArrayList<>();
            for (Grade g : s.getGrades()) {
                if (!g.getSubmitted()) {
                    continue;
                }
                if (g.getAssignmentReference().getCategoryReference() == null) {
                    gradesInCat.add(g);
                    retVal.add(g);
                    g.setDropped(false);
                }
            }

            if (numToDrop <= 0) {
                continue;
            }

            if (numToDrop >= gradesInCat.size()) {
                for (Grade g : gradesInCat) {
                    g.setDropped(true);
                }
            } else {
                Collections.sort(gradesInCat, Grade.scoreComparator);
                for (int i = 0; i < numToDrop; ++i) {
                    gradesInCat.get(i).setDropped(true);

                    if (!retVal.contains(gradesInCat.get(i).getStudentReference()))
                        retVal.add(gradesInCat.get(i).getStudentReference());

                    if (!retVal.contains(gradesInCat.get(i).getAssignmentReference()))
                        retVal.add(gradesInCat.get(i).getAssignmentReference());
                }

                s.setAverageGrade(s.getGrades(), this.getScale());

                for (Assignment a : this.getlAssignments()) {
                    a.calculateStats();
                }
            }
        }


        return retVal;
    }
}
