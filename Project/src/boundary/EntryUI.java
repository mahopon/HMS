package boundary;

import boundary.user.administrator.A_HomeUI;
import boundary.user.doctor.D_HomeUI;
import boundary.user.patient.P_HomeUI;
import boundary.user.pharmacist.PH_HomeUI;
import control.user.SessionManager;
import control.user.UserController;
import entity.user.Administrator;
import entity.user.Doctor;
import entity.user.Pharmacist;
import entity.user.User;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import exception.user.InvalidUserTypeException;
import exception.user.NoUserLoggedInException;
import interfaces.boundary.IUserInterface;
import java.util.Scanner;
import utility.ClearConsole;
import utility.InputHandler;
import utility.KeystrokeWait;

/**
 * Entry point for the Hospital Management System.
 * Handles user login, password change, and navigation to role-specific UIs.
 */
public class EntryUI {
    private static final Scanner scanner = InputHandler.getInstance();
    private final SessionManager session;

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     * @throws InvalidUserTypeException If an invalid user type is encountered during login.
     */
    public static void main(String[] args) throws InvalidUserTypeException {
        EntryUI entryUI = new EntryUI();
        entryUI.login();
    }

    /**
     * Constructor to initialize the EntryUI with a new session manager.
     */
    public EntryUI() {
        this.session = new SessionManager();
    }

    /**
     * Handles the login process, including user authentication and optional password change.
     *
     * @throws InvalidUserTypeException If the user type cannot be determined.
     */
    public void login() throws InvalidUserTypeException {
        System.out.println("\n===========================");
        System.out.println("██╗  ██╗███╗   ███╗███████╗");
        System.out.println("██║  ██║████╗ ████║██╔════╝");
        System.out.println("███████║██╔████╔██║███████╗");
        System.out.println("██╔══██║██║╚██╔╝██║╚════██║");
        System.out.println("██║  ██║██║ ╚═╝ ██║███████║");
        System.out.println("╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝");
        System.out.println("\nHOSPITAL MANAGEMENT SYSTEM");
        System.out.println("===========================");
        
        String inputId;
        String inputPassword;
        User user = null;

        while (user == null) {
            try {
                inputId = getUserId();
                inputPassword = getPassword();

                user = UserController.login(inputId, inputPassword);

                if (user != null) {
                    System.out.println("Login successful. Welcome, " + inputId + "!");
                    if (inputPassword.equals("password")) {
                        System.out.println("You are using the default password. You are required to change it before proceeding.");
                        changePassword(user);
                    }
                    session.setCurrentUser(user);
                } else {
                    System.out.println("Invalid User ID or Password. Please try again.\n");
                }
            } catch (EntityNotFoundException | InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }

        nextStep(session);
    }

    /**
     * Prompts the user to enter their user ID.
     *
     * @return The entered user ID.
     */
    private String getUserId() {
        String userId;
        while (true) {
            try {
                System.out.println("\nPlease login to enter");
                System.out.print("Enter your User ID: ");
                userId = scanner.nextLine().trim();

                if (userId.isEmpty()) {
                    throw new InvalidInputException("User ID cannot be empty.");
                }
                return userId;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Prompts the user to enter their password.
     *
     * @return The entered password.
     */
    private String getPassword() {
        String password;
        while (true) {
            try {
                System.out.print("Enter your Password: ");
                password = scanner.nextLine().trim();

                if (password.isEmpty()) {
                    throw new InvalidInputException("Password cannot be empty.");
                }
                return password;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the process of changing a user's password.
     *
     * @param user The user whose password is to be changed.
     */
    public void changePassword(User user) {
        String newPassword;
        String confirmPassword;

        while (true) {
            try {
                System.out.print("Enter your new password: ");
                newPassword = scanner.nextLine().trim();

                if (newPassword.isEmpty() || newPassword.length() < 8) {
                    throw new InvalidInputException("Password must be at least 8 characters long.");
                }

                System.out.print("Confirm your new password: ");
                confirmPassword = scanner.nextLine().trim();

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("Passwords do not match. Please try again.");
                } else {
                    UserController.passwordChange(user, newPassword);
                    System.out.println("Password changed successfully.");
                    break;
                }
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Redirects the user to their role-specific interface after successful login.
     *
     * @param session The current user session.
     */
    public void nextStep(SessionManager session) {
        try {
            IUserInterface nextUI;
            User user = session.getCurrentUser();

            if (user instanceof Doctor) {
                nextUI = new D_HomeUI(session);
            } else if (user instanceof Pharmacist) {
                nextUI = new PH_HomeUI(session);
            } else if (user instanceof Administrator) {
                nextUI = new A_HomeUI(session);
            } else {
                nextUI = new P_HomeUI(session);
            }

            nextUI.show_options();
        } catch (NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
