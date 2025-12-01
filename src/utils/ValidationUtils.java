package utils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Reusable validators for interactive menu input. Centralising the rules here
 * makes it easier to evolve validation behaviour without touching every menu.
 */
public final class ValidationUtils {

    private static final Set<String> VALID_TASK_STATUSES = Set.of("Pending", "In Progress", "Completed");
    private static final Set<String> VALID_PROJECT_TYPES = Set.of("Software", "Hardware");
    private static final Set<String> VALID_USER_ROLES = Set.of("Admin", "Regular");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private ValidationUtils() {
        // utility class
    }

    /**
     * Ensures a task status value matches one of the supported states ignoring case.
     */
    public static boolean validateTaskStatus(String status) {
        return status != null && VALID_TASK_STATUSES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(status.trim()));
    }

    /**
     * Validates supported project types (Software/Hardware).
     */
    public static boolean validateProjectType(String type) {
        return type != null && VALID_PROJECT_TYPES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(type.trim()));
    }

    /**
     * Basic email validation for user creation.
     */
    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Accepts team sizes that are positive integers.
     */
    public static boolean validateTeamSize(int teamSize) {
        return teamSize > 0;
    }

    /**
     * Ensures the provided budget is non-negative.
     */
    public static boolean validateBudget(double budget) {
        return budget >= 0;
    }

    /**
     * Validates that a budget range is coherent (positive and min <= max).
     */
    public static boolean validateBudgetRange(double minBudget, double maxBudget) {
        return minBudget >= 0 && maxBudget >= 0 && minBudget <= maxBudget;
    }

    /**
     * Validates supported user roles (Admin/Regular).
     */
    public static boolean validateUserRole(String role) {
        return role != null && VALID_USER_ROLES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(role.trim()));
    }

    /**
     * Quick helper to check if a string has non-empty content once trimmed.
     */
    public static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a string represents a valid date in the specified format.
     * @param dateString the date string to validate
     * @param pattern the expected date format (e.g., "yyyy-MM-dd")
     * @return true if the date string is valid and matches the format
     */
    public static boolean validateDateFormat(String dateString, String pattern) {
        if (dateString == null || pattern == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateString.trim(), formatter);
            return true;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }
}
