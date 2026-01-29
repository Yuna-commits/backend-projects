package tasktracker.model;

import java.time.LocalDateTime;

/**
 * @param status "todo", "in_progress", "done"
 */
public record Task(int id, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt)
implements Comparable<Task> {

    public boolean isUpdated() {
        return updatedAt.isAfter(createdAt);
    }

    @Override
    public String toString() {
        return String.format("""
                        {
                        "id": %d
                        "description": %s
                        "status": %s
                        "createdAt": %s
                        "updatedAt": %s
                        }""",
                this.id,
                this.description,
                this.status.getStatus(),
                this.createdAt.toString(),
                isUpdated() ? this.updatedAt.toString() : "N/A"
        );
    }

    @Override
    public int compareTo(Task other) {
        // id 기준 오름차순 정렬
        return Integer.compare(this.id, other.id);
    }
}
