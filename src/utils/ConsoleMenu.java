
package utils;

import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import services.ProjectService;

import  java.util.Scanner;

import java.util.List;

public class ConsoleMenu {

    private static  ProjectService projectService;
    static Scanner scanner = new Scanner(System.in);


    public static void setProjectService(ProjectService service) {
        projectService = service;
    }






    // Interactive method to create a new project
    public static String createProjectInteractive() {
        System.out.print("Enter Project Type (Software/Hardware): ");
        String type = scanner.nextLine();

        System.out.print("Enter Project ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

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

        if (type.equalsIgnoreCase("Software")) {
            System.out.print("Enter Programming Language: ");
            String language = scanner.nextLine();
          SoftwareProject project = new  SoftwareProject(id, name, description, "Software", teamSize, language,projectBudget);
          projectService.addProject(project);
          return "Software project was created successfully";
        } else if (type.equalsIgnoreCase("Hardware")) {
            System.out.print("Enter Hardware Type: ");
            String hardwareType = scanner.nextLine();
            HardwareProject project = new HardwareProject(id, name, description, "Hardware", teamSize, hardwareType,projectBudget);
            projectService.addProject(project);
            return "Hardware project was created successfully";
        } else {
            System.out.println("Invalid project type! Returning null.");
            return null;
        }
    }


    //printing title


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


//Main menu to display for user to interact
    public  static void mainMenu(){
        printingTitle("JAVA PROJECT MANAGEMENT SYSTEM");
        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n 5. %s%n%n","", "Manage Projects", "Manage Tasks ", "View Status Reports", "Switch User","Exit");
        System.out.print("Enter your choice __");
        scanner.nextInt();
        scanner.nextLine();
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




    public  static void projectCatalog(){
        printingTitle("PROJECT CATALOG ");
        System.out.printf("%s 1. %s%n 2. %s%n 3. %s%n 4. %s%n%n","", "View All Projects", "Software Projects Only", "Hardware Projects Only", "Search by Budget Range");
        System.out.print("Enter filter choice __");
      int input =   scanner.nextInt();
        scanner.nextLine();

        switch (input)
        {
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

                projectService.searchByBudgetRange(min,max);
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
        System.out.println("-------------------------------------------------------------------------------------------");
        for (Project p : projects) {
            System.out.printf("%-5d %-20s %-30s %-10s %-10d%n", p.id, p.name, p.description, p.type, p.teamSize);
        }
        System.out.println("-------------------------------------------------------------------------------------------");

        System.out.printf("%n%n");

        returnToMain();

    }

    // Overloaded: Display only projects of a given type
    public static void displayProjects(List<Project> projects, String projectType) {
        List<Project> filtered = projects.stream()
                .filter(p -> p.type.equalsIgnoreCase(projectType))
                .toList();

        System.out.println("=== " + projectType + " Projects ===");
        displayProjects(filtered);
    }


}
