package com.cannapaceus.grader;

import java.util.ArrayList;

public class Question {

    //Name of the question.
    private String sQuestion;
    //Array of potential answers. If multiple choice, first answer in the list is the correct one.
    private ArrayList<String> lAnswers;
    //List of courses names that use this question.
    private ArrayList<String> lCourseNames;
    //structs QuestionType qtQuestionType;
    //structs Statistics stQuestionStats;

    public Question(String sQuestion, ArrayList<String> lAnswers, ArrayList<String> lCourseNames,)
    {
        this.sQuestion = sQuestion;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.lCourseNames = new ArrayList<>(lCourseNames);
    }

    public Question(Question queQuestion)
    {
        this.sQuestion = queQuestion.getQuestion();
        this.lAnswers = queQuestion.getAnswers();
        this.lCourseNames = queQuestion.getCourses();
    }

    public void editQuestion(String sQuestion /*TODO: Question Type and stats*/, ArrayList<String> lAnswers, ArrayList<String> lCourseNames )
    {
        this.sQuestion = sQuestion;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.lCourseNames = new ArrayList<>(lCourseNames);
    }

    public void setQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public void setQuestionType()
    {
        //Get that questionType defined
    }

    public void setQuestionStats()
    {
        //Get that question stats defined
    }

    public void setAnwsers(ArrayList<String> lAnswers)
    {
        this.lAnswers = new ArrayList<>(lAnswers);
    }

    public void setCourses(ArrayList<String> lCourseNames)
    {
        this.lAnswers = new ArrayList<>(lCourseNames);
    }

    public String getQuestion()
    {
        return new String(this.sQuestion);
    }

    /*public String getQuestionType()
    {
        return
    }

    public String getQuestionStats()
    {

    }*/

    public ArrayList<String> getAnswers()
    {
        return new ArrayList<>(lAnswers);
    }

    public ArrayList<String> getCourses()
    {
        return new ArrayList<>(lCourseNames);
    }
}
