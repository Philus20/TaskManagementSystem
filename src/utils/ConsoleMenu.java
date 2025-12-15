package utils;

import Controllers.ProjectController;
import Controllers.ReportController;
import Controllers.TaskController;
import Controllers.UserController;
import services.*;

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
        // To resolve circular dependency (controllers need MenuRouter, MenuRouter needs controllers):
        // 1. Create MenuRouter with null controllers
        // 2. Create controllers with MenuRouter (which implements INavigation)
        // 3. Initialize MenuRouter's controllers using initializeControllers method

        // Step 1: Create MenuRouter (controllers will be set later)
        this.menuRouter = new MenuRouter(null, null, null, null, printer, validationUtils, permissionService, userService);

        // Step 2: Initialize controllers with MenuRouter
        ProjectController projectController = new ProjectController(
                projectService, validationUtils, printer, projectIdGenerator, userService, taskService,
                new ProjectUserAssignmentOperations(projectService, 20), this.menuRouter);

        TaskController taskController = new TaskController(
                taskService, userService, validationUtils, printer, taskIdGenerator, permissionService, this.menuRouter);

        UserController userController = new UserController(
                userService, validationUtils, printer, this.menuRouter);

        ReportController reportController = new ReportController(
                reportService, printer, this.menuRouter, validationUtils);

        // Step 3: Initialize MenuRouter's controllers
        this.menuRouter.initializeControllers(projectController, taskController, userController, reportController);
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
