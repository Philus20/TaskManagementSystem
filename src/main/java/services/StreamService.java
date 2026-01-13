package services;

import interfaces.IStreamService;
import interfaces.TaskFilter;
import interfaces.ProjectFilter;
import models.Project;
import models.Task;
import models.User;
import utils.FileUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream Service implementation for handling all streaming operations
 * Provides high-performance, thread-safe streaming with functional programming
 */
public class StreamService implements IStreamService {
    
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutor;
    private final AtomicInteger activeOperations;
    
    public StreamService(TaskService taskService, ProjectService projectService, 
                        UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.scheduledExecutor = Executors.newScheduledThreadPool(2);
        this.activeOperations = new AtomicInteger(0);
    }
    
    // Task streaming operations
    @Override
    public Stream<Task> streamAllTasks() {
        return taskService.getAllTasks().stream()
                .filter(Objects::nonNull);
    }
    
    @Override
    public Stream<Task> streamTasksByProjectId(String projectId) {
        return streamAllTasks()
                .filter(task -> projectId.equals(task.getProjectId()));
    }
    
    @Override
    public Stream<Task> streamTasksByStatus(String status) {
        return streamAllTasks()
                .filter(task -> status.equalsIgnoreCase(task.getTaskStatus()));
    }
    
    @Override
    public Stream<Task> streamTasksByAssignedUser(String userId) {
        return streamAllTasks()
                .filter(task -> userId.equals(task.getAssignedUserId()));
    }
    
    @Override
    public CompletableFuture<List<Task>> streamTasksConcurrently(Predicate<Task> filter) {
        return CompletableFuture.supplyAsync(() -> 
            streamAllTasks()
                .parallel()
                .filter(filter)
                .collect(Collectors.toList()),
            executorService);
    }
    
    // Project streaming operations
    @Override
    public Stream<Project> streamAllProjects() {
        return projectService.getAllProjects().stream()
                .filter(Objects::nonNull);
    }
    
    @Override
    public Stream<Project> streamProjectsByType(String type) {
        return streamAllProjects()
                .filter(project -> type.equalsIgnoreCase(project.getType()));
    }
    
    @Override
    public Stream<Project> streamProjectsByBudgetRange(double min, double max) {
        return streamAllProjects()
                .filter(project -> project.getBudget() >= min && project.getBudget() <= max);
    }
    
    @Override
    public CompletableFuture<List<Project>> streamProjectsConcurrently(Predicate<Project> filter) {
        return CompletableFuture.supplyAsync(() -> 
            streamAllProjects()
                .parallel()
                .filter(filter)
                .collect(Collectors.toList()),
            executorService);
    }
    
    // User streaming operations
    @Override
    public Stream<User> streamAllUsers() {
        return userService.getAllUsers().stream()
                .filter(Objects::nonNull);
    }
    
    @Override
    public Stream<User> streamUsersByRole(String role) {
        return streamAllUsers()
                .filter(user -> role.equalsIgnoreCase(user.getRole()));
    }
    
    @Override
    public CompletableFuture<List<User>> streamUsersConcurrently(Predicate<User> filter) {
        return CompletableFuture.supplyAsync(() -> 
            streamAllUsers()
                .parallel()
                .filter(filter)
                .collect(Collectors.toList()),
            executorService);
    }
    
