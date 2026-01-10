package interfaces;

import models.Task;

import java.util.List;

/**
 * TaskService interface following Dependency Inversion Principle (DIP)
 * High-level modules should depend on abstractions, not concretions
 */
public interface ITaskService {
    void addTask(Task task);
    List<Task> getAllTasks();
    Task getTaskById(String taskId);
    Task updateTaskStatus(String taskId, String taskStatus);
    void deleteTask(String taskId);
    List<Task> getTasksByProjectId(String projectId);
    double calculateCompletionRate(String projectId);
}

