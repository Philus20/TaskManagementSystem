package models;

public class Task {
    private String taskName ;
    private String taskId;
    private String taskStatus;
    private String projectId;
    private static int idCounter = 1;


    public Task(String aTaskname, String aTaskStatus, String aProjectId)
    {
        this.taskName = aTaskname;
        this.taskId =  "T" + String.format("%04d", idCounter++);;
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
