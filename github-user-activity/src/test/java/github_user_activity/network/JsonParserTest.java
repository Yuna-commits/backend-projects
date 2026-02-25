package github_user_activity.network;

import github_user_activity.model.EventType;
import github_user_activity.model.GitHubEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonParserTest {

    @Test
    @DisplayName("정상적인 PushEvent JSON이 주어지면 객체로 올바르게 파싱되어야 한다")
    void parse_PushEvent() {// 가독성을 위해 언더스코어 사용
        // Given (준비)
        String dummyJson = "[{" +
                "\"type\": \"PushEvent\", " +
                "\"repo\": {\"name\": \"kamranahmedse/developer-roadmap\"}, " +
                "\"payload\": {\"size\": 3}" +
                "}]";

        // When (실행)
        List<GitHubEvent> events = JsonParser.fromJsonToEvents(dummyJson);

        // Then (검증)
        // 1. 리스크 크기가 1인지 확인
        assertThat(events).hasSize(1);

        // 2. 파싱된 첫 번째 객체의 값들이 기대한 값인지 확인
        GitHubEvent event = events.getFirst();
        assertThat(event.type()).isEqualTo(EventType.PUSH);
        assertThat(event.repoName()).isEqualTo("kamranahmedse/developer-roadmap");
        assertThat(event.payload()).isEqualTo("3");
    }

    @Test
    @DisplayName("정상적인 CreateEvent JSON이 주어지면 ref_type을 payload로 파싱해야 한다")
    void parse_CreateEvent() {
        // Given (준비)
        String createJson = "[{" +
                "\"type\": \"CreateEvent\", " +
                "\"repo\": {\"name\": \"kamranahmedse/developer-roadmap\"}, " +
                "\"payload\": {\"ref_type\": \"branch\", \"master_branch\": \"main\"}" +
                "}]";

        // When (실행)
        List<GitHubEvent> events = JsonParser.fromJsonToEvents(createJson);

        // Then (검증)
        GitHubEvent event = events.getFirst();
        assertThat(event.type()).isEqualTo(EventType.CREATE);
        assertThat(event.payload()).isEqualTo("branch"); // ref_type인 "branch"가 뽑혀야 함
    }

    @Test
    @DisplayName("최근 활동이 없는 유저의 빈 배열([])이 주어지면 빈 리스트를 반환해야 한다")
    void parse_EmptyArray_ReturnsEmptyList() {
        // Given (준비)
        String emptyJson = "[]";

        // When (실행)
        List<GitHubEvent> events = JsonParser.fromJsonToEvents(emptyJson);

        // Then (검증)
        // 에러 없이 사이즈가 0인 리스트가 나오는지 확인
        assertThat(events).isEmpty();
    }

}
