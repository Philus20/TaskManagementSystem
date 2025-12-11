package utils;

import Controllers.ProjectController;
import Controllers.ReportController;
import Controllers.TaskController;
import Controllers.UserController;
import services.GenerateProjectId;
import services.GenerateTaskId;
import services.PermissionService;
import services.ProjectService;
import services.TaskService;
import services.UserService;
import services.ReportService;

import java.util.Scanner;

/**
 * ConsoleMenu following Single Responsibility Principle (SRP)
 * - Only responsible for initializing dependencies and starting the application
 * - All business logic delegated to controllers
 * - All navigation delegated to MenuRouter
 */
public class ConsoleMenu {
    private final Scanner scanner;
    private final MenuRouter menuRouter;

    public ConsoleMenu(ProjectService projectService, TaskService taskService,
            ReportService reportService, UserService userService) {

        // Initialize utilities
        this.scanner = new Scanner(System.in);
        Printer printer = new Printer();
        ValidationUtils validationUtils = new ValidationUtils(scanner, projectService, userService,
                new GenerateProjectId());

        // Initialize ID generators
        GenerateProjectId projectIdGenerator = new GenerateProjectId();
        GenerateTaskId taskIdGenerator = new GenerateTaskId();

        // Initialize permission service
        PermissionService permissionService = new PermissionService(userService);

        // Initialize controllers (following Dependency Inversion Principle)
        ProjectController projectController = new ProjectController(
                projectService, validationUtils, printer, projectIdGenerator);

        TaskController taskController = new TaskController(
                taskService, userService, validationUtils, printer, taskIdGenerator, permissionService);

        UserController userController = new UserController(
                userService, validationUtils, printer);

        ReportController reportController = new ReportController(
                reportService, printer);

        // Initialize menu router
        this.menuRouter = new MenuRouter(
                projectController,
                taskController,
                userController,
                reportController,
                printer,
                validationUtils,
                permissionService,
                userService);
    }

    /**
     * Start the application by showing initial login menu
     */
    public void start() {
        menuRouter.showInitialLoginMenu();
    }

    /**
     * Close scanner resource
     */
    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
