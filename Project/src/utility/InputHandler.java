package utility;

import java.util.Scanner;

/**
 * Utility class for providing a single instance of a Scanner.
 * This class ensures that only one instance of the Scanner is created and reused throughout the application.
 */
public class InputHandler {

    // Singleton instance of Scanner
    private static Scanner scannerInstance;

    // Private constructor to prevent instantiation
    private InputHandler() { 
        // Intentionally left empty to enforce singleton pattern
    }

    /**
     * Retrieves the singleton instance of the Scanner.
     * If the instance does not already exist, it initializes it.
     *
     * @return The singleton instance of the Scanner.
     */
    public static Scanner getInstance() {
        if (scannerInstance == null) {
            scannerInstance = new Scanner(System.in);
        }
        return scannerInstance;
    }
}
