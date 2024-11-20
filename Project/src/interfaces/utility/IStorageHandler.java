package interfaces.utility;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface for handling file storage operations, including reading and writing data.
 */
public interface IStorageHandler {
    
    /**
     * Reads a list of objects from a file.
     *
     * @param <T> The type of objects to be read from the file.
     * @param filePath The path to the file to be read.
     * @param clazz The class type of the objects to be read.
     * @return A list of objects of the specified type.
     * @throws IOException If an I/O error occurs during file reading.
     */
    public <T> List<T> readFromFile(String filePath, Class<T> clazz) throws IOException;

    /**
     * Writes a map of objects to a file.
     *
     * @param <T> The type of objects to be written to the file.
     * @param filePath The path to the file to be written to.
     * @param data A map containing the objects to be written, keyed by their identifiers.
     * @param clazz The class type of the objects to be written.
     * @throws IOException If an I/O error occurs during file writing.
     */
    public <T> void writeToFile(String filePath, Map<String, T> data, Class<T> clazz) throws IOException;
}
