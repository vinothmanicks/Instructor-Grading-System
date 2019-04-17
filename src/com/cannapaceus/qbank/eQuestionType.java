package com.cannapaceus.qbank;

public enum eQuestionType {
    MULTIPLECHOICE ("Multiple Choice", 0),
    FILLINTHEBLANK("Fill in the Blank", 1),
    TRUEORFALSE("True or False", 2),
    SHORTANSWER("Short Answer", 3),
    LONGANSWER("Long Answer", 4);

    private final String name;

    private final int value;

    private eQuestionType(String s, int i) {
        name = s;
        value = i;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public static eQuestionType fromInt(int x) {
        switch(x) {
            case 0:
                return MULTIPLECHOICE;
            case 1:
                return FILLINTHEBLANK;
            case 2:
                return TRUEORFALSE;
            case 3:
                return SHORTANSWER;
            case 4:
                return LONGANSWER;
        }
        return null;
    }

    public static eQuestionType fromStr(String s) {
        switch(s) {
            case "Multiple Choice":
                return MULTIPLECHOICE;
            case "Fill in the Blank":
                return FILLINTHEBLANK;
            case "True or False":
                return TRUEORFALSE;
            case "Short Answer":
                return SHORTANSWER;
            case "Long Answer":
                return LONGANSWER;
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
