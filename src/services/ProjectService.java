
package services;

import models.Project;
import models.ProjectStatusReportDto;
import models.Task;
import utils.ConsoleMenu;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProjectService {
    public List<Project> projectsRepository;
    TaskService taskService = new TaskService();
    

    public ProjectService() {
        projectsRepository = new ArrayList<>();
    }


    //Adding new project. It first check if the project id does not exist
    public void addProject(Project project) {
        boolean exists = projectsRepository.stream()
                .anyMatch(p -> p.getId().equals(project.getId()));

        if (!exists) {
            projectsRepository.add(project);
        }
    }


//An endpoint to retrive all the projects
    public List<Project> getAllProjects() {
        return projectsRepository;
    }

    // Filter projects by type (e.g., "Software" or "Hardware")
    public String filterProjectByType(String type) {
        if (projectsRepository == null || type == null) {
            System.out.printf("No records found for %s", type);
            return "Empty";
        }
        ConsoleMenu.displayProjects(projectsRepository.stream()
                .filter(p -> type.equalsIgnoreCase(p.getType()))
                .collect(Collectors.toList()));

         return "Success";
    }


    // Display all projects with their specific attributes
    public void displayAllProjects() {
        ConsoleMenu.displayProjects(projectsRepository);
    }


    // Search projects by budget range
    public String searchByBudgetRange(double min, double max) {
        List<Project> repoData = projectsRepository.stream()
                .filter(p -> p.getBudget() >= min && p.getBudget() <= max)
                .collect(Collectors.toList());

        if (repoData.isEmpty()) {
            System.out.printf("No project budget falls between %.2f and %.2f%n", min, max);
            return "Empty";
        }

        ConsoleMenu.displayProjects(repoData);
        return "Success";
    }

    //get product by id

    public Project getProjectById(String id)
    {
        return projectsRepository.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);

    }

    /**
     * Builds a status report for every project by counting total and completed
     * tasks coming from the provided task service. The method defensively handles
     * null inputs and empty repositories so callers never have to repeat these
     * checks.
     */
    public List<ProjectStatusReportDto> getProjectStatusReport(TaskService sourceTaskService) {
        List<ProjectStatusReportDto> projectStatusReport = new ArrayList<>();

        if (projectsRepository == null || projectsRepository.isEmpty() || sourceTaskService == null) {
            return projectStatusReport;
        }

        for (Project project : projectsRepository) {
            if (project == null || project.getId() == null) {
                continue;
            }

            List<Task> tasks = sourceTaskService.getTasksByProjectId(project.getId());
            int totalTasks = tasks == null ? 0 : tasks.size();

            long completedCount = tasks == null ? 0L : tasks.stream()
                    .filter(t -> t != null && t.getTaskStatus() != null
                            && t.getTaskStatus().equalsIgnoreCase("Completed"))
                    .count();

            projectStatusReport.add(new ProjectStatusReportDto(
                    project.getId(),
                    project.name,
                    totalTasks,
                    (int) completedCount));
        }

        return projectStatusReport;
    }

    public double calculateAverageProjectStatusReport(TaskService sourceTaskService) {
        List<ProjectStatusReportDto> report = getProjectStatusReport(sourceTaskService);
        if (report.isEmpty()) {
            return 0.0;
        }

        int totalTasks = report.stream().mapToInt(r -> r.Tasks).sum();
        if (totalTasks == 0) {
            return 0.0;
        }

        int completedTasks = report.stream().mapToInt(r -> r.Completed).sum();
        return (completedTasks * 100.0) / totalTasks;
    }

    // Assign a user to a project
    public boolean assignUserToProject(String projectId, String userId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            project.assignUser(userId);
            return true;
        }
        return false;
    }

    // Remove a user from a project
    public boolean removeUserFromProject(String projectId, String userId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            project.removeUser(userId);
            return true;
        }
        return false;
    }
}
