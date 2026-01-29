package tasktracker.cli;

import tasktracker.exception.NoSuchTaskException;
import tasktracker.model.Task;
import tasktracker.model.TaskStatus;
import tasktracker.service.TaskService;

import java.io.IOException;
import java.util.List;

public class TaskCLI {

    private final TaskService taskService;
    private final TaskListPrinter taskListPrinter;

    public TaskCLI(TaskService taskService) {
        this.taskService = taskService;
        this.taskListPrinter = new TaskListPrinter();
    }

    public void run(String[] args) {
        if(args.length < 1) {
            printHelp();
            return;
        }

        String command = args[0];

        switch(command) {
            case "add" -> add(args);
            case "update" -> update(args);
            case "delete" -> delete(args);
            case "mark-todo" -> markStatus(args, "todo");
            case "mark-in-progress" -> markStatus(args, "in-progress");
            case "mark-done" -> markStatus(args, "done");
            case "list" -> list(args);
            default -> {
                System.out.println("Unknown command: " + command);
                printHelp();
            }
        }
    }

    private void add(String[] args) {
        if(args.length < 2) {
            System.out.println("Error: Description is required.");
            return;
        }
        try {
            int newId = taskService.addTask(args[1]); // args[1] -> "description"
            System.out.printf("Task added successfully (ID: %d)%n", newId);
        } catch (IOException e) {
            System.out.println("Error: System failed to save file. (" + e.getMessage() + ")"); // 파일 에러일 때
        }
    }

    private void update(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: ID and new description are required.");
            return;
        }
        try {
            int id = Integer.parseInt(args[1]); // args[1] -> id

            taskService.updateTask(id, args[2]);
            System.out.printf("Task updated successfully (ID: %d)%n", id);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number."); // 숫자가 아닐 때
        } catch (NoSuchTaskException e) {
            System.out.println(e.getMessage()); // id가 없을 때
        } catch (IOException e) {
            System.out.println("Error: System failed to save file. (" + e.getMessage() + ")"); // 파일 에러일 때
        }
    }

    private void delete(String[] args) {
        if(args.length < 2) {
            System.out.println("Error: ID is required.");
            return;
        }
        try {
            int id = Integer.parseInt(args[1]);

            taskService.deleteTask(id);
            System.out.printf("Task deleted successfully (ID: %d)%n", id);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number."); // 숫자가 아닐 때
        } catch (NoSuchTaskException e) {
            System.out.println(e.getMessage()); // id가 없을 때
        } catch (IOException e) {
            System.out.println("Error: System failed to save file. (" + e.getMessage() + ")"); // 파일 에러일 때
        }
    }

    private void markStatus(String[] args, String status) {
        if(args.length < 2) {
            System.out.println("Error: ID is required.");
            return;
        }
        try {
            int id = Integer.parseInt(args[1]);
            TaskStatus newStatus = TaskStatus.valueOf(status.toUpperCase().replace("-", "_"));

            taskService.markTasksStatus(id, newStatus);
            System.out.printf("Task marked as %s (ID: %d)%n", status, id);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number."); // 숫자가 아닐 때
        } catch (NoSuchTaskException e) {
            System.out.println(e.getMessage()); // id가 없을 때
        } catch (IllegalArgumentException e) { // status가 없을 때
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: System failed to save file. (" + e.getMessage() + ")"); // 파일 에러일 때
        }
    }

    private void list(String[] args) {
        // args[1]이 없으면 전체 조회, 있으면 args[1] 상태의 작업 조회
        String filter = (args.length > 1) ? args[1] : null;
        TaskStatus status = null;

        if(filter != null) {
            try {
                // 입력값("in-progress") -> Enum명("IN_PROGRESS") 변환
                status = TaskStatus.valueOf(filter.toUpperCase().replace("-", "_"));
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid status filter. Use todo, in-progress, or done.");
                return;
            }
        }

        List<Task> tasks = taskService.getTasksByStatus(status);

        if(tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        taskListPrinter.printTaskList(tasks);
    }

    private void printHelp() {
        String help = """
                - add [description]             : Add a new task
                - update [id] [description]     : Update a task
                - delete [id]                   : Delete a task
                - mark-todo [id]                : Mark a task as Todo
                - mark-in-progress [id]         : Mark a task as In-Progress
                - mark-done [id]                : Mark a task as Done
                - list [done|todo|in-progress]  : List tasks (optional filter)
                """;
        System.out.println(help);
    }

}
