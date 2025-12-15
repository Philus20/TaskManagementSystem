package interfaces;

import models.Project;

/**
 * ProjectService interface following Dependency Inversion Principle (DIP)
 */
public interface IProjectService {
    void addProject(Project project);
    Project getProjectById(String id);
    Project[] getAllProjects();
    void deleteProjectById(String id);
    Project[] filterByType(String type);
    Project[] findByBudgetRange(double min, double max);
}

