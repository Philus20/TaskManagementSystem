package services;

import models.Task;

import java.util.Arrays;

public class TaskService {

    // A repository to store Tasks (fixed capacity)
    private final Task[] tasks;

    // Constructor to initialize the Tasks repo
    public TaskService() {
        tasks = new Task[50]; // pick capacity you want
    }

    // Add a new Task (only if id doesn't exist)
    public void addTask(Task task) {
        if (task == null) return;
        // check duplicate id
        for (Task t : tasks) {
            if (t != null && t.getTaskId().equals(task.getTaskId())) {
                System.out.println("Task with id " + task.getTaskId() + " already exists.");
                return;
            }
        }
        // find first free slot
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] == null) {
                tasks[i] = task;
                return;
            }
        }
        System.out.println("Task repository is full.");
    }

    // Return all tasks as a trimmed array (no nulls)
    public Task[] getAllTasks() {
        int count = 0;
        for (Task t : tasks) if (t != null) count++;
        Task[] out = new Task[count];
        int i = 0;
        for (Task t : tasks) if (t != null) out[i++] = t;
        return out;
    }

    // Get a task by id (arrays-only)
    public Task getTaskById(String taskId) {
        if (taskId == null) return null;
        for (Task t : tasks) {
            if (t != null && taskId.equals(t.getTaskId())) return t;
        }
        return null;
    }

    // Update a task status
    public Task updateTaskStatus(String taskId, String taskStatus) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setTaskStatus(taskStatus);
        }
        return task;
    }

    // Delete a task by id (compacts array)
    public void deleteTask(String taskId) {
        if (taskId == null) return;
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] != null && taskId.equals(tasks[i].getTaskId())) {
                // remove and shift left to compact
                for (int j = i; j < tasks.length - 1; j++) tasks[j] = tasks[j + 1];
                tasks[tasks.length - 1] = null;
                return;
            }
        }
    }

    // Get tasks by project id (already arrays-only)
    public Task[] getTasksByProjectId(String projectId) {
        if (projectId == null) return new Task[0];
        Task[] result = new Task[tasks.length];
        int count = 0;
        for (Task t : tasks) {
            if (t != null && projectId.equals(t.getProjectId())) {
                result[count++] = t;
            }
        }
        return Arrays.copyOf(result, count);
    }

    // Calculate completion rate for a project (arrays-only)
    public double calculateCompletionRate(String projectId) {
        Task[] projTasks = getTasksByProjectId(projectId);
        if (projTasks == null || projTasks.length == 0) return 0.0;
        int completed = 0;
        for (Task t : projTasks) {
            if (t != null && "Completed".equalsIgnoreCase(t.getTaskStatus())) completed++;
        }
        return (completed * 100.0) / projTasks.length;
    }
}
