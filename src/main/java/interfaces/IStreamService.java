package interfaces;

import models.Project;
import models.Task;
import models.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Stream Service interface for handling all streaming operations
 * Following SOLID principles with functional programming approach
 */
public interface IStreamService {
    
    // Task streaming operations
    Stream<Task> streamAllTasks();
    Stream<Task> streamTasksByProjectId(String projectId);
    Stream<Task> streamTasksByStatus(String status);
    Stream<Task> streamTasksByAssignedUser(String userId);
    CompletableFuture<List<Task>> streamTasksConcurrently(Predicate<Task> filter);
    
    // Project streaming operations
    Stream<Project> streamAllProjects();
    Stream<Project> streamProjectsByType(String type);
    Stream<Project> streamProjectsByBudgetRange(double min, double max);
    CompletableFuture<List<Project>> streamProjectsConcurrently(Predicate<Project> filter);
    
    // User streaming operations
    Stream<User> streamAllUsers();
    Stream<User> streamUsersByRole(String role);
    CompletableFuture<List<User>> streamUsersConcurrently(Predicate<User> filter);
    
    // Analytics and aggregation operations
    CompletableFuture<Double> calculateProjectCompletionRateConcurrently(String projectId);
    CompletableFuture<Map<String, Long>> getTaskStatusDistributionConcurrently(String projectId);
    CompletableFuture<Map<String, Double>> getProjectBudgetAnalyticsConcurrently();
    CompletableFuture<List<String>> getTopPerformingProjectsConcurrently(int limit);
    
    // Batch operations with streaming
    CompletableFuture<List<Task>> batchUpdateTasksConcurrently(List<String> taskIds, String newStatus);
    CompletableFuture<List<Project>> batchUpdateProjectsConcurrently(List<String> projectIds, Predicate<Project> updateCondition);
    
    // File streaming operations
    CompletableFuture<Void> streamTasksToFileAsync(Stream<Task> taskStream);
    CompletableFuture<Void> streamProjectsToFileAsync(Stream<Project> projectStream);
    CompletableFuture<Void> streamUsersToFileAsync(Stream<User> userStream);
    
    // Real-time monitoring operations
    CompletableFuture<Void> monitorTaskProgressConcurrently(String projectId);
    CompletableFuture<Void> generateRealTimeReportConcurrently();
    
    // Utility operations
    <T> CompletableFuture<List<T>> parallelProcessStream(Stream<T> stream, java.util.function.Function<T, T> processor);
    CompletableFuture<Void> shutdown();
}
