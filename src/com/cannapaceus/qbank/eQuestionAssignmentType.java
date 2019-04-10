package com.cannapaceus.qbank;

public enum eQuestionAssignmentType {
    TEST("Test", 0),
    QUIZ("Quiz", 1),
    HOMEWORK("Homework", 2),
    OTHER("Other", 3);

    private final String name;

    private final int value;

    private eQuestionAssignmentType(String s, int i) {
        name = s;
        value = i;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public static eQuestionAssignmentType fromInt(int x) {
        switch(x) {
            case 0:
                return TEST;
            case 1:
                return QUIZ;
            case 2:
                return HOMEWORK;
            case 3:
                return OTHER;
        }
        return null;
    }

    public static eQuestionAssignmentType fromStr(String s) {
        switch(s) {
            case "Test":
                return TEST;
            case "Quiz":
                return QUIZ;
            case "Homework":
                return HOMEWORK;
            case "Other":
                return OTHER;
        }
        return null;
    }

    public String toString() {
        return this.name;
    }

    public int getInt() {
        return this.value;
    }
}
