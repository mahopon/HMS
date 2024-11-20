package control.user;

import entity.user.Administrator;
import entity.user.Doctor;
import entity.user.Patient;
import entity.user.Pharmacist;
import entity.user.User;
import exception.user.InvalidUserTypeException;
import exception.user.NoUserLoggedInException;

/**
 * Manages the current session by storing and handling the logged-in user.
 */
public class SessionManager {

    private User currentUser;

    /**
     * Constructs a SessionManager with no user logged in.
     */
    public SessionManager() {
        this.currentUser = null;
    }

    /**
     * Sets the current user for the session.
     *
     * @param user The user to set as the current user.
     * @throws InvalidUserTypeException If the user type is invalid.
     * @throws IllegalArgumentException If the user is null.
     */
    public void setCurrentUser(User user) throws InvalidUserTypeException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user instanceof Doctor) {
            currentUser = (Doctor) user;
        } else if (user instanceof Pharmacist) {
            currentUser = (Pharmacist) user;
        } else if (user instanceof Administrator) {
            currentUser = (Administrator) user;
        } else if (user instanceof Patient) {
            currentUser = (Patient) user;
        } else {
            throw new InvalidUserTypeException();
        }
    }

    /**
     * Retrieves the current user of the session.
     *
     * @return The current user.
     * @throws NoUserLoggedInException If no user is currently logged in.
     */
    public User getCurrentUser() throws NoUserLoggedInException {
        if (currentUser == null) {
            throw new NoUserLoggedInException();
        }
        return currentUser;
    }

    /**
     * Clears the current user from the session, logging out the user.
     */
    public void clearCurrentUser() {
        currentUser = null;
    }
}
