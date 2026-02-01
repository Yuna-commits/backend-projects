package tasktracker.model;

import java.util.Arrays;

public enum TaskStatus {

    TODO("todo"), IN_PROGRESS("in-progress"), DONE("done");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 입력과 일치하는 enum 반환
    public static TaskStatus from(String text) {
        return Arrays.stream(TaskStatus.values())
                .filter(status -> status.value.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + text));
    }

}
