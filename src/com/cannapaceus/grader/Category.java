package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Comparator;

public class Category {
    //Long to hold the ID of the category from the database
    private long lDBID = 0;
    //The name of the category
    private String sCategoryName;
    //The weight assigned to this category
    private float fCategoryWeight;

    private int iDropped;
    //The list of assignments assigned to this category
    private ArrayList<Assignment> lCatAssignments;

    public Category(String sCategoryName, float fCategoryWeight, int iDropped)
    {
        this.sCategoryName = sCategoryName;
        this.fCategoryWeight = fCategoryWeight;
        this.iDropped = iDropped;
        lCatAssignments = new ArrayList<>();
    }

    public Category(Category catCategory)
    {
        this.lDBID = catCategory.getDBID();
        this.sCategoryName = catCategory.getName();
        this.fCategoryWeight = catCategory.getWeight();
        this.iDropped = catCategory.getDropped();
        this.lCatAssignments = new ArrayList<>(catCategory.getAssignments());
    }

    /**
     * Getter for a copy of the category's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
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

    public int getDropped()
    {
        return iDropped;
    }

    public ArrayList<Assignment> getAssignments()
    {
        return lCatAssignments;
    }

    public void addAssignment(Assignment aAssignment)
    {
        lCatAssignments.add(aAssignment);
    }

    public void removeAssigment(Assignment aAssignment)
    {
        lCatAssignments.remove(aAssignment);
    }

    /**
     * Setter for the category's ID from the database
     * @param lDBID ID of the category from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    public void setWeight(float fCategoryWeight)
    {
        this.fCategoryWeight = fCategoryWeight;
    }

    public void setCategoryName(String categoryName)
    {
        sCategoryName = new String(categoryName);
    }

    public void setDropped(int iDropped) {
        this.iDropped = iDropped;
    }

    public int compareTo(Category cat) {
        return this.getName().compareTo(cat.getName());
    }

    public static Comparator<Category> nameComparator = new Comparator<Category>() {
        @Override
        public int compare(Category cat1, Category cat2) {
            return cat1.getName().toUpperCase().compareTo(cat2.getName().toUpperCase());
        }
    };
}
