package interfaces.boundary;
/**
 * Interface representing the user interface for interacting with the system.
 * This interface defines the methods for displaying options to the user and handling their input.
 */
public interface IUserInterface {
    /**
     * Displays the available options to the user.
     * This method should be implemented to present a list of choices to the user.
     */
    public void show_options();

    /**
     * Handles the user's selection based on the option chosen.
     * The implementation should define what actions to take for each option.
     * 
     * @param option The option selected by the user.
     */
    public void handle_option(int option);
}
