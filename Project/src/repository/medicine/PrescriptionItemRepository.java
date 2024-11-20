package repository.medicine;

import entity.medicine.PrescriptionItem;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * PrescriptionItemRepository is a singleton class that manages the collection of PrescriptionItem entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over the list of
 * prescription items stored in a CSV file.
 */
public class PrescriptionItemRepository extends Repository<PrescriptionItem> {
        
    private static PrescriptionItemRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\PrescriptionItem_List.csv";
    private static final String PREFIX = "PRSCI";

    /**
     * The main method demonstrates how to use the PrescriptionItemRepository. It prints the total number
     * of prescription items and iterates through all prescription items in the repository.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            PrescriptionItemRepository repo = PrescriptionItemRepository.getInstance();
            Iterator<PrescriptionItem> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Private constructor for the PrescriptionItemRepository. It calls the parent constructor
     * and loads the list of prescription items from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private PrescriptionItemRepository() throws IOException {
        super();
        load();
    }
    
    /**
     * Provides access to the singleton instance of the PrescriptionItemRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of PrescriptionItemRepository
     */
    public static PrescriptionItemRepository getInstance() {
        if (repo == null) {
            try {
                repo = new PrescriptionItemRepository();
            } catch (IOException e) {
                System.err.println("Failed to create PrescriptionItemRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize PrescriptionRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying PrescriptionItem entities.
     *
     * @return the prefix "PRSCI"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }
    
    /**
     * Returns the file path where the prescription item data is stored.
     *
     * @return the file path "Project\\data\\PrescriptionItem_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is PrescriptionItem.
     *
     * @return the class type PrescriptionItem.class
     */
    @Override
    public Class<PrescriptionItem> getEntityClass() {
        return PrescriptionItem.class;
    }
}
