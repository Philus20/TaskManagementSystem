package interfaces;

import models.Task;

/**
 * TaskService interface following Dependency Inversion Principle (DIP)
 * High-level modules should depend on abstractions, not concretions
 */
public interface ITaskService {
    void addTask(Task task);
    Task[] getAllTasks();
    Task getTaskById(String taskId);
    Task updateTaskStatus(String taskId, String taskStatus);
    void deleteTask(String taskId);
    Task[] getTasksByProjectId(String projectId);
    double calculateCompletionRate(String projectId);
}

