package github_user_activity.service;

import github_user_activity.model.EventType;
import github_user_activity.model.GitHubEvent;
import github_user_activity.network.GitHubApiClient;
import github_user_activity.network.JsonParser;

import java.util.List;
import java.util.stream.Collectors;

public class ActivityService {

    private final GitHubApiClient apiClient;

    public ActivityService(GitHubApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * GitHub API를 호출하고 결과를 파싱하여 반환
     *
     * @param username 조회할 대상의 GitHub 사용자 이름
     * @return 파싱 완료된 GitHubEvent 객체 리스트
     * @throws Exception CLI 계층으로 예외 위임
     */
    public List<GitHubEvent> getUserActivity(String username) throws Exception {
        // 1. GitHubApiClient를 통해 원시 JSON 문자열 확보
        String json = apiClient.fetchUserActivity(username);
        // 2. JsonParser를 통해 Java 객체(리스트)로 변환
        return JsonParser.fromJsonToEvents(json);
    }

    /**
     * 추출된 GitHubEvent 객체 리스트를 문자열 리스트로 변환
     *
     * @param events 가공할 GitHubEvent 객체 리스트
     * @return 출력용 포맷으로 완성된 문자열 리스트
     */
    public List<String> formatEvents(List<GitHubEvent> events) {
        // 개별 이벤트를 문자열로 매핑한 뒤 리스트로 반환
        return events.stream()
                .map(this::formatSingleEvent)
                .collect(Collectors.toList());
    }

    private String formatSingleEvent(GitHubEvent event) {
        // 이벤트 타입별 출력 포맷 분기
        return switch (event.type()) {
            case PUSH -> "- Pushed " + event.payload() + " commits to " + event.repoName();
            case ISSUES -> "- " + capitalize(event.payload()) + " a new issue in " + event.repoName();
            case WATCH -> "- Starred " + event.repoName();
            case CREATE -> "- Created " + event.payload() + " in " + event.repoName();
            case PULL_REQUEST -> "- " + capitalize(event.payload()) + " a pull request in " + event.repoName();
            default -> "- Unknown event type " + event.type();
        };
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

}
