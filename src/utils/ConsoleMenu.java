
        package utils;

import models.Project;
import models.ProjectStatusReportDto;
import models.SoftwareProject;
import models.Task;
import models.HardwareProject;
import services.ProjectService;
import services.ReportService;
import services.TaskService;
import services.UserService;
import models.User;
import utils.ValidationUtils;

import java.util.Scanner;

import java.util.List;

public class ConsoleMenu {

    private static ProjectService projectService;
    private static TaskService taskService;
    private static ReportService reportService;
    private static UserService userService;

    static Scanner scanner = new Scanner(System.in);

    public static void setProjectService(ProjectService service) {
        projectService = service;
    }

    public static void setTaskService(TaskService service) {
        taskService = service;
    }

    public static void setReportService(ReportService service) {
        reportService = service;
    }

    public static void setUserService(UserService service) {
        userService = service;
    }

    // Helper validation input methods
    private static int readIntInRange(String prompt, int min, int max) {
        int val;
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }
            val = scanner.nextInt();
            scanner.nextLine();
            if (val < min || val > max) {
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
                continue;
            }
            return val;
        }
    }

    private static int readPositiveInt(String prompt) {
        int val;
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.nextLine();
                continue;
            }
            val = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtils.validateTeamSize(val)) {
                System.out.println("Team size must be a positive integer. Please try again.");
                continue;
            }
            return val;
        }
    }

    private static double readNonNegativeDouble(String prompt) {
        double val;
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a number (0 or greater).");
                scanner.nextLine();
                continue;
            }
            val = scanner.nextDouble();
            scanner.nextLine();
            if (!ValidationUtils.validateBudget(val)) {
                System.out.println("Budget must be non-negative. Please try again.");
                continue;
            }
            return val;
        }
    }

    private static String readNonEmptyText(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (!ValidationUtils.hasText(input)) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            return input.trim();
        }
    }

    private static String readExistingProjectId(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                return "0";
            }
            if (!ValidationUtils.hasText(input)) {
                System.out.println("Project ID cannot be empty. Please try again.");
                continue;
            }
            if (projectService == null || projectService.getProjectById(input) == null) {
                System.out.printf("No project found with ID %s. Enter a valid project ID or 0 to return.%n", input);
                continue;
            }
            return input;
        }
    }

    private static String readValidTaskStatus(String prompt) {
        String status;
        while (true) {
            System.out.print(prompt);
            status = scanner.nextLine().trim();
            if (!ValidationUtils.hasText(status)) {
                System.out.println("Status cannot be empty. Try: Pending, In Progress, Completed.");
                continue;
            }
            if (!ValidationUtils.validateTaskStatus(status)) {
                System.out.println("Invalid status. Supported: Pending, In Progress, Completed. Try again.");
                continue;
            }
            return status;
        }
    }

    // Interactive method to create a new project

    public static String createProjectInteractive() {
        while (true) {
            System.out.printf("Enter Project Type %n 1. Software %n 2. Hardware %n 3. Exit: ");
            int typeChoice = readIntInRange("", 1, 3);

            if (typeChoice == 3) {
                System.out.println("Exiting project creation...");
                return "Exited";
            }

            String name = readNonEmptyText("Enter Project Name: ");
            String description = readNonEmptyText("Enter Description: ");
            int teamSize = readPositiveInt("Enter Team Size: ");
            double projectBudget = readNonNegativeDouble("Enter Project Budget: ");

            if (typeChoice == 1) {
                String language = readNonEmptyText("Enter Programming Language: ");
                SoftwareProject project = new SoftwareProject(name, description, "Software", teamSize, language, projectBudget);
                projectService.addProject(project);
                return "Software project was created successfully";
            } else {
                String hardwareType = readNonEmptyText("Enter Hardware Type: ");
                HardwareProject project = new HardwareProject(name, description, "Hardware", teamSize, hardwareType, projectBudget);
                projectService.addProject(project);
                return "Hardware project was created successfully";
            }
        }
    }

    // printing title

    public static void printingTitle(String title) {
        int boxWidth = 60; // total width of the box

        // Top border (hyphens)
        System.out.println("-".repeat(boxWidth));

        // Calculate padding for centering the title
        int padding = (boxWidth - title.length() - 2) / 2; // subtract 2 for the pipes

        // Print title with pipes on both sides
        System.out.println("|" + " ".repeat(padding) + title + " ".repeat(padding) + "|");

        // Bottom border (hyphens)
        System.out.println("-".repeat(boxWidth));
    }

    // Main menu to display for user to interact
    public static void mainMenu() {
        printingTitle("JAVA PROJECT MANAGEMENT SYSTEM");

        // Display current user role if logged in
        if (userService != null && userService.getCurrentUser() != null) {
            System.out.println();
            userService.getCurrentUser().displayRole();
            System.out.println();
        }

        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n 5. %s%n 6. %s%n%n", "", "Manage Projects", "Manage Tasks ",
                "View Status Reports", "Switch User","Testing", "Exit");
        int choice = readIntInRange("Enter your choice __", 1, 6);

        switch (choice) {
            case 1:
                projectCatalog();
                break;
            case 2:
                taskMainMenu();
                break;
            case 3:
                printProjectStatusReporting();
                break;
            case 4:
                switchUserMenu();
                break;
            case 5:
                TestingUserMenu();
                break;
            case 6:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice!");
                mainMenu();
        }
    }

    // Returning to the main menu with validation
    public static void returnToMain() {
        while (true) {
            System.out.print("Enter 100 to return to main menu: ");
            if (scanner.hasNextInt()) {
                int inp = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (inp == 100) {
                    mainMenu();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter 100.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    // Returning to the main menu with validation
    public static void returnToTesting() {
        while (true) {
            System.out.print("Enter 100 to return to main menu: ");
            if (scanner.hasNextInt()) {
                int inp = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (inp == 100) {
                    TestingUserMenu();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter 100.");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    public static void projectCatalog() {
        printingTitle("PROJECT CATALOG ");
        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n%n", "", "View All Projects", "Software Projects Only",
                "Hardware Projects Only", "Search by Budget Range");
        int input = readIntInRange("Enter filter choice __", 1, 4);

        switch (input) {
            case 1:
                projectService.displayAllProjects();
                break;
            case 2:
                projectService.filterProjectByType("Software");
                break;
            case 3:
                projectService.filterProjectByType("Hardware");
                break;

            case 4:
                printingTitle("Filter by minimum and maximum budget ");
                double min = readNonNegativeDouble("Enter the minimum budget __");
                double max = readNonNegativeDouble("Enter the Maximum budget __");

                if (!ValidationUtils.validateBudgetRange(min, max)) {
                    System.out.println("Invalid budget range. Minimum should be <= Maximum and both non-negative.");
                } else {
                    projectService.searchByBudgetRange(min, max);
                }
                break;

            default:
                mainMenu();
        }
    }

    // Generic display for all projects
    public static void displayProjects(List<Project> projects) {

        if (projects.isEmpty()) {
            System.out.println("No projects found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-10s %-10s%n", "ID", "Name", "Description", "Type", "TeamSize");
        System.out
                .println("-------------------------------------------------------------------------------------------");
        for (Project p : projects) {
            System.out.printf("%-5s %-20s %-30s %-10s %-10d%n", p.id, p.name, p.description, p.type, p.teamSize);
        }
        System.out
                .println("-------------------------------------------------------------------------------------------");

        System.out.printf("%n%n");

        enterProjectId();

        //     returnToMain();

    }

    // Overloaded: Display only projects of a given type
    public static void displayProjects(List<Project> projects, String projectType) {
        List<Project> filtered = projects.stream()
                .filter(p -> p.type.equalsIgnoreCase(projectType))
                .toList();

        System.out.println("=== " + projectType + " Projects ===");
        displayProjects(filtered);
    }

    // Generic display for all TaskPerProject
    public static void displayProjectTaks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No Task found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s%n", "ID", "TASK NAME", "STATUS");
        System.out
                .println("-----------------------------------------------------------");
        for (Task t : tasks) {
            System.out.printf("%-5s %-20s %-30s%n ", t.getTaskId(), t.getTaskName(), t.getTaskStatus());
        }
        System.out
                .println("------------------------------------------------------------");

        System.out.printf("%n%n");

        returnToMain();

    }

    // Generic display for all TaskPerProject
    public static void displayProjectTaks(List<Task> tasks, double completionRate) {
        if (tasks.isEmpty()) {
            System.out.println("No Task found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s%n", "ID", "TASK NAME", "STATUS");
        System.out
                .println("-----------------------------------------------------------");
        for (Task t : tasks) {
            System.out.printf("%-5s %-20s %-30s%n ", t.getTaskId(), t.getTaskName(), t.getTaskStatus());
        }
        System.out
                .println("------------------------------------------------------------");

        System.out.printf("%n%n");

        //ADD PERCENTAGE SYMBOL
        System.out.printf("Completion Rate : %f %%  %n", completionRate);

        System.out.printf("Options:  %n");
        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n%n", "", "Add New Task", "Update Task Status",
                "Remove Task", "Back to Main Menu");

        int input = readIntInRange("Enter Your choice __", 1, 4);

        switch (input) {
            case 1:
                addingNewTaskMenu();
                break;
            case 2:
                updatingTask();
                break;
            case 3:
                removingTask();
                break;

            default:
                mainMenu();
        }


    }

    // display project Details with tasks and completion
    public static void displayProjectDetails(String id) {

        Project project = projectService.getProjectById(id);
        List<Task> tasks = taskService.getTasksByProjectId(id);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        double completionRate = taskService.calculateCompletionRate(project.id);
        printingTitle("PROJECT DETAILS : " + project.id);

        System.out.println("Project Name: " + project.name);
        System.out.println("Project Type: " + project.type);
        System.out.println("Project Team Size: " + project.teamSize);
        System.out.printf("Project Budget: %f%n", project.budget);
        System.out.printf("Associated Tasks: %n%n");

        displayProjectTaks(tasks, completionRate);



    }

    public static void addingNewTaskMenu() {
        printingTitle("ADD NEW TASK");

        String taskName = readNonEmptyText("Enter task name: ");
        String projectId = readExistingProjectId("Enter Assigned Project ID (or 0 to cancel): ");
        if ("0".equals(projectId)) {
            System.out.println("Cancelled adding task.");
            returnToMain();
            return;
        }
        String status = readValidTaskStatus("Enter Task Status (Pending, In Progress, Completed): ");

        taskService.addTask(new Task(taskName, status, projectId));

        System.out.printf("Task \"%s\" added successfully to project %s%n", taskName, projectId);
        returnToMain();
    }

    public static void updatingTask() {
        printingTitle("Updating TASK");
        String taskId = readNonEmptyText("Enter task Id: ");
        String status = readValidTaskStatus("Enter new Task status (Pending, In Progress, Completed): ");

        Task task = taskService.updateTaskStatus(taskId, status);
        if (task == null) {
            System.out.printf("No task found with ID %s. Nothing was updated.%n", taskId);
        } else {
            System.out.printf("Task \"%s\" marked as %s%n", task.getTaskName(), status);
        }
        returnToMain();
    }

    public static void removingTask() {
        printingTitle("Deleting TASK");
        String taskId = readNonEmptyText("Enter task Id: ");

        taskService.deleteTask(taskId);

        System.out.printf("Task with the Id %s has been successfully Removed %n", taskId);
        returnToMain();
    }


    // User management methods
    public static void createUserMenu() {
        printingTitle("CREATE USER PROFILE");
        String name = readNonEmptyText("Enter User Name: ");

        String email;
        while (true) {
            email = readNonEmptyText("Enter User Email: ");
            if (!ValidationUtils.validateEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email address.");
                continue;
            }
            break;
        }

        String role;
        while (true) {
            role = readNonEmptyText("Enter User Role (Regular/Admin): ");
            if (!ValidationUtils.validateUserRole(role)) {
                System.out.println("Invalid role. Supported roles: Regular, Admin. Try again.");
                continue;
            }
            break;
        }

        User user;
        if (role.equalsIgnoreCase("Admin")) {
            user = userService.createAdminUser(name, email);
        } else {
            user = userService.createRegularUser(name, email);
        }

        System.out.printf("User created successfully!%n");
        System.out.println("User Details: " + user.toString());
        System.out.println();
    }

    public static void switchUserMenu() {
        printingTitle("SWITCH USER");

        System.out.println("1. Create New User");
        System.out.println("2. Login with Existing User");
        System.out.println("3. View All Users");
        System.out.println("4. Display Current User");
        System.out.println("5. Logout");
        System.out.println("6. Back to Main Menu");

        int choice = readIntInRange("Enter your choice __", 1, 6);

        switch (choice) {
            case 1:
                createUserMenu();
                switchUserMenu();
                break;
            case 2:
                loginUserMenu();
                break;
            case 3:
                displayAllUsers();
                break;
            case 4:
                displayCurrentUser();
                break;
            case 5:
                userService.logout();
                System.out.println("Logged out successfully.");
                System.out.println();
                mainMenu();
                break;
            case 6:
                mainMenu();
                break;
            default:
                System.out.println("Invalid choice!");
                switchUserMenu();
        }
    }

    public static void loginUserMenu() {
        printingTitle("LOGIN");
        String userId = readNonEmptyText("Enter User ID: ");

        User user = userService.login(userId);
        if (user != null) {
            System.out.println("Login successful!");
            user.displayRole();
            System.out.println();
        } else {
            System.out.println("User not found with ID: " + userId);
            System.out.println();
        }

        returnToMain();
    }

    public static void displayAllUsers() {
        printingTitle("ALL USERS");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.printf("%-10s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Role");
            System.out.println("--------------------------------------------------------------------------------");
            for (User u : users) {
                System.out.printf("%-10s %-20s %-30s %-15s%n",
                        u.getId(), u.getName(), u.getEmail(), u.getRole());
            }
            System.out.println("--------------------------------------------------------------------------------");
        }
        System.out.println();

        returnToMain();
    }

    public static void displayCurrentUser() {
        printingTitle("CURRENT USER");
        userService.displayCurrentUser();
        System.out.println();

        returnToMain();
    }

    //Enter project id to display project details
    public static void enterProjectId() {
        String projectId = readExistingProjectId("Enter project id to view details (or 0 to return to main menu): ");
        if (projectId.equals("0")) {
            mainMenu();
        } else {
            displayProjectDetails(projectId);
            enterProjectId();
        }
    }

    //Managing Task Add tasks to specific project, Asging Task status to completed or pending, in Progress, update or delete task, view all tasks per project progress details
    public static void taskMainMenu() {
        printingTitle("TASK MANAGEMENT");

        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n 5. %s%n%n", "", "Add New Task", "Update Task Status",
                "Remove Task", "View Project Status Report", "Back to Main Menu");
        int choice = readIntInRange("Enter your choice __", 1, 5);

        switch (choice) {
            case 1:
                addingNewTaskMenu();
                break;
            case 2:
                updatingTask();
                break;
            case 3:
                removingTask();
                break;
            case 4:
                printProjectStatusReporting();
                break;
            case 5:
                mainMenu();
                break;
            default:
                System.out.println("Invalid choice!");
                taskMainMenu();
        }


    }

    //View Project Status Report
    public static void  printProjectStatusReporting() {
        List<ProjectStatusReportDto> projectStatusReport = reportService.getProjectStatusReport();
        double average = reportService.calculateAverageProjectStatusReport();
        printingTitle("PROJECT STATUS REPORT");
        System.out.println("Project ID | Project Name | Tasks | Completed ");
        System.out.println("----------------------------------------");
        for (ProjectStatusReportDto report : projectStatusReport) {
            System.out.printf("%-10s | %-15s | %-5d | %-5d %n", report.projectId, report.projectName, report.Tasks, report.Completed);
        }
        System.out.println("----------------------------------------");

        System.out.println("AVERAGE COMPLETION: "+ average+"%");

        returnToMain();
    }

//Testing SCREEN


    // Testing SCREEN
    public static void TestingUserMenu() {
        int choice;
        do {
            printingTitle("TESTING MODE");

            System.out.println("1. Create New Project");
            System.out.println("2. View Projects");
            System.out.println("3. Add Task");
            System.out.println("4. View Task");
            System.out.println("5. Update Task Status");


            System.out.println("6. Exit Testing Mode");

            choice = readIntInRange("Enter your choice: ", 1, 6);

            switch (choice) {
                case 1:
                    createProjectInteractive();
                    break;
                case 2:
                    projectService.displayAllProjects();
                    break;
                case 3:
                    addingNewTaskMenu();
                    break;
                case 4:
                    printProjectStatusReporting();
                    break;
                case 5:
                  updatingTask();
                    break;
                case 6:
                    System.out.println("Exiting Testing Mode...");
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 6); // loop until user chooses to exit
    }

}
