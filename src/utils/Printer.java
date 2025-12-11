package utils;

import interfaces.IUserService;
import models.Project;
import models.Task;
import models.User;

import java.util.List;

/**
 * Printer following Single Responsibility Principle (SRP)
 * - Only responsible for all output/display operations
 */
public class Printer {
    
    public void printTitle(String title) {
        System.out.println("-".repeat(60));
        int padding = (60 - title.length() - 2) / 2;
        System.out.println("|" + " ".repeat(Math.max(0, padding)) + title + " ".repeat(Math.max(0, padding)) + "|");
        System.out.println("-".repeat(60));
    }

    public void printMessage(String msg) {
        System.out.println(msg);
    }

    public void printProjectsTable(Project[] projects) {
        if (projects == null || projects.length == 0) {
            System.out.println("No projects");
            return;
        }
        System.out.printf("%-5s %-20s %-30s %-10s %-10s%n", "ID", "Name", "Description", "Type", "Team");
        System.out.println("--------------------------------------------------------------------------------");
        for (Project p : projects) {
            if (p == null) continue;
            System.out.printf("%-5s %-20s %-30s %-10s %-10d%n",
                p.getId(), p.getName(), p.getDescription(), p.getType(), p.getTeamSize());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void printUsersTable(User[] users) {
        if (users == null || users.length == 0) {
            System.out.println("No users found.");
            return;
        }
        System.out.printf("%-10s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Role");
        System.out.println("--------------------------------------------------------------------------------");
        for (User u : users) {
            if (u == null) continue;
            System.out.printf("%-10s %-20s %-30s %-15s%n",
                u.getId(), u.getName(), u.getEmail(), u.getRole());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void printTasksTable(List<Task> tasks, IUserService userService, double completionRate) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No Task found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-15s %-30s%n", "ID", "TASK NAME", "STATUS", "ASSIGNED USER", "USER EMAIL");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        
        for (Task t : tasks) {
            if (t == null) continue;
            String userId = t.getAssignedUserId();
            String userName = "Unassigned";
            String userEmail = "-";
            
            if (userId != null && userService != null) {
                User assignedUser = userService.getUserById(userId);
                if (assignedUser != null) {
                    userName = assignedUser.getName();
                    userEmail = assignedUser.getEmail();
                }
            }
            
            System.out.printf("%-5s %-20s %-30s %-15s %-30s%n",
                t.getTaskId(), t.getTaskName(), t.getTaskStatus(), userName, userEmail);
        }
        
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.println("");
        System.out.printf("Completion Rate : %.2f%%%n", completionRate);
    }

    public void printTasksTable(List<Task> tasks, IUserService userService) {
        printTasksTable(tasks, userService, 0.0);
    }
}