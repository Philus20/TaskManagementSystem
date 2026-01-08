package utils.exceptions;

/**
 * Thrown when provided input does not meet validation rules.
 * Following naming convention: singular form for exception classes.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super("Invalid input provided.");
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
