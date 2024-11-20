package utility;

import entity.user.HospitalStaff;
import entity.user.StaffFactory;
import interfaces.utility.IStorageHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Utility class for handling CSV files
 */
public class CSV_handler implements IStorageHandler {

    /**
     * Reads a list of HospitalStaff entities from a CSV file.
     *
     * @param filePath The path to the CSV file.
     * @return A list of HospitalStaff entities.
     * @throws IOException If an I/O error occurs or the CSV file is empty.
     */
    public List<HospitalStaff> readHospitalStaffFromCSV(String filePath) throws IOException {
        List<HospitalStaff> staffList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty");
            }
            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                HospitalStaff staff = createHospitalStaffFromCSV(values, headers);
                staffList.add(staff);
            }
        }

        return staffList;
    }

    /**
     * Creates a HospitalStaff entity from CSV data.
     *
     * @param values The CSV data values.
     * @param headers The headers from the CSV file.
     * @return A HospitalStaff entity.
     */
    @SuppressWarnings(value = { "unchecked" })
    private HospitalStaff createHospitalStaffFromCSV(String[] values, String[] headers) {
        try {
            String id = values[1].trim();
            HospitalStaff staff = StaffFactory.createStaffByPrefix(id);

            for (int i = 0; i < headers.length; i++) {
                String fieldName = headers[i].trim();
                Field field = getFieldFromClassHierarchy(staff.getClass(), fieldName);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                String value = values[i].trim();

                if (fieldType.isEnum()) {
                    Object enumValue = Enum.valueOf((Class<? extends Enum>) fieldType, value.toUpperCase());
                    field.set(staff, enumValue);
                } else {
                    Object convertedValue = convertValue(fieldType, value);
                    field.set(staff, convertedValue);
                }
            }

            return staff;
        } catch (Exception e) {
            throw new RuntimeException("Error creating HospitalStaff entity from CSV", e);
        }
    }

    /**
     * Retrieves a Field object from the class hierarchy of a given class.
     *
     * @param clazz The class to search.
     * @param fieldName The name of the field to retrieve.
     * @return The Field object.
     * @throws NoSuchFieldException If the field is not found in the class hierarchy.
     */
    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    /**
     * Converts a string value to the appropriate data type based on the field type.
     *
     * @param fieldType The type of the field.
     * @param value The string value to be converted.
     * @return The converted value.
     * @throws IllegalArgumentException If the value cannot be converted to the specified type.
     */
    private Object convertValue(Class<?> fieldType, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            if (fieldType == String.class) {
                return value;
            } else if (fieldType == int.class || fieldType == Integer.class) {
                return Integer.parseInt(value);
            } else if (fieldType == double.class || fieldType == Double.class) {
                return Double.parseDouble(value);
            } else if (fieldType == float.class || fieldType == Float.class) {
                return Float.parseFloat(value);
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }else if (fieldType == LocalDateTime.class) {
                return LocalDateTime.parse(value);
            } else if (fieldType == LocalDate.class) {
                return LocalDate.parse(value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid format for value: " + value + " of type " + fieldType.getName(), e);
        }

        throw new IllegalArgumentException("Unsupported field type: " + fieldType);
    }

    /**
     * Reads a list of entities of the specified class from a CSV file.
     *
     * @param filePath The path to the CSV file.
     * @param clazz The class of the entities to be read.
     * @param <T> The type of the entities.
     * @return A list of entities of the specified class.
     * @throws IOException If an I/O error occurs or the CSV file is empty.
     */
    @SuppressWarnings(value = {"unchecked"})
    @Override
    public <T> List<T> readFromFile(String filePath, Class<T> clazz) throws IOException {
        List<T> entities = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty");
            }
            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                T entity = createEntityFromCSV(values, headers, clazz);
                entities.add(entity);
            }
        }

        return entities;
    }

    /**
     * Creates an entity of the specified class from CSV data using reflection.
     *
     * @param values The CSV data values.
     * @param headers The headers from the CSV file.
     * @param clazz The class of the entity to be created.
     * @param <T> The type of the entity.
     * @return An entity of the specified class.
     */
    @SuppressWarnings(value="unchecked")
    private <T> T createEntityFromCSV(String[] values, String[] headers, Class<T> clazz) {
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();

            for (int i = 0; i < headers.length; i++) {
                String fieldName = headers[i].trim();
                Field field;

                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    field = clazz.getSuperclass().getDeclaredField(fieldName);
                }

                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                String value = values[i].trim();

                if (fieldType.isEnum()) {
                    Object enumValue = Enum.valueOf((Class<? extends Enum>) fieldType, value.toUpperCase());
                    field.set(entity, enumValue);
                } else {
                    Object convertedValue = convertValue(fieldType, value);
                    field.set(entity, convertedValue);
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error creating entity from CSV", e);
        }
    }

    /**
     * Writes a map of entities to a CSV file.
     *
     * @param filePath The path to the CSV file.
     * @param objMap A map of entities to be written to the CSV file.
     * @param clazz The class of the entities.
     * @param <T> The type of the entities.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public <T> void writeToFile(String filePath, Map<String, T> objMap, Class<T> clazz) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            Field[] superFields = clazz.getSuperclass().getDeclaredFields();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder header = new StringBuilder();
            for (Field superField : superFields) {
                header.append(superField.getName()).append(",");
            }
            for (Field field : fields) {
                header.append(field.getName()).append(",");
            }
            bw.write(header.substring(0, header.length() - 1));
            bw.newLine();

            for (T obj : objMap.values()) {
                StringBuilder line = new StringBuilder();
                for (Field superField : superFields) {
                    line.append(getFieldValue(obj, superField)).append(",");
                }
                for (Field field : fields) {
                    line.append(getFieldValue(obj, field)).append(",");
                }
                bw.write(line.substring(0, line.length() - 1));
                bw.newLine();
            }
        }
    }

    /**
     * Retrieves the value of a field from an object.
     *
     * @param obj The object from which to retrieve the field value.
     * @param field The field whose value is to be retrieved.
     * @param <T> The type of the object.
     * @return The value of the field as a string, or an empty string if the value is null.
     */
    private <T> String getFieldValue(T obj, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                return "";
            }
            if (value instanceof LocalDateTime) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                return ((LocalDateTime) value).format(formatter);
            }
            if (value instanceof Double || value instanceof Float) {
                DecimalFormat df = new DecimalFormat("#.00");
                return df.format(value);
            }
            return value.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing field value", e);
        }
    }

}
