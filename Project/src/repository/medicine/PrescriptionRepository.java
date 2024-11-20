package repository.medicine;

import entity.medicine.Prescription;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * PrescriptionRepository is a singleton class that manages the collection of Prescription entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over the list of
 * prescriptions stored in a CSV file.
 */
public class PrescriptionRepository extends Repository<Prescription> {
        
    private static PrescriptionRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\Prescription_List.csv";
    private static final String PREFIX = "PRSC";

    /**
     * The main method demonstrates how to use the PrescriptionRepository. It prints the total number
     * of prescriptions and iterates through all prescriptions in the repository.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            PrescriptionRepository repo = PrescriptionRepository.getInstance();
            Iterator<Prescription> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private constructor for the PrescriptionRepository. It calls the parent constructor
     * and loads the list of prescriptions from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private PrescriptionRepository() throws IOException {
        super();
        load();
    }
    
    /**
     * Provides access to the singleton instance of the PrescriptionRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of PrescriptionRepository
     */
    public static PrescriptionRepository getInstance() {
        if (repo == null) {
            try {
                repo = new PrescriptionRepository();
            } catch (IOException e) {
                System.err.println("Failed to create PrescriptionRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize PrescriptionRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying Prescription entities.
     *
     * @return the prefix "PRSC"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }
    
    /**
     * Returns the file path where the prescription data is stored.
     *
     * @return the file path "Project\\data\\Prescription_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is Prescription.
     *
     * @return the class type Prescription.class
     */
    @Override
    public Class<Prescription> getEntityClass() {
        return Prescription.class;
    }
}
