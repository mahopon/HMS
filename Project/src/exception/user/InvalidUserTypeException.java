package exception.user;
/**
 * Custom exception class that is thrown when an invalid user type is encountered.
 * This exception is used to indicate that the user type provided does not match
 * the expected type.
 */
public class InvalidUserTypeException extends Exception {
    /**
     * Constructs a new InvalidUserTypeException with a default detail message.
     * The message specifies that the user type provided is invalid.
     */
    public InvalidUserTypeException() {
        super("Invalid user type.");
    }
}