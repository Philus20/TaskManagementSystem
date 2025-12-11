package utils;

import services.GenerateProjectId;
import services.ProjectService;
import services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Combines interactive input reading and validation rules.
 * - Instance-based so it can be injected, mocked, and tested.
 * - Optionally accepts ProjectService and UserService to validate IDs.
 */
public final class ValidationUtils {
    private final Scanner scanner;
    private final ProjectService projectService; // optional
    private final UserService userService;       // optional
    private final GenerateProjectId idGenerator;


    // Validation constants
    private static final Set<String> VALID_TASK_STATUSES = Set.of("Pending", "In Progress", "Completed");
    private static final Set<String> VALID_PROJECT_TYPES = Set.of("Software", "Hardware");
    private static final Set<String> VALID_USER_ROLES = Set.of("Admin", "Regular");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public ValidationUtils(Scanner scanner, GenerateProjectId idGenerator) {
        this(scanner, null, null, idGenerator);
    }

    public ValidationUtils(Scanner scanner, ProjectService projectService, UserService userService, GenerateProjectId idGenerator) {
        this.idGenerator = idGenerator;
        if (scanner == null) throw new IllegalArgumentException("Scanner cannot be null");
        this.scanner = scanner;
        this.projectService = projectService;
        this.userService = userService;
    }

    /* ---------------------------
       Interactive read methods
       --------------------------- */

    public int readIntInRange(String prompt, int min, int max) {
        int val;
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }
            val = scanner.nextInt();
            scanner.nextLine();
            if (val < min || val > max) {
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
                continue;
            }
            return val;
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.nextLine();
                continue;
            }
            int val = scanner.nextInt();
            scanner.nextLine();
            if (!validateTeamSize(val)) {
                System.out.println("Team size must be a positive integer. Please try again.");
                continue;
            }
            return val;
        }
    }

    public double readNonNegativeDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a number (0 or greater).");
                scanner.nextLine();
                continue;
            }
            double val = scanner.nextDouble();
            scanner.nextLine();
            if (!validateBudget(val)) {
                System.out.println("Budget must be non-negative. Please try again.");
                continue;
            }
            return val;
        }
    }

    public String readNonEmptyText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!hasText(input)) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            return input.trim();
        }
    }

    public String readValidTaskStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String status = scanner.nextLine().trim();
            if (!hasText(status)) {
                System.out.println("Status cannot be empty. Try: Pending, In Progress, Completed.");
                continue;
            }
            if (!validateTaskStatus(status)) {
                System.out.println("Invalid status. Supported: Pending, In Progress, Completed. Try again.");
                continue;
            }
            return status;
        }
    }

    /**
     * Read a project id that exists in projectService (if provided).
     * Returns "0" if user typed 0 to cancel.
     */
    public String readExistingProjectId(String prompt) {
        if (projectService == null) {
            // fallback: just read non-empty text
            return readNonEmptyText(prompt);
        }

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) return "0";
            if (!hasText(input)) {
                System.out.println("Project ID cannot be empty. Please try again.");
                continue;
            }
            if (projectService.getProjectById(idGenerator.elementIndex(input)) == null) {
                System.out.printf("No project found with ID %s. Enter a valid project ID or 0 to return.%n", input);
                continue;
            }
            return input;
        }
    }

    /**
     * Read a user id that exists in userService (if provided).
     * Returns "0" to cancel.
     */
    public String readExistingUserId(String prompt) {
        if (userService == null) {
            return readNonEmptyText(prompt);
        }

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) return "0";
            if (!hasText(input)) {
                System.out.println("User ID cannot be empty. Please try again.");
                continue;
            }
            if (userService.getUserById(input) == null) {
                System.out.printf("No user found with ID %s. Enter a valid user ID or 0 to skip.%n", input);
                continue;
            }
            return input;
        }
    }

    public void close() {
        scanner.close();
    }

    /* ---------------------------
       Validation helpers (previously in ValidationUtils)
       --------------------------- */

    public static boolean validateTaskStatus(String status) {
        return status != null && VALID_TASK_STATUSES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(status.trim()));
    }

    public static boolean validateProjectType(String type) {
        return type != null && VALID_PROJECT_TYPES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(type.trim()));
    }

    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean validateTeamSize(int teamSize) {
        return teamSize > 0;
    }

    public static boolean validateBudget(double budget) {
        return budget >= 0;
    }

    public static boolean validateBudgetRange(double minBudget, double maxBudget) {
        return minBudget >= 0 && maxBudget >= 0 && minBudget <= maxBudget;
    }

    public static boolean validateUserRole(String role) {
        return role != null && VALID_USER_ROLES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(role.trim()));
    }

    public static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean validateDateFormat(String dateString, String pattern) {
        if (dateString == null || pattern == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateString.trim(), formatter);
            return true;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }
}