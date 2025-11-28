package services;

import models.Project;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public class ReportService {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ReportService(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    public static class ProjectReportDTO {
        public String projectId;
        public String projectName;
        public int totalTasks;
        public int completedTasks;
        public double completionPercentage;
    }

    public List<ProjectReportDTO> getProjectReports() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectReportDTO> reportData = new ArrayList<>();

        for (Project project : projects) {
            List<Task> tasks = taskService.getTasksByProjectId(project.getId());
            int totalTasks = tasks.size();
            long completed = tasks.stream()
                    .filter(t -> t.getTaskStatus().equalsIgnoreCase("Completed"))
                    .count();

            double percent = totalTasks == 0 ? 0.0 : (completed * 100.0 / totalTasks);

            ProjectReportDTO entry = new ProjectReportDTO();
            entry.projectId = project.getId();
            entry.projectName = project.name;
            entry.totalTasks = totalTasks;
            entry.completedTasks = (int) completed;
            entry.completionPercentage = percent;

            reportData.add(entry);
        }

        return reportData;
    }


    public double getAverageCompletionPercentage() {
        List<ProjectReportDTO> list = getProjectReports();
        if (list.isEmpty()) return 0.0;

        return list.stream()
                .mapToDouble(r -> r.completionPercentage)
                .average()
                .orElse(0.0);
    }
}
