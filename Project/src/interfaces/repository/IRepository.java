package interfaces.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface for a generic repository to manage storage and retrieval of objects.
 *
 * @param <T> The type of objects managed by the repository.
 */
public interface IRepository<T> {

    /**
     * Loads data from the underlying storage into the repository.
     *
     * @throws IOException If an I/O error occurs during loading.
     */
    public void load() throws IOException;

    /**
     * Saves data from the repository to the underlying storage.
     */
    public void save();

    /**
     * Adds a new object to the repository.
     *
     * @param obj The object to be added.
     */
    public void add(T obj);

    /**
     * Removes an object from the repository.
     *
     * @param obj The object to be removed.
     */
    public void remove(T obj);

    /**
     * Retrieves an object from the repository by its identifier.
     *
     * @param id The unique identifier of the object.
     * @return The object with the specified ID, or null if not found.
     */
    public T get(String id);

    /**
     * Converts all objects in the repository to a list.
     *
     * @return A list of all objects in the repository.
     */
    public List<T> toList();

    /**
     * Retrieves a map of all objects in the repository, keyed by their identifiers.
     *
     * @return A map containing all objects in the repository.
     */
    public Map<String, T> getObjects();

    /**
     * Generates the next unique identifier with the specified prefix.
     *
     * @param prefix The prefix for the identifier.
     * @return The next unique identifier as a string.
     */
    public String getNextId(String prefix);

    /**
     * Generates the next unique identifier for the class without a specific prefix.
     *
     * @return The next unique class-level identifier as a string.
     */
    public String getNextClassId();
}
