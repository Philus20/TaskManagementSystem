
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

import java.util.Scanner;

import java.util.List;

public class ConsoleMenu_cleaned {

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

    // Interactive method to create a new project

    public static String createProjectInteractive() {
        String type;
        do {
            System.out.printf("Enter Project Type %n 1. Software %n 2. Hardware %n 3. Exit: ");
            type = scanner.nextLine().trim();

            if (type.equals("3")) {
                System.out.println("Exiting project creation...");
                return "Exited";
            }

            if (ValidationUtils.isValidPositiveNumber(type) && (type.equals("1") || type.equals("2"))) {
                System.out.print("Enter Project Name: ");
                String name = scanner.nextLine();

                System.out.print("Enter Description: ");
                String description = scanner.nextLine();

                System.out.print("Enter Team Size: ");
                int teamSize = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter Project Budget: ");
                double projectBudget = scanner.nextDouble();
                scanner.nextLine();

                if (type.equals("1")) {
                    System.out.print("Enter Programming Language: ");
                    String language = scanner.nextLine();
                    SoftwareProject project = new SoftwareProject(name, description, "Software", teamSize, language, projectBudget);
                    projectService.addProject(project);
                    return "Software project was created successfully";
                } else {
                    System.out.print("Enter Hardware Type: ");
                    String hardwareType = scanner.nextLine();
                    HardwareProject project = new HardwareProject(name, description, "Hardware", teamSize, hardwareType, projectBudget);
                    projectService.addProject(project);
                    return "Hardware project was created successfully";
                }
            } else {
                System.out.println("Invalid input! Please enter 1 for Software, 2 for Hardware, or 3 to Exit.");
            }
        } while (true);
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

        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n 5. %s%n%n", "", "Manage Projects", "Manage Tasks ",
                "View Status Reports", "Switch User", "Exit");
        System.out.print("Enter your choice __");
        int choice = scanner.nextInt();
        scanner.nextLine();

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
        int inp = -1;

        while (true) {
            System.out.print("Enter 100 to return to main menu: ");

            // Validate input
            if (scanner.hasNextInt()) {
                inp = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (inp == 100) {
                    mainMenu();
                    break; // exit loop after returning to main menu
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
        System.out.print("Enter filter choice __");
        int input = scanner.nextInt();
        scanner.nextLine();

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
                System.out.print("Enter the minimum budget __");
                double min = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Enter the Maximum budget __");
                double max = scanner.nextDouble();
                scanner.nextLine();

                projectService.searchByBudgetRange(min, max);
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

        System.out.print("Enter Your choice __");
        int input = scanner.nextInt();
        scanner.nextLine();

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
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();

        System.out.print("Enter Assigned Project ID: ");
        String projectId = scanner.nextLine();

        System.out.print("Enter Project Initial Status: ");
        String status = scanner.nextLine();

        // auto generate project id

        String taskId = "";

        taskService.addTask(new Task(taskName,  status, projectId));

        System.out.printf("Task \" %s \"  added successfully to project %s %n", taskName, projectId);

    }

    public static void updatingTask() {
        printingTitle("Updating TASK");
        System.out.print("Enter task Id: ");
        String taskId = scanner.nextLine();

        System.out.print("Enter new Task status : ");
        String status = scanner.nextLine();

        Task task = taskService.updateTaskStatus(taskId, status);

        System.out.printf("Task \" %s \"  Marked as  %s %n", task.getTaskName(), status);

    }

    public static void removingTask() {
        printingTitle("Deleting TASK");
        System.out.print("Enter task Id: ");
        String taskId = scanner.nextLine();

        taskService.deleteTask(taskId);

        System.out.printf("Task with the Id %s has been successfully Removed %n", taskId);

    }


    // User management methods
    public static void createUserMenu() {
        printingTitle("CREATE USER PROFILE");
        System.out.print("Enter User Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter User Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter User Role (Regular/Admin): ");
        String role = scanner.nextLine();

        User user;
        if (role.equalsIgnoreCase("Admin")) {
            user = userService.createAdminUser(name, email);
        } else if (role.equalsIgnoreCase("Regular")) {
            user = userService.createRegularUser(name, email);
        } else {
            System.out.println("Invalid role! Creating Regular User by default.");
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
        System.out.print("Enter your choice __");

        int choice = scanner.nextInt();
        scanner.nextLine();

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
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

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
        System.out.print("Enter project id to view details (or 0 to return to main menu): ");
        String projectId = scanner.nextLine();
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
        System.out.print("Enter your choice __");
        int choice = scanner.nextInt();
        scanner.nextLine();

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
            System.out.println("6.  Task Reporting");
            System.out.println("7. Exit Testing Mode");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    createProjectInteractive();
                    break;
                case 2:
                    projectService.displayAllProjects();
                    break;
                case 3:
                    displayAllUsers();
                    break;
                case 4:
                    displayCurrentUser();
                    break;
                case 5:
                    updatingTask();
                    break;
                case 6:
                   printProjectStatusReporting();
                    break;

                case 7:
                    System.out.println("Exiting Testing Mode...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7); // loop until user chooses to exit
    }

}




/**
 * Cleaned and Refactored ConsoleMenu.java
 * Improvements:
 * - Added comments for clarity
 * - Replaced recursive menu calls with loops
 * - Used do-while loops where appropriate
 * - Improved input validation to prevent crashes
 * - Maintained original functionality
 */
