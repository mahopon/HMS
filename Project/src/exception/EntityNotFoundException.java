package exception;
/**
 * Custom exception class that is thrown when an entity is not found.
 * This exception is typically used when a requested entity cannot be found
 * in a data store (e.g., database, in-memory collection) based on its ID.
 */
public class EntityNotFoundException extends Exception {
    /**
     * Constructs a new EntityNotFoundException with a detailed message 
     * specifying the type of entity and its ID.
     * 
     * @param entityType The type of the entity (e.g., "User", "Prescription").
     * @param id The ID of the entity that was not found.
     */
    public EntityNotFoundException(String entityType, String id) {
        super(entityType + " not found with ID: " + id);
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     * 
     * @param message The detail message providing additional information about the exception.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}