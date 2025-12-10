package utils;

import models.Project;
import models.Task;
import models.User;

public class Printer {
    public void printTitle(String title) {
        System.out.println("-".repeat(60));
        int padding = (60 - title.length() - 2) / 2;
        System.out.println("|" + " ".repeat(Math.max(0,padding)) + title + " ".repeat(Math.max(0,padding)) + "|");
        System.out.println("-".repeat(60));
    }

    public void printProjectsTable(Project[] projects) {
        if (projects == null || projects.length == 0) { System.out.println("No projects"); return; }
        System.out.printf("%-5s %-20s %-30s %-10s %-10s%n", "ID", "Name", "Description", "Type", "Team");
        System.out.println("--------------------------------------------------------------------------------");
        for (Project p : projects) {
            if (p == null) continue;
            System.out.printf("%-5s %-20s %-30s %-10s %-10d%n", p.getId(), p.getName(), p.getDescription(), p.getType(), p.getTeamSize());
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void printMessage(String msg) { System.out.println(msg); }
    // ... other print helpers used by your UI ...
}
