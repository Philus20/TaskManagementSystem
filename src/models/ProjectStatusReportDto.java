
package models;

public class ProjectStatusReportDto {
    public String projectId;
    public String projectName;
    public int Tasks;
    public int Completed;

    // Constructor
    public ProjectStatusReportDto(String projectId, String projectName, int Tasks, int Completed) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.Tasks = Tasks;
        this.Completed = Completed;
    }

    
   
}
