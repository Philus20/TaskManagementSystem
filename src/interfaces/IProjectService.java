package interfaces;

import models.Project;

/**
 * ProjectService interface following Dependency Inversion Principle (DIP)
 */
public interface IProjectService {
    void addProject(Project project, int index);
    Project getProjectById(int id);
    Project[] getAllProjects();
    void deleteProjectById(int id);
}

