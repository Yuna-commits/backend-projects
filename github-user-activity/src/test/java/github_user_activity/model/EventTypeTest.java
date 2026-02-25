package github_user_activity.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTypeTest {

    @Test
    @DisplayName("정확한 이벤트 이름이 들어오면 해당 Enum을 반환한다")
    void fromValue_ExactMatch() {
        assertThat(EventType.fromValue("PushEvent")).isEqualTo(EventType.PUSH);
        assertThat(EventType.fromValue("IssuesEvent")).isEqualTo(EventType.ISSUES);
    }

    @Test
    @DisplayName("대소문자가 섞여 있어도 무시하고 알맞은 Enum을 찾아 반환한다")
    void fromValue_IgnoreCase() {
        // API 응답이 "pushevent"나 "PUSHevent"로 오더라도 PUSH로 인식해야 함
        assertThat(EventType.fromValue("pushevent")).isEqualTo(EventType.PUSH);
        assertThat(EventType.fromValue("CREATEEVENT")).isEqualTo(EventType.CREATE);
    }

    @Test
    @DisplayName("정의되지 않은 이상한 이벤트 이름이 들어오면 UNKNOWN을 반환한다")
    void fromValue_UnknownEvent() {
        // 방어적 프로그래밍이 잘 동작하는지 확인
        assertThat(EventType.fromValue("GollumEvent")).isEqualTo(EventType.UNKNOWN);
        assertThat(EventType.fromValue("")).isEqualTo(EventType.UNKNOWN);
        assertThat(EventType.fromValue(null)).isEqualTo(EventType.UNKNOWN); // NullPointerException 방어 확인
    }

}
