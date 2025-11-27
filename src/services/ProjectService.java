
package services;

import models.Project;
import utils.ConsoleMenu;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProjectService {
    public List<Project> projectsRepository;

    public ProjectService() {
        projectsRepository = new ArrayList<>();
    }

    public void addProject(Project project) {
        projectsRepository.add(project);
    }

    public List<Project> getAllProjects() {
        return projectsRepository;
    }

    // Filter projects by type (e.g., "Software" or "Hardware")
    public List<Project> filterProjectByType(String type) {
        return projectsRepository.stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    // Display all projects with their specific attributes
    public void displayAllProjects() {
        ConsoleMenu.displayProjects(projectsRepository);
    }

    // Search projects by budget range
    public List<Project> searchByBudgetRange(double min, double max) {
        return projectsRepository.stream()
                .filter(p -> p.getBudget() >= min && p.getBudget() <= max)
                .collect(Collectors.toList());
    }
}
