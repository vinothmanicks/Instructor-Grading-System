package com.cannapaceus.grader;

import java.util.ArrayList;

public class Statistics {

    private float fMean;
    private float fMedian;
    private float fMode;
    private float fStandardDev;

    public Statistics()
    {

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
}
