package xyz.wolkarkadiusz.githubservice.exception;

/**
 * Thrown when extracting field values using reflections fails.
 */
public class ExtractFieldsException extends Exception {
    public ExtractFieldsException() {
        super();
    }

    public ExtractFieldsException(String message) {
        super(message);
    }
}
