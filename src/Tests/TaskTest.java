package Tests;

import Repository.TaskRepository;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GenerateTaskId;
import services.TaskService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 tests for TaskService
 * Tests cover:
 * - Completion percentage calculation (3 scenarios: no tasks, partial, all
 * completed)
 * - Task status updates
 * - Validation logic
 * - Exception handling
 */
class TaskTest {

    private TaskService taskService;
    private TaskRepository taskRepository;
    private GenerateTaskId taskIdGenerator;

    @BeforeEach
    void setUp() {
        // Initialize fresh dependencies for each test
        taskRepository = new TaskRepository(50);
        taskIdGenerator = new GenerateTaskId();
        taskService = new TaskService(taskRepository, taskIdGenerator);
    }

    /**
     * Test Scenario 1: No tasks - completion percentage should be 0.0
     */
    @Test
    void testCalculateCompletionRate_NoTasks() {
        // Given: A project with no tasks
        String projectId = "P0001";

        // When: Calculating completion rate
        double completionRate = taskService.calculateCompletionRate(projectId);

        // Then: Should return 0.0
        assertEquals(0.0, completionRate, 0.01,
                "Completion rate should be 0.0 when there are no tasks");
    }

    /**
     * Test Scenario 2: Partial completion - some tasks completed
     */
    @Test
    void testCalculateCompletionRate_PartialCompletion() {
        // Given: A project with 4 tasks, 2 completed
        String projectId = "P0001";

        Task task1 = new Task("Task 1", "Completed", projectId);
        Task task2 = new Task("Task 2", "Completed", projectId);
        Task task3 = new Task("Task 3", "In Progress", projectId);
        Task task4 = new Task("Task 4", "Pending", projectId);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);
        taskService.addTask(task4);

        // When: Calculating completion rate
        double completionRate = taskService.calculateCompletionRate(projectId);

