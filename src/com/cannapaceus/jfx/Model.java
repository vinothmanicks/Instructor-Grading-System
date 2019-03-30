package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import java.util.ArrayList;
import java.util.Collections;

public class Model {
    DBService db = DBService.getInstance();

    static Model instance = null;

    ArrayList<Term> lCurrentModel = null;

    ArrayList<Object> updatedObjects = null;
    ArrayList<Object> newObjects = null;
    ArrayList<Object> removedObjects = null;

    Term selectedTerm = null;
    Course selectedCourse = null;
    Category selectedCategory = null;
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
        lCurrentModel = db.retrieveModel();

        updatedObjects = new ArrayList<>();
        newObjects = new ArrayList<>();
        removedObjects = new ArrayList<>();
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

    public ArrayList<Object> getRemovedObjects() {
        return removedObjects;
    }

    public void addNewObject(Object o) {
        newObjects.add(o);
    }

    public void addUpdatedObject(Object o) {
        if (!newObjects.contains(o))
            updatedObjects.add(o);
    }

    public void addRemovedObject(Object o) {
        if (!newObjects.remove(o)) {
            removedObjects.add(o);
        }

        updatedObjects.remove(o);
    }

    public int getNewNum() {
        return newObjects.size();
    }

    public int getUpdatedNum() {
        return updatedObjects.size();
    }

    public int getRemovedNum() {
        return removedObjects.size();
    }

    public int getNumChanges() {
        return updatedObjects.size() + newObjects.size() + removedObjects.size();
    }

    public void addTerm(Term t) {
        lCurrentModel.add(t);
        Collections.sort(lCurrentModel);
    }

    public void addCourse(Course c) {
        selectedTerm.addCourse(c);
        Collections.sort(selectedTerm.getCourses());
    }

    public void addStudent(Student s) {
        selectedCourse.addStudent(s);
        Collections.sort(selectedCourse.getlStudents());
    }

    public void addCategory(Category cat) {
        selectedCourse.addCategory(cat);
        Collections.sort(selectedCourse.getlCategories());
    }

    public void addAssignment(Assignment a) {
        selectedCourse.addAssignment(a);
        Collections.sort(selectedCourse.getlAssignments());
    }

    public void addGrade(Grade g) {
        selectedCourse.addGrade(g);
        selectedStudent.addGrade(g);
        selectedAssignment.addGrade(g);
        Collections.sort(selectedCourse.getlGrades());
        Collections.sort(selectedStudent.getGrades());
        Collections.sort(selectedAssignment.getGrades());
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

    public void removeCategory(Category cat) {
        selectedCourse.getlCategories().remove(cat);
        for (Assignment a : cat.getAssignments()) {
            a.setCategory(null);
        }
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
        else
            selectedTerm = null;
    }

    public void setSelectedCourse(Course c) {
        if (selectedTerm != null && selectedTerm.getCourses().contains(c))
            selectedCourse = c;
        else
            selectedCourse = null;
    }

    public void setSelectedCategory(Category cat) {
        if (selectedCourse != null && selectedCourse.getlCategories().contains(cat))
            selectedCategory = cat;
        else
            selectedCategory = null;
    }

    public void setSelectedStudent(Student s) {
        if (selectedCourse != null && selectedCourse.getlStudents().contains(s))
            selectedStudent = s;
        else
            selectedStudent = null;
    }

    public void setSelectedAssignment(Assignment a) {
        if (selectedCourse != null && selectedCourse.getlAssignments().contains(a))
            selectedAssignment = a;
        else
            selectedAssignment = null;
    }

    public Term getSelectedTerm() {
        return selectedTerm;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public Assignment getSelectedAssignment() {
        return selectedAssignment;
    }

    public void commitChanges() {
        db.storeNewObjects(newObjects, lCurrentModel);
        db.updateObjects(updatedObjects);
        db.deleteObjects(removedObjects);

        long termID = 0;
        long courseID = 0;
        long categoryID = 0;
        long studentID = 0;
        long assignmentID = 0;

        if (selectedTerm != null)
            termID = selectedTerm.getDBID();

        if (selectedCourse != null)
            courseID = selectedCourse.getDBID();

        if (selectedCategory != null)
            categoryID = selectedCategory.getDBID();

        if (selectedStudent != null)
            studentID = selectedStudent.getDBID();

        if (selectedAssignment != null)
            assignmentID = selectedAssignment.getDBID();

        lCurrentModel = db.retrieveModel();

        selectedTerm = null;
        selectedCourse = null;
        selectedCategory = null;
        selectedStudent = null;
        selectedAssignment = null;

        for (Term t : lCurrentModel) {
            if (t.getDBID() == termID) {
                setSelectedTerm(t);
            }
        }

        for (Course c : selectedTerm.getCourses()) {
            if (c.getDBID() == courseID) {
                setSelectedCourse(c);
            }
        }

        for (Category cat : selectedCourse.getlCategories()) {
            if (cat.getDBID() == categoryID) {
                setSelectedCategory(cat);
            }
        }

        for (Assignment a : selectedCourse.getlAssignments()) {
            if (a.getDBID() == assignmentID) {
                setSelectedAssignment(a);
            }
        }

        for (Student s : selectedCourse.getlStudents()) {
            if (s.getDBID() == studentID) {
                setSelectedStudent(s);
            }
        }

        newObjects.clear();
        updatedObjects.clear();
        removedObjects.clear();
    }

    public void revertChanges() {
        long termID = 0;
        long courseID = 0;
        long categoryID = 0;
        long studentID = 0;
        long assignmentID = 0;

        if (selectedTerm != null)
            termID = selectedTerm.getDBID();

        if (selectedCourse != null)
            courseID = selectedCourse.getDBID();

        if (selectedCategory != null)
            categoryID = selectedCategory.getDBID();

        if (selectedStudent != null)
            studentID = selectedStudent.getDBID();

        if (selectedAssignment != null)
            assignmentID = selectedAssignment.getDBID();

        lCurrentModel = db.retrieveModel();

        selectedTerm = null;
        selectedCourse = null;
        selectedCategory = null;
        selectedStudent = null;
        selectedAssignment = null;

        for (Term t : lCurrentModel) {
            if (t.getDBID() == termID) {
                setSelectedTerm(t);
            }
        }

        for (Course c : selectedTerm.getCourses()) {
            if (c.getDBID() == courseID) {
                setSelectedCourse(c);
            }
        }

        for (Category cat : selectedCourse.getlCategories()) {
            if (cat.getDBID() == categoryID) {
                setSelectedCategory(cat);
            }
        }

        for (Assignment a : selectedCourse.getlAssignments()) {
            if (a.getDBID() == assignmentID) {
                setSelectedAssignment(a);
            }
        }

        for (Student s : selectedCourse.getlStudents()) {
            if (s.getDBID() == studentID) {
                setSelectedStudent(s);
            }
        }

        updatedObjects.clear();
        newObjects.clear();
        removedObjects.clear();
    }

    public boolean isChanged(Object o) {
        return (updatedObjects.contains(o) || newObjects.contains(o));
    }

    public boolean isRemoved(Object o) {
        return (removedObjects.contains(o));
    }
}
