package github_user_activity.model;

public record GitHubEvent(
        EventType type, // 이벤트 종류
        String repoName, // 이벤트가 발생한 저장소 이름
        String payload // 이벤트 타입별 가변 데이터
) {}
