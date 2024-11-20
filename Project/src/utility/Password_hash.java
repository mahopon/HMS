package utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utility class for hashing passwords securely using the SHA-256 algorithm.
 */
public class Password_hash {

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * @param password The plain text password to be hashed.
     * @return The hashed password as a hexadecimal string.
     * @throws RuntimeException If the SHA-256 algorithm is not available.
     */
    public static String hashPassword(String password) {
        try {
            // Initialize a SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing operation
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes into a hexadecimal string
            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // Handle cases where SHA-256 is not supported
            throw new RuntimeException("SHA-256 algorithm not found!", e);
        }
    }
}
