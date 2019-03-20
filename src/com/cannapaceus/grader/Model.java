package com.cannapaceus.grader;

import java.util.ArrayList;

public class Model {
    DBService db = DBService.getInstance();

    static Model instance = null;

    ArrayList<Term> lTerms = null;

    Term selectedTerm = null;
    Course selectedCourse = null;
    Student selectedStudent = null;
    Assignment selectedAssignment = null;

    private Model() {
        initModel();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    private void initModel() {
        lTerms = db.retrieveModel();
    }

    public ArrayList<Term> getTerms() {
        return lTerms;
    }

    public void setSelectedTerm(Term t) {
        if (lTerms.contains(t))
            selectedTerm = t;
    }

    public void setSelectedCourse(Course c) {
        if (selectedTerm.getCourses().contains(c))
            selectedCourse = c;
    }

    public void setSelectedStudent(Student s) {
        if (selectedCourse.getlStudents().contains(s))
            selectedStudent = s;
    }

    public void setSelectedAssignment(Assignment a) {
        if (selectedCourse.getlAssignments().contains(a))
            selectedAssignment = a;
    }

    public Term getSelectedTerm() {
        return selectedTerm;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public Assignment getSelectedAssignment() {
        return selectedAssignment;
    }
}
