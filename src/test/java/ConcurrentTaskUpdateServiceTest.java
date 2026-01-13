package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import interfaces.TaskFilter;
import models.Task;
import Repository.TaskRepository;
import interfaces.IdGenerator;
import services.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for ConcurrentTaskUpdateService
 * Testing concurrent task updates for Week 3 requirements
 */
@DisplayName("ConcurrentTaskUpdateService Tests")
class ConcurrentTaskUpdateServiceTest {

    @Mock
    private TaskService taskService;
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private IdGenerator idGenerator;
    
    private ConcurrentTaskUpdateService concurrentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        concurrentService = new ConcurrentTaskUpdateService(taskService);
    }

    @AfterEach
    void tearDown() {
        if (concurrentService != null) {
            concurrentService.shutdown();
        }
    }

    @Nested
    @DisplayName("Concurrent Task Updates")
    class ConcurrentTaskUpdates {

        @Test
        @DisplayName("Should update tasks concurrently successfully")
        void testConcurrentTaskUpdates() throws Exception {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Pending", "P001");
            task1.setTaskId("T001");
            
            Task task2 = new Task("Task 2", "Pending", "P001");
            task2.setTaskId("T002");
            
            Task task3 = new Task("Task 3", "Pending", "P002");
            task3.setTaskId("T003");

            // Mock task service responses
            when(taskService.getTaskById("T001")).thenReturn(task1);
            when(taskService.getTaskById("T002")).thenReturn(task2);
            when(taskService.getTaskById("T003")).thenReturn(task3);
            
            when(taskService.updateTaskStatus("T001", "Completed")).thenReturn(task1);
            when(taskService.updateTaskStatus("T002", "Completed")).thenReturn(task2);
            when(taskService.updateTaskStatus("T003", "Completed")).thenReturn(task3);

            // Execute concurrent updates
            List<String> taskIds = Arrays.asList("T001", "T002", "T003");
            CompletableFuture<List<Task>> future = concurrentService.updateTasksConcurrently(taskIds, "Completed");

            // Wait for completion
            List<Task> updatedTasks = future.get(5, TimeUnit.SECONDS);
            
            // Verify results
            assertNotNull(updatedTasks);
            assertEquals(3, updatedTasks.size());
            
            // Verify interactions
            verify(taskService, times(1)).getTaskById("T001");
            verify(taskService, times(1)).getTaskById("T002");
            verify(taskService, times(1)).getTaskById("T003");
            
            verify(taskService, times(1)).updateTaskStatus("T001", "Completed");
            verify(taskService, times(1)).updateTaskStatus("T002", "Completed");
            verify(taskService, times(1)).updateTaskStatus("T003", "Completed");
        }

        @Test
        @DisplayName("Should handle missing tasks gracefully")
        void testConcurrentUpdatesWithMissingTasks() throws Exception {
            // Mock task service to return null for missing tasks
            when(taskService.getTaskById("T001")).thenReturn(null);
            when(taskService.getTaskById("T002")).thenReturn(null);

            // Execute concurrent updates
            List<String> taskIds = Arrays.asList("T001", "T002");
            CompletableFuture<List<Task>> future = concurrentService.updateTasksConcurrently(taskIds, "Completed");

            // Wait for completion
            List<Task> updatedTasks = future.get(5, TimeUnit.SECONDS);
            
            // Verify results
            assertNotNull(updatedTasks);
            assertEquals(2, updatedTasks.size());
            
            // Verify interactions
            verify(taskService, times(1)).getTaskById("T001");
            verify(taskService, times(1)).getTaskById("T002");
            
            verify(taskService, times(1)).updateTaskStatus("T001", "Completed");
            verify(taskService, times(1)).updateTaskStatus("T002", "Completed");
        }
    }

    @Nested
    @DisplayName("Parallel Stream Updates")
    class ParallelStreamUpdates {

        @Test
        @DisplayName("Should update tasks using parallel streams")
        void testParallelStreamUpdates() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Pending", "P001");
            task1.setTaskId("T001");
            
            Task task2 = new Task("Task 2", "Pending", "P001");
            task2.setTaskId("T002");

            // Mock task service responses
            when(taskService.getTaskById("T001")).thenReturn(task1);
            when(taskService.getTaskById("T002")).thenReturn(task2);
            
            when(taskService.updateTaskStatus("T001", "In Progress")).thenReturn(task1);
            when(taskService.updateTaskStatus("T002", "In Progress")).thenReturn(task2);

            // Execute parallel stream updates
            List<String> taskIds = Arrays.asList("T001", "T002");
            assertDoesNotThrow(() -> concurrentService.updateTasksWithParallelStream(taskIds, "In Progress"));

            // Verify interactions
            verify(taskService, atLeastOnce()).getTaskById("T001");
            verify(taskService, atLeastOnce()).getTaskById("T002");
        }
    }

    @Nested
    @DisplayName("Concurrent Task Creation")
    class ConcurrentTaskCreation {

        @Test
        @DisplayName("Should create tasks concurrently")
        void testConcurrentTaskCreation() throws Exception {
            // Setup test tasks
            Task task1 = new Task("New Task 1", "Pending", "P001");
            Task task2 = new Task("New Task 2", "Pending", "P001");

            // Mock task service to not throw exceptions
            doNothing().when(taskService).addTask(any(Task.class));

            // Execute concurrent creation
            List<Task> tasks = Arrays.asList(task1, task2);
            CompletableFuture<List<Task>> future = concurrentService.createTasksConcurrently(tasks);

            // Wait for completion
            List<Task> createdTasks = future.get(5, TimeUnit.SECONDS);

            // Verify results
            assertEquals(2, createdTasks.size());
            
            // Verify interactions
            verify(taskService, times(1)).addTask(task1);
            verify(taskService, times(1)).addTask(task2);
        }
    }

    @Nested
    @DisplayName("Completion Rate Calculation")
    class CompletionRateCalculation {

        @Test
        @DisplayName("Should calculate completion rate concurrently")
        void testCalculateCompletionRateConcurrently() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Pending", "P001");
            Task task3 = new Task("Task 3", "Completed", "P001");

            List<Task> projectTasks = Arrays.asList(task1, task2, task3);

            // Mock task service response
            when(taskService.getTasksByProjectId("P001")).thenReturn(projectTasks);

            // Calculate completion rate
            double completionRate = concurrentService.calculateCompletionRateConcurrently("P001");

            // Verify result (2 out of 3 tasks completed = 66.67%)
            assertEquals(66.67, completionRate, 0.01);

            // Verify interaction
            verify(taskService, times(1)).getTasksByProjectId("P001");
        }

        @Test
        @DisplayName("Should handle empty project gracefully")
        void testCalculateCompletionRateEmptyProject() {
            // Mock empty task list
            when(taskService.getTasksByProjectId("P999")).thenReturn(Arrays.asList());

            // Calculate completion rate
            double completionRate = concurrentService.calculateCompletionRateConcurrently("P999");

            // Should return 0.0 for empty project
            assertEquals(0.0, completionRate);

            // Verify interaction
            verify(taskService, times(1)).getTasksByProjectId("P999");
        }
    }

    @Nested
    @DisplayName("Statistics and Monitoring")
    class StatisticsAndMonitoring {

        @Test
        @DisplayName("Should track completed update count")
        void testCompletedUpdateCount() throws Exception {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Pending", "P001");
            task1.setTaskId("T001");

            // Mock task service responses
            when(taskService.getTaskById("T001")).thenReturn(task1);
            when(taskService.updateTaskStatus("T001", "Completed")).thenReturn(task1);

            // Execute concurrent updates
            List<String> taskIds = Arrays.asList("T001");
            CompletableFuture<List<Task>> future = concurrentService.updateTasksConcurrently(taskIds, "Completed");

            // Wait for completion
            List<Task> updatedTasks = future.get(5, TimeUnit.SECONDS);

            // Check update count
            int completedCount = concurrentService.getCompletedUpdateCount();
            assertEquals(1, completedCount);
        }

        @Test
        @DisplayName("Should handle multiple concurrent updates")
        void testMultipleConcurrentUpdates() throws Exception {
            // Setup multiple test tasks
            Task[] tasks = new Task[5];
            for (int i = 0; i < 5; i++) {
                tasks[i] = new Task("Task " + (i + 1), "Pending", "P001");
                tasks[i].setTaskId("T" + String.format("%03d", i + 1));
                
                // Mock responses
                when(taskService.getTaskById(tasks[i].getTaskId())).thenReturn(tasks[i]);
                when(taskService.updateTaskStatus(tasks[i].getTaskId(), "Completed")).thenReturn(tasks[i]);
            }

            // Execute concurrent updates
            List<String> taskIds = Arrays.asList("T001", "T002", "T003", "T004", "T005");
            CompletableFuture<List<Task>> future = concurrentService.updateTasksConcurrently(taskIds, "Completed");

            // Wait for completion
            List<Task> updatedTasks = future.get(10, TimeUnit.SECONDS);
            
            // Check update count
            int completedCount = concurrentService.getCompletedUpdateCount();
            assertEquals(5, completedCount);
        }
    }
}
