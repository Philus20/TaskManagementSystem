
package services;

import models.Project;
import utils.ConsoleMenu;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProjectService {
    public List<Project> projectsRepository;

    public ProjectService() {
        projectsRepository = new ArrayList<>();
    }


    //Adding new project. It first check if the project id does not exist
    public void addProject(Project project) {
        boolean exists = projectsRepository.stream()
                .anyMatch(p -> p.getId().equals(project.getId()));

        if (!exists) {
            projectsRepository.add(project);
        }
    }


//An endpoint to retrive all the projects
    public List<Project> getAllProjects() {
        return projectsRepository;
    }

    // Filter projects by type (e.g., "Software" or "Hardware")
    public String filterProjectByType(String type) {
        if (projectsRepository == null || type == null) {
            System.out.printf("No records found for %s", type);
            return "Empty";
        }
        ConsoleMenu.displayProjects(projectsRepository.stream()
                .filter(p -> type.equalsIgnoreCase(p.getType()))
                .collect(Collectors.toList()));

         return "Success";
    }


    // Display all projects with their specific attributes
    public void displayAllProjects() {
        ConsoleMenu.displayProjects(projectsRepository);
    }


    // Search projects by budget range
    public String searchByBudgetRange(double min, double max) {
        List<Project> repoData = projectsRepository.stream()
                .filter(p -> p.getBudget() >= min && p.getBudget() <= max)
                .collect(Collectors.toList());

        if (repoData.isEmpty()) {
            System.out.printf("No project budget falls between %.2f and %.2f%n", min, max);
            return "Empty";
        }

        ConsoleMenu.displayProjects(repoData);
        return "Success";
    }

    //get product by id

    public Project getProjectById(String id)
    {
        return projectsRepository.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);

    }
    
    

}
