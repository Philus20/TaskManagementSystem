package utils.exceptions;

/**
 * Thrown when an operation expects at least one project but none are present.
 */
public class EmptyProjectException extends RuntimeException {
    public EmptyProjectException() {
        super("No projects found for the requested operation.");
    }

    public EmptyProjectException(String message) {
        super(message);
    }

    public EmptyProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
