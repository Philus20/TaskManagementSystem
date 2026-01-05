package services;

import Repository.TaskRepository;
import interfaces.IdGenerator;
import interfaces.ITaskService;
import models.Task;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.TaskNotFoundException;

/**
 * TaskService following SOLID principles:
 * - Single Responsibility: Manages task business logic only
 * - Dependency Inversion: Depends on TaskRepository abstraction, not concrete
 * implementation
 * - Open/Closed: Can be extended without modification
 */
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IdGenerator taskIdGenerator;

    public TaskService(TaskRepository taskRepository, IdGenerator taskIdGenerator) {
        if (taskRepository == null)
            throw new IllegalArgumentException("TaskRepository cannot be null");
        if (taskIdGenerator == null)
            throw new IllegalArgumentException("TaskIdGenerator cannot be null");
        this.taskRepository = taskRepository;
        this.taskIdGenerator = taskIdGenerator;
    }

    /**
     * Add a new task with auto-generated ID
     * Enhanced with try-catch-finally for robust exception handling
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        String generatedId = null;
        try {
            // Generate ID if not set
            if (task.getTaskId() == null || task.getTaskId().isEmpty()) {
                generatedId = taskIdGenerator.generate();
                task.setTaskId(generatedId);
            }

            // Check for duplicate task ID
            Task existing = taskRepository.findByTaskId(task.getTaskId());
            if (existing != null) {
                throw new IllegalStateException("Task with id " + task.getTaskId() + " already exists.");
            }

            int index = taskIdGenerator.elementIndex(task.getTaskId());
            taskRepository.add(task, index);
        } catch (TaskNotFoundException e) {
            // Re-throw with more context
            throw new IllegalStateException("Failed to add task: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any unexpected exceptions
            throw new IllegalStateException("Unexpected error while adding task: " + e.getMessage(), e);
        } finally {
            // Cleanup: Ensure task state is consistent
            // In this case, no cleanup needed, but finally block ensures execution
        }
    }

    /**
     * Get all tasks
     */
    public Task[] getAllTasks() {
        return taskRepository.getAll();
    }

    /**
     * Get task by ID
     */
    public Task getTaskById(String taskId) {
        if (taskId == null)
            return null;
        return taskRepository.findByTaskId(taskId);
    }

    /**
     * Update task status
     * Enhanced with try-catch-finally for robust exception handling
     */
    public Task updateTaskStatus(String taskId, String taskStatus) {
        if (taskId == null) {
            return null;
        }

        Task task = null;
        try {
            task = getTaskById(taskId);
            if (task != null) {
                if(taskStatus == "Completed")taskRepository.markAsComplete(task);
               else task.setTaskStatus(taskStatus);
                // Update in repository
                int index = taskIdGenerator.elementIndex(taskId);
                taskRepository.update(index, task);
            }
        } catch (TaskNotFoundException e) {
            // Task not found - return null gracefully
            return null;
        } catch (IllegalArgumentException e) {
            // Invalid task ID format - return null gracefully
            return null;
        } catch (Exception e) {
            // Handle any unexpected exceptions
            throw new IllegalStateException("Unexpected error while updating task status: " + e.getMessage(), e);
        } finally {
            // Cleanup: Ensure state is consistent
            // In this case, no cleanup needed
        }
        return task;
    }

    /**
     * Delete a task
     * Enhanced with try-catch-finally for robust exception handling
     */
    public void deleteTask(String taskId) {
        if (taskId == null) {
            return;
        }

        try {
            Task task = getTaskById(taskId);
            if (task != null) {
                int index = taskIdGenerator.elementIndex(taskId);
                taskRepository.removeById(index);
            }
        } catch (TaskNotFoundException e) {
            // Task not found - handle gracefully (no-op or log)
            // In this case, we'll silently ignore as task doesn't exist
        } catch (IllegalArgumentException e) {
            // Invalid task ID format - handle gracefully
            // In this case, we'll silently ignore
        } catch (Exception e) {
            // Handle any unexpected exceptions
            throw new IllegalStateException("Unexpected error while deleting task: " + e.getMessage(), e);
        } finally {
            // Cleanup: Ensure state is consistent
            // In this case, no cleanup needed
        }
    }

    /**
     * Get tasks by project ID
     * Enhanced with try-catch for robust exception handling
     */
    public Task[] getTasksByProjectId(String projectId) {
        if (projectId == null) {
            return new Task[0];
        }

        try {
            return taskRepository.findByProjectId(projectId);
        } catch (EmptyProjectException e) {
            // Project ID is null or invalid - return empty array gracefully
            return new Task[0];
        } catch (Exception e) {
            // Handle any unexpected exceptions
            throw new IllegalStateException("Unexpected error while retrieving tasks by project ID: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Calculate completion rate for a project
     * Enhanced with try-catch for robust exception handling and division by zero
     * protection
     */
    public double calculateCompletionRate(String projectId) {
        try {
            Task[] projectTasks = getTasksByProjectId(projectId);
            if (projectTasks == null || projectTasks.length == 0) {
                return 0.0;
            }

            int completed = 0;
            for (Task t : projectTasks) {
                if (t != null && "Completed".equalsIgnoreCase(t.getTaskStatus())) {
                    completed++;
                }
            }

            // Protection against division by zero (shouldn't happen, but safety first)
            if (projectTasks.length == 0) {
                return 0.0;
            }

            return (completed * 100.0) / projectTasks.length;
        } catch (ArithmeticException e) {
            // Division by zero protection
            return 0.0;
        } catch (Exception e) {
            // Handle any unexpected exceptions gracefully
            throw new IllegalStateException("Unexpected error while calculating completion rate: " + e.getMessage(), e);
        }
    }

    /**
     * Get tasks assigned to a user
     */
    public Task[] getTasksByAssignedUserId(String userId) {
        if (userId == null)
            return new Task[0];
        return taskRepository.findByAssignedUserId(userId);
    }
}
