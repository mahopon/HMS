package utility;

import java.util.Scanner;

/**
 * Utility class for handling user input by waiting for a keystroke.
 * <p>
 * This class provides functionality to pause program execution and 
 * wait for the user to press the Enter key before continuing. It is
 * useful for creating pauses in command-line applications, allowing
 * users to acknowledge a message or prompt before proceeding.
 * </p>
 */
public class KeystrokeWait {

    /**
     * Pauses the program and waits for the user to press the Enter key.
     * <p>
     * This method uses the shared {@link Scanner} instance provided by the
     * {@link InputHandler} class to capture user input.
     * </p>
     * Example usage:
     * <pre>
     *     KeystrokeWait.waitForKeyPress();
     * </pre>
     * The method displays a message prompting the user to press Enter, then
     * waits until the Enter key is pressed before returning control.
     */
    public static void waitForKeyPress() {
        Scanner scanner = InputHandler.getInstance();
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }
}
