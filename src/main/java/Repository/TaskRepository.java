package Repository;

import interfaces.Completable;
import interfaces.IRepository;
import models.Task;
import services.GenerateTaskId;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.TaskNotFoundException;
import utils.exceptions.UserNotFoundException;

import java.util.*;

/**
 * TaskRepository following Single Responsibility Principle (SRP)
 * - Only responsible for Task data persistence
 * - Implements IRepository for Dependency Inversion (DIP)
 */
public class TaskRepository implements IRepository<Task>, Completable {

    private List<Task> tasks;
    private GenerateTaskId taskIdGenerator;

    public TaskRepository() {
        this.taskIdGenerator = new GenerateTaskId();
        this.tasks = new ArrayList<>();
    }



    @Override
    public void add(Task task, String taskId) {
        if (task == null) throw new TaskNotFoundException("Task cannot be null");
        int idx = this.taskIdGenerator.elementIndex(taskId);
        if (idx < 0 || idx >= tasks.size()) {
            // Expand list if needed
            while (tasks.size() <= idx) {
                tasks.add(null);
            }
        }
        
        if (tasks.contains(task))
            throw new TaskNotFoundException("Task already exists at index " + taskId);

        tasks.add(idx, task);
    }

    @Override
    public Task getById(String taskId) {
        int idx = taskIdGenerator.elementIndex(taskId);

        if (idx < 0 || idx >= tasks.size()) {
            // Expand list if needed
            while (tasks.size() <= idx) {
                tasks.add(null);
            }
        }

        Task task = tasks.get(idx);
        if (task == null) {
            throw new TaskNotFoundException("No Task Found for this Id: " + taskId);
        }

        return task;
    }


    @Override
    public List<Task> getAll() {
        return tasks;
    }

    @Override
    public void update(String taskId, Task task) {
        int idx = taskIdGenerator.elementIndex(taskId);
        if (idx < 0 || idx >= tasks.size()) {
            // Expand list if needed
            while (tasks.size() <= idx) {
                tasks.add(null);
            }
        }
        tasks.set(idx, task);
    }


    @Override
    public void removeById(String taskId) {
        int idx = taskIdGenerator.elementIndex(taskId);
        if (idx < 0 || idx >= tasks.size()) {
            return; // Task doesn't exist, nothing to remove
        }
        tasks.remove(idx);
    }

    /**
     * Query helpers following Open/Closed Principle (OCP)
     * - Open for extension (can add more query methods)
     * - Closed for modification
     */
    public List<Task> findByProjectId(String projectId) {
        if (projectId == null) throw new EmptyProjectException("Project ID cannot be null");
        return tasks.stream()
                .filter(t -> t != null && projectId.equals(t.getProjectId()))
                .toList();
    }

    public Task findByTaskId(String taskId) {
        if (taskId == null) throw new TaskNotFoundException("Task ID cannot be null");
        return tasks.stream()
                .filter(t -> t != null && taskId.equals(t.getTaskId()))
                .findFirst()
                .orElse(null);
    }

    public List<Task> findByAssignedUserId(String userId) {
        if (userId == null) throw new UserNotFoundException("User ID cannot be null");
        return tasks.stream()
                .filter(t -> t != null && userId.equals(t.getAssignedUserId()))
                .toList();
    }

    @Override
    public void markAsComplete(Task task) {

        task.setTaskStatus("Completed");

    }

}
