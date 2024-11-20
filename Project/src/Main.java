import boundary.EntryUI;
import exception.user.InvalidUserTypeException;

//import User.Role;

/**
 * The entry point for the application.
 * This class handles the initialization of the login process and handles any errors related to invalid user types.
 */
public class Main {

    /**
     * Main method that starts the application.
     * Initializes the EntryUI and calls its login method to begin the login process.
     * Catches and handles the InvalidUserTypeException if the user type is invalid.
     *
     * @param args Command line arguments (not used in this case).
     */
    public static void main(String[] args) {
        try {
            EntryUI entryUI = new EntryUI();
            entryUI.login();
        } catch (InvalidUserTypeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
