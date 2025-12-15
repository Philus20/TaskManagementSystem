package models;

public class Task {
    private String taskName;
    private String taskId;
    private String taskStatus;
    private String projectId;
    private String assignedUserId;

    /**
     * Constructor - ID should be set by service layer using IdGenerator
     * Following Dependency Inversion Principle
     */
    public Task(String taskName, String taskStatus, String projectId) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.projectId = projectId;
        this.assignedUserId = null;
        // ID will be set by service layer
    }

    public Task(String taskName, String taskStatus, String projectId, String assignedUserId) {
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.projectId = projectId;
        this.assignedUserId = assignedUserId;
        // ID will be set by service layer
    }
    public void setTaskStatus (String status){
        taskStatus=status;
    }
    public String getTaskName() {
        return taskName;
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public String getProjectId() {
        return projectId;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

}
