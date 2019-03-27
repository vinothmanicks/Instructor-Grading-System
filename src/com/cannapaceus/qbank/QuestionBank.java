package com.cannapaceus.qbank;

import java.util.ArrayList;

public class QuestionBank {
    private static QuestionBank ourInstance = new QuestionBank();

    public static QuestionBank getInstance() {
        return ourInstance;
    }

    private ArrayList<Question> lQuestions;

    private QuestionBank() {
        this.lQuestions = new ArrayList<>();
    }

    public void addQuestion(Question qQuestion)
    {
        lQuestions.add(qQuestion);
    }

    public ArrayList<Question> getQuestionList()
    {
        return new ArrayList<>(this.lQuestions);
    }

    public ArrayList<Question> getQuestionList(String sCourseName) {
        ArrayList<Question> toReturn = new ArrayList<>();
        for (Question que : lQuestions) {
            String sCourse = que.getCourse();
                if (sCourse == sCourseName) {
                    toReturn.add(que);
                }
            }

        return toReturn;
    }

    public ArrayList<Question> getQuestionList(QuestionType qtQuestionType)
    {
        ArrayList<Question> toReturn = new ArrayList<>();
        for (Question que : lQuestions) {
            if(que.getQuestionType() == qtQuestionType)
            {
                toReturn.add(que);
            }
        }

        return toReturn;
    }
}
