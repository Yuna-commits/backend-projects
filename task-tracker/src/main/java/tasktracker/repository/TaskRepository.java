package tasktracker.repository;

import tasktracker.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskRepository {

    // 프로젝트 루트의 tasks.json에 작업 저장
    private final Path FILE_PATH = Paths.get("tasks.json");

    // tasks.json 파일에서 데이터를 읽고 List<Task>로 반환
    public List<Task> loadTasks() throws IOException {
        // tasks.json 파일이 없으면 빈 리스트 반환
        if(!Files.exists(FILE_PATH)) {
            return new ArrayList<>();
        }

        // 파일이 있으면 내용 읽기
        String json = Files.readString(FILE_PATH); // File Open -> Read -> Close 한 번에 처리

        // 빈 파일이면 빈 리스트 반환
        if(json.isEmpty() || json.equals("[]")) {
            return new ArrayList<>();
        }

        // 대괄호 제거 -> {"id":1, ..., "updatedAt":"2026-01-26T15:30:00"}, {"id":2, ..., "updatedAt":"2026-01-27T15:30:00"}
        if(json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        // 객체 단위로 쪼개기 -> {"id":1, ..., "updatedAt":"2026-01-26T15:30:00"}
        // (?<=}) : 후방탐색 -> 뒤에 닫는 중괄호가 있는지?
        // ,\\s* : 실제 구분자 -> 콤마 하나와 그 뒤의 0개 이상의 공백
        // (?=\{) " 전방탐색 -> 앞에 여는 중괄호가 있는지?
        String[] jsonObjects = json.split("(?<=}),\\s*(?=\\{)");

        return Arrays.stream(jsonObjects)
                .map(JsonUtils::convertJsonToTask)
                .filter(Objects::nonNull) // 파싱 실패한 객체는 제외
                .collect(Collectors.toList());
    }

    // List<Task>를 JSON 문자열로 변환하여 tasks.json 파일에 저장
    public void saveTasks(List<Task> tasks) throws IOException {
        // List<Task> -> JSON 문자열로 변환
        String taskToJson = "[]";
        if(!tasks.isEmpty()) {
            taskToJson = tasks.stream()
                    .map(JsonUtils::convertTaskToJson)
                    // JSON : [{"id":1, ... }, {"id":2, ...}]
                    .collect(Collectors.joining(",", "[", "]"));
        }

        // 파일에 작업 쓰기 -> Files.writeString은 파일이 없으면 자동 생성(TRUNCATE_EXISTING)
        Files.writeString(FILE_PATH, taskToJson); // File Open -> Read -> Close 한 번에 처리
    }

}
