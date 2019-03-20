package com.cannapaceus.grader;

public enum eSeason {
    WINTER ("Winter"),
    SPRING ("Spring"),
    SUMMER ("Summer"),
    FALL ("Fall");

    private final String name;

    private eSeason(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
