package com.cannapaceus.grader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Student{

    //Long to hold the ID of the student from the database
    private long lDBID = 0;

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

    //Grade class containing student's average grade for the course
    private float fAverageGrade;

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

        setAverageGrade(this.lGrades);
    }

    /**
     * <h1>Student Constructor</h1>
     * Constructor for the student class that takes a student class.
     * @param stuStudent Student class to copy to a new instance
     */
    public Student(Student stuStudent)
    {
        this.lDBID = stuStudent.getDBID();
        this.sLastName = stuStudent.getLastName();
        this.sFirstMIName = stuStudent.getFirstMIName();
        this.sStudentID = stuStudent.getStudentID();
        this.sEmail = stuStudent.getStudentEmail();
        this.lGrades = new ArrayList<>();

        setAverageGrade(this.lGrades);
    }

    //Setter functions
    public void addGrade(Grade grade)
    {
        lGrades.add(grade);
    }

    /**
     * Setter for the student's ID from the database
     * @param lDBID ID of the student from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
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

    public void setAverageGrade(float fAverageGrade) {
        this.fAverageGrade = fAverageGrade;
    }

    public void setAverageGrade(ArrayList<Grade> lGrades) {
        float temp = 0;
        float tempWeight = 0;
        float scoreSum = 0;
        float fullScoresSum = 0;
        float tempFullScore = 0;

        for (Grade g : lGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            Category cat = g.getAssignmentReference().getCategoryCopy();

            if (cat == null) {
                tempWeight = 1.0f;
            } else {
                tempWeight = cat.getWeight();
            }

            tempFullScore = g.getAssignmentReference().getMaxScore();
            temp = g.getGrade() * tempWeight;
            scoreSum += temp;
            fullScoresSum += tempFullScore * tempWeight;
        }

        if (fullScoresSum == 0) {
            this.fAverageGrade = 0;
        } else {
            this.fAverageGrade = (scoreSum / fullScoresSum) * 100;
        }
    }

    //Getter functions
    public ArrayList<Grade> getGrades()
    {
        return lGrades;
    }

    /**
     * Getter for a copy of the student's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
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

    public float getAverageGrade()
    {
        float fAverageCopy = this.fAverageGrade;
        return fAverageCopy;
    }

    public int compareTo(Student anotherStudent) {
        return this.getLastName().compareToIgnoreCase(anotherStudent.getLastName());
    }

    public static Comparator<Student> nameComparator = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            int cmp = s1.getLastName().toUpperCase().compareTo(s2.getLastName().toUpperCase());
            if (cmp == 0)
                return s1.getFirstMIName().toUpperCase().compareTo(s2.getFirstMIName().toUpperCase());
            else
                return cmp;
        }
    };

    /**
     * Function to generate a report for a student based on their course grades provided assignments have been assigned.
     * @return Report in the form of a string to print out.
     */
    public String GenerateStudentReport()
    {
        String sReport = new String( "Student name: " + this.sFirstMIName + " " + this.sLastName + "\n\n"+"Assignments:\n");

        for (Grade gGrade:lGrades)
        {
            String sNextAssignment = new String(gGrade.getAssignmentCopy().getAssignmentName() + ":\t" + gGrade.getGrade() + "\n");
            sReport = sReport + sNextAssignment;

            if(gGrade.getMissing() != false)
            {
                String sMissingData = new String("-Missing\n");
                sReport = sReport + sNextAssignment;
            }
            if(gGrade.getOverdue() != true)
            {
                String sOverdueData = new String("-Overdue\n");
                sReport = sReport + sNextAssignment;
            }
            if(gGrade.getDropped() == true)
            {
                String sDroppedData = new String("-Dropped\n");
                sReport = sReport + sNextAssignment;
            }
            sReport = sReport + "\n";
        }

        return sReport;
    }
}
