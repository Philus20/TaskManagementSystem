package models;

public class Task {
    private String taskName ;
    private String taskId;
    private String taskStatus;
    private String projectId;


    public Task(String aTaskname, String aTaskId, String aTaskStatus, String aProjectId)
    {
        this.taskName = aTaskname;
        this.taskId = aTaskId;
        this.taskStatus = aTaskStatus;
        this.projectId = aProjectId;
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


}
