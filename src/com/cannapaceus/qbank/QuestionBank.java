package com.cannapaceus.qbank;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.DBService;

import java.util.ArrayList;

public class QuestionBank {
    private static QuestionBank ourInstance = new QuestionBank();

    public static QuestionBank getInstance() {
        return ourInstance;
    }

    private DBService db;

    private ArrayList<Question> lQuestions;

    private QuestionBank() {
        this.lQuestions = new ArrayList<>();
        db = DBService.getInstance();
    }

    public void addQuestion(Question qQuestion)
    {
        lQuestions.add(qQuestion);
    }

    public ArrayList<Question> getQuestionList()
    {
        return new ArrayList<>(this.lQuestions);
    }

    public ArrayList<Question> getQuestionList(Course c) {
        ArrayList<Question> toReturn = new ArrayList<>();
//        for (Question que : lQuestions) {
//            String sCourse = que.getCourse();
//                if (sCourse == sCourseName) {
//                    toReturn.add(que);
//                }
//            }

        return toReturn;
    }

    public ArrayList<Question> getQuestionList(eQuestionType qtQuestionType)
    {
        ArrayList<Question> toReturn = new ArrayList<>();
//        for (Question que : lQuestions) {
//            if(que.getQuestionType() == qtQuestionType)
//            {
//                toReturn.add(que);
//            }
//        }

        return toReturn;
    }
}
