package entity;
/**
 * The abstract base class for all entity objects in the system.
 * Provides a structure for defining entity objects with a unique identifier.
 */
public abstract class EntityObject {

    
    /** 
     * @param isValid(
     * @return String
     */
    /**
     * Abstract method to retrieve the unique identifier for the entity object.
     * Concrete subclasses must implement this method to return their specific ID.
     * 
     * @return The unique identifier for the entity object.
     */
    public abstract String getId();

    /**
     * Checks if the entity object is valid by verifying if its ID is not null.
     * A valid entity is considered to have a non-null ID.
     * 
     * @return true if the entity's ID is not null, otherwise false.
     */
    public boolean isValid() {
        return getId() != null;
    }    
}
