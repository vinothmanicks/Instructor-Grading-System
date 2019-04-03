package com.cannapaceus.grader;

import java.util.ArrayList;

public class Statistics {

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

    public void setMean(Float newMean) { this.fMean = newMean; }

    public void setMedian(float newMedian) { this.fMedian = newMedian; }

    public void setMode(float newMode) { this.fMode = newMode; }

    public void setStandardDev(float newStandardDev) { this.fStandardDev = newStandardDev; }

    public void calculateMean(ArrayList<Grade> listOfGrades)
    {
        float temp = 0;
        float tempWeight = 0;
        float scoreSum = 0;
        float fullScoresSum = 0;
        float tempFullScore = 0;

        ArrayList<Grade> validGrades = new ArrayList<>();

        for (Grade g : listOfGrades) {
            if (!g.getSubmitted() || g.getDropped()) {
                continue;
            }

            validGrades.add(g);
        }

        for (Grade g : validGrades) {
            Category cat = g.getAssignmentReference().getCategoryCopy();

            if (cat == null) {
                tempWeight = 1.0f;
            } else {
                tempWeight = cat.getWeight();
            }

            tempFullScore = g.getAssignmentReference().getMaxScore();
            temp = g.getGrade() * tempWeight;
            scoreSum += temp;
            fullScoresSum += tempFullScore * tempWeight;
        }

        if (fullScoresSum == 0) {
            this.fMean = 0;
        } else {
            this.fMean = (scoreSum / fullScoresSum) * 100;
        }
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
            calculateMedian(validGrades);
            for (Grade gGrade : validGrades)
            {
                float theGrade = gGrade.getGrade();
                fCalculationValue += Math.multiplyExact((long)(theGrade - this.fMedian),(long)(theGrade - this.fMedian));
            }

            fCalculationValue = (float)Math.sqrt((double)(fCalculationValue/validGrades.size()));
            this.fStandardDev = fCalculationValue;
        }
    }

    public static int geNumOfInstances() {
        return counter;
    }
}
