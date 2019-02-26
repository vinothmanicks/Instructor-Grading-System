package com.cannapaceus.grader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private String sLastName;
    private String sFirstMIName;
    private String sStudentID;
    private String sEmail;
    //TODO: Resolve grade class missing & making it a list of references.
    private ArrayList<Grade> lGrades;

    public Student(String sFirstMIName, String sLastName, String sStudentID, String sStudentEmail)
    {
        this.sFirstMIName = sFirstMIName;
        this.sLastName = sLastName;
        this.sStudentID = sStudentID;
        this.sEmail = sStudentEmail;
    }

    //TODO: Resolve passing grade by reference
    public void addGrade(Grade grade)
    {
        lGrades.add(grade);
    }
    public void setFirstMIName(String sFirstMIName)
    {
        this.sFirstMIName = sFirstMIName;
    }
    public void setLastName(String sLastName)
    {
        this.sLastName = sLastName;
    }
    public void setStudentID(String sStudentID)
    {
        this.sStudentID = sStudentID;
    }
    public void setStudentEmail(String sStudentEmail)
    {
        this.sEmail = sStudentEmail;
    }
    public ArrayList<Grade> getGrades()
    {
        ArrayList<Grade> lGradeCopies = new ArrayList<>(lGrades);
        return lGradeCopies;
    }
    public String getFirstMIName()
    {
        String sFirstMINameCopy = new String(this.sFirstMIName);
        return new String(sFirstMINameCopy);
    }
    public String getStudentID()
    {
        String sStudentIDCopy = new String(this.sStudentID);
        return new String(sStudentIDCopy);
    }

    public String getLastName()
    {
        String sEmailCopy = new String(this.sEmail);
        return new String(sEmailCopy);
    }
}
