package tasktracker;

import tasktracker.cli.TaskCLI;
import tasktracker.repository.TaskRepository;
import tasktracker.service.TaskService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Repository -> Service -> CLI 의존성 조립
        new TaskCLI(new TaskService(new TaskRepository())).run(args);
    }
}