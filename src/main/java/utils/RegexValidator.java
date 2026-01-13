package utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utility class for common regex validation patterns
 * Provides centralized validation for various data formats
 */
public class RegexValidator {
    
    // Common regex patterns
    private static final Pattern TASK_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^U\\d{3}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,50}$");
    private static final Pattern TASK_STATUS_PATTERN = Pattern.compile("^(Pending|In Progress|Completed)$");
    private static final Pattern PROJECT_TYPE_PATTERN = Pattern.compile("^(Software|Hardware)$");
    private static final Pattern USER_ROLE_PATTERN = Pattern.compile("^(Admin|RegularUser)$");

    /**
     * Validate task ID format (T001, T002, etc.)
     */
    public static boolean isValidTaskId(String taskId) {
        return taskId != null && TASK_ID_PATTERN.matcher(taskId).matches();
    }
    
    /**
     * Validate project ID format (P001, P002, etc.)
     */
    public static boolean isValidProjectId(String projectId) {
        return projectId != null && PROJECT_ID_PATTERN.matcher(projectId).matches();
    }
    
    /**
     * Validate user ID format (U001, U002, etc.)
     */
    public static boolean isValidUserId(String userId) {
        return userId != null && USER_ID_PATTERN.matcher(userId).matches();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate name format (letters and spaces only, 2-50 characters)
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
    
    /**
     * Validate task status
     */
    public static boolean isValidTaskStatus(String status) {
        return status != null && TASK_STATUS_PATTERN.matcher(status).matches();
    }
    
    /**
     * Validate project type
     */
    public static boolean isValidProjectType(String type) {
        return type != null && PROJECT_TYPE_PATTERN.matcher(type).matches();
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isValidPositiveInteger(String input) {
        return input != null && input.matches("^[1-9]\\d*$");
    }
    
    /**
     * Validate decimal number format
     */
    public static boolean isValidDecimal(String input) {
        return input != null && input.matches("^\\d+(\\.\\d+)?$");
    }
    
    /**
     * Validate user role
     */
    public static boolean isValidUserRole(String role) {
        return role != null && USER_ROLE_PATTERN.matcher(role).matches();
    }
    
    /**
     * Get all validation errors for a given input
     */
    public static String getValidationErrors(String taskId, String projectId, String email, String name) {
        StringBuilder errors = new StringBuilder();
        
        if (!isValidTaskId(taskId)) {
            errors.append("Invalid task ID format. Expected T001, T002, etc.\n");
        }
        
        if (!isValidProjectId(projectId)) {
            errors.append("Invalid project ID format. Expected P001, P002, etc.\n");
        }
        
        if (!isValidEmail(email)) {
            errors.append("Invalid email format.\n");
        }
        
        if (!isValidName(name)) {
            errors.append("Invalid name format. Only letters and spaces allowed, 2-50 characters.\n");
        }
        
        return errors.toString();
    }
}
