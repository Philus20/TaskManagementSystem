import Repository.ProjectRepository;
import Repository.TaskRepository;
import Repository.UserRepository;
import services.*;
import utils.ConsoleMenu;

public class Main {
    public static void main(String[] args) {

        // Step 1: Create repositories (following Dependency Inversion Principle)
        ProjectRepository projectRepository = new ProjectRepository(20);
        TaskRepository taskRepository = new TaskRepository(50);
        UserRepository userRepository = new UserRepository(20);

        // Step 2: Create ID generators
        GenerateTaskId taskIdGenerator = new GenerateTaskId();
        GenerateUserId userIdGenerator = new GenerateUserId();

        // Step 3: Create services with dependency injection (DIP)
        ProjectService projectService = new ProjectService(projectRepository);
        TaskService taskService = new TaskService(taskRepository, taskIdGenerator);
        UserService userService = new UserService(userRepository, userIdGenerator);
        ReportService reportService = new ReportService(taskService, projectService);

        // Step 4: Create console menu (it internally creates all controllers and router)
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
