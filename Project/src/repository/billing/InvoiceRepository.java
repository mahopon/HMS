package repository.billing;

import entity.billing.Invoice;
import java.io.IOException;
import java.util.Iterator;
import repository.Repository;

/**
 * InvoiceRepository is a singleton class responsible for managing and interacting with
 * a collection of Invoice entities. It extends the Repository class and provides
 * functionality to load, retrieve, and iterate over invoices stored in a CSV file.
 */
public class InvoiceRepository extends Repository<Invoice> {

    private static InvoiceRepository repo = null;
    private static final String FILE_PATH = "Project\\data\\Invoice_List.csv";
    private static final String PREFIX = "INV";

    /**
     * The main method serves as a demonstration of how to use the InvoiceRepository.
     * It prints the total number of invoices and iterates through all invoices in the repository.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            InvoiceRepository repo = InvoiceRepository.getInstance();
            Iterator<Invoice> iterator = repo.iterator();

            System.out.println("Total Invoices: " + repo.getSize());
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Private constructor for the InvoiceRepository. It calls the parent constructor
     * and loads the invoices from the CSV file.
     *
     * @throws IOException if there is an error loading the invoices from the file
     */
    private InvoiceRepository() throws IOException {
        super();
        load();
    }

    /**
     * Provides access to the singleton instance of the InvoiceRepository.
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of InvoiceRepository
     */
    public static InvoiceRepository getInstance() {
        if (repo == null) {
            try {
                repo = new InvoiceRepository();
            } catch (IOException e) {
                System.err.println("Failed to create InvoiceRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize InvoiceRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for identifying Invoice entities.
     *
     * @return the prefix "INV"
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns the file path where the invoice data is stored.
     *
     * @return the file path "Project\\data\\Invoice_List.csv"
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the entity managed by this repository, which is Invoice.
     *
     * @return the class type Invoice.class
     */
    @Override
    public Class<Invoice> getEntityClass() {
        return Invoice.class;
    }
}
