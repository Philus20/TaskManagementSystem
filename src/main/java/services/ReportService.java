package services;

import interfaces.IReporting;
import models.Project;
import models.ProjectStatusReportDto;
import models.Task;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportService implements IReporting {

    private final TaskService taskService;
    private final ProjectService projectService;

    // Constructor injection -> loose coupling
    public ReportService(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    /**
     * Generates a status report per project:
     * For every project, counts total tasks and completed tasks,
     * builds a ProjectStatusReportDto[] and displays it.
     */

    // build the report and return it as an array (arrays-only)
    private List<ProjectStatusReportDto> buildReport() {
        List<Project> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProjectStatusReportDto >temp = new ArrayList<>();
        int count = 0;

        for (Project p : projects) {
            if (p == null) continue;
            List<Task>  tasks = taskService.getTasksByProjectId(p.getId());
            int total = (tasks.isEmpty()) ? 0 : tasks.size();
            int completed = 0;
            if (!tasks.isEmpty()) {
                for (Task t : tasks) {
                    if (t != null && "Completed".equalsIgnoreCase(t.getTaskStatus())) {
                        completed++;
                    }
                }
            }
            temp.add(count++,new ProjectStatusReportDto(p.getId(), p.getName(), total, completed)) ;
        }

        return temp;
    }

    @Override
    public void generateReport() {
        List<ProjectStatusReportDto> report = buildReport();
        displayReport(report);

        // print overall average completion
        double avg = calculateAverageProjectStatusReport();
        System.out.printf("Overall completion: %.2f%%%n", avg);
    }

    // parameterless, uses injected taskService/projectService via buildReport()
    public double calculateAverageProjectStatusReport() {
        List<ProjectStatusReportDto> report = buildReport();
        if (report.isEmpty()) return 0.0;

        int totalTasks = 0;
        int totalCompleted = 0;

        for (ProjectStatusReportDto dto : report) {
            if (dto == null) continue;
            totalTasks += dto.tasks();       // use accessor methods
            totalCompleted += dto.completed();
        }

        if (totalTasks == 0) return 0.0;
        return (totalCompleted * 100.0) / totalTasks;
    }


    // Helper that prints report to console (loose UI coupling)
    private void displayReport(List<ProjectStatusReportDto> report) {
        if (report.isEmpty()) {
            System.out.println("No report data.");
            return;
        }

        System.out.println("Project Status Report");
        System.out.println("---------------------");
        for (ProjectStatusReportDto dto : report) {
            if (dto == null) continue;
            System.out.printf("Project: %s | Name: %s | Total Tasks: %d | Completed: %d%n",
                    dto.projectId(), dto.projectName(), dto.tasks(), dto.completed());
        }
    }



}
