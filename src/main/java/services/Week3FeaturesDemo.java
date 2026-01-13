//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import Repository.ProjectRepository;
import Repository.TaskRepository;
import interfaces.ProjectFilter;
import interfaces.TaskFilter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import models.HardwareProject;
import models.Project;
import models.SoftwareProject;
import models.Task;

public class Week3FeaturesDemo {
    private final ProjectService projectService = new ProjectService(new ProjectRepository(), new GenerateProjectId());
    private final TaskService taskService = new TaskService(new TaskRepository(), new GenerateTaskId());
    private final ConcurrentTaskUpdateService concurrentService;
    private final ValidationService validationService;

    public Week3FeaturesDemo() {
        this.concurrentService = new ConcurrentTaskUpdateService(this.taskService);
        this.validationService = new ValidationService();
    }

    public void demonstrateAllFeatures() {
        System.out.println("=== Week 3 Enhanced Task Management System Demo ===\n");
        this.demonstrateRegexValidation();
        this.demonstrateFunctionalInterfaces();
        this.demonstrateStreamsAndCollections();
        this.demonstrateNioFilePersistence();
        this.demonstrateConcurrency();
        this.demonstrateFunctionalProgramming();
        System.out.println("=== Demo Complete ===");
    }

    private void demonstrateRegexValidation() {
        System.out.println("1. REGEX VALIDATION DEMO");
        System.out.println("========================");
        System.out.println("Task ID Validation:");
        System.out.println("T001 valid: " + ValidationService.isValidTaskId("T001"));
        System.out.println("T123 valid: " + ValidationService.isValidTaskId("T123"));
        System.out.println("T1 invalid: " + ValidationService.isValidTaskId("T1"));
        System.out.println("T1234 invalid: " + ValidationService.isValidTaskId("T1234"));
        System.out.println("\nProject ID Validation:");
        System.out.println("P001 valid: " + ValidationService.isValidProjectId("P001"));
        System.out.println("P999 valid: " + ValidationService.isValidProjectId("P999"));
        System.out.println("P12 invalid: " + ValidationService.isValidProjectId("P12"));
        System.out.println("\nEmail Validation:");
        System.out.println("user@example.com valid: " + ValidationService.isValidEmail("user@example.com"));
        System.out.println("invalid-email invalid: " + ValidationService.isValidEmail("invalid-email"));
        System.out.println();
    }

    private void demonstrateFunctionalInterfaces() {
        System.out.println("2. FUNCTIONAL INTERFACES DEMO");
        System.out.println("==============================");
        Task var1 = new Task("Design Database", "Completed", "P001");
        Task var2 = new Task("Implement API", "In Progress", "P001");
        Task var3 = new Task("Write Tests", "Pending", "P002");
        Task var4 = new Task("Deploy App", "Completed", "P002");
        List<Task> var5 = Arrays.asList(var1, var2, var3, var4);
        System.out.println("All tasks:");
        var5.forEach((var0) -> {
            PrintStream var10000 = System.out;
            String var10001 = var0.getTaskName();
            var10000.println("  " + var10001 + " - " + var0.getTaskStatus());
        });
        TaskFilter var6 = TaskFilter.completedTasks();
        System.out.println("\nCompleted tasks:");
        var5.stream().filter(var6).forEach((var0) -> {
            PrintStream var10000 = System.out;
            String var10001 = var0.getTaskName();
            var10000.println("  " + var10001 + " - " + var0.getTaskStatus());
        });
        TaskFilter var7 = TaskFilter.byProjectId("P001");
        System.out.println("\nTasks for project P001:");
        var5.stream().filter(var7).forEach((var0) -> {
            PrintStream var10000 = System.out;
            String var10001 = var0.getTaskName();
            var10000.println("  " + var10001 + " - " + var0.getTaskStatus());
        });
        TaskFilter var8 = TaskFilter.byProjectId("P001").and(TaskFilter.completedTasks());
        System.out.println("\nCompleted tasks for project P001:");
        var5.stream().filter(var8).forEach((var0) -> {
            PrintStream var10000 = System.out;
            String var10001 = var0.getTaskName();
            var10000.println("  " + var10001 + " - " + var0.getTaskStatus());
        });
        System.out.println();
    }

