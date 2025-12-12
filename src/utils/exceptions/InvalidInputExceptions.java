package utils.exceptions;

/**
 * Thrown when provided input does not meet validation rules.
 */
public class InvalidInputExceptions extends RuntimeException {
    public InvalidInputExceptions() {
        super("Invalid input provided.");
    }

    public InvalidInputExceptions(String message) {
        super(message);
    }

    public InvalidInputExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
