package services;

import models.ProjectStatusReportDto;

import java.util.List;

public class ReportService {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ReportService(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

   

    // Return total tasks and completed tasks for each project
    public List<ProjectStatusReportDto> getProjectStatusReport() {
        return projectService.getProjectStatusReport(taskService);
    }

    // Average completion across all projects (guarded against divide-by-zero)
    public double calculateAverageProjectStatusReport() {
        return projectService.calculateAverageProjectStatusReport(taskService);
    }


}
