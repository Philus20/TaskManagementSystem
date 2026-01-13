package Tests;

import interfaces.IStreamService;
import models.Project;
import models.Task;
import models.User;
import models.SoftwareProject;
import models.RegularUser;
import Repository.TaskRepository;
import Repository.ProjectRepository;
import Repository.UserRepository;
import services.*;
import services.GenerateTaskId;
import services.GenerateProjectId;
import services.GenerateUserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for StreamService
 * Tests streaming operations, concurrent processing, and performance
 */
class StreamServiceTest {

    private IStreamService streamService;
    private TaskService taskService;
    private ProjectService projectService;
    private UserService userService;
    private FilePersistenceService filePersistenceService;
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Initialize repositories
        taskRepository = new TaskRepository();
        projectRepository = new ProjectRepository();
        userRepository = new UserRepository();

        // Initialize services
        taskService = new TaskService(taskRepository, new GenerateTaskId());
        projectService = new ProjectService(projectRepository, new GenerateProjectId());
        userService = new UserService(userRepository, new GenerateUserId());
        filePersistenceService = new FilePersistenceService();

        // Create stream service with all dependencies
        streamService = new StreamService(taskService, projectService, userService, filePersistenceService);

        // Set up bidirectional dependencies
        taskService.setStreamService(streamService);
        projectService.setStreamService(streamService);
        projectService.setTaskService(taskService);
    }

    @AfterEach
    void tearDown() {
        if (streamService != null) {
            streamService.shutdown();
        }
    }

    @Test
    void testStreamAllTasks() {
        // Given: Add some tasks
        Task task1 = new Task("Task 1", "Pending", "P001");
        Task task2 = new Task("Task 2", "Completed", "P001");
        taskService.addTask(task1);
        taskService.addTask(task2);

        // When: Stream all tasks
        List<Task> tasks = streamService.streamAllTasks().toList();

        // Then: Should return all tasks
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testStreamTasksByProjectId() {
        // Given: Add tasks for different projects
        Task task1 = new Task("Task 1", "Pending", "P001");
        Task task2 = new Task("Task 2", "Completed", "P002");
        taskService.addTask(task1);
        taskService.addTask(task2);

        // When: Stream tasks by project ID
        List<Task> project1Tasks = streamService.streamTasksByProjectId("P001").toList();

        // Then: Should return only tasks for that project
        assertEquals(1, project1Tasks.size());
        assertEquals("Task 1", project1Tasks.get(0).getTaskName());
    }

    @Test
    void testStreamTasksByStatus() {
        // Given: Add tasks with different statuses
        Task task1 = new Task("Task 1", "Pending", "P001");
        Task task2 = new Task("Task 2", "Completed", "P001");
        Task task3 = new Task("Task 3", "Completed", "P001");
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Stream completed tasks
        List<Task> completedTasks = streamService.streamTasksByStatus("Completed").toList();

        // Then: Should return only completed tasks
        assertEquals(2, completedTasks.size());
        assertTrue(completedTasks.stream().allMatch(task -> "Completed".equals(task.getTaskStatus())));
    }

    @Test
    void testStreamAllProjects() {
        // Given: Add some projects
        Project project1 = new SoftwareProject("Project 1", "Description 1", "Software", 5, "Java", 10000.0);
        Project project2 = new SoftwareProject("Project 2", "Description 2", "Software", 3, "Python", 8000.0);
        projectService.addProject(project1);
        projectService.addProject(project2);

        // When: Stream all projects
        List<Project> projects = streamService.streamAllProjects().toList();

        // Then: Should return all projects
        assertEquals(2, projects.size());
        assertTrue(projects.contains(project1));
        assertTrue(projects.contains(project2));
    }

    @Test
    void testStreamProjectsByType() {
        // Given: Add projects of different types
        Project softwareProject = new SoftwareProject("Software Project", "Description", "Software", 5, "Java", 10000.0);
        Project hardwareProject = new SoftwareProject("Hardware Project", "Description", "Hardware", 3, "Python", 8000.0);
        projectService.addProject(softwareProject);
        projectService.addProject(hardwareProject);

        // When: Stream software projects
        List<Project> softwareProjects = streamService.streamProjectsByType("Software").toList();

        // Then: Should return only software projects
        assertEquals(1, softwareProjects.size());
        assertEquals("Software", softwareProjects.get(0).getType());
    }

    @Test
    void testCalculateProjectCompletionRateConcurrently() throws Exception {
        // Given: Add tasks with different completion statuses
        String projectId = "P001";
        Task task1 = new Task("Task 1", "Completed", projectId);
        Task task2 = new Task("Task 2", "Completed", projectId);
        Task task3 = new Task("Task 3", "Pending", projectId);
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Calculate completion rate concurrently
        CompletableFuture<Double> future = streamService.calculateProjectCompletionRateConcurrently(projectId);
        Double completionRate = future.get(5, TimeUnit.SECONDS);

        // Then: Should return correct completion rate
        assertEquals(66.67, completionRate, 0.1);
    }

    @Test
    void testGetTaskStatusDistributionConcurrently() throws Exception {
        // Given: Add tasks with different statuses
        String projectId = "P001";
        Task task1 = new Task("Task 1", "Completed", projectId);
        Task task2 = new Task("Task 2", "Completed", projectId);
        Task task3 = new Task("Task 3", "Pending", projectId);
        Task task4 = new Task("Task 4", "In Progress", projectId);
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);
        taskService.addTask(task4);

        // When: Get status distribution concurrently
        CompletableFuture<Map<String, Long>> future = streamService.getTaskStatusDistributionConcurrently(projectId);
        Map<String, Long> distribution = future.get(5, TimeUnit.SECONDS);

        // Then: Should return correct distribution
        assertEquals(3, distribution.size());
        assertEquals(2L, distribution.get("Completed"));
        assertEquals(1L, distribution.get("Pending"));
        assertEquals(1L, distribution.get("In Progress"));
    }

    @Test
    void testBatchUpdateTasksConcurrently() throws Exception {
        // Given: Add some tasks
        Task task1 = new Task("Task 1", "Pending", "P001");
        Task task2 = new Task("Task 2", "Pending", "P001");
        Task task3 = new Task("Task 3", "Pending", "P001");
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        // When: Batch update tasks concurrently
        List<String> taskIds = List.of(task1.getTaskId(), task2.getTaskId(), task3.getTaskId());
        CompletableFuture<List<Task>> future = streamService.batchUpdateTasksConcurrently(taskIds, "Completed");
        List<Task> updatedTasks = future.get(10, TimeUnit.SECONDS);

        // Then: All tasks should be updated
        assertEquals(3, updatedTasks.size());
        assertTrue(updatedTasks.stream().allMatch(task -> "Completed".equals(task.getTaskStatus())));
    }

    @Test
    void testGetTopPerformingProjectsConcurrently() throws Exception {
        // Given: Add projects with different completion rates
        Project project1 = new SoftwareProject("Project 1", "Description", "Software", 5, "Java", 10000.0);
        Project project2 = new SoftwareProject("Project 2", "Description", "Software", 3, "Python", 8000.0);
        projectService.addProject(project1);
        projectService.addProject(project2);

        // Add tasks for project1 (100% completion)
        Task task1 = new Task("Task 1", "Completed", project1.getId());
        Task task2 = new Task("Task 2", "Completed", project1.getId());
        taskService.addTask(task1);
        taskService.addTask(task2);

        // Add tasks for project2 (50% completion)
        Task task3 = new Task("Task 3", "Completed", project2.getId());
        Task task4 = new Task("Task 4", "Pending", project2.getId());
        taskService.addTask(task3);
        taskService.addTask(task4);

        // When: Get top performing projects
        CompletableFuture<List<String>> future = streamService.getTopPerformingProjectsConcurrently(2);
        List<String> topProjects = future.get(5, TimeUnit.SECONDS);

        // Then: Should return projects ordered by completion rate
        assertEquals(2, topProjects.size());
        assertEquals(project1.getId(), topProjects.get(0)); // 100% completion first
        assertEquals(project2.getId(), topProjects.get(1)); // 50% completion second
    }

    @Test
    void testStreamTasksToFileAsync() throws Exception {
        // Given: Add some tasks
        Task task1 = new Task("Task 1", "Pending", "P001");
        Task task2 = new Task("Task 2", "Completed", "P001");
        taskService.addTask(task1);
        taskService.addTask(task2);

        // When: Stream tasks to file asynchronously
        CompletableFuture<Void> future = streamService.streamTasksToFileAsync(streamService.streamAllTasks());
        future.get(5, TimeUnit.SECONDS);

        // Then: Tasks should be saved to file (verify by loading)
        taskService.loadTasksFromFile();
        List<Task> loadedTasks = taskService.getAllTasks();
        assertEquals(2, loadedTasks.size());
    }

    @Test
    void testParallelProcessStream() throws Exception {
        // Given: Create a stream of tasks
        List<Task> tasks = List.of(
            new Task("Task 1", "Pending", "P001"),
            new Task("Task 2", "Pending", "P001"),
            new Task("Task 3", "Pending", "P001")
        );

        // When: Process stream in parallel
        CompletableFuture<List<Task>> future = streamService.parallelProcessStream(
            tasks.stream(),
            task -> {
                task.setTaskStatus("Processed");
                return task;
            }
        );
        List<Task> processedTasks = future.get(5, TimeUnit.SECONDS);

        // Then: All tasks should be processed
        assertEquals(3, processedTasks.size());
        assertTrue(processedTasks.stream().allMatch(task -> "Processed".equals(task.getTaskStatus())));
    }

    @Test
    void testHealthCheck() throws Exception {
        // Given: Add some data
        Task task = new Task("Task 1", "Pending", "P001");
        taskService.addTask(task);

        // When: Perform health check
        CompletableFuture<Void> future = ((StreamService) streamService).performHealthCheck();
        future.get(5, TimeUnit.SECONDS);

        // Then: Should complete without exception
        assertTrue(future.isDone());
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    void testGetActiveOperationCount() {
        // When: Get active operation count
        int count = ((StreamService) streamService).getActiveOperationCount();

        // Then: Should return non-negative count
        assertTrue(count >= 0);
    }
}
