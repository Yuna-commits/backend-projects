package tasktracker.cli;

import tasktracker.model.Task;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskListPrinter {

    private static final String BORDER = "+-------+------------------------------------------+--------------+------------------+------------------+";
    private static final String HEADER = "| ID    | Description                              | Status       | Created At     | Updated At     |";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String TASK_FORMAT = "| %-5s | %-40s | %-12s | %-16s | %-16s |%n";
    private static final int DESCRIPTION_MAX_LENGTH = 40;

    public void printTaskList(List<Task> tasks) {
        System.out.println(BORDER);
        System.out.println(HEADER);
        System.out.println(BORDER);

        tasks.forEach(task -> System.out.printf(
                TASK_FORMAT,
                task.id(),
                truncate(task.description()),
                task.status().getStatus(),
                task.createdAt().format(DATE_FORMATTER),
                task.updatedAt().format(DATE_FORMATTER)
        ));

        System.out.println(BORDER);
    }

    private String truncate(String description) {
        if(description.length() > DESCRIPTION_MAX_LENGTH) {
            return description.substring(0, DESCRIPTION_MAX_LENGTH - 3) + "...";
        }
        return description;
    }

}
