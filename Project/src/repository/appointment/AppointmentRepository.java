package repository.appointment;

import entity.appointment.Appointment;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * A singleton repository class for managing Appointment entities.
 * Handles loading, saving, and managing appointments stored in a CSV file.
 */
public class AppointmentRepository extends Repository<Appointment> {

    /** The singleton instance of the AppointmentRepository. */
    private static AppointmentRepository repo = null;

    /** The file path where the appointment data is stored. */
    private static final String FILE_PATH = "Project\\data\\Appointment_List.csv";

    /** The prefix for generating unique IDs for appointments. */
    private static final String PREFIX = "APPT";

    /**
     * The main method for testing the repository functionality.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            AppointmentRepository repo = AppointmentRepository.getInstance();
            Iterator<Appointment> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private constructor for the AppointmentRepository to enforce the singleton pattern.
     * Loads the appointments from the file during initialization.
     * 
     * @throws IOException If there is an error reading the appointment data from the file.
     */
    private AppointmentRepository() throws IOException {
        super();
        load();
    }

    /**
     * Retrieves the singleton instance of the AppointmentRepository.
     * If the instance does not exist, it initializes one.
     * 
     * @return The singleton instance of the AppointmentRepository.
     */
    public static AppointmentRepository getInstance() {
        if (repo == null) {
            try {
                repo = new AppointmentRepository();
            } catch (IOException e) {
                System.err.println("Failed to create AppointmentRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize AppointmentRepository", e);
            }
        }
        return repo;
    }

    /**
     * Retrieves the prefix used for generating unique IDs for appointments.
     * 
     * @return The prefix string "APPT".
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Retrieves the file path where appointment data is stored.
     * 
     * @return The file path string.
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Retrieves the class type of the entity managed by this repository.
     * 
     * @return The Appointment class type.
     */
    @Override
    public Class<Appointment> getEntityClass() {
        return Appointment.class;
    }
}
