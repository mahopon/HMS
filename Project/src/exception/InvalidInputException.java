package exception;
/**
 * Custom exception class that is thrown when an invalid input is encountered.
 * This exception can be used to handle cases where user input or data is invalid.
 */
public class InvalidInputException extends Exception {
    /**
     * Constructs a new InvalidInputException with the specified detail message.
     * The message provides information about the invalid input or condition.
     * 
     * @param message The detail message explaining the cause of the exception.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}