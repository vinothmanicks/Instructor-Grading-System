package com.cannapaceus.grader;

import java.util.ArrayList;

public class Statistics {

    //Long to hold the ID of the statistics collection from the database
    private long lDBID = 0;

    private static int counter = 0;
    private float fMean;
    private float fMedian;
    private float fMode;
    private float fStandardDev;

    public Statistics()
    {
        counter++;
    }
    public Statistics(Statistics satStatistics)
    {
        this.fMean = satStatistics.getMean();
        this.fMode = satStatistics.getMode();
        this.fMedian = satStatistics.getMedian();
        this.fStandardDev = satStatistics.getStandardDev();
    }

    public float getMean()
    {
        return this.fMean;
    }

    public float getMedian()
    {
        return this.fMedian;
    }

    public float getMode()
    {
        return this.fMode;
    }

    public float getStandardDev()
    {
        return this.fStandardDev;
    }

    /**
     * Getter for a copy of the assignment's ID from the database
     * @return
     */
    public long getDBID() {
        return this.lDBID;
    }

    public void setMean(Float newMean) { this.fMean = newMean; }

    public void setMedian(float newMedian) { this.fMedian = newMedian; }

    public void setMode(float newMode) { this.fMode = newMode; }

    public void setStandardDev(float newStandardDev) { this.fStandardDev = newStandardDev; }

    /**
     * Setter for the assignment's ID from the database
     * @param lDBID ID of the assignment from the database
     */
    public void setDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    public void calculateMean(ArrayList<Grade> listOfGrades)
    {
        float sumTemp = 0;
        for (Grade gGrade:listOfGrades)
        {
            sumTemp += gGrade.getGrade();
        }

        sumTemp = (sumTemp/listOfGrades.size());

        this.fMean = sumTemp;
    }

    public void calculateMedian(ArrayList<Grade> listOfGrades)
    {
        int medianMarker = Math.round(listOfGrades.size()/2);
        this.fMedian = listOfGrades.get(medianMarker).getGrade();
    }

    public void calculateMode(ArrayList<Grade> listOfGrades)
    {
        float maxValue = 0;
        int maxCount = 0;

        for (int i = 0; i < listOfGrades.size(); ++i) {
            int count = 0;
            for (int j = 0; j < listOfGrades.size(); ++j) {
                if (listOfGrades.get(j).getGrade() == listOfGrades.get(i).getGrade() ) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = listOfGrades.get(i).getGrade();
            }
        }

        this.fMode = maxValue;
    }

    public void calculateStandardDev(ArrayList<Grade> listOfGrades)
    {
        float fCalculationValue = 0;
        calculateMedian(listOfGrades);
        for (Grade gGrade:listOfGrades)
        {
            float theGrade = gGrade.getGrade();
            fCalculationValue += Math.multiplyExact((long)(theGrade - this.fMedian),(long)(theGrade - this.fMedian));
        }

        fCalculationValue = (float)Math.sqrt((double)(fCalculationValue/listOfGrades.size()));
        this.fStandardDev = fCalculationValue;
    }

    public static int geNumOfInstances() {
        return counter;
    }
}
