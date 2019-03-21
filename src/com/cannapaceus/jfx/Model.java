package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;

import java.util.ArrayList;

public class Model {
    DBService db = DBService.getInstance();

    static Model instance = null;

    ArrayList<Term> lStoredModel = null;

    ArrayList<Term> lCurrentModel = null;

    ArrayList<Object> updatedObjects = null;
    ArrayList<Object> newObjects = null;

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
        lStoredModel = db.retrieveModel();
        lCurrentModel = db.retrieveModel();
        updatedObjects = new ArrayList<>();
        newObjects = new ArrayList<>();
    }

    public ArrayList<Term> getTerms() {
        return lCurrentModel;
    }

    public ArrayList<Object> getUpdatedObjects() {
        return updatedObjects;
    }

    public ArrayList<Object> getNewObjects() {
        return newObjects;
    }

    public void updateObject(Object o) {
        updatedObjects.add(o);
    }

    public void addNewObject(Object o) {
        newObjects.add(o);
    }

    public int getNumberOfChanges() {
        return updatedObjects.size() + newObjects.size();
    }

    public void addTerm(Term t) {
        lCurrentModel.add(t);
    }

    public void addCourse(Course c) {
        selectedTerm.getCourses().add(c);
    }

    public void addStudent(Student s) {
        selectedCourse.getlStudents().add(s);
    }

    public void addCategory(Category c) {
        selectedCourse.getlCategories().add(c);
    }

    public void addAssignment(Assignment a) {
        selectedCourse.getlAssignments().add(a);
    }

    public void addGrade(Grade g) {
        selectedCourse.getlGrades().add(g);
        selectedStudent.getGrades().add(g);
        selectedAssignment.getGrades().add(g);
    }

    public void removeTerm(Term t) {
        lCurrentModel.remove(t);
    }

    public void removeCourse(Course c) {
        selectedTerm.getCourses().remove(c);
    }

    public void removeStudent(Student s) {
        selectedCourse.getlStudents().remove(s);
    }

    public void removeCategory(Category c) {
        selectedCourse.getlCategories().remove(c);
    }

    public void removeAssignment(Assignment a) {
        selectedCourse.getlAssignments().remove(a);
    }

    public void removeGrade(Grade g) {
        selectedCourse.getlGrades().remove(g);
        selectedStudent.getGrades().remove(g);
        selectedAssignment.getGrades().remove(g);
    }

    public void setSelectedTerm(Term t) {
        if (lCurrentModel.contains(t))
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
