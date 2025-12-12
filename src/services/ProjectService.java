// services/ProjectService.java
package services;

import Repository.ProjectRepository;
import interfaces.IProjectService;
import models.Project;
import utils.exceptions.EmptyProjectException;

/**
 * ProjectService following SOLID principles:
 * - Single Responsibility: Manages project business logic only
 * - Dependency Inversion: Depends on ProjectRepository abstraction
 */
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService( ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;

    }




    // Add a project (store in array slot based on ID number)
    public void addProject(Project project, int index) {
        projectRepository.add(project,index);
    }

    // Get project by id
    public Project getProjectById(int id) {
        return (Project) projectRepository.getById(id);
    }

    public Project [] getAllProjects(){
        return projectRepository.getAll();
    }
    //Delete A project by id
    public void deleteProjectById(int id) {
        projectRepository.removeById(id);
    }





//    public  void displayAllProjects() {
//        ConsoleMenu.displayProjects(getAllProjectsComplete());
//    }




    // Get project status report (arrays-only)
//    public ProjectStatusReportDto[] getProjectStatusReport(TaskService sourceTaskService) {
//        // temp buffer sized to number of project slots (worst case)
//        ProjectStatusReportDto[] temp = new ProjectStatusReportDto[projectsRepository.length];
//        int count = 0;
//
//        if (sourceTaskService == null) {
//            return Arrays.copyOf(temp, 0); // return empty array
//        }
//
//        for (int i = 0; i < projectsRepository.length; i++) {
//            Project project = projectsRepository[i];
//            if (project == null || project.getId() == null) continue;
//
//            // assume Task[] is returned; handle null
//            Task[] tasks = sourceTaskService.getTasksByProjectId(project.getId());
//            int totalTasks = (tasks == null) ? 0 : tasks.length;
//
//            int completedCount = 0;
//            if (tasks != null) {
//                for (int t = 0; t < tasks.length; t++) {
//                    Task task = tasks[t];
//                    if (task == null) continue;
//                    String status = task.getTaskStatus();
//                    if (status != null && status.equalsIgnoreCase("Completed")) {
//                        completedCount++;
//                    }
//                }
//            }
//
//            // create DTO and put into temp buffer
//            temp[count++] = new ProjectStatusReportDto(
//                    project.getId(),
//                    project.getName(),
//                    totalTasks,
//                    completedCount
//            );
//        }
//
//        // return trimmed array (no null slots)
//        return Arrays.copyOf(temp, count);
//    }


}
