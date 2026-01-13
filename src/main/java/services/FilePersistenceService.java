package services;

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

/**
 * File persistence service using NIO for JSON-like data storage
 * Following Week 3 requirements for NIO-based file persistence
 */
public class FilePersistenceService {
    
    private static final String DATA_DIRECTORY = "src/data";
    private static final String PROJECTS_FILE = "projects_data.json";
    private static final String TASKS_FILE = "tasks_data.json";
    private static final String USERS_FILE = "users_data.json";
    
    private final Path dataDir;
    private final Path projectsPath;
    private final Path tasksPath;
    private final Path usersPath;
    
    public FilePersistenceService() {
        this.dataDir = Paths.get(DATA_DIRECTORY);
        this.projectsPath = dataDir.resolve(PROJECTS_FILE);
        this.tasksPath = dataDir.resolve(TASKS_FILE);
        this.usersPath = dataDir.resolve(USERS_FILE);
        
        // Create data directory if it doesn't exist
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }
    
    /**
     * Save all projects to file using NIO
     */
    public synchronized void saveProjects(Map<String, Project> projects, List<Task> allTasks) {
        try {
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
            
            Files.writeString(projectsPath, jsonBuilder.toString());
            System.out.println("Projects saved successfully to " + projectsPath);
            
        } catch (IOException e) {
            System.err.println("Failed to save projects: " + e.getMessage());
        }
    }
    
    /**
     * Load all projects from file using NIO
     */
    public synchronized Map<String, Project> loadProjects() {
        Map<String, Project> projects = new HashMap<>();
        
        if (!Files.exists(projectsPath)) {
            System.out.println("Projects file not found. Starting with empty project list.");
            return projects;
        }
        
        try {
            String content = Files.readString(projectsPath);
            if (content.trim().isEmpty()) {
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
            
        } catch (IOException e) {
            System.err.println("Failed to load projects: " + e.getMessage());
            System.out.println("Starting with empty project list.");
        }
        
        return projects;
    }
    
    /**
     * Save all tasks to file using NIO
     */
    public synchronized void saveTasks(List<Task> tasks) {
        try {
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
            
            Files.writeString(tasksPath, jsonBuilder.toString());
            System.out.println("Tasks saved successfully to " + tasksPath);
            
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }
    
    /**
     * Load all tasks from file using NIO
     */
    public synchronized List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        
        if (!Files.exists(tasksPath)) {
            System.out.println("Tasks file not found. Starting with empty task list.");
            return tasks;
        }
        
        try {
            String content = Files.readString(tasksPath);
            if (content.trim().isEmpty()) {
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
            
        } catch (IOException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
            System.out.println("Starting with empty task list.");
        }
        
        return tasks;
    }
    
    /**
     * Save all users to file using NIO
     */
    public synchronized void saveUsers(Map<String, User> users) {
        try {
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
            
            Files.writeString(usersPath, jsonBuilder.toString());
            System.out.println("Users saved successfully to " + usersPath);
            
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }
    
    /**
     * Load all users from file using NIO
     */
    public synchronized Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        
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
    private String projectToJson(Project project, List<Task> allTasks) {
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
    
    private String taskToJson(Task task) {
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
    
    private String userToJson(User user) {
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
    private Project jsonToProject(String json) {
        // This is a simplified JSON parser for demonstration
        // In production, use a proper JSON library
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
    
    private Task jsonToTask(String json) {
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
    
    private User jsonToUser(String json) {
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
    
    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        // Try numeric value
        pattern = "\"" + key + "\"\\s*:\\s*(\\d+)";
        p = java.util.regex.Pattern.compile(pattern);
        m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        
        return null;
    }
}
