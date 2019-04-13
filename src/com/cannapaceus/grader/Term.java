package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Comparator;

public class Term {

    //Long to hold the course's ID from the database
    private long lDBID = 0;
    eSeason sCourseSeason;
    private ArrayList<Course> lCourses;
    private int iYear;
    private boolean bArchived;

    public Term(int iYear, eSeason sCourseSeason)
    {
        this.iYear = iYear;
        this.sCourseSeason = sCourseSeason;
        lCourses = new ArrayList<>();
        bArchived = false;
    }

    public Term(Term tTerm)
    {
        this.iYear = tTerm.getYear();
        this.sCourseSeason = tTerm.getSeason();
        this.bArchived = tTerm.getArchivedStatus();
        this.lCourses = new ArrayList<>(tTerm.getCourses());
    }

    /**
     * Getter for a copy of the term's ID from the database
     * @return
     */
    public long getDBID() {
        return lDBID;
    }

    public eSeason getSeason()
    {
        return this.sCourseSeason;
    }

    public int getYear()
    {
        return this.iYear;
    }

    public boolean getArchivedStatus()
    {
        return this.bArchived;
    }

    public ArrayList<Course> getCourses()
    {
        return lCourses;
    }

    /**
     * Setter for the term's ID from the database
     * @param lDBID ID of the term from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    public void setCourseSeason(eSeason sCourseSeason)
    {
        this.sCourseSeason = sCourseSeason;
    }

    public void setYear(int iYear)
    {
        this.iYear = iYear;
    }

    public void archiveTerm()
    {
        this.bArchived = true;
    }

    public void addCourse(Course coCourse)
    {
        lCourses.add(coCourse);
    }

    public void removeCourse(Course coCourse)
    {
        lCourses.remove(coCourse);
    }

    public int compareTo(Term t) {
        if (this.getYear() == t.getYear()) {
            return Integer.compare(this.getSeason().getInt(), t.getSeason().getInt());
        }

        return Integer.compare(this.getYear(), t.getYear());
    }

    public static Comparator<Term> termComparator = new Comparator<Term>() {
        @Override
        public int compare(Term t1, Term t2) {
            if (t1.getYear() == t2.getYear()) {
                return -Integer.compare(t1.getSeason().getInt(), t2.getSeason().getInt());
            }

            return -Integer.compare(t1.getYear(), t2.getYear());
        }
    };
}
