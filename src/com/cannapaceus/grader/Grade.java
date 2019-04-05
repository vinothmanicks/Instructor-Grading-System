package com.cannapaceus.grader;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.util.Comparator;

public class Grade extends RecursiveTreeObject<Grade> {

    //Long to hold the ID of the grade from the database
    private long lDBID = 0;

    private Student stuStudent;
    private Assignment aAssignment;

    private final FloatProperty fGrade = new SimpleFloatProperty();
    private final BooleanProperty bOverdue = new SimpleBooleanProperty();
    private final BooleanProperty bMissing = new SimpleBooleanProperty();
    private final BooleanProperty bDropped = new SimpleBooleanProperty();
    private final BooleanProperty bSubmitted = new SimpleBooleanProperty();

    /**
     * <h1>Grade Constructor</h1>
     * Constructor for the assignment class.
     * @param fGrade Grade for the assignment if grade is already known when creating
     * @param stuStudent Student this grade is for. Is a reference and should be assigned at instantiation.
     * @param aAssignment Assignemnt this grade is for. Is a reference and should be assigned at instantiation.
     */
    public Grade(float  fGrade, Student stuStudent, Assignment aAssignment) {
        this.fGrade.set(fGrade);

        //Should not be able to change these variables
        this.stuStudent = stuStudent;
        this.aAssignment = aAssignment;

        this.bOverdue.set(false);
        this.bMissing.set(false);
        this.bDropped.set(false);
        this.bSubmitted.set(false);
    }

    /**
     *
     */
    public Grade(Grade gGrade)
    {
        this.fGrade.set(gGrade.getGrade());
        this.stuStudent = gGrade.getStudentReference();
        this.aAssignment = gGrade.getAssignmentReference();
        this.bOverdue.set(gGrade.getOverdue());
        this.bMissing.set(gGrade.getMissing());
        this.bDropped.set(gGrade.getDropped());
        this.bSubmitted.set(gGrade.getSubmitted());
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
        this.fGrade.set(fGrade);
    }

    /**
     * Setter for the Grade class's overdue flag
     * @param bOverdue
     */
    public void setOverdue(boolean bOverdue)
    {
        this.bOverdue.set(bOverdue);
    }

    /**
     * Setter for the Grade class's missing flag
     * @param bMissing
     */
    public void setMissing(boolean bMissing)
    {
        this.bMissing.set(bMissing);
    }

    /**
     * Setter for the Grade class's dropped flag
     * @param bDropped
     */
    public void setDropped(boolean bDropped)
    {
        this.bDropped.set(bDropped);
    }

    /**
     * Setter for the Grade class's submitted flag
     * @param bSubmitted
     */
    public void setSubmitted(boolean bSubmitted)
    {
        this.bSubmitted.set(bSubmitted);
    }

    //Getter functions
    /**
     * Getter for a copy of the grade's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

    public FloatProperty getGradeProperty()
    {
        return fGrade;
    }

    /**
     * Getter for the Grade class's grade float
     * @return Copy of the grade float
     */
    public float getGrade()
    {
        return fGrade.getValue();
    }

    public BooleanProperty getOverdueProperty()
    {
        return bOverdue;
    }

    /**
     * Setter for the Grade class's overdue flag
     * @return Copy of the overdue flag status
     */
    public boolean getOverdue()
    {
        return bOverdue.getValue();
    }

    public BooleanProperty getMissingProperty()
    {
        return bMissing;
    }

    /**
     * Setter for the Grade class's missing flag
     * @return Copy of the missing flag's status
     */
    public boolean getMissing()
    {
        return bMissing.getValue();
    }

    public BooleanProperty getDroppedProperty()
    {
        return bDropped;
    }

    /**
     * Setter for the Grade class's dropped flag
     * @return Copy of the dropped flag's status
     */
    public boolean getDropped()
    {
        return bDropped.getValue();
    }

    public BooleanProperty getSubmittedProperty()
    {
        return bSubmitted;
    }

    /**
     * Setter for the Grade class's submitted flag
     * @return submitted flag's status
     */
    public boolean getSubmitted()
    {
        return bSubmitted.getValue();
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

    public static Comparator<Grade> nameComparator = new Comparator<Grade>() {
        @Override
        public int compare(Grade g1, Grade g2) {
            int stuComp = Student.nameComparator.compare(g1.getStudentReference(), g2.getStudentReference());
            if (stuComp != 0) {
                return stuComp;
            }

            return Assignment.nameComparator.compare(g1.getAssignmentReference(), g2.getAssignmentReference());
        }
    };

    public static Comparator<Grade> scoreComparator = new Comparator<Grade>() {
        @Override
        public int compare(Grade g1, Grade g2) {
            return Float.compare(g1.getGrade(), g2.getGrade());
        }
    };


    //Potential functions if we need to get references

    /**
     * Getter for a reference to the assignment this grade is for
     * @return Reference of the grade's assignment variable
     */
    public Assignment getAssignmentReference()
    {
        return aAssignment;
    }


    /**
     * Getter for a reference to the the student this grade is for
     * @return Reference of the grade's student variable
     */
    public Student getStudentReference()
    {
        return stuStudent;
    }

}
