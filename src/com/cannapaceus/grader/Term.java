package com.cannapaceus.grader;


import java.util.ArrayList;

public class Term {

    private enum eSeason {WINTER,SPRING,SUMMER,FALL}
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
        return new ArrayList<>(lCourses);
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
}
