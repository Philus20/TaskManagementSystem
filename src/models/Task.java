package models;

public class Task {
    private String taskName ;
    private String taskId;
    private String taskStatus;
    private String projectId;
    private String assignedUserId;
    private static int idCounter = 1;


    public Task(String aTaskname, String aTaskStatus, String aProjectId)
    {
        this.taskName = aTaskname;
        this.taskId =  "T" + String.format("%04d", idCounter++);;
        this.taskStatus = aTaskStatus;
        this.projectId = aProjectId;
        this.assignedUserId = null;
    }

    public Task(String aTaskname, String aTaskStatus, String aProjectId, String assignedUserId)
    {
        this.taskName = aTaskname;
        this.taskId =  "T" + String.format("%04d", idCounter++);;
        this.taskStatus = aTaskStatus;
        this.projectId = aProjectId;
        this.assignedUserId = assignedUserId;
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
