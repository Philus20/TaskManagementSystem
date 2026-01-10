package Repository;

import interfaces.Completable;
import interfaces.IRepository;
import models.Task;
import services.GenerateTaskId;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.TaskNotFoundException;
import utils.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TaskRepository following Single Responsibility Principle (SRP)
 * - Only responsible for Task data persistence
 * - Implements IRepository for Dependency Inversion (DIP)
 */
public class TaskRepository implements IRepository<Task>, Completable {

    List<Task> tasks;
    GenerateTaskId taskIdGenerator;

    public TaskRepository() {
        this.taskIdGenerator = new GenerateTaskId();
        this.tasks = new ArrayList<>() ;

    }



    @Override
    public void add(Task task, String index) {
        if (task == null) throw new TaskNotFoundException("Task cannot be null");
        int idx = this.taskIdGenerator.elementIndex(index);
        if (this.tasks.contains(task))
            throw new TaskNotFoundException("Task already exists at index " + index);

        this.tasks.add(idx, task);
    }

    @Override
    public Task getById(String index) {
        int idx = taskIdGenerator.elementIndex(index);

        if (idx < 0 || idx >= tasks.size()) {
            throw new TaskNotFoundException("No task exists for index: " + index);
        }

        Task task = tasks.get(idx);
        if (task == null) {
            throw new TaskNotFoundException("Task at index " + index + " is null");
        }

        return task;
    }


    @Override
    public List<Task> getAll() {

        return tasks;
    }

    @Override
    public void update(String index, Task task) {
        int idx = parseAndValidateIndex(index);
        tasks.set(idx, task);
    }


    @Override
    public void removeById(String index) {
        int idx = parseAndValidateIndex(index);
        this.tasks.remove(idx);
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
        if (taskId == null) throw new TaskNotFoundException("Project ID cannot be null");
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

    private int parseAndValidateIndex(String index){
        int idxToNumber = this.taskIdGenerator.elementIndex(index);
        if (idxToNumber < 0 || idxToNumber > tasks.size()) throw new TaskNotFoundException("Invalid Index");

        return idxToNumber;
    }
}
