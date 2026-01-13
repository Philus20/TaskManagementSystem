package interfaces;

import models.Task;
import java.util.function.Predicate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Enhanced functional interface for task filtering and validation
 * Following functional programming principles for Week 3 enhancements
 * Provides comprehensive filtering capabilities with composition support
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
    
    static TaskFilter byTaskName(String name) {
        return task -> task != null && task.getTaskName() != null && 
                      task.getTaskName().toLowerCase().contains(name.toLowerCase());
    }
    
    static TaskFilter byTaskNamePattern(String pattern) {
        return task -> task != null && task.getTaskName() != null && 
                      task.getTaskName().toLowerCase().matches(pattern.toLowerCase());
    }
    
    static TaskFilter byStatusAndProject(String status, String projectId) {
        return task -> task != null && 
                      status.equalsIgnoreCase(task.getTaskStatus()) && 
                      projectId.equals(task.getProjectId());
    }
    
    static TaskFilter byStatusAndUser(String status, String userId) {
        return task -> task != null && 
                      status.equalsIgnoreCase(task.getTaskStatus()) && 
                      userId.equals(task.getAssignedUserId());
    }
    
    static TaskFilter byProjectAndUser(String projectId, String userId) {
        return task -> task != null && 
                      projectId.equals(task.getProjectId()) && 
                      userId.equals(task.getAssignedUserId());
    }
    
    static TaskFilter byStatusProjectAndUser(String status, String projectId, String userId) {
        return task -> task != null && 
                      status.equalsIgnoreCase(task.getTaskStatus()) && 
                      projectId.equals(task.getProjectId()) && 
                      userId.equals(task.getAssignedUserId());
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
    
    static TaskFilter unassignedTasks() {
        return task -> task != null && task.getAssignedUserId() == null;
    }
    
    static TaskFilter assignedTasks() {
        return task -> task != null && task.getAssignedUserId() != null;
    }
    
    static TaskFilter tasksWithValidIds() {
        return task -> task != null && 
                      task.getTaskId() != null && 
                      task.getProjectId() != null &&
                      utils.RegexValidator.isValidTaskId(task.getTaskId()) &&
                      utils.RegexValidator.isValidProjectId(task.getProjectId());
    }
    
    static TaskFilter tasksByNameLength(int minLength, int maxLength) {
        return task -> task != null && task.getTaskName() != null &&
                      task.getTaskName().length() >= minLength && 
                      task.getTaskName().length() <= maxLength;
    }
    
    /**
     * Complex filters combining multiple criteria
     */
    static TaskFilter highPriorityTasks() {
        return task -> task != null && 
                      ("In Progress".equals(task.getTaskStatus()) ||
                       ("Pending".equals(task.getTaskStatus()) && "P001".equals(task.getProjectId())));
    }
    
    /**
     * Combining filters with logical operations
     */
    default TaskFilter and(TaskFilter other) {
        return task -> this.test(task) && other.test(task);
    }
    
    default TaskFilter or(TaskFilter other) {
        return task -> this.test(task) || other.test(task);
    }
    
    default TaskFilter negate() {
        return task -> !this.test(task);
    }
    
    /**
     * Utility methods for working with collections
     */
    default List<Task> filterList(List<Task> tasks) {
        return tasks.stream().filter(this).collect(Collectors.toList());
    }
    
    default long countMatches(List<Task> tasks) {
        return tasks.stream().filter(this).count();
    }
    
    default Task findFirst(List<Task> tasks) {
        return tasks.stream().filter(this).findFirst().orElse(null);
    }
    
    default Task findAny(List<Task> tasks) {
        return tasks.stream().filter(this).findAny().orElse(null);
    }
    
    default boolean anyMatch(List<Task> tasks) {
        return tasks.stream().anyMatch(this);
    }
    
    default boolean allMatch(List<Task> tasks) {
        return tasks.stream().allMatch(this);
    }
    
    default boolean noneMatch(List<Task> tasks) {
        return tasks.stream().noneMatch(this);
    }
    
    /**
     * Partition tasks into matching and non-matching groups
     */
    default java.util.Map<Boolean, List<Task>> partition(List<Task> tasks) {
        return tasks.stream().collect(Collectors.partitioningBy(this));
    }
    
    /**
     * Group tasks by a classifier after filtering
     */
    default <K> java.util.Map<K, List<Task>> groupByAfterFilter(List<Task> tasks, 
                                                               java.util.function.Function<Task, K> classifier) {
        return tasks.stream().filter(this).collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Create a filter that only applies if a condition is met
     */
    static TaskFilter conditional(boolean condition, TaskFilter filter) {
        return condition ? filter : task -> true;
    }
    
    /**
     * Create a filter that applies different filters based on a condition
     */
    static TaskFilter conditionalFilter(java.util.function.Predicate<Task> condition, 
                                      TaskFilter trueFilter, 
                                      TaskFilter falseFilter) {
        return task -> condition.test(task) ? trueFilter.test(task) : falseFilter.test(task);
    }
    
    /**
     * Create a filter that caches results (memoization)
     */
    default TaskFilter memoized() {
        java.util.Map<Task, Boolean> cache = new java.util.HashMap<>();
        return task -> cache.computeIfAbsent(task, this::test);
    }
    
    /**
     * Create a filter with logging
     */
    default TaskFilter withLogging(String filterName) {
        return task -> {
            boolean result = this.test(task);
            System.out.println(filterName + " - Task " + 
                (task != null ? task.getTaskId() : "null") + ": " + result);
            return result;
        };
    }
    
    /**
     * Create a filter that validates task before applying main filter
     */
    default TaskFilter withValidation() {
        return task -> task != null && this.test(task);
    }
    
    /**
     * Chain multiple filters with AND logic
     */
    @SafeVarargs
    static TaskFilter chainAnd(TaskFilter... filters) {
        return Arrays.stream(filters).reduce(task -> true, TaskFilter::and);
    }
    
    /**
     * Chain multiple filters with OR logic
     */
    @SafeVarargs
    static TaskFilter chainOr(TaskFilter... filters) {
        return Arrays.stream(filters).reduce(task -> false, TaskFilter::or);
    }
    
    /**
     * Create a filter that matches if exactly one of the given filters matches
     */
    @SafeVarargs
    static TaskFilter exclusive(TaskFilter... filters) {
        return task -> Arrays.stream(filters).map(filter -> filter.test(task)).count() == 1;
    }
    
    /**
     * Create a filter that matches if at least N of the given filters match
     */
    @SafeVarargs
    static TaskFilter atLeast(int minMatches, TaskFilter... filters) {
        return task -> Arrays.stream(filters).map(filter -> filter.test(task)).count() >= minMatches;
    }
}
