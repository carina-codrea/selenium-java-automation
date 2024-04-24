package codrea.enums;

public enum Occupation {
    DOCTOR("Doctor"),
    STUDENT("Student"),
    SCIENTIST("Scientist"),
    ENGINEER("Engineer");

    private final String displayName;

    Occupation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}