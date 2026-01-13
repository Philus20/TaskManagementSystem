package services;

import utils.RegexValidator;

/**
 * Validation service using regex patterns for ID validation
 * Following Week 3 requirements for regex validation
 * Refactored to use centralized RegexValidator utility
 */
public class ValidationService {
    
    /**
     * Validate task ID format (T001, T002, etc.)
     */
    public static boolean isValidTaskId(String taskId) {
        return RegexValidator.isValidTaskId(taskId);
    }

    /**
     * Validate project ID format (P001, P002, etc.)
     */
    public static boolean isValidProjectId(String projectId) {
        return RegexValidator.isValidProjectId(projectId);
    }
    
    /**
     * Validate user ID format (U001, U002, etc.)
     */
    public static boolean isValidUserId(String userId) {
        return RegexValidator.isValidUserId(userId);
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return RegexValidator.isValidEmail(email);
    }
    
    /**
     * Validate task status
     */
    public static boolean isValidTaskStatus(String status) {
        return RegexValidator.isValidTaskStatus(status);
    }
    
    /**
     * Validate project type
     */
    public static boolean isValidProjectType(String type) {
        return RegexValidator.isValidProjectType(type);
    }
    
    /**
     * Get detailed validation error message
     */
    public static String getValidationErrorMessage(String taskId, String projectId, String userId, String email) {
        return RegexValidator.getValidationErrors(taskId, projectId, email, "User");
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
