package com.cannapaceus.grader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Student implements Comparable<Student>{

    //String to hold the student's last name
    private String sLastName;

    //String to hold the student's first name and middle initial
    private String sFirstMIName;

    //String to hold the student's ID
    private String sStudentID;

    //String to hold the student's email address
    private String sEmail;

    //List to hold all course grades
    private ArrayList<Grade> lGrades;

    /**
     * <h1>Student Constructor</h1>
     * Constructor for the student class that takes individual variable inputs.
     * @param sFirstMIName First name and middle initial of the student
     * @param sLastName Last name of the student
     * @param sStudentID Student's ID
     * @param sStudentEmail Student's Email
     */
    public Student(String sFirstMIName, String sLastName, String sStudentID, String sStudentEmail)
    {
        this.sFirstMIName = sFirstMIName;
        this.sLastName = sLastName;
        this.sStudentID = sStudentID;
        this.sEmail = sStudentEmail;
        this.lGrades = new ArrayList<>();
    }

    /**
     * <h1>Student Constructor</h1>
     * Constructor for the student class that takes a student class.
     * @param stuStudent Student class to copy to a new instance
     */
    public Student(Student stuStudent)
    {
        this.sLastName = stuStudent.getLastName();
        this.sFirstMIName = stuStudent.getFirstMIName();
        this.sStudentID = stuStudent.getStudentID();
        this.sEmail = stuStudent.getStudentEmail();
        this.lGrades = new ArrayList<>();
    }

    //Setter functions
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

    //Getter functions
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
        String sLastNameCopy = new String(this.sLastName);
        return new String(sLastNameCopy);
    }

    public String getStudentEmail()
    {
        String sEmailCopy = new String(this.sEmail);
        return new String(sEmailCopy);
    }
    
    public int compareTo(Student anotherStudent) {
        return this.getLastName().compareToIgnoreCase(anotherStudent.getLastName());
    }
}
