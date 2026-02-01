package tasktracker.repository;

import tasktracker.model.Task;
import tasktracker.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    private JsonUtils() {}

    // JSON 문자열 -> Task 객체 : loadTasks
    public static Task convertJsonToTask(String json) {
        // param : {"id":1, "description":"...", ..., "updatedAt":"2026-01-26T15:30:00"}
        String idStr = extractValue(json, "id");
        String description = extractValue(json, "description");
        String statusStr = extractValue(json, "status");
        String createdStr = extractValue(json, "createdAt");
        String updatedStr = extractValue(json, "updatedAt");

        // 필수 값 검증
        if(idStr == null || description == null) {
            return null;
        }

        int id = Integer.parseInt(idStr);

        // 값이 없거나 오류 시 기본값 사용
        TaskStatus status =  (statusStr != null) ? TaskStatus.from(statusStr) : TaskStatus.TODO;
        LocalDateTime createdAt = (createdStr != null) ? LocalDateTime.parse(createdStr) : LocalDateTime.now();
        LocalDateTime updatedAt = (updatedStr != null) ? LocalDateTime.parse(updatedStr) : LocalDateTime.now();

        return new Task(id, description, status, createdAt, updatedAt);
    }

    private static String extractValue(String json, String key) {
        // pattern : "key":"값"(description, date) || "key":숫자(id)
        // \s* : 콜론 뒤에 0개 이상의 공백
        // (?:A|B)
        // "([^"]*)" : "로 시작하고, "가 아닌 문자가 0개 이상 연속되고, "로 끝나는 부분 -> 문자열 매칭
        // ([^,}\s]+) : 콤마, }, 공백이 아닌 문자가 1개 이상 연속되는 부분 -> 숫자 매칭
        String regex = "\"" + key + "\":\\s*(?:\"([^\"]*)\"|([^,}\\s]+))";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);

        if(matcher.find()) {
            if(matcher.group(1) != null) {
                return matcher.group(1);
            } else {
                return matcher.group(2);
            }
        }

        return null;
    }

    /*
    private static String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":"; // "id": || "description": || ...

        int keyIndex = json.indexOf(searchKey);
        if(keyIndex == -1) return null; // 키가 없음

        int startIndex = keyIndex + searchKey.length(); // key 대응하는 값(1)의 시작점
        int endIndex = getEndIndex(json, startIndex);

        if(endIndex == -1) return null; // 값이 없음

        // "status":"done" -> substring(startIndex, endIndex)로 자르면 "done이 반환됨
        // -> enum 변환 실패 에러 발생
        if (json.charAt(startIndex) == '"') {
            return json.substring(startIndex + 1, endIndex);
        }

        // 자른 값 반환
        return json.substring(startIndex, endIndex);
    }

    private static int getEndIndex(String json, int startIndex) {
        int endIndex;

        // 값이 문자열인 경우
        if(json.charAt(startIndex) == '"') {
            // 여는 따옴표 다음부터 닫는 따옴표까지
            endIndex = json.indexOf("\"", startIndex + 1);
        } else { // 값이 숫자인 경우 -> 따옴표 없음
            int commaIndex = json.indexOf(",", startIndex);
            int braceIndex = json.indexOf("}", startIndex);

            if(commaIndex == -1) endIndex = braceIndex; // 콤마로 끝나는 경우
            else if(braceIndex == -1) endIndex = commaIndex; // 닫는 중괄호로 끝나는 경우
            else endIndex = Math.min(commaIndex, braceIndex); // 먼저 나오는 것 선택
        }

        return endIndex;
    }
    */

    // Task 객체 -> JSON 문자열 : saveTasks
    public static String convertTaskToJson(Task task) {
        return "{" +
                "\"id\":" + task.id() + "," +
                "\"description\":\"" + task.description() + "\"," +
                "\"status\":\"" + task.status().getValue() + "\"," +
                "\"createdAt\":\"" + task.createdAt().toString() + "\"," +
                "\"updatedAt\":\"" + task.updatedAt() + "\"" +
                "}";
    }

}
