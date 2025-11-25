package io.github.masked4j.exception;

/**
 * Exception thrown when an error occurs during the masking process.
 */
public class MaskingException extends RuntimeException {
    /**
     * Constructs a new MaskingException with the specified detail message.
     *
     * @param message the detail message
     */
    public MaskingException(String message) {
        super(message);
    }

    /**
     * Constructs a new MaskingException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public MaskingException(String message, Throwable cause) {
        super(message, cause);
    }
}
