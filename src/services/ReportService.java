package services;

import interfaces.IReporting;
import models.Project;
import models.ProjectStatusReportDto;
import models.Task;

import java.util.Arrays;

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
    private ProjectStatusReportDto[] buildReport() {
        Project[] projects = projectService.getAllProjects();
        if (projects == null || projects.length == 0) {
            return new ProjectStatusReportDto[0];
        }

        ProjectStatusReportDto[] temp = new ProjectStatusReportDto[projects.length];
        int count = 0;

        for (Project p : projects) {
            if (p == null) continue;
            Task[] tasks = taskService.getTasksByProjectId(p.getId());
            int total = (tasks == null) ? 0 : tasks.length;
            int completed = 0;
            if (tasks != null) {
                for (Task t : tasks) {
                    if (t != null && "Completed".equalsIgnoreCase(t.getTaskStatus())) {
                        completed++;
                    }
                }
            }
            temp[count++] = new ProjectStatusReportDto(p.getId(), p.getName(), total, completed);
        }

        return Arrays.copyOf(temp, count);
    }

    @Override
    public void generateReport() {
        ProjectStatusReportDto[] report = buildReport();
        displayReport(report);

        // print overall average completion
        double avg = calculateAverageProjectStatusReport();
        System.out.printf("Overall completion: %.2f%%%n", avg);
    }

    // parameterless, uses injected taskService/projectService via buildReport()
    public double calculateAverageProjectStatusReport() {
        ProjectStatusReportDto[] report = buildReport();
        if (report == null || report.length == 0) return 0.0;

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
    private void displayReport(ProjectStatusReportDto[] report) {
        if (report == null || report.length == 0) {
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
