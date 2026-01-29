package tasktracker.service;

import tasktracker.exception.NoSuchTaskException;
import tasktracker.model.Task;
import tasktracker.model.TaskStatus;
import tasktracker.repository.TaskRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;
    private final List<Task> tasks;

    public TaskService(TaskRepository taskRepository) throws IOException {
        this.taskRepository = taskRepository;
        this.tasks = taskRepository.loadTasks();
    }

    // 작업 추가
    public int addTask(String description) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        // 새로운 ID 생성 (기존 ID 최댓값 + 1)
        int newId = tasks.stream()
                .mapToInt(Task::id)
                .max()
                .orElse(0) + 1;

        // 새 Task 객체 생성 및 리스트에 추가
        Task task = new Task(newId, description, TaskStatus.TODO, now, now);
        tasks.add(task);

        taskRepository.saveTasks(tasks);

        return task.id();
    }

    // 작업 수정
    public void updateTask(int id, String newDescription) throws IOException {
        // 리스트에서 해당 id를 가진 Task 찾기
        Task originalTask = findTaskById(id);

        // Task는 Record 클래스이기 때문에 수정 불가 -> 새로운 객체 생성
        Task updatedTask = new Task(
                originalTask.id(),
                newDescription,
                originalTask.status(),
                originalTask.createdAt(),
                LocalDateTime.now()
        );

        // 수정 전 작업 삭제
        tasks.remove(originalTask);
        tasks.add(updatedTask);

        taskRepository.saveTasks(tasks);
    }

    // 작업 삭제
    public void deleteTask(int id) throws IOException {
        // 리스트에서 해당 id의 Task 삭제
        tasks.remove(findTaskById(id));
        taskRepository.saveTasks(tasks);
    }

    // 작업 상태 변경
    public void markTasksStatus(int id, TaskStatus newStatus) throws IOException {
        Task originalTask = findTaskById(id);

        Task changedStatusTask = new Task(
                originalTask.id(),
                originalTask.description(),
                newStatus,
                originalTask.createdAt(),
                LocalDateTime.now()
        );

        tasks.remove(originalTask);
        tasks.add(changedStatusTask);

        taskRepository.saveTasks(tasks);
    }

    // 작업 리스트 반환
    public List<Task> getTasksByStatus(TaskStatus status) {
        return tasks.stream()
                // status가 null이면 모든 작업 통과, 아니면 상태 일치 확인
                .filter(task -> status == null || task.status().equals(status))
                .sorted()
                .toList();
    }

    private Task findTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchTaskException(String.format("Task with ID %d not found", id)));
    }

}
