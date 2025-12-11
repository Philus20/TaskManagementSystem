package services;

import Repository.TaskRepository;
import interfaces.IdGenerator;
import interfaces.ITaskService;
import models.Task;

/**
 * TaskService following SOLID principles:
 * - Single Responsibility: Manages task business logic only
 * - Dependency Inversion: Depends on TaskRepository abstraction, not concrete implementation
 * - Open/Closed: Can be extended without modification
 */
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IdGenerator taskIdGenerator;

    public TaskService(TaskRepository taskRepository, IdGenerator taskIdGenerator) {
        if (taskRepository == null) throw new IllegalArgumentException("TaskRepository cannot be null");
        if (taskIdGenerator == null) throw new IllegalArgumentException("TaskIdGenerator cannot be null");
        this.taskRepository = taskRepository;
        this.taskIdGenerator = taskIdGenerator;
    }

    /**
     * Add a new task with auto-generated ID
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        // Generate ID if not set
        if (task.getTaskId() == null || task.getTaskId().isEmpty()) {
            String generatedId = taskIdGenerator.generate();
            task.setTaskId(generatedId);
        }

        // Check for duplicate task ID
        Task existing = taskRepository.findByTaskId(task.getTaskId());
        if (existing != null) {
            throw new IllegalStateException("Task with id " + task.getTaskId() + " already exists.");
        }

        int index = taskIdGenerator.elementIndex(task.getTaskId());
        taskRepository.add(task, index);
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
        if (taskId == null) return null;
        return taskRepository.findByTaskId(taskId);
    }

    /**
     * Update task status
     */
    public Task updateTaskStatus(String taskId, String taskStatus) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setTaskStatus(taskStatus);
            // Update in repository
            int index = taskIdGenerator.elementIndex(taskId);
            taskRepository.update(index, task);
        }
        return task;
    }

    /**
     * Delete a task
     */
    public void deleteTask(String taskId) {
        if (taskId == null) return;
        Task task = getTaskById(taskId);
        if (task != null) {
            int index = taskIdGenerator.elementIndex(taskId);
            taskRepository.removeById(index);
        }
    }

    /**
     * Get tasks by project ID
     */
    public Task[] getTasksByProjectId(String projectId) {
        if (projectId == null) return new Task[0];
        return taskRepository.findByProjectId(projectId);
    }

    /**
     * Calculate completion rate for a project
     */
    public double calculateCompletionRate(String projectId) {
        Task[] projectTasks = getTasksByProjectId(projectId);
        if (projectTasks == null || projectTasks.length == 0) return 0.0;

        int completed = 0;
        for (Task t : projectTasks) {
            if (t != null && "Completed".equalsIgnoreCase(t.getTaskStatus())) {
                completed++;
            }
        }
        return (completed * 100.0) / projectTasks.length;
    }

    /**
     * Get tasks assigned to a user
     */
    public Task[] getTasksByAssignedUserId(String userId) {
        if (userId == null) return new Task[0];
        return taskRepository.findByAssignedUserId(userId);
    }
}
