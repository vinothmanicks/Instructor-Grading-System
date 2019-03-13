package com.cannapaceus.grader;

import java.util.ArrayList;

public class Category {

    //The name of the category
    private String sCategoryName;
    //The weight assigned to this category
    private float fCategoryWeight;
    //The list of assignments assigned to this category
    private ArrayList<Assignment> lCatAssignments;

    public Category(String sCategoryName, float fCategoryWeight)
    {
        this.sCategoryName = sCategoryName;
        this.fCategoryWeight = fCategoryWeight;
        lCatAssignments = new ArrayList<>();
    }

    public Category(Category catCategory)
    {
        this.sCategoryName = catCategory.getName();
        this.fCategoryWeight = catCategory.getWeight();
        this.lCatAssignments = new ArrayList<>(catCategory.getAssignments());
    }

    public String getName()
    {
        String copyString = new String(sCategoryName);
        return copyString;
    }

    public float getWeight()
    {
        return fCategoryWeight;
    }

    public ArrayList<Assignment> getAssignments()
    {
        return new ArrayList<>(lCatAssignments);
    }

    public void addAssignment(Assignment aAssignment)
    {
        lCatAssignments.add(aAssignment);
    }

    public void removeAssigment(Assignment aAssignment)
    {
        lCatAssignments.remove(aAssignment);
    }

    public void setWeight(float fCategoryWeight)
    {
        this.fCategoryWeight = fCategoryWeight;
    }

    public void setCategoryName(String categoryName)
    {
        sCategoryName = new String(categoryName);
    }
}
