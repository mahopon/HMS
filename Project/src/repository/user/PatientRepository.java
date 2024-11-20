package repository.user;

import entity.user.Patient;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * PatientRepository is a singleton class that manages the collection of Patient entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over the list of
 * patients stored in a CSV file.
 */
public class PatientRepository extends Repository<Patient> {
    private static PatientRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\Patient_List.csv";
    private static final String PREFIX = "P";

    /**
     * The main method demonstrates how to use the PatientRepository. It prints the total number
     * of patients and iterates through all patients in the repository.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            PatientRepository repo = PatientRepository.getInstance();
            Iterator<Patient> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private constructor for the PatientRepository. It calls the parent constructor
     * and loads the list of patients from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private PatientRepository() throws IOException {
        super();
        load();
    }

    /**
     * Provides access to the singleton instance of the PatientRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of PatientRepository
     */
    public static PatientRepository getInstance() {
        if (repo == null) {
            try {
                repo = new PatientRepository();
            } catch (IOException e) {
                System.err.println("Failed to create PatientRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize PatientRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying Patient entities.
     *
     * @return the prefix "P"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns the file path where the patient data is stored.
     *
     * @return the file path "Project\\data\\Patient_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is Patient.
     *
     * @return the class type Patient.class
     */
    @Override
    public Class<Patient> getEntityClass() {
        return Patient.class;
    }
}
