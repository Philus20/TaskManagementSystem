package Tests;

import Repository.ProjectRepository;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import services.GenerateProjectId;
import services.ProjectService;
import utils.exceptions.EmptyProjectException;


import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {



    /**
     * Comprehensive JUnit 5 tests for ProjectService
     * Tests cover:
     * - Project creation (Software and Hardware)
     * - Project retrieval (by ID, all projects)
     * - Project deletion
     * - Project filtering (by type, by budget range)
     * - Exception handling
     */


        private ProjectService projectService;
        private ProjectRepository projectRepository;
        private GenerateProjectId projectIdGenerator;

        @BeforeEach
        void setUp() {
            // Initialize fresh dependencies for each test
            projectRepository = new ProjectRepository(20);
            projectIdGenerator = new GenerateProjectId();
            projectService = new ProjectService(projectRepository, projectIdGenerator);
        }

        /**
         * Test: Add Software Project - valid project
         */
        @Test
        void testAddProject_SoftwareProject() {
            // Given: A valid software project
            SoftwareProject project = new SoftwareProject(
                    "Web Application",
                    "A web-based task management system",
                    "Software",
                    5,
                    "Java",
                    50000.0);

            // When: Adding project
            // Then: Should not throw exception
            assertDoesNotThrow(() -> projectService.addProject(project),
                    "Adding a valid software project should not throw exception");

            // Verify project was added and ID was generated
            assertNotNull(project.getId(), "Project ID should be generated");
            assertFalse(project.getId().isEmpty(), "Project ID should not be empty");
        }

        /**
         * Test: Add Hardware Project - valid project
         */
        @Test
        void testAddProject_HardwareProject() {
            // Given: A valid hardware project
            HardwareProject project = new HardwareProject(
                    "Server Setup",
                    "Setting up new server infrastructure",
                    "Hardware",
                    3,
                    "Server",
                    100000.0);

            // When: Adding project
            // Then: Should not throw exception
            assertDoesNotThrow(() -> projectService.addProject(project),
                    "Adding a valid hardware project should not throw exception");

            // Verify project was added and ID was generated
            assertNotNull(project.getId(), "Project ID should be generated");
            assertFalse(project.getId().isEmpty(), "Project ID should not be empty");
        }

        /**
         * Test: Get project by ID - existing project
         */
        @Test
        void testGetProjectById_ExistingProject() {
            // Given: A project added to service
            SoftwareProject project = new SoftwareProject(
                    "Mobile App",
                    "iOS mobile application",
                    "Software",
                    4,
                    "Swift",
                    75000.0);
            projectService.addProject(project);
            String projectId = project.getId();

            // When: Getting project by ID
            Project retrievedProject = projectService.getProjectById(projectId);

            // Then: Should return the correct project
            assertNotNull(retrievedProject, "Retrieved project should not be null");
            assertEquals(projectId, retrievedProject.getId(), "Project ID should match");
            assertEquals("Mobile App", retrievedProject.getName(), "Project name should match");
            assertEquals("Software", retrievedProject.getType(), "Project type should match");
        }

        /**
         * Test: Get project by ID - non-existent project
         */
        @Test
        void testGetProjectById_NonExistentProject() {
            // Given: A non-existent project ID
            String nonExistentProjectId = "P9999";

            // When/Then: Should throw EmptyProjectException
            assertThrows(EmptyProjectException.class,
                    () -> projectService.getProjectById(nonExistentProjectId),
                    "Getting non-existent project should throw EmptyProjectException");
        }

        /**
         * Test: Get project by ID - invalid index (negative)
         */
        @Test
        void testGetProjectById_InvalidNegativeIndex() {
            // Given: An invalid project ID that results in negative index
            // This would happen if ID format is invalid
            // When/Then: Should throw IndexIsLessThanZero or EmptyProjectException
            // Note: This depends on ID generator implementation
            String invalidId = "INVALID";

            // The actual behavior depends on how elementIndex handles invalid IDs
            // This test verifies exception handling
            assertThrows(Exception.class,
                    () -> projectService.getProjectById(invalidId),
                    "Invalid project ID should throw an exception");
        }

        /**
         * Test: Get all projects - multiple projects
         */
        @Test
        void testGetAllProjects_MultipleProjects() {
            // Given: Multiple projects added
            SoftwareProject project1 = new SoftwareProject("Project 1", "Desc 1", "Software", 3, "Java", 30000.0);
            HardwareProject project2 = new HardwareProject("Project 2", "Desc 2", "Hardware", 2, "Server", 50000.0);
            SoftwareProject project3 = new SoftwareProject("Project 3", "Desc 3", "Software", 4, "Python", 40000.0);

            projectService.addProject(project1);
            projectService.addProject(project2);
            projectService.addProject(project3);

            // When: Getting all projects
            Project[] projects = projectService.getAllProjects();

            // Then: Should return all projects
            assertNotNull(projects, "Projects array should not be null");
            assertTrue(projects.length >= 3, "Should return at least 3 projects");
        }

        /**
         * Test: Get all projects - no projects
         */
        @Test
        void testGetAllProjects_NoProjects() {
            // Given: No projects added
            // When/Then: Should throw EmptyProjectException
            assertThrows(EmptyProjectException.class,
                    () -> projectService.getAllProjects(),
                    "Getting all projects when none exist should throw EmptyProjectException");
        }

        /**
         * Test: Delete project by ID - existing project
         */
        @Test
        void testDeleteProjectById_ExistingProject() {
            // Given: A project added to service
            SoftwareProject project = new SoftwareProject("Test Project", "Description", "Software", 2, "Java", 20000.0);
            projectService.addProject(project);
            String projectId = project.getId();

            // When: Deleting project
            projectService.deleteProjectById(projectId);

            // Then: Project should no longer be retrievable
            assertThrows(EmptyProjectException.class,
                    () -> projectService.getProjectById(projectId),
                    "Deleted project should not be retrievable");
        }

        /**
         * Test: Delete project by ID - non-existent project
         */
        @Test
        void testDeleteProjectById_NonExistentProject() {
            // Given: A non-existent project ID
            String nonExistentProjectId = "P9999";

            // When/Then: Should throw exception
            assertThrows(Exception.class,
                    () -> projectService.deleteProjectById(nonExistentProjectId),
                    "Deleting non-existent project should throw an exception");
        }

        /**
         * Test: Filter projects by type - Software projects
         */
        @Test
        void testFilterByType_SoftwareProjects() {
            // Given: Multiple projects of different types
            SoftwareProject software1 = new SoftwareProject("App 1", "Desc", "Software", 2, "Java", 30000.0);
            SoftwareProject software2 = new SoftwareProject("App 2", "Desc", "Software", 3, "Python", 40000.0);
            HardwareProject hardware1 = new HardwareProject("Server 1", "Desc", "Hardware", 2, "Server", 50000.0);

            projectService.addProject(software1);
            projectService.addProject(software2);
            projectService.addProject(hardware1);

            // When: Filtering by Software type
            Project[] softwareProjects = projectService.filterByType("Software");

            // Then: Should return only Software projects
            assertNotNull(softwareProjects, "Filtered projects should not be null");
            assertEquals(2, softwareProjects.length, "Should return 2 Software projects");
            for (Project p : softwareProjects) {
                assertEquals("Software", p.getType(), "All projects should be Software type");
            }
        }

        /**
         * Test: Filter projects by type - Hardware projects
         */
        @Test
        void testFilterByType_HardwareProjects() {
            // Given: Multiple projects of different types
            SoftwareProject software1 = new SoftwareProject("App 1", "Desc", "Software", 2, "Java", 30000.0);
            HardwareProject hardware1 = new HardwareProject("Server 1", "Desc", "Hardware", 2, "Server", 50000.0);
            HardwareProject hardware2 = new HardwareProject("Network 1", "Desc", "Hardware", 3, "Network", 60000.0);

            projectService.addProject(software1);
            projectService.addProject(hardware1);
            projectService.addProject(hardware2);

            // When: Filtering by Hardware type
            Project[] hardwareProjects = projectService.filterByType("Hardware");

            // Then: Should return only Hardware projects
            assertNotNull(hardwareProjects, "Filtered projects should not be null");
            assertEquals(2, hardwareProjects.length, "Should return 2 Hardware projects");
            for (Project p : hardwareProjects) {
                assertEquals("Hardware", p.getType(), "All projects should be Hardware type");
            }
        }

        /**
         * Test: Filter projects by type - case insensitive
         */
        @Test
        void testFilterByType_CaseInsensitive() {
            // Given: Software projects
            SoftwareProject project = new SoftwareProject("App", "Desc", "Software", 2, "Java", 30000.0);
            projectService.addProject(project);

            // When: Filtering with different case
            Project[] projects1 = projectService.filterByType("software");
            Project[] projects2 = projectService.filterByType("SOFTWARE");
            Project[] projects3 = projectService.filterByType("Software");

            // Then: All should return the same projects
            assertEquals(1, projects1.length, "Lowercase filter should work");
            assertEquals(1, projects2.length, "Uppercase filter should work");
            assertEquals(1, projects3.length, "Mixed case filter should work");
        }

        /**
         * Test: Find projects by budget range - valid range
         */
        @Test
        void testFindByBudgetRange_ValidRange() {
            // Given: Projects with different budgets
            SoftwareProject project1 = new SoftwareProject("Low Budget", "Desc", "Software", 2, "Java", 10000.0);
            SoftwareProject project2 = new SoftwareProject("Mid Budget", "Desc", "Software", 3, "Python", 50000.0);
            SoftwareProject project3 = new SoftwareProject("High Budget", "Desc", "Software", 4, "C++", 100000.0);

            projectService.addProject(project1);
            projectService.addProject(project2);
            projectService.addProject(project3);

            // When: Finding projects in budget range 20000-80000
            Project[] projects = projectService.findByBudgetRange(20000.0, 80000.0);

            // Then: Should return projects within range
            assertNotNull(projects, "Filtered projects should not be null");
            assertEquals(1, projects.length, "Should return 1 project in range");
            assertTrue(projects[0].getBudget() >= 20000.0 && projects[0].getBudget() <= 80000.0,
                    "Project budget should be within range");
        }

        /**
         * Test: Find projects by budget range - all projects in range
         */
        @Test
        void testFindByBudgetRange_AllInRange() {
            // Given: Projects all within a budget range
            SoftwareProject project1 = new SoftwareProject("Project 1", "Desc", "Software", 2, "Java", 30000.0);
            SoftwareProject project2 = new SoftwareProject("Project 2", "Desc", "Software", 3, "Python", 40000.0);
            SoftwareProject project3 = new SoftwareProject("Project 3", "Desc", "Software", 4, "C++", 50000.0);

            projectService.addProject(project1);
            projectService.addProject(project2);
            projectService.addProject(project3);

            // When: Finding projects in budget range 25000-55000
            Project[] projects = projectService.findByBudgetRange(25000.0, 55000.0);

            // Then: Should return all projects
            assertNotNull(projects, "Filtered projects should not be null");
            assertEquals(3, projects.length, "Should return all 3 projects");
        }

        /**
         * Test: Find projects by budget range - no projects in range
         */
        @Test
        void testFindByBudgetRange_NoProjectsInRange() {
            // Given: Projects outside a budget range
            SoftwareProject project1 = new SoftwareProject("Project 1", "Desc", "Software", 2, "Java", 10000.0);
            SoftwareProject project2 = new SoftwareProject("Project 2", "Desc", "Software", 3, "Python", 20000.0);

            projectService.addProject(project1);
            projectService.addProject(project2);

            // When: Finding projects in budget range 50000-100000
            Project[] projects = projectService.findByBudgetRange(50000.0, 100000.0);

            // Then: Should return empty array
            assertNotNull(projects, "Filtered projects should not be null");
            assertEquals(0, projects.length, "Should return empty array when no projects in range");
        }

        /**
         * Test: Project ID auto-generation
         */
        @Test
        void testProjectId_AutoGeneration() {
            // Given: A project without ID
            SoftwareProject project = new SoftwareProject("Test", "Desc", "Software", 2, "Java", 30000.0);
            assertTrue(project.getId() == null || project.getId().isEmpty(),
                    "Project should start without ID");

            // When: Adding project
            projectService.addProject(project);

            // Then: ID should be auto-generated
            assertNotNull(project.getId(), "Project ID should be generated");
            assertFalse(project.getId().isEmpty(), "Project ID should not be empty");
        }

        /**
         * Test: Multiple projects have unique IDs
         */
        @Test
        void testProjectId_UniqueIds() {
            // Given: Multiple projects
            SoftwareProject project1 = new SoftwareProject("Project 1", "Desc", "Software", 2, "Java", 30000.0);
            SoftwareProject project2 = new SoftwareProject("Project 2", "Desc", "Software", 3, "Python", 40000.0);
            SoftwareProject project3 = new SoftwareProject("Project 3", "Desc", "Software", 4, "C++", 50000.0);

            projectService.addProject(project1);
            projectService.addProject(project2);
            projectService.addProject(project3);

            // Then: All projects should have unique IDs
            assertNotEquals(project1.getId(), project2.getId(), "Project 1 and 2 should have different IDs");
            assertNotEquals(project2.getId(), project3.getId(), "Project 2 and 3 should have different IDs");
            assertNotEquals(project1.getId(), project3.getId(), "Project 1 and 3 should have different IDs");
        }

        /**
         * Test: Project properties are preserved
         */
        @Test
        void testProjectProperties_Preserved() {
            // Given: A project with specific properties
            SoftwareProject project = new SoftwareProject(
                    "Web App",
                    "A web application",
                    "Software",
                    5,
                    "JavaScript",
                    60000.0);

            projectService.addProject(project);

            // When: Retrieving project
            Project retrieved = projectService.getProjectById(project.getId());

            // Then: All properties should be preserved
            assertEquals("Web App", retrieved.getName(), "Project name should match");
            assertEquals("A web application", retrieved.getDescription(), "Project description should match");
            assertEquals("Software", retrieved.getType(), "Project type should match");
            assertEquals(5, retrieved.getTeamSize(), "Team size should match");
            assertEquals(60000.0, retrieved.getBudget(), 0.01, "Budget should match");

            // Verify SoftwareProject specific property
            if (retrieved instanceof SoftwareProject) {
                SoftwareProject softwareProject = (SoftwareProject) retrieved;
                assertEquals("JavaScript", softwareProject.getProgrammingLanguage(),
                        "Programming language should match");
            }
        }
    }

