package com.cannapaceus.grader;

public enum eSeason {
    WINTER ("Winter", 0),
    SPRING ("Spring", 1),
    SUMMER ("Summer", 2),
    FALL ("Fall", 3);

    private final String name;

    private final int value;

    private eSeason(String s, int i) {
        name = s;
        value = i;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public int getInt() {
        return this.value;
    }
}
