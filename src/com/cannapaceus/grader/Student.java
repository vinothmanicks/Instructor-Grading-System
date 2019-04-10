package com.cannapaceus.grader;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Comparator;

public class Student extends RecursiveTreeObject<Student> {

    //Long to hold the ID of the student from the database
    private long lDBID = 0;

    //String to hold the student's last name
    private final StringProperty sLastName;

    //String to hold the student's first name and middle initial
    private final StringProperty sFirstMIName;

    //String to hold the student's ID
    private final StringProperty sStudentID;

    //String to hold the student's email address
    private final StringProperty sEmail;

    //List to hold all course grades
    private ArrayList<Grade> lGrades;

    //Grade class containing student's average grade for the course
    private FloatProperty fAverageGrade;

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
        this.sFirstMIName = new SimpleStringProperty(sFirstMIName);
        this.sLastName = new SimpleStringProperty(sLastName);
        this.sStudentID = new SimpleStringProperty(sStudentID);
        this.sEmail = new SimpleStringProperty(sStudentEmail);
        this.lGrades = new ArrayList<>();

        this.fAverageGrade = new SimpleFloatProperty();

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
        this.sLastName = new SimpleStringProperty(stuStudent.getLastName());
        this.sFirstMIName = new SimpleStringProperty(stuStudent.getFirstMIName());
        this.sStudentID = new SimpleStringProperty(stuStudent.getStudentID());
        this.sEmail = new SimpleStringProperty(stuStudent.getStudentEmail());
        this.lGrades = new ArrayList<>();

        this.fAverageGrade = new SimpleFloatProperty();

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
        this.sFirstMIName.set(sFirstMIName);
    }

    public void setLastName(String sLastName)
    {
        this.sLastName.set(sLastName);
    }

    public void setStudentID(String sStudentID)
    {
        this.sStudentID.set(sStudentID);
    }

    public void setStudentEmail(String sStudentEmail)
    {
        this.sEmail.set(sStudentEmail);
    }

    public void setAverageGrade(float fAverageGrade) {
        this.fAverageGrade.setValue(fAverageGrade);
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
            this.fAverageGrade.setValue(0);
        } else {
            this.fAverageGrade.setValue((scoreSum / fullScoresSum) * 100);
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

    public StringProperty getFirstMINameProperty()
    {
        return sFirstMIName;
    }

    public StringProperty getLastNameProperty()
    {
        return sLastName;
    }

    public StringProperty getStudentIDProperty()
    {
        return sStudentID;
    }

    public StringProperty getStudentEmailProperty()
    {
        return sEmail;
    }

    public String getFirstMIName()
    {
        String sFirstMINameCopy = new String(this.sFirstMIName.getValue());
        return sFirstMINameCopy;
    }

    public String getStudentID()
    {
        String sStudentIDCopy = new String(this.sStudentID.getValue());
        return sStudentIDCopy;
    }

    public String getLastName()
    {
        String sLastNameCopy = new String(this.sLastName.getValue());
        return sLastNameCopy;
    }

    public String getStudentEmail()
    {
        String sEmailCopy = new String(this.sEmail.getValue());
        return sEmailCopy;
    }

    public float getAverageGrade()
    {
        return this.fAverageGrade.getValue();
    }

    public FloatProperty getfAverageGradeProperty() {
        return this.fAverageGrade;
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
        String sReport = new String( "Student name: " + this.sFirstMIName.getValue() + " " + this.sLastName.getValue() + "\n\n"+"Assignments:\n");

        for (Grade gGrade:lGrades)
        {
            String sNextAssignment = new String(gGrade.getAssignmentCopy().getAssignmentName() + ":\t" + gGrade.getGrade()+"\\"+ gGrade.getAssignmentCopy().getMaxScore() + "\n");
            sReport = sReport + sNextAssignment;

            if(gGrade.getMissing() == true)
            {
                String sMissingData = new String("-Missing\n");
                sReport = sReport + sMissingData;
            }
            if(gGrade.getOverdue() == true)
            {
                String sOverdueData = new String("-Overdue\n");
                sReport = sReport +sOverdueData;
            }
            if(gGrade.getDropped() == true)
            {
                String sDroppedData = new String("-Dropped\n");
                sReport = sReport + sDroppedData;
            }
            sReport = sReport + "\n";
        }

        sReport = sReport + "\n" + "Overall grade: " + this.getAverageGrade();

        return sReport;
    }
}
