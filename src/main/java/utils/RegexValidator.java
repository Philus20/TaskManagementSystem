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
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,50}$");
    private static final Pattern TASK_STATUS_PATTERN = Pattern.compile("^(Pending|In Progress|Completed)$");
    private static final Pattern PROJECT_TYPE_PATTERN = Pattern.compile("^(Software|Hardware)$");
    private static final Pattern USER_ROLE_PATTERN = Pattern.compile("^(Admin|RegularUser)$");
    private static final Pattern JSON_KEY_PATTERN = Pattern.compile("^\"[^\"]+\"\\s*:");

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
     * Validate phone number format (international format)
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
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
     * Validate user role
     */
    public static boolean isValidUserRole(String role) {
        return role != null && USER_ROLE_PATTERN.matcher(role).matches();
    }
    
    /**
     * Validate JSON key format
     */
    public static boolean isValidJsonKey(String jsonKey) {
        return jsonKey != null && JSON_KEY_PATTERN.matcher(jsonKey).matches();
    }
    
    /**
     * Extract string value from JSON
     */
    public static String extractJsonStringValue(String json, String key) {
        if (json == null || key == null) return null;
        
        String patternStr = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    /**
     * Extract numeric value from JSON
     */
    public static String extractJsonNumberValue(String json, String key) {
        if (json == null || key == null) return null;
        
        String patternStr = "\"" + key + "\"\\s*:\\s*(\\d+(\\.\\d+)?)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(json);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    

    
    /**
     * Validate if string contains only alphanumeric characters
     */
    public static boolean isAlphanumeric(String input) {
        return input != null && input.matches("^[A-Za-z0-9]+$");
    }
    
    /**
     * Validate if string contains only letters
     */
    public static boolean isAlphaOnly(String input) {
        return input != null && input.matches("^[A-Za-z]+$");
    }
    
    /**
     * Validate if string contains only numbers
     */
    public static boolean isNumericOnly(String input) {
        return input != null && input.matches("^\\d+$");
    }
    
    /**
     * Validate password strength (at least 8 chars, one uppercase, one lowercase, one digit)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
    
    /**
     * Validate URL format
     */
    public static boolean isValidUrl(String url) {
        return url != null && url.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");
    }
    
    /**
     * Validate date format (YYYY-MM-DD)
     */
    public static boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
    
    /**
     * Validate time format (HH:MM)
     */
    public static boolean isValidTime(String time) {
        return time != null && time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
    
    /**
     * Validate decimal number format
     */
    public static boolean isValidDecimal(String input) {
        return input != null && input.matches("^\\d+(\\.\\d+)?$");
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isValidPositiveInteger(String input) {
        return input != null && input.matches("^[1-9]\\d*$");
    }
    
    /**
     * Validate non-negative integer
     */
    public static boolean isValidNonNegativeInteger(String input) {
        return input != null && input.matches("^\\d+$");
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
    
    /**
     * Custom pattern validation
     */
    public static boolean matchesPattern(String input, String regex) {
        if (input == null || regex == null) return false;
        return Pattern.matches(regex, input);
    }
    
    /**
     * Extract all matches for a pattern
     */
    public static String[] extractAllMatches(String input, String regex) {
        if (input == null || regex == null) return new String[0];
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        
        java.util.List<String> matches = new java.util.ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        
        return matches.toArray(new String[0]);
    }
}
