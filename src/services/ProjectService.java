// services/ProjectService.java
package services;

import interfaces.IdGenerator;
import models.Project;
import models.ProjectStatusReportDto;
import models.Task;
import utils.ConsoleMenu;

import java.util.Arrays;

public class ProjectService {

    // project storage (index = numeric part of ID - 1)
    private Project[] projectsRepository;
    private static final IdGenerator projectIdGenerator = new GenerateProjectId();

    // parallel assignment storage:
    // assignments[index] is a fixed-size String[] for that project (size = team's capacity)
    // sizes[index] stores how many entries in assignments[index] are used (packed on left)
    private String[][] assignments;
    private int[] sizes;

    private final TaskService taskService = new TaskService();



    // Helper: parse "P0004" -> 4
    private int getProjectNumber(String id) {
        return Integer.parseInt(id.substring(1));
    }
    public ProjectService(int capacity) {
        if (capacity <= 0) capacity = 10;
        projectsRepository = new Project[capacity];     // <- use capacity here
        assignments = new String[capacity][];
        sizes = new int[capacity];
    }

    private void ensureCapacityForIndex(int index) {
        if (index < projectsRepository.length) return;

        int newCap = Math.max(projectsRepository.length * 2, 1);
        while (newCap <= index) newCap *= 2;

        projectsRepository = Arrays.copyOf(projectsRepository, newCap);
        assignments = Arrays.copyOf(assignments, newCap);
        sizes = Arrays.copyOf(sizes, newCap);
    }


    // Add a project (store in array slot based on ID number)
    public void addProject(Project project) {
        if (project == null) return;

        String ProjectId = projectIdGenerator.generate();
        int index = getProjectNumber(ProjectId) - 1;
        if (index < 0) return;

        ensureCapacityForIndex(index);

        if (projectsRepository[index] != null) {
            System.out.println("Project with ID " + project.getId() + " already exists.");
            return;
        }

        project.setId(ProjectId);
        projectsRepository[index] = project;

        // allocate the assignment array for this project based on its team size
        int teamSize = Math.max(1, project.getTeamSize());
        assignments[index] = new String[teamSize];
        sizes[index] = 0;

        System.out.println("Project " + project.getId() + " added at index " + index);
    }

    // Get project by id
    public Project getProjectById(String id) {
        if (id == null) return null;
        int idx = getProjectNumber(id) - 1;
        if (idx < 0 || idx >= projectsRepository.length) return null;
        return projectsRepository[idx];
    }

    // Assign a user to a project — returns true if assigned
    public boolean assignUserToProject(String projectId, String userId) {
        if (projectId == null || userId == null) return false;
        int idx = getProjectNumber(projectId) - 1;
        if (idx < 0 || idx >= projectsRepository.length) return false;
        if (projectsRepository[idx] == null) return false; // project missing

        String[] row = assignments[idx];
        int size = sizes[idx];

        // check duplicate among used slots
        for (int i = 0; i < size; i++) {
            if (userId.equals(row[i])) return false; // already assigned
        }

        // check capacity
        if (size >= row.length) return false; // full

        // insert at packed position
        row[size] = userId;
        sizes[idx] = size + 1;
        return true;
    }

    // Remove a user from a project — returns true if removed
    public boolean removeUserFromProject(String projectId, String userId) {
        if (projectId == null || userId == null) return false;
        int idx = getProjectNumber(projectId) - 1;
        if (idx < 0 || idx >= projectsRepository.length) return false;
        if (projectsRepository[idx] == null) return false;

        String[] row = assignments[idx];
        int size = sizes[idx];

        for (int i = 0; i < size; i++) {
            if (userId.equals(row[i])) {
                // shift left to keep packed
                for (int j = i; j < size - 1; j++) {
                    row[j] = row[j + 1];
                }
                row[size - 1] = null;
                sizes[idx] = size - 1;
                return true;
            }
        }
        return false;
    }

    // Return a trimmed array of assigned users (no nulls)
    public String[] getAssignedUsers(String projectId) {
        if (projectId == null) return new String[0];
        int idx = getProjectNumber(projectId) - 1;
        if (idx < 0 || idx >= projectsRepository.length) return new String[0];
        if (projectsRepository[idx] == null) return new String[0];

        int size = sizes[idx];
        if (size == 0) return new String[0];

        return Arrays.copyOf(assignments[idx], size);
    }



    // Compact version: return only non-null projects
    public Project[] getAllProjectsComplete() {
        int count = 0;
        for (Project p : projectsRepository) if (p != null) count++;
        Project[] result = new Project[count];
        int j = 0;
        for (Project p : projectsRepository) {
            if (p != null) result[j++] = p;
        }
        return result;
    }

    public  void displayAllProjects() {
        ConsoleMenu.displayProjects(getAllProjectsComplete());
    }

    // Filter projects by type (arrays-only) and display
    public void filterProjectByType(String type) {
        if (type == null) {
            System.out.println("No records found for null type");
            return;
        }

        Project[] temp = new Project[projectsRepository.length];
        int count = 0;
        for (Project p : projectsRepository) {
            if (p == null) continue;
            if (type.equalsIgnoreCase(p.getType())) temp[count++] = p;
        }
        if (count == 0) {
            System.out.printf("No records found for %s%n", type);
            return;
        }
        Project[] result = Arrays.copyOf(temp, count);
        ConsoleMenu.displayProjects(result);
    }

    // Search by budget range and display (arrays-only)
    public void searchByBudgetRange(double min, double max) {
        Project[] temp = new Project[projectsRepository.length];
        int count = 0;
        for (Project p : projectsRepository) {
            if (p == null) continue;
            double b = p.getBudget();
            if (b >= min && b <= max) temp[count++] = p;
        }
        if (count == 0) {
            System.out.printf("No project budget falls between %.2f and %.2f%n", min, max);
            return;
        }
        Project[] result = Arrays.copyOf(temp, count);
        ConsoleMenu.displayProjects(result);
    }


    // Get project status report (arrays-only)
    public ProjectStatusReportDto[] getProjectStatusReport(TaskService sourceTaskService) {
        // temp buffer sized to number of project slots (worst case)
        ProjectStatusReportDto[] temp = new ProjectStatusReportDto[projectsRepository.length];
        int count = 0;

        if (sourceTaskService == null) {
            return Arrays.copyOf(temp, 0); // return empty array
        }

        for (int i = 0; i < projectsRepository.length; i++) {
            Project project = projectsRepository[i];
            if (project == null || project.getId() == null) continue;

            // assume Task[] is returned; handle null
            Task[] tasks = sourceTaskService.getTasksByProjectId(project.getId());
            int totalTasks = (tasks == null) ? 0 : tasks.length;

            int completedCount = 0;
            if (tasks != null) {
                for (int t = 0; t < tasks.length; t++) {
                    Task task = tasks[t];
                    if (task == null) continue;
                    String status = task.getTaskStatus();
                    if (status != null && status.equalsIgnoreCase("Completed")) {
                        completedCount++;
                    }
                }
            }

            // create DTO and put into temp buffer
            temp[count++] = new ProjectStatusReportDto(
                    project.getId(),
                    project.getName(),
                    totalTasks,
                    completedCount
            );
        }

        // return trimmed array (no null slots)
        return Arrays.copyOf(temp, count);
    }


}
