package repository;

import entity.EntityObject;
import interfaces.repository.IRepository;
import interfaces.utility.IStorageHandler;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utility.CSV_handler;

/**
 * Repository is an abstract class that serves as a generic repository for managing a collection of entities
 * that extend from EntityObject. It provides methods to load, save, add, remove, update, and retrieve objects.
 * The repository interacts with a CSV file to store and retrieve data.
 *
 * @param <T> the type of entity the repository manages, extending EntityObject
 */
public abstract class Repository<T extends EntityObject> implements Iterable<T>, IRepository<T> {

    private final Map<String, T> objMap;
    private final IStorageHandler csv_handler;

    /**
     * Constructor initializes the repository and creates an empty map to store entities.
     */
    public Repository() {
        objMap = new HashMap<>();
        csv_handler = new CSV_handler();
    }

    
    /** 
     * @param load(
     * @return String
     * @throws IOException
     */
    /**
     * Abstract method that returns the file path where the data is stored.
     * 
     * @return the file path as a string
     */
    public abstract String getFilePath();

    /**
     * Abstract method that returns the class type of the entity managed by this repository.
     * 
     * @return the class type of the entity
     */
    public abstract Class<T> getEntityClass();

    /**
     * Abstract method that returns the prefix used for identifying entities in the repository.
     * 
     * @return the prefix as a string
     */
    public abstract String getPrefix();

    /**
     * Loads entities from the CSV file into the repository by reading data and setting objects.
     * 
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void load() throws IOException {
        List<T> objs = csv_handler.readFromFile(getFilePath(), getEntityClass());
        setObjects(objs);
    }

    /**
     * Saves the current state of the repository back to the CSV file.
     */
    @Override
    public void save() {
        try {
            csv_handler.writeToFile(getFilePath(), objMap, getEntityClass());
        } catch (IOException e) {
            System.err.println("Error saving data to CSV file: " + getFilePath());
            e.printStackTrace();
        }
    }

    /**
     * Returns the map containing all entities with their respective IDs as keys.
     * 
     * @return a map of entity ID to entity object
     */
    @Override
    public Map<String, T> getObjects() {
        return objMap;
    }

    /**
     * Converts the map of entities into a list.
     * 
     * @return a list of all entities in the repository
     */
    @Override
    public List<T> toList() {
        List<T> objs = new ArrayList<>();
        objMap.forEach((k, v) -> {
            objs.add(v);
        });

        return objs;
    }

    /**
     * Returns the number of entities in the repository.
     * 
     * @return the size of the repository
     */
    public int getSize() {
        return objMap.size();
    }

    /**
     * Removes an entity from the repository.
     * 
     * @param obj the entity to remove
     */
    @Override
    public void remove(T obj) {
        objMap.remove(obj.getId());
    }

    /**
     * Adds a new entity to the repository.
     * 
     * @param obj the entity to add
     */
    @Override
    public void add(T obj) {
        objMap.put(obj.getId(), obj);
    }

    /**
     * Retrieves an entity by its ID.
     * 
     * @param id the ID of the entity
     * @return the entity corresponding to the given ID
     */
    @Override
    public T get(String id) {
        return objMap.get(id);
    }

    /**
     * Updates an existing entity in the repository.
     * 
     * @param obj the entity with updated information
     */
    public void update(T obj) {
        String id = obj.getId();
        if (objMap.containsKey(id)) {
            objMap.put(id, obj);
        }
    }

    /**
     * Sets the list of entities in the repository.
     * 
     * @param entities the list of entities to set in the repository
     */
    public void setObjects(List<T> entities) {
        for (T entity : entities) {
            objMap.put(entity.getId(), entity);
        }
    }

    /**
     * Finds entities in the repository by a specific field and its value.
     * 
     * @param fieldName the name of the field to search by
     * @param value the value to search for
     * @return a list of entities that match the given field and value
     */
    public List<T> findByField(String fieldName, Object value) {
        List<T> matchingObjects = new ArrayList<>();
    
        for (T obj : objMap.values()) {
            try {
                Field field = getFieldFromClassHierarchy(obj.getClass(), fieldName);
                field.setAccessible(true);  // Allows access to private fields
                Object fieldValue = field.get(obj);
    
                if (fieldValue != null && fieldValue.equals(value)) {
                    matchingObjects.add(obj);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Error accessing field '" + fieldName + "' on object: " + obj);
                e.printStackTrace();
            }
        }
    
        return matchingObjects;
    }

    /**
     * Finds the field in the class hierarchy, throwing NoSuchFieldException if the field is not found.
     * 
     * @param clazz the class to search the field in
     * @param fieldName the name of the field to find
     * @return the field object representing the field in the class hierarchy
     * @throws NoSuchFieldException if the field is not found in the class hierarchy
     */
    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass(); // Move up the hierarchy
            }
        }
        throw new NoSuchFieldException(fieldName); // Field not found in the hierarchy
    }

    /**
     * Returns a list of all IDs in the repository.
     * 
     * @return a list of all IDs
     */
    public List<String> getAllIds() {
        List<String> idList = new ArrayList<>();
    
        for (T obj : objMap.values()) {
            idList.add(obj.getId());
        }
    
        return idList;
    }

    /**
     * Returns the next unique ID based on the given prefix.
     * 
     * @param prefix the prefix used for the ID
     * @return the next available ID with the specified prefix
     */
    @Override
    public String getNextId(String prefix) {
        if (objMap.isEmpty()) {
            return prefix + "001";
        }
        int highestCount = 0;
        Pattern pattern = Pattern.compile("^" + prefix + "(\\d+)$");
    
        for (T obj : objMap.values()) {
            String id = obj.getId();
            Matcher matcher = pattern.matcher(id);
            if (matcher.matches()) {
                int count = Integer.parseInt(matcher.group(1));
                highestCount = Math.max(highestCount, count);
            }
        }
        int nextCount = highestCount + 1;
        return prefix + String.format("%03d", nextCount);
    }

    /**
     * Returns the next unique ID based on the class's prefix.
     * 
     * @return the next available class-specific ID
     */
    @Override
    public String getNextClassId() {
        return getNextId(getPrefix());
    }

    /**
     * Iterates over the entities in the repository.
     * 
     * @return an iterator over the repository's entities
     */
    @Override
    public Iterator<T> iterator() {
        return objMap.values().iterator();
    }

    /**
     * Prints all the entities in the repository.
     */
    public void printObjs() {
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T iter = iterator.next();
            System.out.println(iter);
        }
    }

    /**
     * Retrieves CSV_handler instance.
     */
    protected IStorageHandler getCSV_handler() {
        return this.csv_handler;
    }
}
