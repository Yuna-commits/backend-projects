package github_user_activity.network;

import github_user_activity.model.EventType;
import github_user_activity.model.GitHubEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonParser {

    private static final String TYPE_REGEX = "\"type\":\\s*\"([^\"]*)\"";
    private static final String REPO_REGEX = "\"repo\":\\s*.*\"name\":\\s*\"([^\"]*)\"";
    private static final String SIZE_REGEX = "\"size\":\\s*(\\d+)";
    private static final String ACTION_REGEX = "\"action\":\\s*\"([^\"]*)\"";
    private static final String REF_TYPE_REGEX = "\"ref_type\":\\s*\"([^\"]*)\"";

    /**
     * JSON 문자열을 파싱하여 GitHubEvent 리스트로 변환
     *
     * @param json GitHub API로부터 받은 원본 JSON 문자열 (ex: "[{event 1}, {event 2}, ..., {event n}]")
     * @return 파싱된 GitHubEvent 객체 리스트
     */
    public static List<GitHubEvent> fromJsonToEvents(String json) {
        // 1. JSON 배열 양 끝 대괄호 제거
        // ex: "{event 1}, {event 2}, ..., {event n}"
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        // 데이터가 비어있는 경우
        if(json.isBlank()) {
            return List.of();
        }

        // 2. 객체 단위로 문자열 분리
        // 정규식: (?<=}) -> 앞에 '}'가 있고, \s* -> 공백이 0개 이상이며, (?=\{) -> 뒤에 '{'가 있는 콤마(,) 찾기
        // => 객체와 객체 사이를 구분하는 콤마만 타겟팅
        String[] jsonObjects = json.split("(?<=}),\\s*(?=\\{)");

        // 3. 분리된 문자열 배열을 GitHubEvent 객체로 변환하여 리스트로 수집
        return Arrays.stream(jsonObjects)
                .map(JsonParser::parseEvent)
                .collect(Collectors.toList());
    }

    /**
     * 단일 JSON 문자열에서 필요한 필드를 추출하여 GitHubEvent 생성
     *
     * @param json 하나의 이벤트 정보를 담은 JSON 문자열 (ex: "{"type": "PushEvent", ...}")
     * @return 데이터 바인딩된 GitHubEvent 객체
     */
    private static GitHubEvent parseEvent(String json) {
        // 1. 이벤트 타입 추출
        // 정규식: "type": 뒤에 오는 "..." 문자열 캡처
        EventType type = EventType.fromValue(
                extractValue(json, TYPE_REGEX).orElse(""));

        // 2. 리포지토리 이름 추출
        // 정규식: "repo": 뒤에 중첩된 구조를 건너뛰고 "name": 뒤의 "..." 문자열 캡처
        String repoName = extractValue(json, REPO_REGEX)
                .orElse("Unknown Repo");

        // 3. 이벤트 타입에 따른 payload 추출
        String payload =
                switch (type) {
                    // PushEvent: 커밋 개수(size) 추출
                    case PUSH -> extractValue(json, SIZE_REGEX).orElse("1");
                    // IssuesEvent	, PullRequestEvent	: 상태(action) 추출 (ex: opened, closed, merged)
                    case ISSUES, PULL_REQUEST -> extractValue(json, ACTION_REGEX).orElse("");
                    // CreateEvent: 생성된 대상(ref_type) 추출 (ex: branch, repository)
                    case CREATE -> extractValue(json, REF_TYPE_REGEX).orElse("");
                    default -> "";
                };

        return new GitHubEvent(type, repoName, payload);
    }

    /**
     * 정규표현식을 사용하여 JSON 문자열에서 특정 키에 해당하는 값 추출
     *
     * @param json  검색 대상 JSON 문자열
     * @param regex 값을 추출하기 위한 정규표현식
     * @return 추출된 문자열을 담은 Optional
     */
    private static Optional<String> extractValue(String json, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(json);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

}
