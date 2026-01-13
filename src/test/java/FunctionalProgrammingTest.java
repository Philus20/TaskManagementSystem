package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import interfaces.ProjectFilter;
import interfaces.TaskFilter;
import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import models.Task;
import Repository.ProjectRepository;
import Repository.TaskRepository;
import interfaces.IdGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 tests for functional programming features
 * Testing streams, filters, and collections for Week 3 requirements
 */
@DisplayName("Functional Programming Tests")
class FunctionalProgrammingTest {

    @Mock
    private ProjectRepository projectRepository;
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private IdGenerator projectIdGenerator;
    
    @Mock
    private IdGenerator taskIdGenerator;
    
    private ProjectService projectService;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectService(projectRepository, (GenerateProjectId) projectIdGenerator);
        taskService = new TaskService(taskRepository, taskIdGenerator);
    }

    @Nested
    @DisplayName("Project Filter Tests")
    class ProjectFilterTests {

        @Test
        @DisplayName("Should filter projects by type using functional interface")
        void testFilterProjectsByType() {
            // Setup test projects
            Project softwareProject = new SoftwareProject("Software Proj", "Desc", "Software", 5, "Java", 50000.0);
            Project hardwareProject = new HardwareProject("Hardware Proj", "Desc", "Hardware", 3, "IoT", 25000.0);
            Project anotherSoftwareProject = new SoftwareProject("Another Software", "Desc", "Software", 4, "Python", 40000.0);

            List<Project> allProjects = Arrays.asList(softwareProject, hardwareProject, anotherSoftwareProject);
            when(projectRepository.getAll()).thenReturn(allProjects);

            // Use functional filter
            ProjectFilter softwareFilter = ProjectFilter.softwareProjects();
            List<Project> filteredProjects = projectService.filterProjects(softwareFilter);

            assertEquals(2, filteredProjects.size());
            assertTrue(filteredProjects.contains(softwareProject));
            assertTrue(filteredProjects.contains(anotherSoftwareProject));
            assertFalse(filteredProjects.contains(hardwareProject));
        }

        @Test
        @DisplayName("Should filter projects by budget range")
        void testFilterProjectsByBudget() {
            // Setup test projects
            Project project1 = new SoftwareProject("Project 1", "Desc", "Software", 3, "Java", 30000.0);
            Project project2 = new HardwareProject("Project 2", "Desc", "Hardware", 2, "IoT", 60000.0);
            Project project3 = new SoftwareProject("Project 3", "Desc", "Software", 4, "Python", 45000.0);

            List<Project> allProjects = Arrays.asList(project1, project2, project3);
            when(projectRepository.getAll()).thenReturn(allProjects);

            // Use functional filter
            ProjectFilter budgetFilter = ProjectFilter.byBudgetRange(25000.0, 50000.0);
            List<Project> filteredProjects = projectService.filterProjects(budgetFilter);

            assertEquals(2, filteredProjects.size());
            assertTrue(filteredProjects.contains(project1));
            assertTrue(filteredProjects.contains(project3));
            assertFalse(filteredProjects.contains(project2));
        }

        @Test
        @DisplayName("Should combine multiple filters with AND logic")
        void testCombinedFilters() {
            // Setup test projects
            Project softwareProject1 = new SoftwareProject("Software 1", "Desc", "Software", 5, "Java", 50000.0);
            Project softwareProject2 = new SoftwareProject("Software 2", "Desc", "Software", 2, "Python", 20000.0);
            Project hardwareProject = new HardwareProject("Hardware 1", "Desc", "Hardware", 3, "IoT", 40000.0);

            List<Project> allProjects = Arrays.asList(softwareProject1, softwareProject2, hardwareProject);
            when(projectRepository.getAll()).thenReturn(allProjects);

            // Combine filters: Software projects with budget > 30000
            ProjectFilter combinedFilter = ProjectFilter.softwareProjects()
                .and(ProjectFilter.budgetGreaterThan(30000.0));
            
            List<Project> filteredProjects = projectService.filterProjects(combinedFilter);

            assertEquals(1, filteredProjects.size());
            assertTrue(filteredProjects.contains(softwareProject1));
            assertFalse(filteredProjects.contains(softwareProject2));
            assertFalse(filteredProjects.contains(hardwareProject));
        }
    }

    @Nested
    @DisplayName("Task Filter Tests")
    class TaskFilterTests {

        @Test
        @DisplayName("Should filter tasks by status using functional interface")
        void testFilterTasksByStatus() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Pending", "P001");
            Task task3 = new Task("Task 3", "Completed", "P002");

            List<Task> allTasks = Arrays.asList(task1, task2, task3);
            when(taskRepository.getAll()).thenReturn(allTasks);

            // Use functional filter
            TaskFilter completedFilter = TaskFilter.completedTasks();
            List<Task> filteredTasks = taskService.filterTasks(completedFilter);

            assertEquals(2, filteredTasks.size());
            assertTrue(filteredTasks.contains(task1));
            assertTrue(filteredTasks.contains(task3));
            assertFalse(filteredTasks.contains(task2));
        }

        @Test
        @DisplayName("Should filter tasks by project ID")
        void testFilterTasksByProject() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Pending", "P001");
            Task task2 = new Task("Task 2", "In Progress", "P001");
            Task task3 = new Task("Task 3", "Completed", "P002");

            List<Task> allTasks = Arrays.asList(task1, task2, task3);
            when(taskRepository.getAll()).thenReturn(allTasks);

            // Use functional filter
            TaskFilter projectFilter = TaskFilter.byProjectId("P001");
            List<Task> filteredTasks = taskService.filterTasks(projectFilter);

            assertEquals(2, filteredTasks.size());
            assertTrue(filteredTasks.contains(task1));
            assertTrue(filteredTasks.contains(task2));
            assertFalse(filteredTasks.contains(task3));
        }

        @Test
        @DisplayName("Should combine task filters with OR logic")
        void testCombinedTaskFilters() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Pending", "P002");
            Task task3 = new Task("Task 3", "In Progress", "P001");

            List<Task> allTasks = Arrays.asList(task1, task2, task3);
            when(taskRepository.getAll()).thenReturn(allTasks);

            // Combine filters: Completed tasks OR tasks from P001
            TaskFilter combinedFilter = TaskFilter.completedTasks()
                .or(TaskFilter.byProjectId("P001"));
            
            List<Task> filteredTasks = taskService.filterTasks(combinedFilter);

            assertEquals(3, filteredTasks.size()); // All tasks should match (task1 is completed, task1&3 are from P001)
            assertTrue(filteredTasks.contains(task1));
            assertTrue(filteredTasks.contains(task2));
            assertTrue(filteredTasks.contains(task3));
        }
    }

    @Nested
    @DisplayName("Stream Operations Tests")
    class StreamOperationsTests {

        @Test
        @DisplayName("Should calculate average budget using streams")
        void testCalculateAverageBudget() {
            // Setup test projects
            Project project1 = new SoftwareProject("Project 1", "Desc", "Software", 3, "Java", 30000.0);
            Project project2 = new HardwareProject("Project 2", "Desc", "Hardware", 2, "IoT", 60000.0);
            Project project3 = new SoftwareProject("Project 3", "Desc", "Software", 4, "Python", 45000.0);

            List<Project> allProjects = Arrays.asList(project1, project2, project3);
            when(projectRepository.getAll()).thenReturn(allProjects);

            // Calculate average budget
            double averageBudget = projectService.getAverageBudget();

            assertEquals(45000.0, averageBudget, 0.01);
        }

        @Test
        @DisplayName("Should count projects by type using streams")
        void testCountProjectsByType() {
            // Setup test projects
            Project project1 = new SoftwareProject("Software 1", "Desc", "Software", 3, "Java", 30000.0);
            Project project2 = new SoftwareProject("Software 2", "Desc", "Software", 2, "Python", 20000.0);
            Project project3 = new HardwareProject("Hardware 1", "Desc", "Hardware", 3, "IoT", 40000.0);

            List<Project> allProjects = Arrays.asList(project1, project2, project3);
            when(projectRepository.getAll()).thenReturn(allProjects);

            // Count software projects
            long softwareCount = projectService.getProjectCountByType("Software");
            long hardwareCount = projectService.getProjectCountByType("Hardware");

            assertEquals(2, softwareCount);
            assertEquals(1, hardwareCount);
        }

        @Test
        @DisplayName("Should count tasks by status using streams")
        void testCountTasksByStatus() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Completed", "P002");
            Task task3 = new Task("Task 3", "Pending", "P001");
            Task task4 = new Task("Task 4", "In Progress", "P002");

            List<Task> allTasks = Arrays.asList(task1, task2, task3, task4);
            when(taskRepository.getAll()).thenReturn(allTasks);

            // Count tasks by status
            long completedCount = taskService.getTaskCountByStatus("Completed");
            long pendingCount = taskService.getTaskCountByStatus("Pending");
            long inProgressCount = taskService.getTaskCountByStatus("In Progress");

            assertEquals(2, completedCount);
            assertEquals(1, pendingCount);
            assertEquals(1, inProgressCount);
        }

        @Test
        @DisplayName("Should get distinct project IDs using streams")
        void testGetDistinctProjectIds() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Pending", "P001");
            Task task3 = new Task("Task 3", "In Progress", "P002");
            Task task4 = new Task("Task 4", "Completed", "P002");
            Task task5 = new Task("Task 5", "Pending", "P003");

            List<Task> allTasks = Arrays.asList(task1, task2, task3, task4, task5);
            when(taskRepository.getAll()).thenReturn(allTasks);

            // Get distinct project IDs
            List<String> projectIds = taskService.getProjectIdsWithTasks();

            assertEquals(3, projectIds.size());
            assertTrue(projectIds.contains("P001"));
            assertTrue(projectIds.contains("P002"));
            assertTrue(projectIds.contains("P003"));
        }
    }

    @Nested
    @DisplayName("Collections and Aggregation Tests")
    class CollectionsTests {

        @Test
        @DisplayName("Should calculate completion rate using streams")
        void testCalculateCompletionRate() {
            // Setup test tasks
            Task task1 = new Task("Task 1", "Completed", "P001");
            Task task2 = new Task("Task 2", "Pending", "P001");
            Task task3 = new Task("Task 3", "Completed", "P001");
            Task task4 = new Task("Task 4", "In Progress", "P001");

            List<Task> projectTasks = Arrays.asList(task1, task2, task3, task4);
            when(taskRepository.findByProjectId("P001")).thenReturn(projectTasks);

            // Calculate completion rate
            double completionRate = taskService.calculateCompletionRate("P001");

            assertEquals(50.0, completionRate, 0.01); // 2 out of 4 tasks completed
        }

        @Test
        @DisplayName("Should handle empty collections gracefully")
        void testEmptyCollections() {
            // Test empty project list
            when(projectRepository.getAll()).thenReturn(Arrays.asList());
            double averageBudget = projectService.getAverageBudget();
            assertEquals(0.0, averageBudget);

            // Test empty task list
            when(taskRepository.findByProjectId("P999")).thenReturn(Arrays.asList());
            double completionRate = taskService.calculateCompletionRate("P999");
            assertEquals(0.0, completionRate);
        }
    }
}
