package com.cannapaceus.grader;

public class Grade {

    private Student stuStudent;
    private Assignment aAssignment;
    private float fGrade;
    private boolean bOverdue;
    private boolean bMissing;
    private boolean bDropped;
    private boolean bSubmitted;

    //TODO:Fix student & assignment references
    public Grade(float  fGrade, Student stuStudent, Assignment aAssignment) {
        this.fGrade = fGrade;
        this.stuStudent = stuStudent;
        this.aAssignment = aAssignment;
    }

    public void setGrade(float fGrade)
    {
        this.fGrade = fGrade;
    }
    public void setOverdue(boolean bOverdue)
    {
        this.bOverdue = bOverdue;
    }
    public void setMissing(boolean bMissing)
    {
        this.bMissing = bMissing;
    }
    public void setDropped(boolean bDropped)
    {
        this.bDropped = bDropped;
    }
    public void setSubmitted(boolean bSubmitted)
    {
        this.bSubmitted = bSubmitted;
    }
    public float getGrade()
    {
        return fGrade;
    }
    public boolean getOverdue()
    {
        return bOverdue;
    }
    public boolean getMissing()
    {
        return bMissing;
    }
    public boolean getDropped()
    {
        return bDropped;
    }
    public boolean getSubmitted()
    {
        return bSubmitted;
    }
}
