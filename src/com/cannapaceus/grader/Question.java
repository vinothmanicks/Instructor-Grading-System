package com.cannapaceus.grader;

import java.util.ArrayList;

public class Question {

    //Name of the question.
    private String sQuestion;
    //Array of potential answers. If multiple choice, first answer in the list is the correct one.
    private ArrayList<String> lAnswers;
    //List of courses names that use this question.
    private ArrayList<String> lCourseNames;
    //Enum to sub for lack of structs
    //TODO: Figure out the best way to do this
    private QuestionType qtQuestionType;
    //TODO determine if we're going to use a separate class
    private QuestionStats stQuestionStats;

    public Question(String sQuestion, ArrayList<String> lAnswers, ArrayList<String> lCourseNames, QuestionStats stQuestionStats,QuestionType qtQuestionType)
    {
        this.sQuestion = sQuestion;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.lCourseNames = new ArrayList<>(lCourseNames);
        this.stQuestionStats = new QuestionStats(stQuestionStats);
        this.qtQuestionType = qtQuestionType;
    }

    public Question(Question queQuestion)
    {
        this.sQuestion = queQuestion.getQuestion();
        this.lAnswers = queQuestion.getAnswers();
        this.lCourseNames = queQuestion.getCourses();
        this.stQuestionStats = queQuestion.getQuestionStats();
        this.qtQuestionType = queQuestion.getQuestionType();
    }

    public void editQuestion(String sQuestion, ArrayList<String> lAnswers, ArrayList<String> lCourseNames, QuestionType qtQuestionType, QuestionStats stQuestionStats)
    {
        this.sQuestion = sQuestion;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.lCourseNames = new ArrayList<>(lCourseNames);
        this.stQuestionStats = new QuestionStats(stQuestionStats);
        this.qtQuestionType = qtQuestionType;
    }
    public void setQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public void setQuestionType(QuestionType qtQuestionType)
    {
        this.qtQuestionType = qtQuestionType;
    }

    public void setQuestionStats(QuestionStats stQuestionStats)
    {
        this.stQuestionStats = stQuestionStats;
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

    public QuestionType getQuestionType()
    {
        return this.qtQuestionType;
    }

    public QuestionStats getQuestionStats()
    {
        return new QuestionStats(stQuestionStats);
    }

    public ArrayList<String> getAnswers()
    {
        return new ArrayList<>(lAnswers);
    }

    public ArrayList<String> getCourses()
    {
        return new ArrayList<>(lCourseNames);
    }
}
