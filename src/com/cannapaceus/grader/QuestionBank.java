package com.cannapaceus.grader;

import java.util.ArrayList;

public class QuestionBank {
    private static QuestionBank ourInstance = new QuestionBank();

    public static QuestionBank getInstance() {
        return ourInstance;
    }

    ArrayList<Question> lQuestions;

    private QuestionBank() {
    }
}
