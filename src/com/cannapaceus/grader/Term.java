package com.cannapaceus.grader;

import java.util.*;

enum Season {
    Winter, Spring, Summer, Fall
};  //Term season

public class Term {

    //Long to hold the course's ID from the database
    private long lDBID = 0;

    private Season eSeason;

    private int iYear;  //Term year

    private boolean bArchived;  //Is the Term archived?

    private ArrayList<Course> lCourses;

    Term(Season eSeason, int iYear, boolean bArchived) {
        this.eSeason = eSeason;
        this.iYear = iYear;
        this.bArchived = bArchived;
        this.lCourses = new ArrayList<>();
    }

    Term(Term newTerm) {
        this.eSeason = newTerm.getSeason();
        this.iYear = newTerm.getYear();
        this.bArchived = newTerm.getArchived();
        this.lCourses = new ArrayList<>();
    }

    //Setter methods
    /**
     * Setter for the term's ID from the database
     * @param lDBID ID of the term from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    /**
     * Setter for the term's season
     * @param eSeason new season of the term
     */
    public void setSeason(Season eSeason) {
        this.eSeason = eSeason;
    }

    /**
     * Setter for the term's year
     * @param iYear new year of the term
     */
    public void setYear(int iYear) {
        this.iYear = iYear;
    }

    /**
     * Setter for the term's archived boolean
     * @param bArchived new archived boolean of the term
     */
    public void setArchived(boolean bArchived) {
        this.bArchived = bArchived;
    }

    /**
     * Method to add a course to the term's list of courses
     * @param newCourse new course to add
     */
    public void addCourse(Course newCourse) {
        this.lCourses.add(newCourse);
    }

    //Getter methods
    /**
     * Getter for a copy of the term's ID from the database
     * @return
     */
    public long getDBID() {
        return lDBID;
    }

    /**
     * Getter for a copy of the term's season
     * @return
     */
    public Season getSeason() {
        return this.eSeason;
    }

    /**
     * Getter for a copy of the term's year
     * @return
     */
    public int getYear() {
        return this.iYear;
    }

    /**
     * Getter for a copy of the term's archived boolean
     * @return
     */
    public boolean getArchived() {
        return bArchived;
    }

    /**
     * Getter for a copy of the term's list of courses
     * @return
     */
    public ArrayList<Course> getCourses() {
        ArrayList<Course> lCoursesCopy = new ArrayList<>(this.lCourses);
        return lCoursesCopy;
    }
}
