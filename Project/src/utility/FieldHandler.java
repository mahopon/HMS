package utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling and retrieving field information from objects using reflection.
 */
public class FieldHandler {

    /**
     * Retrieves the names of all fields (including those inherited) of the given object's class hierarchy.
     *
     * @param obj The object whose field names are to be retrieved.
     * @return A list of field names from the class hierarchy of the given object.
     */
    public static List<String> getFieldNames(Object obj) {
        List<String> fieldNames = new ArrayList<>();
        List<Class<?>> classHierarchy = new ArrayList<>();

        // Traverse the class hierarchy, adding each class to the list
        Class<?> currentClass = obj.getClass();
        while (currentClass != null) {
            classHierarchy.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }

        // Iterate through the class hierarchy in reverse (top-down inheritance)
        for (int i = classHierarchy.size() - 1; i >= 0; i--) {
            Class<?> clazz = classHierarchy.get(i);
            // Retrieve and add field names of the current class
            for (Field field : clazz.getDeclaredFields()) {
                fieldNames.add(field.getName());
            }
        }

        return fieldNames;
    }
}
