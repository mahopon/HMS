package exception.user;

/**
 * Custom exception class that is thrown when an invalid duration is encountered.
 * Specifically, this exception is used when the start time is after the end time.
 */
public class InvalidDurationException extends Exception {
    /**
     * Constructs a new InvalidDurationException with a default detail message.
     * The message specifies that the start time must be before the end time.
     */
    public InvalidDurationException() {
        super("Invalid duration: Start time must be before end time.");
    }
}
