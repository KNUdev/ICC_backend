package common;

public enum ApplicationStatus {
    IN_QUEUE("В черзі"),
    IN_WORK("В роботі"),
    REJECTED("Відхилено"),
    DONE("Виконано");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
