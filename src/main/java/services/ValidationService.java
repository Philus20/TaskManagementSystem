package services;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Validation service using regex patterns for ID validation
 * Following Week 3 requirements for regex validation
 */
public class ValidationService {
    
    // Regex patterns
    private static final Pattern TASK_ID_PATTERN = Pattern.compile("T\\d{3}");
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("P\\d{3}");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("U\\d{3}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    /**
     * Validate task ID format (T001, T002, etc.)
     */
    public static boolean isValidTaskId(String taskId) {
        if (taskId == null) return false;
        Matcher matcher = TASK_ID_PATTERN.matcher(taskId);
        return matcher.matches();
    }

    /**
     * Validate project ID format (P001, P002, etc.)
     */
    public static boolean isValidProjectId(String projectId) {
        if (projectId == null) return false;
        Matcher matcher = PROJECT_ID_PATTERN.matcher(projectId);
        return matcher.matches();
    }
    
    /**
     * Validate user ID format (U001, U002, etc.)
     */
    public static boolean isValidUserId(String userId) {
        if (userId == null) return false;
        Matcher matcher = USER_ID_PATTERN.matcher(userId);
        return matcher.matches();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    
    /**
     * Validate task status
     */
    public static boolean isValidTaskStatus(String status) {
        if (status == null) return false;
        return status.equalsIgnoreCase("Pending") || 
               status.equalsIgnoreCase("In Progress") || 
               status.equalsIgnoreCase("Completed");
    }
    
    /**
     * Validate project type
     */
    public static boolean isValidProjectType(String type) {
        if (type == null) return false;
        return type.equalsIgnoreCase("Software") || 
               type.equalsIgnoreCase("Hardware");
    }
    
    /**
     * Get detailed validation error message
     */
    public static String getValidationErrorMessage(String taskId, String projectId, String userId, String email) {
        StringBuilder errors = new StringBuilder();
        
        if (!isValidTaskId(taskId)) {
            errors.append("Invalid Task ID format. Expected T001, T002, etc.\\n");
        }
        
        if (!isValidProjectId(projectId)) {
            errors.append("Invalid Project ID format. Expected P001, P002, etc.\\n");
        }
        
        if (!isValidUserId(userId)) {
            errors.append("Invalid User ID format. Expected U001, U002, etc.\\n");
        }
        
        if (!isValidEmail(email)) {
            errors.append("Invalid email format.\\n");
        }
        
        return errors.length() > 0 ? errors.toString() : "All validations passed.";
    }
    
    /**
     * Extract numeric part from ID
     */
    public static int extractIdNumber(String id) {
        if (id == null || id.length() < 2) {
            throw new IllegalArgumentException("Invalid ID format");
        }
        
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric part in ID: " + id, e);
        }
    }
}
