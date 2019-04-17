package com.cannapaceus.qbank;

public enum eQuestionLevel {
    EASY("Easy", 0),
    MEDIUM("Medium", 1),
    HARD("Hard", 2),
    HOTS("Higher Order Thinking Skills", 3);

    private final String name;

    private final int value;

    private eQuestionLevel(String s, int i) {
        name = s;
        value = i;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public static eQuestionLevel fromInt(int x) {
        switch(x) {
            case 0:
                return EASY;
            case 1:
                return MEDIUM;
            case 2:
                return HARD;
            case 3:
                return HOTS;
        }
        return null;
    }

    public static eQuestionLevel fromStr(String s) {
        switch(s) {
            case "Easy":
                return EASY;
            case "Medium":
                return MEDIUM;
            case "Hard":
                return HARD;
            case "Higher Order Thinking Skills":
                return HOTS;
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