        // Then: Should return 50.0 (2 out of 4 tasks completed)
        assertEquals(50.0, completionRate, 0.01,
                "Completion rate should be 50.0 when 2 out of 4 tasks are completed");
    }

    /**
     * Test Scenario 3: All tasks completed - completion percentage should be 100.0
     */
    @Test
    void testCalculateCompletionRate_AllTasksCompleted() {
        // Given: A project with 3 tasks, all completed
        String projectId = "P0001";

        Task task1 = new Task("Task 1", "Completed", projectId);
        Task task2 = new Task("Task 2", "Completed", projectId);
        Task task3 = new Task("Task 3", "Completed", projectId);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Calculating completion rate
        double completionRate = taskService.calculateCompletionRate(projectId);

        // Then: Should return 100.0
        assertEquals(100.0, completionRate, 0.01,
                "Completion rate should be 100.0 when all tasks are completed");
    }

    /**
     * Test: Task status update - valid update
     */
    @Test
    void testUpdateTaskStatus_ValidUpdate() {
        // Given: A task with status "Pending"
        String projectId = "P0001";
        Task task = new Task("Test Task", "Pending", projectId);
        taskService.addTask(task);
        String taskId = task.getTaskId();

        // When: Updating task status to "Completed"
        Task updatedTask = taskService.updateTaskStatus(taskId, "Completed");

        // Then: Task status should be updated
        assertNotNull(updatedTask, "Updated task should not be null");
        assertEquals("Completed", updatedTask.getTaskStatus(),
                "Task status should be updated to 'Completed'");
    }

    /**
     * Test: Task status update - task not found
     */
    @Test
    void testUpdateTaskStatus_TaskNotFound() {
        // Given: A non-existent task ID
        String nonExistentTaskId = "T9999";

        // When: Updating task status
        Task updatedTask = taskService.updateTaskStatus(nonExistentTaskId, "Completed");

        // Then: Should return null
        assertNull(updatedTask, "Should return null when task is not found");
    }

    /**
     * Test: Task status update - null task ID
     */
    @Test
    void testUpdateTaskStatus_NullTaskId() {
        // Given: Null task ID
        // When: Updating task status
        Task updatedTask = taskService.updateTaskStatus(null, "Completed");

        // Then: Should return null
        assertNull(updatedTask, "Should return null when task ID is null");
    }

    /**
     * Test: Add task - valid task
     */
    @Test
    void testAddTask_ValidTask() {
        // Given: A valid task
        Task task = new Task("Test Task", "Pending", "P0001");

        // When: Adding task
        // Then: Should not throw exception
        assertDoesNotThrow(() -> taskService.addTask(task),
                "Adding a valid task should not throw exception");

        // Verify task was added
        Task retrievedTask = taskService.getTaskById(task.getTaskId());
        assertNotNull(retrievedTask, "Task should be retrievable after adding");
        assertEquals("Test Task", retrievedTask.getTaskName(),
                "Task name should match");
    }

    /**
     * Test: Add task - null task should throw exception
     */
    @Test
    void testAddTask_NullTask() {
        // Given: Null task
        // When/Then: Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> taskService.addTask(null),
                "Adding null task should throw IllegalArgumentException");
    }

    /**
     * Test: Get task by ID - existing task
     */
    @Test
    void testGetTaskById_ExistingTask() {
        // Given: A task added to service
        Task task = new Task("Test Task", "Pending", "P0001");
        taskService.addTask(task);
        String taskId = task.getTaskId();

        // When: Getting task by ID
        Task retrievedTask = taskService.getTaskById(taskId);

        // Then: Should return the correct task
        assertNotNull(retrievedTask, "Retrieved task should not be null");
        assertEquals(taskId, retrievedTask.getTaskId(),
                "Task ID should match");
        assertEquals("Test Task", retrievedTask.getTaskName(),
                "Task name should match");
    }

    /**
     * Test: Get task by ID - non-existent task
     */
    @Test
    void testGetTaskById_NonExistentTask() {
        // Given: A non-existent task ID
        String nonExistentTaskId = "T9999";

        // When: Getting task by ID
        Task retrievedTask = taskService.getTaskById(nonExistentTaskId);

        // Then: Should return null
        assertNull(retrievedTask, "Should return null for non-existent task");
    }

    /**
     * Test: Get tasks by project ID - multiple tasks
     */
    @Test
    void testGetTasksByProjectId_MultipleTasks() {
        // Given: Multiple tasks for the same project
        String projectId = "P0001";
        Task task1 = new Task("Task 1", "Pending", projectId);
        Task task2 = new Task("Task 2", "In Progress", projectId);
        Task task3 = new Task("Task 3", "Completed", projectId);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Getting tasks by project ID
        Task[] tasks = taskService.getTasksByProjectId(projectId);

        // Then: Should return all tasks for the project
        assertEquals(3, tasks.length,
                "Should return 3 tasks for the project");
    }

    /**
     * Test: Get tasks by project ID - no tasks
     */
    @Test
    void testGetTasksByProjectId_NoTasks() {
        // Given: A project with no tasks
        String projectId = "P0001";

        // When: Getting tasks by project ID
        Task[] tasks = taskService.getTasksByProjectId(projectId);

        // Then: Should return empty array
        assertEquals(0, tasks.length,
                "Should return empty array when no tasks exist for project");
    }

    /**
     * Test: Delete task - existing task
     */
    @Test
    void testDeleteTask_ExistingTask() {
        // Given: A task added to service
        Task task = new Task("Test Task", "Pending", "P0001");
        taskService.addTask(task);
        String taskId = task.getTaskId();

        // When: Deleting task
        taskService.deleteTask(taskId);

        // Then: Task should no longer be retrievable
        Task retrievedTask = taskService.getTaskById(taskId);
        assertNull(retrievedTask, "Task should be deleted and not retrievable");
    }

    /**
     * Test: Delete task - non-existent task (should not throw)
     */
    @Test
    void testDeleteTask_NonExistentTask() {
        // Given: A non-existent task ID
        String nonExistentTaskId = "T9999";

        // When/Then: Should not throw exception
        assertDoesNotThrow(() -> taskService.deleteTask(nonExistentTaskId),
                "Deleting non-existent task should not throw exception");
    }

    /**
     * Test: Calculate completion rate - case insensitive status check
     */
    @Test
    void testCalculateCompletionRate_CaseInsensitive() {
        // Given: Tasks with different case statuses
        String projectId = "P0001";
        Task task1 = new Task("Task 1", "completed", projectId); // lowercase
        Task task2 = new Task("Task 2", "COMPLETED", projectId); // uppercase
        Task task3 = new Task("Task 3", "Pending", projectId);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Calculating completion rate
        double completionRate = taskService.calculateCompletionRate(projectId);

        // Then: Should count both case variations as completed
        assertEquals(66.67, completionRate, 0.1,
                "Completion rate should be ~66.67% (2 out of 3 tasks completed)");
    }

    /**
     * Test: Calculate completion rate - mixed statuses
     */
    @Test
    void testCalculateCompletionRate_MixedStatuses() {
        // Given: Tasks with various statuses
        String projectId = "P0001";
        Task task1 = new Task("Task 1", "Completed", projectId);
        Task task2 = new Task("Task 2", "In Progress", projectId);
        Task task3 = new Task("Task 3", "Pending", projectId);
        Task task4 = new Task("Task 4", "Completed", projectId);
        Task task5 = new Task("Task 5", "Pending", projectId);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);
        taskService.addTask(task4);
        taskService.addTask(task5);

        // When: Calculating completion rate
        double completionRate = taskService.calculateCompletionRate(projectId);

        // Then: Should return 40.0 (2 out of 5 tasks completed)
        assertEquals(40.0, completionRate, 0.01,
                "Completion rate should be 40.0 when 2 out of 5 tasks are completed");
    }
}
