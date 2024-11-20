package repository.request;

import entity.request.MedicineRequest;
import entity.request.Request;

import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * MedicineRequestRepository is a singleton class that manages the collection of MedicineRequest entities.
 * It extends the Repository class and provides methods to load, retrieve, and iterate over the list of
 * medicine requests stored in a CSV file.
 */
public class MedicineRequestRepository extends Repository<MedicineRequest> {
    
    private static MedicineRequestRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\MedicineRequest_List.csv";
    private static final String PREFIX = "MEDREQ";

    /**
     * The main method demonstrates how to use the MedicineRequestRepository. It prints the total number
     * of medicine requests and iterates through all medicine requests in the repository. It also shows
     * how to find medicine requests by their status.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            MedicineRequestRepository repo = MedicineRequestRepository.getInstance();
            Iterator<MedicineRequest> iterator = repo.iterator();
            
            System.out.println(repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        // Example of finding requests with status 'PENDING'
        System.out.println(repo.findByField("status", Request.STATUS.PENDING));
    }

    /**
     * Private constructor for the MedicineRequestRepository. It calls the parent constructor
     * and loads the list of medicine requests from the CSV file.
     *
     * @throws IOException if there is an error loading the data from the CSV file
     */
    private MedicineRequestRepository() throws IOException {
        super();
        load();
    }
    
    /**
     * Provides access to the singleton instance of the MedicineRequestRepository. If the instance does
     * not exist, it creates a new one.
     *
     * @return the singleton instance of MedicineRequestRepository
     */
    public static MedicineRequestRepository getInstance() {
        if (repo == null) {
            try {
                repo = new MedicineRequestRepository();
            } catch (IOException e) {
                System.err.println("Failed to create MedicineRequestRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize MedicineRequestRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying MedicineRequest entities.
     *
     * @return the prefix "MEDREQ"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns the file path where the medicine request data is stored.
     *
     * @return the file path "Project\\data\\MedicineRequest_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is MedicineRequest.
     *
     * @return the class type MedicineRequest.class
     */
    @Override
    public Class<MedicineRequest> getEntityClass() {
        return MedicineRequest.class;
    }
}
