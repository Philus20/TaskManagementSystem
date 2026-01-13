package services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import models.Project;
import models.SoftwareProject;
import models.HardwareProject;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for FilePersistenceService
 * Testing NIO-based file persistence for Week 3 requirements
 */
@DisplayName("FilePersistenceService Tests")
class FilePersistenceServiceTest {

    private FilePersistenceService filePersistenceService;
    private static final String TEST_DATA_DIR = "test_data";
    private Path testDirPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory
        testDirPath = Paths.get(TEST_DATA_DIR);
        if (Files.exists(testDirPath)) {
            Files.walk(testDirPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore
                    }
                });
        }
        Files.createDirectories(testDirPath);
        
        filePersistenceService = new FilePersistenceService();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test directory
        if (Files.exists(testDirPath)) {
            Files.walk(testDirPath)
                .sorted((a, b) -> -a.compareTo(b))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        // Ignore
                    }
                });
        }
    }

    @Nested
    @DisplayName("Project Persistence Tests")
    class ProjectPersistenceTests {

        @Test
        @DisplayName("Should save and load projects successfully")
        void testSaveAndLoadProjects() {
            // Create test projects
            Map<String, Project> projects = new HashMap<>();
            
            SoftwareProject softwareProject = new SoftwareProject(
                "Test Software Project", 
                "A test software project", 
                "Software", 
                5, 
                "Java", 
                50000.0
            );
            softwareProject.setId("P001");
            projects.put("P001", softwareProject);

            HardwareProject hardwareProject = new HardwareProject(
                "Test Hardware Project", 
                "A test hardware project", 
                "Hardware", 
                3, 
                "IoT Device", 
                25000.0
            );
            hardwareProject.setId("P002");
            projects.put("P002", hardwareProject);

            // Save projects
            assertDoesNotThrow(() -> filePersistenceService.saveProjects(projects, List.of()));

            // Load projects
            Map<String, Project> loadedProjects = filePersistenceService.loadProjects();

            // Verify loaded projects
            assertEquals(2, loadedProjects.size());
            assertTrue(loadedProjects.containsKey("P001"));
            assertTrue(loadedProjects.containsKey("P002"));

            Project loadedSoftware = loadedProjects.get("P001");
            assertEquals("P001", loadedSoftware.getId());
            assertEquals("Test Software Project", loadedSoftware.getName());
            assertEquals("Software", loadedSoftware.getType());
            assertTrue(loadedSoftware instanceof SoftwareProject);

            Project loadedHardware = loadedProjects.get("P002");
            assertEquals("P002", loadedHardware.getId());
            assertEquals("Test Hardware Project", loadedHardware.getName());
            assertEquals("Hardware", loadedHardware.getType());
            assertTrue(loadedHardware instanceof HardwareProject);
        }

        @Test
        @DisplayName("Should handle empty project list")
        void testEmptyProjectList() {
            Map<String, Project> emptyProjects = new HashMap<>();
            
            assertDoesNotThrow(() -> filePersistenceService.saveProjects(emptyProjects, List.of()));
            
            Map<String, Project> loadedProjects = filePersistenceService.loadProjects();
            assertEquals(0, loadedProjects.size());
        }

        @Test
        @DisplayName("Should handle missing file gracefully")
        void testMissingFile() {
            Map<String, Project> loadedProjects = filePersistenceService.loadProjects();
            assertEquals(0, loadedProjects.size());
        }
    }

    @Nested
    @DisplayName("Task Persistence Tests")
    class TaskPersistenceTests {

        @Test
        @DisplayName("Should save and load tasks successfully")
        void testSaveAndLoadTasks() {
            // Create test tasks
            List<models.Task> tasks = List.of(
                new models.Task("Task 1", "Pending", "P001"),
                new models.Task("Task 2", "In Progress", "P001"),
                new models.Task("Task 3", "Completed", "P002")
            );
            
            // Set task IDs
            tasks.get(0).setTaskId("T001");
            tasks.get(1).setTaskId("T002");
            tasks.get(2).setTaskId("T003");

            // Set assigned users
            tasks.get(0).setAssignedUserId("U001");
            tasks.get(1).setAssignedUserId("U002");

            // Save tasks
            assertDoesNotThrow(() -> filePersistenceService.saveTasks(tasks));

            // Load tasks
            List<models.Task> loadedTasks = filePersistenceService.loadTasks();

            // Verify loaded tasks
            assertEquals(3, loadedTasks.size());

            models.Task task1 = loadedTasks.stream()
                .filter(t -> "T001".equals(t.getTaskId()))
                .findFirst()
                .orElse(null);
            assertNotNull(task1);
            assertEquals("Task 1", task1.getTaskName());
            assertEquals("Pending", task1.getTaskStatus());
            assertEquals("P001", task1.getProjectId());
            assertEquals("U001", task1.getAssignedUserId());

            models.Task task2 = loadedTasks.stream()
                .filter(t -> "T002".equals(t.getTaskId()))
                .findFirst()
                .orElse(null);
            assertNotNull(task2);
            assertEquals("Task 2", task2.getTaskName());
            assertEquals("In Progress", task2.getTaskStatus());
            assertEquals("P001", task2.getProjectId());
            assertEquals("U002", task2.getAssignedUserId());
        }

        @Test
        @DisplayName("Should handle empty task list")
        void testEmptyTaskList() {
            List<models.Task> emptyTasks = List.of();
            
            assertDoesNotThrow(() -> filePersistenceService.saveTasks(emptyTasks));
            
            List<models.Task> loadedTasks = filePersistenceService.loadTasks();
            assertEquals(0, loadedTasks.size());
        }
    }

    @Nested
    @DisplayName("User Persistence Tests")
    class UserPersistenceTests {

        @Test
        @DisplayName("Should save and load users successfully")
        void testSaveAndLoadUsers() {
            // Create test users
            Map<String, models.User> users = new HashMap<>();
            
            models.AdminUser adminUser = new models.AdminUser("Admin User", "admin@example.com");
            adminUser.setId("U001");
            users.put("U001", adminUser);

            models.RegularUser regularUser = new models.RegularUser("Regular User", "user@example.com");
            regularUser.setId("U002");
            users.put("U002", regularUser);

            // Save users
            assertDoesNotThrow(() -> filePersistenceService.saveUsers(users));

            // Load users
            Map<String, models.User> loadedUsers = filePersistenceService.loadUsers();

            // Verify loaded users
            assertEquals(2, loadedUsers.size());
            assertTrue(loadedUsers.containsKey("U001"));
            assertTrue(loadedUsers.containsKey("U002"));

            models.User loadedAdmin = loadedUsers.get("U001");
            assertEquals("U001", loadedAdmin.getId());
            assertEquals("Admin User", loadedAdmin.getName());
            assertEquals("admin@example.com", loadedAdmin.getEmail());
            assertEquals("Admin User", loadedAdmin.getRole());

            models.User loadedRegular = loadedUsers.get("U002");
            assertEquals("U002", loadedRegular.getId());
            assertEquals("Regular User", loadedRegular.getName());
            assertEquals("user@example.com", loadedRegular.getEmail());
            assertEquals("Regular User", loadedRegular.getRole());
        }

        @Test
        @DisplayName("Should handle empty user list")
        void testEmptyUserList() {
            Map<String, models.User> emptyUsers = new HashMap<>();
            
            assertDoesNotThrow(() -> filePersistenceService.saveUsers(emptyUsers));
            
            Map<String, models.User> loadedUsers = filePersistenceService.loadUsers();
            assertEquals(0, loadedUsers.size());
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent save operations")
        void testConcurrentSaves() throws InterruptedException {
            Map<String, Project> projects = new HashMap<>();
            
            for (int i = 1; i <= 10; i++) {
                SoftwareProject project = new SoftwareProject(
                    "Project " + i, 
                    "Description " + i, 
                    "Software", 
                    3, 
                    "Java", 
                    10000.0 * i
                );
                project.setId("P" + String.format("%03d", i));
                projects.put(project.getId(), project);
            }

            // Create multiple threads to save projects concurrently
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    assertDoesNotThrow(() -> filePersistenceService.saveProjects(projects, List.of()));
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Verify data integrity
            Map<String, Project> loadedProjects = filePersistenceService.loadProjects();
            assertEquals(10, loadedProjects.size());
        }
    }
}
