package com.cannapaceus.qbank;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.ArrayList;

public class Question extends RecursiveTreeObject<Question> {

    private long lDBID;
    //Name of the question.
    private String sQuestion;
    //Array of potential answers
    private ArrayList<String> lAnswers;

    //Enum to sub for lack of structs
    private eQuestionType qtQuestionType;
    private eQuestionAssignmentType qatQuestionAssignmentType;
    private eQuestionLevel qlQuestionLevel;
    private float fToDoTime;
    private float fScore;

    public Question(String sQuestion, float fScore, ArrayList<String> lAnswers, eQuestionType qtQuestionType, eQuestionAssignmentType qatQuestionAssignmentType, eQuestionLevel qlQuestionLevel, float fToDoTime)
    {
        this.sQuestion = sQuestion;
        this.fScore = fScore;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.qtQuestionType = qtQuestionType;
        this.qatQuestionAssignmentType = qatQuestionAssignmentType;
        this.qlQuestionLevel = qlQuestionLevel;
        this.fToDoTime = fToDoTime;
    }

    public Question(Question queQuestion)
    {
        this.sQuestion = queQuestion.getQuestion();
        this.fScore = queQuestion.getScore();
        this.lAnswers = queQuestion.getAnswers();
        this.qtQuestionType = queQuestion.getQuestionType();
        this.qatQuestionAssignmentType = queQuestion.getQuestionAssignmentType();
        this.qlQuestionLevel = queQuestion.getQuestionLevel();
        this.fToDoTime = queQuestion.getToDoTime();
    }

    public void editQuestion(String sQuestion, float fScore, ArrayList<String> lAnswers, eQuestionType qtQuestionType, eQuestionAssignmentType qatQuestionAssignmentType, eQuestionLevel qlQuestionLevel, float fToDoTime)
    {
        this.sQuestion = sQuestion;
        this.fScore = fScore;
        this.lAnswers = new ArrayList<>(lAnswers);
        this.qtQuestionType = qtQuestionType;
        this.qatQuestionAssignmentType = qatQuestionAssignmentType;
        this.qlQuestionLevel = qlQuestionLevel;
        this.fToDoTime = fToDoTime;
    }

    public void setlDBID(long lDBID) {
        this.lDBID = lDBID;
    }

    public void setQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public void setQuestionType(eQuestionType qtQuestionType)
    {
        this.qtQuestionType = qtQuestionType;
    }

    public void setQuestionAssignmentType(eQuestionAssignmentType qatQuestionAssignmentType) { this.qatQuestionAssignmentType = qatQuestionAssignmentType; }

    public void setQlQuestionLevel(eQuestionLevel qlQuestionLevel) { this.qlQuestionLevel = qlQuestionLevel; }

    public void setfToDoTime (float fToDoTime) { this.fToDoTime = fToDoTime; }

    public void setfScore(float fScore) {
        this.fScore = fScore;
    }

    public void setAnwsers(ArrayList<String> lAnswers)
    {
        if(this.qtQuestionType == eQuestionType.MULTIPLECHOICE)
            this.lAnswers = new ArrayList<>(lAnswers);
    }

    public long getlDBID() {
        return this.lDBID;
    }

    public String getQuestion()
    {
        return new String(this.sQuestion);
    }

    public float getScore() { return this.fScore; }

    public eQuestionType getQuestionType()
    {
        return this.qtQuestionType;
    }

    public eQuestionAssignmentType getQuestionAssignmentType () { return  this.qatQuestionAssignmentType; }

    public eQuestionLevel getQuestionLevel () { return  this.qlQuestionLevel; }

    public float getToDoTime () { return  this.fToDoTime; }

    public float getfScore() {
        return fScore;
    }

    public ArrayList<String> getAnswers()
    {
        if(this.qtQuestionType == eQuestionType.MULTIPLECHOICE)
            return new ArrayList<>(lAnswers);

        return null;
    }
}
