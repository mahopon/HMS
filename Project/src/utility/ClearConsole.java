package utility;

/**
 * Utility class to provide a method for clearing the console screen.
 * This can be imported to give the functionality of clearing the console,
 * which may be useful in command-line applications.
 */
public class ClearConsole {
    /**
     * Clears the console screen by sending escape sequences.
     * This works in most Unix-like terminal emulators (including Linux and macOS).
     * It uses the ANSI escape codes to reset the cursor to the top of the screen
     * and clear the content of the console.
     * 
     * Note: This method may not work in some IDEs or Windows command prompt
     * without additional configuration.
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}