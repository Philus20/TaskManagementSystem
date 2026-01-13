package utils;

import models.Project;
import models.Task;
import models.User;
import models.SoftwareProject;
import models.HardwareProject;
import models.AdminUser;
import models.RegularUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Utility class for file operations using NIO
 * Provides common file I/O operations with error handling
 */
public class FileUtils {
    
    /**
     * Create directory if it doesn't exist
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Write string content to file
     */
    public static boolean writeToFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read string content from file
     */
    public static String readFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return null;
            }
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("Failed to read from file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Check if file is empty
     */
    public static boolean isFileEmpty(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return true;
            }
            return Files.size(path) == 0;
        } catch (IOException e) {
            System.err.println("Failed to check file size: " + e.getMessage());
            return true;
        }
    }
    
    /**
     * Get file size in bytes
     */
    public static long getFileSize(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return -1;
            }
            return Files.size(path);
        } catch (IOException e) {
            System.err.println("Failed to get file size: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Delete file if it exists
     */
    public static boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read all lines from file
     */
    public static List<String> readAllLines(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return List.of();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Failed to read lines from file: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Write lines to file
     */
    public static boolean writeLines(String filePath, List<String> lines) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, lines);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to write lines to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Async file write operation
     */
    public static CompletableFuture<Boolean> writeToFileAsync(String filePath, String content) {
        return CompletableFuture.supplyAsync(() -> writeToFile(filePath, content));
    }
    
    /**
     * Async file read operation
     */
    public static CompletableFuture<String> readFromFileAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> readFromFile(filePath));
    }
    
    /**
     * Stream lines from file
     */
    public static Stream<String> streamLines(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return Stream.empty();
            }
            return Files.lines(path);
        } catch (IOException e) {
            System.err.println("Failed to stream lines from file: " + e.getMessage());
            return Stream.empty();
        }
    }
    
    /**
     * Get file extension
     */
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return "";
        }
        return filePath.substring(lastDotIndex + 1);
    }
    
    /**
     * Get file name without extension
     */
    public static String getFileNameWithoutExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
    
    /**
     * Backup file by copying with timestamp suffix
     */
    public static boolean backupFile(String filePath) {
        try {
            Path originalPath = Paths.get(filePath);
            if (!Files.exists(originalPath)) {
                return false;
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String backupPath = filePath + ".backup_" + timestamp;
            Path backupFilePath = Paths.get(backupPath);
            
            Files.copy(originalPath, backupFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to backup file: " + e.getMessage());
            return false;
        }
    }
    
    // Data constants for JSON files
    private static final String DATA_DIRECTORY = "src/data";
    private static final String PROJECTS_FILE = "projects_data.json";
    private static final String TASKS_FILE = "tasks_data.json";
    private static final String USERS_FILE = "users_data.json";
    
    /**
     * Save all projects to file using NIO
     */
    public static synchronized void saveProjects(Map<String, Project> projects, List<Task> allTasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"projects\": [\n");
        
        boolean first = true;
        for (Map.Entry<String, Project> entry : projects.entrySet()) {
            if (!first) {
                jsonBuilder.append(",\n");
            }
            first = false;
            jsonBuilder.append(projectToJson(entry.getValue(), allTasks));
        }
        
        jsonBuilder.append("\n  ]\n");
        jsonBuilder.append("}");
        
        String projectsFilePath = DATA_DIRECTORY + "/" + PROJECTS_FILE;
        if (writeToFile(projectsFilePath, jsonBuilder.toString())) {
            System.out.println("Projects saved successfully to " + projectsFilePath);
        }
    }
    
    /**
     * Load all projects from file using NIO
     */
    public static synchronized Map<String, Project> loadProjects() {
        Map<String, Project> projects = new HashMap<>();
        
        String projectsFilePath = DATA_DIRECTORY + "/" + PROJECTS_FILE;
        if (!fileExists(projectsFilePath)) {
            System.out.println("Projects file not found. Starting with empty project list.");
            return projects;
        }
        
        String content = readFromFile(projectsFilePath);
        if (content == null || content.trim().isEmpty()) {
            System.out.println("Projects file is empty. Starting with empty project list.");
            return projects;
        }
        
        // Simple JSON parsing (basic implementation)
        String[] projectLines = content.split("\\{");
        for (String line : projectLines) {
            if (line.contains("\"id\"")) {
                Project project = jsonToProject(line);
                if (project != null) {
                    projects.put(project.getId(), project);
                }
            }
        }
        
        System.out.println("Loaded " + projects.size() + " projects from file.");
        return projects;
    }
    
    /**
     * Save all tasks to file using NIO
     */
    public static synchronized void saveTasks(List<Task> tasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"tasks\": [\n");
        
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                jsonBuilder.append(",\n");
            }
            jsonBuilder.append(taskToJson(tasks.get(i)));
        }
        
        jsonBuilder.append("\n  ]\n");
        jsonBuilder.append("}");
        
        String tasksFilePath = DATA_DIRECTORY + "/" + TASKS_FILE;
        if (writeToFile(tasksFilePath, jsonBuilder.toString())) {
            System.out.println("Tasks saved successfully to " + tasksFilePath);
        }
    }
    
    /**
     * Load all tasks from file using NIO
     */
    public static synchronized List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        
        String tasksFilePath = DATA_DIRECTORY + "/" + TASKS_FILE;
        if (!fileExists(tasksFilePath)) {
            System.out.println("Tasks file not found. Starting with empty task list.");
            return tasks;
        }
        
        String content = readFromFile(tasksFilePath);
        if (content == null || content.trim().isEmpty()) {
            System.out.println("Tasks file is empty. Starting with empty task list.");
            return tasks;
        }
        
        // Simple JSON parsing
        String[] taskLines = content.split("\\{");
        for (String line : taskLines) {
            if (line.contains("\"taskId\"")) {
                Task task = jsonToTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
        
        System.out.println("Loaded " + tasks.size() + " tasks from file.");
        return tasks;
    }
    
    /**
     * Save all users to file using NIO
     */
    public static synchronized void saveUsers(Map<String, User> users) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"users\": [\n");
        
        boolean first = true;
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (!first) {
                jsonBuilder.append(",\n");
            }
            first = false;
            jsonBuilder.append(userToJson(entry.getValue()));
        }
        
        jsonBuilder.append("\n  ]\n");
        jsonBuilder.append("}");
        
        String usersFilePath = DATA_DIRECTORY + "/" + USERS_FILE;
        if (writeToFile(usersFilePath, jsonBuilder.toString())) {
            System.out.println("Users saved successfully to " + usersFilePath);
        }
    }
    
    /**
     * Load all users from file using NIO
     */
    public static synchronized Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        
        String usersFilePath = DATA_DIRECTORY + "/" + USERS_FILE;
        Path usersPath = Paths.get(usersFilePath);
        
        if (!Files.exists(usersPath)) {
            System.out.println("Users file not found. Starting with empty user list.");
            return users;
        }
        
        try {
            String content = Files.readString(usersPath);
            if (content.trim().isEmpty()) {
                System.out.println("Users file is empty. Starting with empty user list.");
                return users;
            }
            
            // Simple JSON parsing
            String[] userLines = content.split("\\{");
            for (String line : userLines) {
                if (line.contains("\"id\"")) {
                    User user = jsonToUser(line);
                    if (user != null) {
                        users.put(user.getId(), user);
                    }
                }
            }
            
            System.out.println("Loaded " + users.size() + " users from file.");
            
        } catch (IOException e) {
            System.err.println("Failed to load users: " + e.getMessage());
            System.out.println("Starting with empty user list.");
        }
        
        return users;
    }
    
    // Helper methods for JSON conversion
    private static String projectToJson(Project project, List<Task> allTasks) {
        if (project == null) return "";
        
        StringBuilder json = new StringBuilder();
        json.append("    {\n");
        json.append("      \"id\": \"").append(project.getId()).append("\",\n");
        json.append("      \"name\": \"").append(project.getName()).append("\",\n");
        json.append("      \"description\": \"").append(project.getDescription()).append("\",\n");
        json.append("      \"type\": \"").append(project.getType()).append("\",\n");
        json.append("      \"teamSize\": ").append(project.getTeamSize()).append(",\n");
        json.append("      \"budget\": ").append(project.getBudget());
        
        if (project instanceof SoftwareProject) {
            json.append(",\n      \"language\": \"").append(((SoftwareProject) project).getProgrammingLanguage()).append("\"");
        } else if (project instanceof HardwareProject) {
            json.append(",\n      \"hardwareType\": \"").append(((HardwareProject) project).getHardwareType()).append("\"");
        }
        
        // Add tasks array for this project
        json.append(",\n      \"tasks\": [\n");
        List<Task> projectTasks = allTasks.stream()
                .filter(task -> task != null && project.getId().equals(task.getProjectId()))
                .toList();
        
        for (int i = 0; i < projectTasks.size(); i++) {
            if (i > 0) {
                json.append(",\n");
            }
            json.append(taskToJson(projectTasks.get(i)));
        }
        
        json.append("\n      ]");
        json.append("\n    }");
        return json.toString();
    }
    
    private static String taskToJson(Task task) {
        if (task == null) return "";
        
        StringBuilder json = new StringBuilder();
        json.append("    {\n");
        json.append("      \"taskId\": \"").append(task.getTaskId()).append("\",\n");
        json.append("      \"taskName\": \"").append(task.getTaskName()).append("\",\n");
        json.append("      \"taskStatus\": \"").append(task.getTaskStatus()).append("\",\n");
        json.append("      \"projectId\": \"").append(task.getProjectId()).append("\"");
        
        if (task.getAssignedUserId() != null) {
            json.append(",\n      \"assignedUserId\": \"").append(task.getAssignedUserId()).append("\"");
        }
        
        json.append("\n    }");
        return json.toString();
    }
    
    private static String userToJson(User user) {
        if (user == null) return "";
        
        StringBuilder json = new StringBuilder();
        json.append("    {\n");
        json.append("      \"id\": \"").append(user.getId()).append("\",\n");
        json.append("      \"name\": \"").append(user.getName()).append("\",\n");
        json.append("      \"email\": \"").append(user.getEmail()).append("\",\n");
        json.append("      \"role\": \"").append(user.getRole()).append("\"");
        json.append("\n    }");
        return json.toString();
    }
    
    // Simple JSON parsing methods (basic implementation)
    private static Project jsonToProject(String json) {
        try {
            String id = extractValue(json, "id");
            String name = extractValue(json, "name");
            String description = extractValue(json, "description");
            String type = extractValue(json, "type");
            String teamSizeStr = extractValue(json, "teamSize");
            String budgetStr = extractValue(json, "budget");
            
            // Validate required fields
            if (id == null || name == null || type == null || teamSizeStr == null || budgetStr == null) {
                return null;
            }
            
            int teamSize = Integer.parseInt(teamSizeStr);
            double budget = Double.parseDouble(budgetStr);
            
            Project project;
            if ("Software".equalsIgnoreCase(type)) {
                String language = extractValue(json, "language");
                project = new SoftwareProject(name, description, type, teamSize, language, budget);
                project.setId(id);
            } else if ("Hardware".equalsIgnoreCase(type)) {
                String hardwareType = extractValue(json, "hardwareType");
                project = new HardwareProject(name, description, type, teamSize, hardwareType, budget);
                project.setId(id);
            } else {
                project = null; // Unknown type
            }
            
            return project;
        } catch (Exception e) {
            System.err.println("Failed to parse project JSON: " + e.getMessage());
            return null;
        }
    }
    
    private static Task jsonToTask(String json) {
        try {
            String taskId = extractValue(json, "taskId");
            String taskName = extractValue(json, "taskName");
            String taskStatus = extractValue(json, "taskStatus");
            String projectId = extractValue(json, "projectId");
            String assignedUserId = extractValue(json, "assignedUserId");
            
            Task task = new Task(taskName, taskStatus, projectId);
            task.setTaskId(taskId);
            if (assignedUserId != null && !assignedUserId.isEmpty()) {
                task.setAssignedUserId(assignedUserId);
            }
            
            return task;
        } catch (Exception e) {
            System.err.println("Failed to parse task JSON: " + e.getMessage());
            return null;
        }
    }
    
    private static User jsonToUser(String json) {
        try {
            String id = extractValue(json, "id");
            String name = extractValue(json, "name");
            String email = extractValue(json, "email");
            String role = extractValue(json, "role");
            
            User user;
            if ("Admin".equalsIgnoreCase(role)) {
                user = new AdminUser(name, email);
                user.setId(id);
            } else {
                user = new RegularUser(name, email);
                user.setId(id);
            }
            
            return user;
        } catch (Exception e) {
            System.err.println("Failed to parse user JSON: " + e.getMessage());
            return null;
        }
    }
    
    private static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        // Try numeric value (including decimals for budget)
        pattern = "\"" + key + "\"\\s*:\\s*(\\d+(?:\\.\\d+)?)";
        p = java.util.regex.Pattern.compile(pattern);
        m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        return null;
    }
}
