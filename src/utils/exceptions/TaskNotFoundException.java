package utils.exceptions;

/**
 * Thrown when a task lookup fails for a given identifier.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task not found.");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
