package com.cannapaceus.grader;

public class Category {

    //Long to hold the ID of the category from the database
    private long lDBID = 0;

    //String to hold the name of the category
    private String sCategoryName;

    //float to hold the weight of the category
    private float fWeight;

    public Category(String sCategoryName, float fWeight) {
        this.sCategoryName = sCategoryName;
        this.fWeight = fWeight;
    }

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

    /**
     * Setter for the category's name
     * @param sCategoryName new name of the category
     */
    public void setName(String sCategoryName) {
        this.sCategoryName = sCategoryName;
    }

    /**
     * Setter for the category's weight from the database
     * @param fWeight weight of the category
     */
    public void setWeight(float fWeight) {
        this.fWeight = fWeight;
    }

    //Getter Functions
    /**
     * Getter for a copy of the category's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

    /**
     * Getter for the category's weight from the database
     * @return weight of the category
     */
    public float getWeight() {
        return this.fWeight;
    }
}
