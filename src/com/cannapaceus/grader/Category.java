package com.cannapaceus.grader;

public class Category {

    //Long to hold the ID of the category from the database
    private long lDBID = 0;

    //Currently empty to resolve errors in other classes.
    public Category(Category catCategory)
    {

    }

    //Setter Functions
    /**
     * Setter for the category's ID from the database
     * @param lDBID ID of the category from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    //Getter Functions
    /**
     * Getter for a copy of the category's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }
}
