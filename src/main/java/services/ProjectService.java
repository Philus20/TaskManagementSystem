// services/ProjectService.java
package services;

import Repository.ProjectRepository;
import interfaces.IProjectService;
import interfaces.ProjectFilter;
import interfaces.IStreamService;
import models.Project;
import models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ProjectService following SOLID principles:
 * - Single Responsibility: Manages project business logic only
 * - Dependency Inversion: Depends on ProjectRepository abstraction
 * - Enhanced with functional programming for Week 3
 */
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final  GenerateProjectId projectIdGenerator;
    private final FilePersistenceService filePersistenceService;
    private TaskService taskService;
    private IStreamService streamService;

    public ProjectService(ProjectRepository projectRepository, GenerateProjectId projectIdGenerator) {
        this.projectRepository = projectRepository;
        this.projectIdGenerator = projectIdGenerator;
        this.filePersistenceService = new FilePersistenceService();
        this.taskService = null; // Will be injected later to avoid circular dependency
        this.streamService = null; // Will be injected later
    }
    
    /**
     * Load projects from file - call this after all dependencies are initialized
     */
    public void loadProjectsFromFile() {
        try {
            Map<String, Project> loadedProjects = filePersistenceService.loadProjects();
            
            // Update the ID generator counter based on existing projects
            if (!loadedProjects.isEmpty()) {
                GenerateProjectId.updateCounterFromExistingIds(new ArrayList<>(loadedProjects.keySet()));
            }
            
            for (Map.Entry<String, Project> entry : loadedProjects.entrySet()) {
                projectRepository.add(entry.getValue(), entry.getKey());
            }
            System.out.println("Loaded " + loadedProjects.size() + " projects from file.");
        } catch (Exception e) {
            System.out.println("Starting with empty project list: " + e.getMessage());
        }
    }




    // Add a project with validation and persistence
    public void addProject(Project project) {
        // Validate project ID format
        if (project.getId() != null && !ValidationService.isValidProjectId(project.getId())) {
            throw new IllegalArgumentException("Invalid project ID format: " + project.getId() + 
                                             ". Expected format: P001, P002, etc.");
        }

        // Generate ID if not set
        if (project.getId() == null || project.getId().isEmpty()) {
            String generatedId = generateUniqueProjectId();
            project.setId(generatedId);
        }

        projectRepository.add(project, project.getId());
        saveProjectsToFile();
    }

    /**
     * Generate a unique project ID using the synchronized counter
     */
    private String generateUniqueProjectId() {
        return projectIdGenerator.generate();
    }

    // Get project by id
    public Project getProjectById(String id) {
      //  int index =   projectIdGenerator.elementIndex(id);
        return (Project) projectRepository.getById(id);
    }

    public List<Project> getAllProjects(){

        return projectRepository.getAll();
    }
    //Delete A project by id with persistence
    public void deleteProjectById(String id) {
        if (!ValidationService.isValidProjectId(id)) {
            throw new IllegalArgumentException("Invalid project ID format: " + id + 
                                             ". Expected format: P001, P002, etc.");
        }
        
        projectRepository.removeById(id);
        saveProjectsToFile();
    }

    @Override
    public List<Project> filterByType(String type) {
        if (!ValidationService.isValidProjectType(type)) {
            throw new IllegalArgumentException("Invalid project type: " + type + 
                                             ". Valid types: Software, Hardware");
        }
        return projectRepository.findByType(type);
    }

    @Override
    public List<Project> findByBudgetRange(double min, double max) {
        return projectRepository.findByBudgetRange(min, max);
    }
    
    /**
     * Functional filtering using ProjectFilter interface
     */
    public List<Project> filterProjects(ProjectFilter filter) {
        return projectRepository.getAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
    
    /**
     * Stream-based project analysis
     */
    public double getAverageBudget() {
        if (streamService != null) {
            try {
                return streamService.getProjectBudgetAnalyticsConcurrently()
                        .get(5, java.util.concurrent.TimeUnit.SECONDS)
                        .getOrDefault("averageBudget", 0.0);
            } catch (Exception e) {
                System.err.println("StreamService calculation failed, falling back to traditional method: " + e.getMessage());
            }
        }
        
        return projectRepository.getAll().stream()
                .mapToDouble(Project::getBudget)
                .average()
                .orElse(0.0);
    }
    
    public long getProjectCountByType(String type) {
        return projectRepository.getAll().stream()
                .filter(project -> type.equalsIgnoreCase(project.getType()))
                .count();
    }
    
    public List<Project> getCompletedProjects() {
        if (streamService != null) {
            try {
                return streamService.getTopPerformingProjectsConcurrently(Integer.MAX_VALUE)
                        .get(5, java.util.concurrent.TimeUnit.SECONDS)
                        .stream()
                        .map(projectId -> projectRepository.getById(projectId))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                System.err.println("StreamService calculation failed, falling back to traditional method: " + e.getMessage());
            }
        }
        
        // Projects with completion rate >= 100%
        return projectRepository.getAll().stream()
                .filter(project -> {
                    // This would need TaskService integration for accurate completion
                    return project.getTeamSize() > 0; // Placeholder logic
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Save projects to file using NIO
     */
    public synchronized void saveProjectsToFile() {
        try {
            Map<String, Project> projects = projectRepository.getAll().stream()
                    .collect(Collectors.toMap(
                        Project::getId,
                        project -> project
                    ));
            
            // Only include tasks if TaskService is available
            List<Task> allTasks = (taskService != null) ? taskService.getAllTasks() : new ArrayList<>();
            filePersistenceService.saveProjects(projects, allTasks);
        } catch (Exception e) {
            System.err.println("Failed to save projects: " + e.getMessage());
        }
    }
    
    /**
     * Set TaskService dependency (injected after construction to avoid circular dependency)
     */
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Set StreamService dependency
     */
    public void setStreamService(IStreamService streamService) {
        this.streamService = streamService;
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
