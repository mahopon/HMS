package exception.user;
/**
 * Custom exception class that is thrown when no user is currently logged in.
 * This exception is used to indicate that an operation requires an active user
 * session, but no user is logged in at the time of the request.
 */
public class NoUserLoggedInException extends Exception {
    /**
     * Constructs a new NoUserLoggedInException with a default detail message.
     * The message specifies that no user is currently logged in.
     */
    public NoUserLoggedInException() {
        super("No user is currently logged in.");
    }
}