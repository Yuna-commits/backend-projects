package tasktracker.model;

public enum TaskStatus {

    TODO("todo"), IN_PROGRESS("in_progress"), DONE("done");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