    // Analytics and aggregation operations
    @Override
    public CompletableFuture<Double> calculateProjectCompletionRateConcurrently(String projectId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Task> projectTasks = streamTasksByProjectId(projectId).collect(Collectors.toList());
            if (projectTasks.isEmpty()) {
                return 0.0;
            }
            
            long completedCount = projectTasks.parallelStream()
                    .filter(task -> "Completed".equalsIgnoreCase(task.getTaskStatus()))
                    .count();
            
            return (completedCount * 100.0) / projectTasks.size();
        }, executorService);
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getTaskStatusDistributionConcurrently(String projectId) {
        return CompletableFuture.supplyAsync(() -> 
            streamTasksByProjectId(projectId)
                .parallel()
                .collect(Collectors.groupingByConcurrent(
                    Task::getTaskStatus,
                    Collectors.counting()
                )),
            executorService);
    }
    
    @Override
    public CompletableFuture<Map<String, Double>> getProjectBudgetAnalyticsConcurrently() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Double> analytics = new ConcurrentHashMap<>();
            
            // Total budget
            double totalBudget = streamAllProjects()
                    .mapToDouble(Project::getBudget)
                    .sum();
            analytics.put("totalBudget", totalBudget);
            
            // Average budget
            double avgBudget = streamAllProjects()
                    .mapToDouble(Project::getBudget)
                    .average()
                    .orElse(0.0);
            analytics.put("averageBudget", avgBudget);
            
            // Budget by type
            Map<String, Double> budgetByType = streamAllProjects()
                    .collect(Collectors.groupingByConcurrent(
                        Project::getType,
                        Collectors.summingDouble(Project::getBudget)
                    ));
            analytics.putAll(budgetByType);
            
            return analytics;
        }, executorService);
    }
    
    @Override
    public CompletableFuture<List<String>> getTopPerformingProjectsConcurrently(int limit) {
        return CompletableFuture.supplyAsync(() -> 
            streamAllProjects()
                .parallel()
                .map(project -> {
                    double completionRate = taskService.calculateCompletionRate(project.getId());
                    return Map.entry(project.getId(), completionRate);
                })
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()),
            executorService);
    }
    
    // Batch operations with streaming
    @Override
    public CompletableFuture<List<Task>> batchUpdateTasksConcurrently(List<String> taskIds, String newStatus) {
        activeOperations.incrementAndGet();
        
        List<CompletableFuture<Task>> futures = taskIds.stream()
                .map(taskId -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return taskService.updateTaskStatus(taskId, newStatus);
                    } catch (Exception e) {
                        System.err.println("Error updating task " + taskId + ": " + e.getMessage());
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    activeOperations.decrementAndGet();
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                });
    }
    
    @Override
    public CompletableFuture<List<Project>> batchUpdateProjectsConcurrently(List<String> projectIds, Predicate<Project> updateCondition) {
        activeOperations.incrementAndGet();
        
        List<CompletableFuture<Project>> futures = projectIds.stream()
                .map(projectId -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Project project = projectService.getProjectById(projectId);
                        if (project != null && updateCondition.test(project)) {
                            // Here you could add project update logic
                            return project;
                        }
                        return null;
                    } catch (Exception e) {
                        System.err.println("Error updating project " + projectId + ": " + e.getMessage());
                        return null;
                    }
                }, executorService))
                .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    activeOperations.decrementAndGet();
                    return futures.stream()
                            .map(CompletableFuture::join)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                });
    }
    
    // File streaming operations
    @Override
    public CompletableFuture<Void> streamTasksToFileAsync(Stream<Task> taskStream) {
        return CompletableFuture.runAsync(() -> {
            try {
                List<Task> tasks = taskStream.collect(Collectors.toList());
                FileUtils.saveTasks(tasks);
            } catch (Exception e) {
                System.err.println("Error streaming tasks to file: " + e.getMessage());
            }
        }, executorService);
    }
    
    @Override
    public CompletableFuture<Void> streamProjectsToFileAsync(Stream<Project> projectStream) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Project> projects = projectStream
                        .collect(Collectors.toMap(Project::getId, project -> project));
                List<Task> allTasks = taskService.getAllTasks();
                FileUtils.saveProjects(projects, allTasks);
            } catch (Exception e) {
                System.err.println("Error streaming projects to file: " + e.getMessage());
            }
        }, executorService);
    }
    
    @Override
    public CompletableFuture<Void> streamUsersToFileAsync(Stream<User> userStream) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, User> users = userStream
                        .collect(Collectors.toMap(User::getId, user -> user));
                FileUtils.saveUsers(users);
            } catch (Exception e) {
                System.err.println("Error streaming users to file: " + e.getMessage());
            }
        }, executorService);
    }
    
    // Real-time monitoring operations
    @Override
    public CompletableFuture<Void> monitorTaskProgressConcurrently(String projectId) {
        return CompletableFuture.runAsync(() -> {
            scheduledExecutor.scheduleAtFixedRate(() -> {
                try {
                    double completionRate = taskService.calculateCompletionRate(projectId);
                    System.out.println("Project " + projectId + " completion: " + 
                                     String.format("%.2f%%", completionRate));
                    
                    // Alert if completion rate is high
                    if (completionRate >= 80.0) {
                        System.out.println("ALERT: Project " + projectId + " is nearly complete!");
                    }
                } catch (Exception e) {
                    System.err.println("Error monitoring project progress: " + e.getMessage());
                }
            }, 0, 30, TimeUnit.SECONDS); // Monitor every 30 seconds
        }, executorService);
    }
    
    @Override
    public CompletableFuture<Void> generateRealTimeReportConcurrently() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Generate comprehensive report
                Map<String, Object> report = new ConcurrentHashMap<>();
                
                // Project statistics
                long totalProjects = streamAllProjects().count();
                report.put("totalProjects", totalProjects);
                
                // Task statistics
                long totalTasks = streamAllTasks().count();
                long completedTasks = streamTasksByStatus("Completed").count();
                report.put("totalTasks", totalTasks);
                report.put("completedTasks", completedTasks);
                report.put("overallCompletionRate", totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0.0);
                
                // User statistics
                long totalUsers = streamAllUsers().count();
                report.put("totalUsers", totalUsers);
                
                // Print report
                System.out.println("=== REAL-TIME SYSTEM REPORT ===");
                report.forEach((key, value) -> 
                    System.out.println(key + ": " + value));
                System.out.println("Active operations: " + activeOperations.get());
                System.out.println("================================");
                
            } catch (Exception e) {
                System.err.println("Error generating real-time report: " + e.getMessage());
            }
        }, executorService);
    }
    
    // Utility operations
    @Override
    public <T> CompletableFuture<List<T>> parallelProcessStream(Stream<T> stream, java.util.function.Function<T, T> processor) {
        return CompletableFuture.supplyAsync(() -> 
            stream.parallel()
                .map(processor)
                .collect(Collectors.toList()),
            executorService);
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            System.out.println("Shutting down StreamService...");
            
            // Shutdown executors gracefully
            executorService.shutdown();
            scheduledExecutor.shutdown();
            
            try {
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
                if (!scheduledExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    scheduledExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                scheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            
            System.out.println("StreamService shutdown complete.");
        });
    }
    
    // Additional utility methods for advanced streaming
    public CompletableFuture<Void> performHealthCheck() {
        return CompletableFuture.runAsync(() -> {
            try {
                // Check service availability
                long taskCount = streamAllTasks().count();
                long projectCount = streamAllProjects().count();
                long userCount = streamAllUsers().count();
                
                System.out.println("HEALTH CHECK - Tasks: " + taskCount + 
                                 ", Projects: " + projectCount + 
                                 ", Users: " + userCount +
                                 ", Active Operations: " + activeOperations.get());
                
                if (activeOperations.get() > 100) {
                    System.out.println("WARNING: High number of active operations detected!");
                }
                
            } catch (Exception e) {
                System.err.println("Health check failed: " + e.getMessage());
            }
        }, executorService);
    }
    
    public int getActiveOperationCount() {
        return activeOperations.get();
    }
}
