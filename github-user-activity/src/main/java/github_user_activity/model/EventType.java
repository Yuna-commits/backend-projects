package github_user_activity.model;

import java.util.Arrays;

public enum EventType {

    PUSH("PushEvent"),
    ISSUES("IssuesEvent"),
    WATCH("WatchEvent"),
    CREATE("CreateEvent"),
    PULL_REQUEST("PullRequestEvent"),
    UNKNOWN("Unknown"); // 정의되지 않는 이벤트 수신인 경우

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    // 문자열(대소문자 구분 x)이 들어오면 알맞은 enum 상수로 반환
    public static EventType fromValue(String text) {
        return Arrays.stream(EventType.values())
                .filter(type -> type.value.equalsIgnoreCase(text))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
