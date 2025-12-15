// services/ProjectService.java
package services;

import Repository.ProjectRepository;
import interfaces.IProjectService;
import models.Project;

/**
 * ProjectService following SOLID principles:
 * - Single Responsibility: Manages project business logic only
 * - Dependency Inversion: Depends on ProjectRepository abstraction
 */
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final  GenerateProjectId projectIdGenerator;



    public ProjectService( ProjectRepository projectRepository, GenerateProjectId projectIdGenerator) {

        this.projectRepository = projectRepository;
        this.projectIdGenerator = projectIdGenerator;

    }




    // Add a project (store in array slot based on ID number)
    public void addProject(Project project) {

        // Generate ID if not set
        if (project.getId() == null || project.getId().isEmpty()) {
            String generatedId = projectIdGenerator.generate();
            project.setId(generatedId);
        }
        int index = projectIdGenerator.elementIndex(project.getId());
        projectRepository.add(project,index);
    }

    // Get project by id
    public Project getProjectById(String id) {
      int index =   projectIdGenerator.elementIndex(id);
        return (Project) projectRepository.getById(index);
    }

    public Project [] getAllProjects(){

        return projectRepository.getAll();
    }
    //Delete A project by id
    public void deleteProjectById(String id) {

        projectRepository.removeById(projectIdGenerator.elementIndex(id));
    }

    @Override
    public Project[] filterByType(String type) {
       return projectRepository.findByType(type);
    }

    @Override
    public Project[] findByBudgetRange(double min, double max) {

        return projectRepository.findByBudgetRange(min,max);
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
