package repository.medicine;

import entity.medicine.Medicine;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * MedicineRepository is a singleton class that manages the collection of Medicine entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over
 * the list of medicines stored in a CSV file.
 */
public class MedicineRepository extends Repository<Medicine> {
    
    private static MedicineRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\Medicine_List.csv";
    private static final String PREFIX = "MED";

    /**
     * The main method demonstrates how to use the MedicineRepository. It prints the total number
     * of medicines and iterates through all medicines in the repository.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            MedicineRepository repo = MedicineRepository.getInstance();
            Iterator<Medicine> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private constructor for the MedicineRepository. It calls the parent constructor
     * and loads the list of medicines from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private MedicineRepository() throws IOException {
        super();
        load();
    }

    /**
     * Provides access to the singleton instance of the MedicineRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of MedicineRepository
     */
    public static MedicineRepository getInstance() {
        if (repo == null) {
            try {
                repo = new MedicineRepository();
            } catch (IOException e) {
                System.err.println("Failed to create MedicineRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize MedicineRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying Medicine entities.
     *
     * @return the prefix "MED"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns the file path where the medicine data is stored.
     *
     * @return the file path "Project\\data\\Medicine_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is Medicine.
     *
     * @return the class type Medicine.class
     */
    @Override
    public Class<Medicine> getEntityClass() {
        return Medicine.class;
    }
}
