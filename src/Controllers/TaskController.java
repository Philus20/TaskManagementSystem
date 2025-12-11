package Controllers;

import interfaces.ITaskService;
import interfaces.IUserService;
import models.Task;
import models.User;
import services.GenerateTaskId;
import services.PermissionService;
import utils.Printer;
import utils.ValidationUtils;

import java.util.Arrays;
import java.util.List;

/**
 * TaskController following Single Responsibility Principle (SRP)
 * - Only responsible for task-related user interactions
 * - Delegates business logic to services
 */
public class TaskController {
    private final ITaskService taskService;
    private final IUserService userService;
    private final ValidationUtils in;
    private final Printer out;
    private final GenerateTaskId idGenerator;
    private final PermissionService permissionService;

    public TaskController(ITaskService taskService, IUserService userService, 
                         ValidationUtils in, Printer out, GenerateTaskId idGenerator,
                         PermissionService permissionService) {
        this.taskService = taskService;
        this.userService = userService;
        this.in = in;
        this.out = out;
        this.idGenerator = idGenerator;
        this.permissionService = permissionService;
    }

    /**
     * Add a new task interactively
     */
    public void addTaskInteractive() {
        if (!permissionService.checkLoggedInPermission("add tasks")) {
            return;
        }

        out.printTitle("ADD NEW TASK");
        String taskName = in.readNonEmptyText("Enter task name: ");
        String projectId = in.readExistingProjectId("Enter Assigned Project ID (or 0 to cancel): ");
        
        if ("0".equals(projectId)) {
            out.printMessage("Cancelled adding task.");
            return;
        }

        String status = in.readValidTaskStatus("Enter Task Status (Pending, In Progress, Completed): ");

        // Display available users
        User[] users = userService.getAllUsers();
        Task task;
        
        if (users == null || users.length == 0) {
            out.printMessage("No users available. Task will be created without assignment.");
            task = new Task(taskName, status, projectId);
            task.setTaskId(idGenerator.generate());
            taskService.addTask(task);
            out.printMessage(String.format("Task \"%s\" added successfully to project %s (No user assigned)", 
                taskName, projectId));
        } else {
            out.printUsersTable(users);
            String userId = in.readExistingUserId("Enter User ID to assign (or 0 to skip): ");
            
            if ("0".equals(userId)) {
                task = new Task(taskName, status, projectId);
                out.printMessage(String.format("Task \"%s\" added successfully to project %s (No user assigned)", 
                    taskName, projectId));
            } else {
                task = new Task(taskName, status, projectId, userId);
                User assignedUser = userService.getUserById(userId);
                out.printMessage(String.format("Task \"%s\" added successfully to project %s", taskName, projectId));
                out.printMessage(String.format("Assigned User: %s (%s) - %s",
                    assignedUser.getName(), assignedUser.getEmail(), assignedUser.getRole()));
            }
            task.setTaskId(idGenerator.generate());
            taskService.addTask(task);
        }
    }

    /**
     * Update task status (Admin only)
     */
    public void updateTaskStatus() {
        if (!permissionService.checkAdminPermission("update tasks")) {
            return;
        }

        out.printTitle("Updating TASK");
        String taskId = in.readNonEmptyText("Enter task Id: ");
        String status = in.readValidTaskStatus("Enter new Task status (Pending, In Progress, Completed): ");

        Task task = taskService.updateTaskStatus(taskId, status);
        if (task == null) {
            out.printMessage(String.format("No task found with ID %s. Nothing was updated.", taskId));
        } else {
            out.printMessage(String.format("Task \"%s\" marked as %s", task.getTaskName(), status));
        }
    }

    /**
     * Delete a task (Admin only)
     */
    public void deleteTask() {
        if (!permissionService.checkAdminPermission("delete tasks")) {
            return;
        }

        out.printTitle("Deleting TASK");
        String taskId = in.readNonEmptyText("Enter task Id: ");
        taskService.deleteTask(taskId);
        out.printMessage(String.format("Task with the Id %s has been successfully Removed", taskId));
    }

    /**
     * Display tasks for a project
     */
    public void displayTasksForProject(String projectId) {
        Task[] tasks = taskService.getTasksByProjectId(projectId);
        if (tasks == null || tasks.length == 0) {
            out.printMessage("No tasks found.");
            return;
        }

        // Convert to list for easier processing
        List<Task> taskList = Arrays.asList(tasks);
        double completionRate = taskService.calculateCompletionRate(projectId);
        
        out.printTasksTable(taskList, userService, completionRate);
    }

    /**
     * Task management menu
     */
    public void taskManagementMenu() {
        out.printTitle("TASK MANAGEMENT");
        out.printMessage("1. Add New Task");
        out.printMessage("2. Update Task Status");
        out.printMessage("3. Remove Task");
        out.printMessage("4. View Project Status Report");
        out.printMessage("5. Back to Main Menu");

        int choice = in.readIntInRange("Enter your choice __", 1, 5);

        switch (choice) {
            case 1:
                addTaskInteractive();
                break;
            case 2:
                updateTaskStatus();
                break;
            case 3:
                deleteTask();
                break;
            case 4:
                // This will be handled by ReportController
                break;
            case 5:
                // Return to main menu - handled by MenuRouter
                break;
        }
    }
}

