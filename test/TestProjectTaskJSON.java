// Test class to verify project-task JSON structure
import services.*;
import models.*;
import Repository.*;

public class TestProjectTaskJSON {
    public static void main(String[] args) {
        // Create repositories
        ProjectRepository projectRepo = new ProjectRepository();
        TaskRepository taskRepo = new TaskRepository();
        
        // Create services
        ProjectService projectService = new ProjectService(projectRepo, new GenerateProjectId());
        TaskService taskService = new TaskService(taskRepo, new GenerateTaskId());
        
        // Inject TaskService into ProjectService
        projectService.setTaskService(taskService);
        
        // Create a project
        Project project = new SoftwareProject("Test Project", "Test Description", "Software", 5, "Java", 10000.0);
        project.setId("P001");
        projectService.addProject(project);
        
        // Create some tasks for the project
        Task task1 = new Task("Task 1", "Pending", "P001");
        task1.setTaskId("T001");
        taskService.addTask(task1);
        
        Task task2 = new Task("Task 2", "In Progress", "P001");
        task2.setTaskId("T002");
        taskService.addTask(task2);
        
        // Save to file
        projectService.saveProjectsToFile();
        
        System.out.println("Test completed! Check projects_data.json");
    }
}