    private void demonstrateStreamsAndCollections() {
        System.out.println("3. STREAMS AND COLLECTIONS DEMO");
        System.out.println("=================================");
        SoftwareProject var1 = new SoftwareProject("E-commerce Platform", "Online shopping", "Software", 5, "Java", (double)75000.0F);
        HardwareProject var2 = new HardwareProject("IoT Sensor", "Temperature sensor", "Hardware", 3, "IoT Device", (double)25000.0F);
        SoftwareProject var3 = new SoftwareProject("Mobile App", "Android application", "Software", 4, "Kotlin", (double)45000.0F);
        List<Project> var4 = Arrays.asList(var1, var2, var3);
        System.out.println("Project Analysis using Streams:");
        double var5 = var4.stream().mapToDouble(Project::getBudget).average().orElse(0.0);
        PrintStream var10000 = System.out;
        var10000.println("  Average budget: $" + String.format("%.2f", var5));
        long var7 = var4.stream().filter((var0) -> "Software".equals(var0.getType())).count();
        long var9 = var4.stream().filter((var0) -> "Hardware".equals(var0.getType())).count();
        System.out.println("  Software projects: " + var7);
        System.out.println("  Hardware projects: " + var9);
        ProjectFilter var11 = ProjectFilter.budgetGreaterThan(50000.0);
        System.out.println("\nProjects with budget > $50,000:");
        var4.stream().filter(var11).forEach((var0) -> {
            System.out.println("  " + var0.getName() + " - $" + String.format("%.2f", var0.getBudget()));
        });
        System.out.println();
    }

    private void demonstrateNioFilePersistence() {
        System.out.println("4. NIO FILE PERSISTENCE DEMO");
        System.out.println("==============================");
        new FilePersistenceService();
        System.out.println("Demonstrating NIO-based file persistence...");
        System.out.println("Data files are stored in 'src/data/' directory:");
        System.out.println("  - projects_data.json");
        System.out.println("  - tasks_data.json");
        System.out.println("  - users_data.json");
        System.out.println("Files are automatically loaded on startup and saved on changes.");
        System.out.println("Uses java.nio.file.Files.readString() and Files.writeString()");
        System.out.println();
    }

    private void demonstrateConcurrency() {
        System.out.println("5. CONCURRENCY DEMO");
        System.out.println("====================");
        Task var1 = new Task("Concurrent Task 1", "Pending", "P001");
        Task var2 = new Task("Concurrent Task 2", "Pending", "P001");
        Task var3 = new Task("Concurrent Task 3", "Pending", "P002");
        var1.setTaskId("T001");
        var2.setTaskId("T002");
        var3.setTaskId("T003");
        List<String> var4 = Arrays.asList("T001", "T002", "T003");
        System.out.println("Demonstrating concurrent task updates...");
        System.out.println("Updating 3 tasks concurrently using ExecutorService...");

        try {
            CompletableFuture<List<Task>> var5 = this.concurrentService.updateTasksConcurrently(var4, "Completed");
            List<Task> var6 = var5.get();
            System.out.println("Concurrent updates completed!");
            System.out.println("Total tasks updated: " + var6.size());
            System.out.println("\nDemonstrating parallel stream processing...");
            this.concurrentService.updateTasksWithParallelStream(var4, "In Progress");
            System.out.println("Parallel stream processing completed!");
        } catch (Exception var7) {
            System.err.println("Concurrency demo error: " + var7.getMessage());
        }

        System.out.println();
    }

    private void demonstrateFunctionalProgramming() {
        System.out.println("6. FUNCTIONAL PROGRAMMING DEMO");
        System.out.println("===============================");
        List<Task> var1 = Arrays.asList(new Task("Task A", "Completed", "P001"), new Task("Task B", "In Progress", "P001"), new Task("Task C", "Pending", "P002"), new Task("Task D", "Completed", "P002"), new Task("Task E", "In Progress", "P003"));
        System.out.println("Functional Programming Patterns:");
        System.out.println("\n1. Method References:");
        System.out.println("  Task names:");
        Stream<String> var10000 = var1.stream().map(Task::getTaskName);
        var10000.forEach(System.out::println);
        System.out.println("\n2. Lambda Expressions:");
        System.out.println("  Tasks with 'In Progress' status:");
        var1.stream().filter((var0) -> "In Progress".equals(var0.getTaskStatus())).forEach((var0) -> System.out.println("    " + var0.getTaskName()));
        System.out.println("\n3. Custom Functional Interfaces:");
        TaskFilter var2 = TaskFilter.byStatus("Completed").or(TaskFilter.byProjectId("P001"));
        System.out.println("  Completed tasks OR tasks from P001:");
        var1.stream().filter(var2).forEach((var0) -> {
            System.out.println("    " + var0.getTaskName() + " (" + var0.getTaskStatus() + ", " + var0.getProjectId() + ")");
        });
        System.out.println("\n4. Stream Aggregation:");
        long var3 = var1.stream().filter((var0) -> "Completed".equals(var0.getTaskStatus())).count();
        System.out.println("  Total completed tasks: " + var3);
        List<String> var5 = var1.stream().map(Task::getProjectId).distinct().toList();
        System.out.println("  Distinct projects: " + String.valueOf(var5));
        System.out.println();
    }

    public static void main(String[] var0) {
        Week3FeaturesDemo var1 = new Week3FeaturesDemo();
        var1.demonstrateAllFeatures();
    }
}
