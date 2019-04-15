package com.cannapaceus.grader;

import java.util.ArrayList;
import java.util.Collections;

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
        float scoreSum = 0;
        int count = 0;

        ArrayList<Grade> validGrades = new ArrayList<>();

        for (Grade g : listOfGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            validGrades.add(g);
        }

        for (Grade g : validGrades) {
            scoreSum += g.getGrade();
            ++count;
        }
        this.fMean = scoreSum / count;
    }

    public void calculateMedian(ArrayList<Grade> listOfGrades)
    {
        ArrayList<Grade> validGrades = new ArrayList<>();

        for (Grade g : listOfGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            validGrades.add(g);
        }

        Collections.sort(validGrades, Grade.scoreComparator);

        if (validGrades.size() == 0) {
            this.fMedian = 0.0f;
        } else {
            int medianMarker = Math.round(validGrades.size()/2.0f - 1);
            this.fMedian = validGrades.get(medianMarker).getGrade();
        }
    }

    public void calculateMode(ArrayList<Grade> listOfGrades)
    {
        float maxValue = 0;
        int maxCount = 0;

        ArrayList<Grade> validGrades = new ArrayList<>();

        for (Grade g : listOfGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            validGrades.add(g);
        }

        for (Grade g : validGrades) {
            int count = 0;

            for (Grade h : validGrades) {
                if (h.getGrade() == g.getGrade() ) ++count;
            }

            if (count > maxCount) {
                maxCount = count;
                maxValue = g.getGrade();
            }
        }

        this.fMode = maxValue;
    }

    public void calculateStandardDev(ArrayList<Grade> listOfGrades)
    {
        ArrayList<Grade> validGrades = new ArrayList<>();

        for (Grade g : listOfGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            validGrades.add(g);
        }

        if (validGrades.size() == 0) {
            this.fStandardDev = 0;
        } else {
            float fCalculationValue = 0;
            calculateMean(validGrades);
            for (Grade gGrade : validGrades)
            {
                float theGrade = gGrade.getGrade();
                fCalculationValue += ((double)(theGrade - this.fMean)) * ((double)(theGrade - this.fMean));
            }

            fCalculationValue = (float)Math.sqrt((double)(fCalculationValue/(validGrades.size() - 1)));
            this.fStandardDev = fCalculationValue;
        }
    }

    public static int geNumOfInstances() {
        return counter;
    }
}
