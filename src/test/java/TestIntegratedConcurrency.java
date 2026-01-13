import services.*;
import models.Task;
import Repository.TaskRepository;
import Repository.ProjectRepository;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestIntegratedConcurrency {
    public static void main(String[] args) {
        System.out.println("=== Testing Integrated Concurrency ===\n");
        
        // Create services with dependency injection
        TaskRepository taskRepo = new TaskRepository();
        TaskService taskService = new TaskService(taskRepo, new GenerateTaskId());
        ConcurrentTaskUpdateService concurrentService = new ConcurrentTaskUpdateService(taskService);
        
        // Inject concurrency service
        taskService.setConcurrentUpdateService(concurrentService);
        
        // Test 1: Add tasks concurrently
        System.out.println("1. Testing concurrent task creation:");
        List<Task> tasksToAdd = Arrays.asList(
            new Task("Task A", "Pending", "P001"),
            new Task("Task B", "Pending", "P001"),
            new Task("Task C", "Pending", "P001")
        );
        
        long startTime = System.currentTimeMillis();
        CompletableFuture<List<Task>> addFuture = taskService.addTasksConcurrently(tasksToAdd);
        List<Task> addedTasks = addFuture.join();
        long endTime = System.currentTimeMillis();
        
        System.out.println("Added " + addedTasks.size() + " tasks concurrently in " + (endTime - startTime) + "ms");
        
        // Test 2: Update tasks concurrently
        System.out.println("\n2. Testing concurrent task updates:");
        List<String> taskIds = addedTasks.stream()
            .map(Task::getTaskId)
            .limit(3)
            .toList();
        
        startTime = System.currentTimeMillis();
        CompletableFuture<List<Task>> updateFuture = taskService.updateTasksConcurrently(taskIds, "Completed");
        List<Task> updatedTasks = updateFuture.join();
        endTime = System.currentTimeMillis();
        
        System.out.println("Updated " + updatedTasks.size() + " tasks concurrently in " + (endTime - startTime) + "ms");
        
        // Show results
        System.out.println("\n=== Results ===");
        updatedTasks.forEach(task -> 
            System.out.println("Task " + task.getTaskId() + ": " + task.getTaskStatus())
        );
        
        concurrentService.shutdown();
        System.out.println("\nConcurrency integration test completed successfully!");
    }
}
