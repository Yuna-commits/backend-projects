package github_user_activity.service;

import github_user_activity.exception.UserNotFoundException;
import github_user_activity.model.EventType;
import github_user_activity.model.GitHubEvent;
import github_user_activity.network.GitHubApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ActivityServiceTest {

    // 통신 테스트가 아니기 때문에 GitHubApiClient에는 null 주입
    private final ActivityService activityService = new ActivityService(null);

    @Test
    @DisplayName("IssuesEvent 객체가 주어지면 상태값 첫 글자를 대문자로 변환하여 문장을 만든다")
    void format_IssuesEvent_CapitalizesAction() {
        // Given
        GitHubEvent dummyEvent = new GitHubEvent(EventType.ISSUES, "kamranahmedse/developer-roadmap", "opened");
        List<GitHubEvent> events = List.of(dummyEvent);

        // When
        List<String> formattedMessages = activityService.formatEvents(events);

        // Then
        assertThat(formattedMessages).hasSize(1);
        assertThat(formattedMessages.getFirst()).isEqualTo("- Opened a new issue in kamranahmedse/developer-roadmap");
    }

    @Test
    @DisplayName("정의되지 않은 이벤트(UNKNOWN)가 주어지면 기본 포맷 문장을 반환해야 한다")
    void format_UnknownEvent_ReturnsDefaultMessage() {
        // Given
        GitHubEvent dummyEvent = new GitHubEvent(EventType.UNKNOWN, "kamranahmedse/developer-roadmap", "");

        // When
        List<String> formattedMessages = activityService.formatEvents(List.of(dummyEvent));

        // Then
        assertThat(formattedMessages.getFirst()).isEqualTo("- Unknown event type UNKNOWN");
    }

    @Test
    @DisplayName("ApiClient에서 예외가 발생하면 Service도 동일한 예외를 위로 던져야 한다")
    void getUserActivity_ThrowsException() {
        // Given: 무조건 404 예외를 던지는 ApiClient 생성
        GitHubApiClient badClient = new GitHubApiClient() {
            @Override
            public String fetchUserActivity(String username) {
                throw new UserNotFoundException("User not found");
            }
        };

        ActivityService serviceWithBadClient = new ActivityService(badClient);

        // When & Then: AssertJ의 예외 검증 문법 (실행했을 때 이 예외가 터져야 성공)
        assertThatThrownBy(() -> serviceWithBadClient.getUserActivity("ghost-user"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

}
