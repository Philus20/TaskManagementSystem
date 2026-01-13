package services;

import models.Task;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Concurrent task update service for Week 3 requirements
 * Provides thread-safe task updates with progress logging
 */
public class ConcurrentTaskUpdateService {
    
    private final TaskService taskService;
    private final ExecutorService executorService;
    private final AtomicInteger completedUpdates;
    
    public ConcurrentTaskUpdateService(TaskService taskService) {
        this.taskService = taskService;
        this.executorService = Executors.newFixedThreadPool(4);
        this.completedUpdates = new AtomicInteger(0);
    }
    
    /**
     * Add multiple tasks concurrently for faster performance
     */
    public CompletableFuture<List<Task>> addTasksConcurrently(List<Task> tasks) {
        System.out.println("Starting concurrent task creation for " + tasks.size() + " tasks...");
        
        List<CompletableFuture<Task>> futures = tasks.stream()
            .map(task -> CompletableFuture.supplyAsync(() -> {
                try {
                    // Simulate task creation time
                    Thread.sleep(50 + (int)(Math.random() * 100));
                    taskService.addTask(task);
                    System.out.println(Thread.currentThread().getName() + " - Created task: " + task.getTaskId());
                    return task;
                } catch (Exception e) {
                    System.err.println("Error creating task: " + e.getMessage());
                    return null;
                }
            }, executorService))
            .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(task -> task != null)
                .collect(Collectors.toList()));
    }
    
    /**
     * Simulate concurrent task status updates using multiple threads
     */
    public CompletableFuture<List<Task>> updateTasksConcurrently(List<String> taskIds, String newStatus) {
        System.out.println("Starting concurrent task updates for " + taskIds.size() + " tasks...");
        
        List<CompletableFuture<Task>> futures = taskIds.stream()
            .map(taskId -> CompletableFuture.supplyAsync(() -> {
                try {
                    updateTaskWithLogging(taskId, newStatus);
                    return taskService.getTaskById(taskId); // Return the updated task
                } catch (Exception e) {
                    System.err.println("Error updating task " + taskId + ": " + e.getMessage());
                    return null;
                }
            }, executorService))
            .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(task -> task != null)
                .collect(Collectors.toList()));
    }
    
    
    /**
     * Update task with progress logging
     */
    private synchronized void updateTaskWithLogging(String taskId, String newStatus) {
        try {
            // Simulate some processing time
            Thread.sleep(100 + (int)(Math.random() * 200));
            
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " - Updating task " + taskId + " to status: " + newStatus);
            
            Task task = taskService.getTaskById(taskId);
            if (task != null) {
                String oldStatus = task.getTaskStatus();
                Task updatedTask = taskService.updateTaskStatus(taskId, newStatus);
                
                if (updatedTask != null) {
                    int count = completedUpdates.incrementAndGet();
                    System.out.println(threadName + " - Successfully updated task " + taskId + 
                                     " from " + oldStatus + " to " + newStatus + 
                                     " (Update #" + count + ")");
                } else {
                    System.err.println(threadName + " - Failed to update task " + taskId);
                }
            } else {
                System.err.println(threadName + " - Task " + taskId + " not found");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + " - Interrupted while updating task " + taskId);
        } catch (Exception e) {
            System.err.println(Thread.currentThread().getName() + " - Error updating task " + taskId + ": " + e.getMessage());
        }
    }
    
    /**
     * Batch update tasks using parallel streams
     */
    public void updateTasksWithParallelStream(List<String> taskIds, String newStatus) {
        System.out.println("Starting parallel stream updates for " + taskIds.size() + " tasks...");
        
        AtomicInteger updateCount = new AtomicInteger(0);
        
        taskIds.parallelStream()
            .forEach(taskId -> {
                try {
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + " - Processing task " + taskId);
                    
                    Task task = taskService.getTaskById(taskId);
                    if (task != null) {
                        String oldStatus = task.getTaskStatus();
                        Task updatedTask = taskService.updateTaskStatus(taskId, newStatus);
                        
                        if (updatedTask != null) {
                            int count = updateCount.incrementAndGet();
                            System.out.println(threadName + " - Updated task " + taskId + 
                                             " from " + oldStatus + " to " + newStatus + 
                                             " (Update #" + count + ")");
                        }
                    }
                    
                    // Simulate processing time
                    Thread.sleep(50);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("Error in parallel stream update: " + e.getMessage());
                }
            });
        
        System.out.println("Parallel stream updates completed. Total updated: " + updateCount.get());
    }
    
    /**
     * Demonstrate thread-safe task creation
     */
    public CompletableFuture<List<Task>> createTasksConcurrently(List<Task> tasks) {
        System.out.println("Starting concurrent task creation for " + tasks.size() + " tasks...");
        
        List<CompletableFuture<Task>> futures = tasks.stream()
            .map(task -> CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(50 + (int)(Math.random() * 100));
                    taskService.addTask(task);
                    System.out.println(Thread.currentThread().getName() + " - Created task: " + task.getTaskId());
                    return task;
                } catch (Exception e) {
                    System.err.println("Error creating task: " + e.getMessage());
                    return null;
                }
            }, executorService))
            .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(task -> task != null)
                .collect(Collectors.toList()));
    }
    
    /**
     * Get completion rate using parallel computation
     */
    public double calculateCompletionRateConcurrently(String projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        
        if (tasks.isEmpty()) {
            return 0.0;
        }
        
        // Use parallel stream for counting completed tasks
        long completedCount = tasks.parallelStream()
            .filter(task -> task != null && "Completed".equalsIgnoreCase(task.getTaskStatus()))
            .count();
        
        return (completedCount * 100.0) / tasks.size();
    }
    
    /**
     * Shutdown the executor service
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Get current update statistics
     */
    public int getCompletedUpdateCount() {
        return completedUpdates.get();
    }
}
