package interfaces;

import models.Task;
import java.util.function.Predicate;

/**
 * Functional interfaces for task filtering and validation
 * Following functional programming principles for Week 3 enhancements
 */
@FunctionalInterface
public interface TaskFilter extends Predicate<Task> {
    /**
     * Test if a task meets the filter criteria
     * @param task the task to test
     * @return true if task matches criteria, false otherwise
     */
    boolean test(Task task);
    
    /**
     * Static factory methods for common filters
     */
    static TaskFilter byStatus(String status) {
        return task -> task != null && status.equalsIgnoreCase(task.getTaskStatus());
    }
    
    static TaskFilter byProjectId(String projectId) {
        return task -> task != null && projectId.equals(task.getProjectId());
    }
    
    static TaskFilter byAssignedUserId(String userId) {
        return task -> task != null && userId.equals(task.getAssignedUserId());
    }
    
    static TaskFilter byStatusAndProject(String status, String projectId) {
        return task -> task != null && 
                      status.equalsIgnoreCase(task.getTaskStatus()) && 
                      projectId.equals(task.getProjectId());
    }
    
    static TaskFilter completedTasks() {
        return byStatus("Completed");
    }
    
    static TaskFilter pendingTasks() {
        return byStatus("Pending");
    }
    
    static TaskFilter inProgressTasks() {
        return byStatus("In Progress");
    }
    
    /**
     * Combine multiple filters with AND logic
     */
    default TaskFilter and(TaskFilter other) {
        return task -> this.test(task) && other.test(task);
    }
    
    /**
     * Combine multiple filters with OR logic
     */
    default TaskFilter or(TaskFilter other) {
        return task -> this.test(task) || other.test(task);
    }
    
    /**
     * Negate the filter
     */
    default TaskFilter negate() {
        return task -> !this.test(task);
    }
}
