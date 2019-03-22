package com.cannapaceus.grader;

public class Grade implements Comparable<Grade> {

    //Long to hold the ID of the grade from the database
    private long lDBID = 0;

    private Student stuStudent;
    private Assignment aAssignment;
    private float fGrade;
    private boolean bOverdue;
    private boolean bMissing;
    private boolean bDropped;
    private boolean bSubmitted;

    /**
     * <h1>Grade Constructor</h1>
     * Constructor for the assignment class.
     * @param fGrade Grade for the assignment if grade is already known when creating
     * @param stuStudent Student this grade is for. Is a reference and should be assigned at instantiation.
     * @param aAssignment Assignemnt this grade is for. Is a reference and should be assigned at instantiation.
     */
    public Grade(float  fGrade, Student stuStudent, Assignment aAssignment) {
        this.fGrade = fGrade;

        //Should not be able to change these variables
        this.stuStudent = stuStudent;
        this.aAssignment = aAssignment;

        bOverdue = false;
        bMissing = false;
        bDropped = false;
        bSubmitted = false;
    }

    //Setter functions
    /**
     * Setter for the grade's ID from the database
     * @param lDBID ID of the grade from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    /**
     * Setter for the Grade class's grade
     * @param fGrade Float of the desired grade for this instance
     */
    public void setGrade(float fGrade)
    {
        this.fGrade = fGrade;
    }

    /**
     * Setter for the Grade class's overdue flag
     * @param bOverdue
     */
    public void setOverdue(boolean bOverdue)
    {
        this.bOverdue = bOverdue;
    }

    /**
     * Setter for the Grade class's missing flag
     * @param bMissing
     */
    public void setMissing(boolean bMissing)
    {
        this.bMissing = bMissing;
    }

    /**
     * Setter for the Grade class's dropped flag
     * @param bDropped
     */
    public void setDropped(boolean bDropped)
    {
        this.bDropped = bDropped;
    }

    /**
     * Setter for the Grade class's submitted flag
     * @param bSubmitted
     */
    public void setSubmitted(boolean bSubmitted)
    {
        this.bSubmitted = bSubmitted;
    }

    //Getter functions
    /**
     * Getter for a copy of the grade's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

    /**
     * Getter for the Grade class's grade float
     * @return Copy of the grade float
     */
    public float getGrade()
    {
        return fGrade;
    }

    /**
     * Setter for the Grade class's overdue flag
     * @return Copy of the overdue flag status
     */
    public boolean getOverdue()
    {
        return bOverdue;
    }

    /**
     * Setter for the Grade class's missing flag
     * @return Copy of the missing flag's status
     */
    public boolean getMissing()
    {
        return bMissing;
    }

    /**
     * Setter for the Grade class's dropped flag
     * @return Copy of the dropped flag's status
     */
    public boolean getDropped()
    {
        return bDropped;
    }

    /**
     * Setter for the Grade class's submitted flag
     * @return submitted flag's status
     */
    public boolean getSubmitted()
    {
        return bSubmitted;
    }

    /**
     * Getter for a copy of the student this grade is for
     * @return Copy of Grade's student variable
     */
    public Student getStudentCopy()
    {
        Student copyStudent = new Student(stuStudent);
        return copyStudent;
    }

    /**
     * Getter for a copy of the assignment this grade is for
     * @return Copy of the Grade's assignment variable
     */
    public Assignment getAssignmentCopy()
    {
        Assignment aAssignmentCopy = new Assignment(aAssignment);
        return aAssignmentCopy;
    }

    public int compareTo(Grade g) {
        int stuComp = this.getStudentCopy().compareTo(g.getStudentCopy());
        if (stuComp != 0) {
            return stuComp;
        }

        return this.getAssignmentCopy().compareTo(g.getAssignmentCopy());
    }


    //Potential functions if we need to get references

    /**
     * Getter for a reference to the assignment this grade is for
     * @return Reference of the grade's assignment variable
     */
    /*
    public Assignment getAssignmentReference()
    {
        return aAssignment;
    }
    */

    /**
     * Getter for a reference to the the student this grade is for
     * @return Reference of the grade's student variable
     */
    /*
    public Student getStudentReference()
    {
        return stuStudent;
    }
    */
}
