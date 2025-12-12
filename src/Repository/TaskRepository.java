package Repository;

import interfaces.IRepository;
import models.Task;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.EntityAttributeException;
import utils.exceptions.TaskNotFoundException;
import utils.exceptions.UserNotFoundException;

import java.util.Arrays;

/**
 * TaskRepository following Single Responsibility Principle (SRP)
 * - Only responsible for Task data persistence
 * - Implements IRepository for Dependency Inversion (DIP)
 */
public class TaskRepository implements IRepository<Task> {

    private Task[] tasks;

    public TaskRepository(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = 50;
        this.tasks = new Task[initialCapacity];
    }

    private void ensureCapacity(int index) {
        if (index < tasks.length) return;

        int newCapacity = Math.max(tasks.length * 2, 1);
        while (newCapacity <= index) newCapacity *= 2;
        tasks = Arrays.copyOf(tasks, newCapacity);
    }

    @Override
    public void add(Task task, int index) {
        if (task == null) throw new TaskNotFoundException("Task cannot be null");
        ensureCapacity(index);
        if (tasks[index] != null)
            throw new TaskNotFoundException("Task already exists at index " + index);

        tasks[index] = task;
    }

    @Override
    public Task getById(int index) {
        if (index < 0 || index >= tasks.length) return null;
        return tasks[index];
    }

    @Override
    public Task[] getAll() {
        // Return trimmed array (no null slots)
        int count = 0;
        for (Task t : tasks) if (t != null) count++;
        Task[] result = new Task[count];
        int i = 0;
        for (Task t : tasks) if (t != null) result[i++] = t;
        return result;
    }

    @Override
    public void update(int index, Task task) {
        if (index < 0) throw new TaskNotFoundException("Invalid Index");
        ensureCapacity(index);
        tasks[index] = task;
    }

    @Override
    public void removeById(int index) {
        if (index < 0 || index >= tasks.length) throw  new TaskNotFoundException("Invalid Index");
        // Compact array by shifting elements
        for (int j = index; j < tasks.length - 1; j++) {
            tasks[j] = tasks[j + 1];
        }
        tasks[tasks.length - 1] = null;
    }

    /**
     * Query helpers following Open/Closed Principle (OCP)
     * - Open for extension (can add more query methods)
     * - Closed for modification
     */
    public Task[] findByProjectId(String projectId) {
        if (projectId == null) throw new EmptyProjectException("Project ID cannot be null");
        return Arrays.stream(tasks)
                .filter(t -> t != null && projectId.equals(t.getProjectId()))
                .toArray(Task[]::new);
    }

    public Task findByTaskId(String taskId) {
        if (taskId == null) throw new TaskNotFoundException("Project ID cannot be null");
        return Arrays.stream(tasks)
                .filter(t -> t != null && taskId.equals(t.getTaskId()))
                .findFirst()
                .orElse(null);
    }

    public Task[] findByAssignedUserId(String userId) {
        if (userId == null) throw new UserNotFoundException("User ID cannot be null");
        return Arrays.stream(tasks)
                .filter(t -> t != null && userId.equals(t.getAssignedUserId()))
                .toArray(Task[]::new);
    }
}
