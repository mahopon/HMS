package repository.user;

import entity.user.HospitalStaff;
import entity.user.StaffFactory;
import java.io.IOException;
import java.util.List;
import repository.Repository;
import utility.CSV_handler;

/**
 * StaffRepository is a singleton class that manages the collection of HospitalStaff entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over the list of
 * hospital staff members stored in a CSV file.
 */
public class StaffRepository extends Repository<HospitalStaff> {
    private static StaffRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\Staff_List.csv";

    /**
     * Private constructor for the StaffRepository. It calls the parent constructor
     * and loads the list of hospital staff from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private StaffRepository() throws IOException {
        super();
        load();
    }

    /**
     * Provides access to the singleton instance of the StaffRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of StaffRepository
     */
    public static StaffRepository getInstance() {
        if (repo == null) {
            try {
                repo = new StaffRepository();
            } catch (IOException e) {
                System.err.println("Failed to create StaffRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize StaffRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying HospitalStaff entities.
     * This method is not supported for the StaffRepository. Instead, use getPrefix(HospitalStaff.Role).
     * 
     * @throws UnsupportedOperationException if called
     */
    @Override
    public String getPrefix() {
        throw new UnsupportedOperationException("Use getPrefix(String role) instead for StaffRepository.");
    }

    /**
     * Returns the prefix used for identifying hospital staff based on their role.
     *
     * @param role the role of the hospital staff (e.g., DOCTOR, PHARMACIST, ADMINISTRATOR)
     * @return the prefix corresponding to the staff role (e.g., "D" for DOCTOR, "PH" for PHARMACIST)
     */
    public String getPrefix(HospitalStaff.Role role) {
        switch (role) {
            case DOCTOR:
                return "D";
            case PHARMACIST:
                return "PH";
            case ADMINISTRATOR:
                return "A";
            default:
                throw new IllegalArgumentException("Invalid staff role: " + role);
        }
    }

    /**
     * Creates a new HospitalStaff entity based on the staff ID prefix.
     * This method uses the StaffFactory to create a HospitalStaff object.
     *
     * @param id the ID of the hospital staff
     * @return a HospitalStaff object corresponding to the ID
     */
    public HospitalStaff createStaffByPrefix(String id) {
        return StaffFactory.createStaffByPrefix(id);
    }

    /**
     * Returns the next unique ID for a given hospital staff role.
     * This method generates the next available ID based on the highest existing ID for the given role.
     *
     * @param role the role of the hospital staff for which to generate the ID
     * @return the next available ID for the given role
     */
    public String getNextId(HospitalStaff.Role role) {
        String prefix = getPrefix(role);
        int maxIdNumber = 0;
    
        for (HospitalStaff staff : getInstance()) {
            String currentId = staff.getId();
            if (currentId.startsWith(prefix)) {
                try {
                    int idNumber = Integer.parseInt(currentId.substring(prefix.length()));
                    if (idNumber > maxIdNumber) {
                        maxIdNumber = idNumber;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format: " + currentId);
                }
            }
        }
    
        int nextIdNumber = maxIdNumber + 1;
        return prefix + String.format("%03d", nextIdNumber);
    }

    /**
     * Loads the hospital staff data from a CSV file and populates the repository.
     * The data is read using the CSV_handler utility.
     *
     * @throws IOException if there is an error reading the CSV file
     */
    @Override
    public void load() throws IOException {
        List<HospitalStaff> objs = ((CSV_handler)super.getCSV_handler()).readHospitalStaffFromCSV(getFilePath());
        setObjects(objs);
    }

    /**
     * Returns the file path where the hospital staff data is stored.
     *
     * @return the file path "Project\\data\\Staff_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is HospitalStaff.
     *
     * @return the class type HospitalStaff.class
     */
    @Override
    public Class<HospitalStaff> getEntityClass() {
        return HospitalStaff.class;
    }
}
