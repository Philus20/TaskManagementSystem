package utils;

import Controllers.ProjectController;
import Controllers.ReportController;
import Controllers.TaskController;
import Controllers.UserController;
import interfaces.IUserService;
import services.PermissionService;

/**
 * MenuRouter following Single Responsibility Principle (SRP)
 * - Only responsible for menu navigation and routing
 * - Delegates actions to appropriate controllers
 */
public class MenuRouter {
    private final ProjectController projectController;
    private final TaskController taskController;
    private final UserController userController;
    private final ReportController reportController;
    private final Printer printer;
    private final ValidationUtils validationUtils;
    private final PermissionService permissionService;
    private final IUserService userService;

    public MenuRouter(ProjectController projectController,
                     TaskController taskController,
                     UserController userController,
                     ReportController reportController,
                     Printer printer,
                     ValidationUtils validationUtils,
                     PermissionService permissionService,
                     IUserService userService) {
        this.projectController = projectController;
        this.taskController = taskController;
        this.userController = userController;
        this.reportController = reportController;
        this.printer = printer;
        this.validationUtils = validationUtils;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    /**
     * Display main menu and handle navigation
     */
    public void showMainMenu() {
        printer.printTitle("JAVA PROJECT MANAGEMENT SYSTEM");

        // Display current user role if logged in
        if (permissionService.isLoggedIn()) {
            printer.printMessage("");
            userService.getCurrentUser().displayRole();
            printer.printMessage("");
        }

        printer.printMessage("1. Manage Projects");
        printer.printMessage("2. Manage Tasks");
        printer.printMessage("3. View Status Reports");
        printer.printMessage("4. Switch User");
        printer.printMessage("5. Testing");
        printer.printMessage("6. Exit");
        printer.printMessage("");

        int choice = validationUtils.readIntInRange("Enter your choice __", 1, 6);

        switch (choice) {
            case 1:
                projectController.projectCatalog();
                break;
            case 2:
                taskController.taskManagementMenu();
                break;
            case 3:
                reportController.generateProjectStatusReport();
                break;
            case 4:
                userController.switchUserMenu();
                break;
            case 5:
                showTestingMenu();
                break;
            case 6:
                printer.printMessage("Exiting...");
                System.exit(0);
                break;
            default:
                printer.printMessage("Invalid choice!");
                showMainMenu();
        }
    }

    /**
     * Show initial login menu
     */
    public void showInitialLoginMenu() {
        int choice = userController.initialLoginMenu();

        switch (choice) {
            case 1:
                if (userController.login()) {
                    showMainMenu();
                } else {
                    showLoginRetryMenu();
                }
                break;
            case 2:
                userController.signUp();
                showMainMenu();
                break;
            case 3:
                printer.printMessage("Continuing without account. You will have limited access (view only).");
                printer.printMessage("");
                showMainMenu();
                break;
            default:
                printer.printMessage("Invalid choice!");
                showInitialLoginMenu();
        }
    }

    /**
     * Show login retry menu
     */
    private void showLoginRetryMenu() {
        printer.printMessage("1. Try again");
        printer.printMessage("2. Sign up");
        printer.printMessage("3. Continue without account");

        int choice = validationUtils.readIntInRange("Enter your choice: ", 1, 3);
        switch (choice) {
            case 1:
                if (userController.login()) {
                    showMainMenu();
                } else {
                    showLoginRetryMenu();
                }
                break;
            case 2:
                userController.signUp();
                showMainMenu();
                break;
            case 3:
                printer.printMessage("Continuing without account. You will have limited access (view only).");
                printer.printMessage("");
                showMainMenu();
                break;
        }
    }

    /**
     * Show testing menu
     */
    private void showTestingMenu() {
        int choice;
        do {
            printer.printTitle("TESTING MODE");
            printer.printMessage("1. Create New Project");
            printer.printMessage("2. View Projects");
            printer.printMessage("3. Add Task");
            printer.printMessage("4. View Task");
            printer.printMessage("5. Update Task Status");
            printer.printMessage("6. Exit Testing Mode");

            choice = validationUtils.readIntInRange("Enter your choice: ", 1, 6);

            switch (choice) {
                case 1:
                    projectController.createProjectInteractive();
                    break;
                case 2:
                    // View projects handled by ProjectController
                    break;
                case 3:
                    taskController.addTaskInteractive();
                    break;
                case 4:
                    reportController.generateProjectStatusReport();
                    break;
                case 5:
                    taskController.updateTaskStatus();
                    break;
                case 6:
                    printer.printMessage("Exiting Testing Mode...");
                    showMainMenu();
                    break;
                default:
                    printer.printMessage("Invalid choice! Please try again.");
            }
        } while (choice != 6);
    }

    /**
     * Return to main menu
     */
    public void returnToMain() {
        printer.printMessage("Enter 100 to return to main menu: ");
        int input = validationUtils.readIntInRange("", 100, 100);
        if (input == 100) {
            showMainMenu();
        }
    }
}

