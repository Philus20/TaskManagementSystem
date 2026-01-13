import Repository.ProjectRepository;
import Repository.TaskRepository;
import Repository.UserRepository;
import services.*;
import utils.ConsoleMenu;

public class Main {
    public static void main(String[] args) {

        // Step 1: Create repositories (following Dependency Inversion Principle)
        ProjectRepository projectRepository = new ProjectRepository();
        TaskRepository taskRepository = new TaskRepository();
        UserRepository userRepository = new UserRepository();

        // Step 2: Create ID generators
        GenerateTaskId taskIdGenerator = new GenerateTaskId();
        GenerateUserId userIdGenerator = new GenerateUserId();

        GenerateProjectId projectIdGenerator = new GenerateProjectId();

        // Step 3: Create services with dependency injection (DIP)
        ProjectService projectService = new ProjectService(projectRepository,projectIdGenerator);
        TaskService taskService = new TaskService(taskRepository, taskIdGenerator);
        UserService userService = new UserService(userRepository, userIdGenerator);
        ReportService reportService = new ReportService(taskService, projectService);
        ConcurrentTaskUpdateService concurrentService = new ConcurrentTaskUpdateService(taskService);
        
        // Inject dependencies to avoid circular dependencies
        projectService.setTaskService(taskService);
        taskService.setConcurrentUpdateService(concurrentService);

        // Step 4: Load data from files (after all services are initialized)
        System.out.println("Loading data from files...");
        projectService.loadProjectsFromFile();
        taskService.loadTasksFromFile();
        System.out.println("Data loading complete.");

        // Step 5: Create console menu (it internally creates all controllers and router)
        ConsoleMenu consoleMenu = new ConsoleMenu(
                projectService,
                taskService,
                reportService,
                userService
        );

        // Step 5: Start the application
        consoleMenu.start();

        // Optional: close scanner at the end
        consoleMenu.closeScanner();
    }
}
