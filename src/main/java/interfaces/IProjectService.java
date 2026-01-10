package interfaces;

import models.Project;

import java.util.List;

/**
 * ProjectService interface following Dependency Inversion Principle (DIP)
 */
public interface IProjectService {
    void addProject(Project project);
    Project getProjectById(String id);
    List<Project> getAllProjects();
    void deleteProjectById(String id);
    List<Project> filterByType(String type);
    List<Project> findByBudgetRange(double min, double max);
}

