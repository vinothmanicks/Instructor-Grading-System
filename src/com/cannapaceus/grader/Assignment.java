package com.cannapaceus.grader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Assignment {

    //Long to hold the ID of the assignment from the database
    private long lDBID = 0;

    //String to hold the name of the assignment
    private String sAssignmentName;

    //Date objects to hold the due date and assigned date of the assignment for reference.
    private LocalDate dtDueDate;
    private LocalDate dtAssignedDate;

    //boolean to denote if an instructor wants to drop the assignment for the whole class. This is different from the grade bDropped variable
    private boolean bDropped;
    //Statistic object to hold information regarding the course averages and other related stats.
    private Statistics stAssignmentStats;

    //List of grades, each corresponding to a student in the course
    private ArrayList<Grade> lGrades;
    //float to hold the %100 mark for the assignment. Can be changed
    private float fMaxScore;

    //Category object that this assignment is assigned to. This hold the weight and name that this assignment will be graded under.
    private Category catCategory;
    //float to hold the manual override weight for this assignment
    private float fWeight;

    /**
     * <h1>Assignment Constructor</h1>
     * Constructor for the assignment class that takes individual variable inputs
     * @param sAssignmentName Name of the assignment
     * @param dtDueDate Date the assignment is due
     * @param dtAssignedDate Date the assignment was assigned
     * @param bDropped Bool if the assignment was dropped
     * @param fMaxScore Float of the %100 score
     * @param catCategory Category the assignment is created under
     * @param fWeight Float if manually overriding the category weight
     */
    public Assignment(String sAssignmentName, LocalDate dtDueDate, LocalDate dtAssignedDate, boolean bDropped, float fMaxScore,Category catCategory, float fWeight)
    {
        this.lGrades = new ArrayList<Grade>();
        this.sAssignmentName = sAssignmentName;
        this.dtDueDate = dtDueDate;
        this.dtAssignedDate = dtAssignedDate;
        this.bDropped = bDropped;
        this.fMaxScore = fMaxScore;
        this.catCategory = catCategory;
        this.fWeight = fWeight;

        this.stAssignmentStats = new Statistics();
    }

    /**
     * <h1>Assignment Constructor</h1>
     * Constructor for the assignment class that takes an assignment object
     * @param aAssignment Assignment object to copy to a new instance
     */
    public Assignment(Assignment aAssignment)
    {
        this.setDBID(aAssignment.getDBID());
        this.lGrades = new ArrayList<Grade>();
        this.setAssignmentName(aAssignment.getAssignmentName());
        aAssignment.getGrades().forEach(grade-> this.lGrades.add(grade));
        this.setAssignedDate(aAssignment.getAssignedDate());
        this.setCategory((aAssignment.getCategoryCopy()));
        this.setDroppedAssignment((aAssignment.getDroppedAssignment()));
        this.setMaxScore(aAssignment.getMaxScore());
        this.setWeight(aAssignment.getWeight());

        this.stAssignmentStats = new Statistics();
        this.stAssignmentStats.calculateMean(this.lGrades);
        this.stAssignmentStats.calculateMedian(this.lGrades);
        this.stAssignmentStats.calculateMode(this.lGrades);
        this.stAssignmentStats.calculateStandardDev(this.lGrades);
    }

    //Setter functions
    /**
     * Function to add a grade to this assignment's list of grades
     * @param grade Grade that is associated with an assignment and student
     */
    public void addGrade(Grade grade)
    {
        this.lGrades.add(grade);
        this.stAssignmentStats.calculateMean(this.lGrades);
        this.stAssignmentStats.calculateMedian(this.lGrades);
        this.stAssignmentStats.calculateMode(this.lGrades);
        this.stAssignmentStats.calculateStandardDev(this.lGrades);
    }

    /**
     * Function to clear the grades from an assignment
     */
    public void clearGrades()
    {
        lGrades.clear();
    }

    /**
     * Setter for the assignment's ID from the database
     * @param lDBID ID of the assignment from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    /**
     * Setter for the assignment's name
     * @param sAssignmentName
     */
    public void setAssignmentName(String sAssignmentName)
    {
        this.sAssignmentName = sAssignmentName;
    }

    /**
     * Setter for the assignment's due date
     * @param dtDueDate
     */
    public void setDueDate(LocalDate dtDueDate)
    {
        this.dtDueDate = dtDueDate;
    }

    /**
     * Setter for the assignment's due date
     * @param dtAssignedDate
     */
    public void setAssignedDate(LocalDate dtAssignedDate)
    {
        this.dtAssignedDate = dtAssignedDate;
    }

    /**
     * Setter for the assignment's max score
     * @param fMaxScore
     */
    public void setMaxScore(float fMaxScore)
    {
        this.fMaxScore = fMaxScore;
    }

    /**
     * Setter for the assignment's category
     * @param catCat
     */
    public void setCategory(Category catCat)
    {
        this.catCategory = catCat;
    }

    /**
     * Setter for the assignment's manual weight
     * @param fWeight
     */
    public void setWeight(float fWeight)
    {
        this.fWeight = fWeight;
    }

    public void setStAssignmentStats(Statistics stAssignmentStats) {
        this.stAssignmentStats = stAssignmentStats;
    }

    /**
     * Setter for the assignment's dropped boolean
     * @param bDropped
     */
    public void setDroppedAssignment(boolean bDropped)
    {
        for (Grade gGrade : lGrades) {
            gGrade.setDropped(bDropped);
        }
        this.bDropped = bDropped;
    }

    //Getter functions
    /**
     * Getter for a copy of the assignment's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

    /**
     * Getter for a copy of the assignment's name
     * @return
     */
    public String getAssignmentName()
    {
        String sAssignmentNameCopy = new String(this.sAssignmentName);
        return sAssignmentNameCopy;
    }

    /**
     * Getter for a copy of assignment's grade list
     * @return
     */
    public ArrayList<Grade> getGrades()
    {
        return lGrades;
    }

    /**
     * Getter for the assignment due date
     * @return
     */
    public LocalDate getDueDate()
    {
        return this.dtDueDate;
    }

    /**
     * Getter for a copy of the assignment's assigned date
     * @return
     */
    public LocalDate getAssignedDate()
    {
        return this.dtAssignedDate;
    }

    /**
     * Getter for a copy the assignment's max score
     * @return
     */
    public float getMaxScore()
    {
        return this.fMaxScore;
    }

    /**
     * Getter for a copy of the assignment's category
     * @return
     */
    public Category getCategoryCopy()
    {
        if (catCategory == null)
            return null;

        Category catCategoryCopy = new Category(this.catCategory);
        return catCategoryCopy;
    }

    /**
     * Getter for a copy of the manual assignment weight
     * @return
     */
    public float getWeight()
    {
        return this.fWeight;
    }

    public Statistics getStAssignmentStats() {
        return this.stAssignmentStats;
    }

    /**
     * Getter for a copy of the dropped boolean flag status
     * @return
     */
    public boolean getDroppedAssignment()
    {
        return this.bDropped;
    }

    public int compareTo(Assignment a) {
        return this.getAssignmentName().compareTo(a.getAssignmentName());
    }

    public static Comparator<Assignment> nameComparator = new Comparator<Assignment>() {
        @Override
        public int compare(Assignment a1, Assignment a2) {
            return a1.getAssignmentName().toUpperCase().compareTo(a2.getAssignmentName().toUpperCase());
        }
    };
}
