package repository.notification;

import entity.notification.Notification;
import java.io.IOException;
import repository.Repository;

/**
 * Repository class for managing notifications.
 * This class provides functionality to load, save, and manage notifications from a CSV file.
 */
public class NotificationRepository extends Repository<Notification> {
    
    // Singleton instance of the NotificationRepository
    private static NotificationRepository repo = null;
    
    // File path for storing notification data
    private static final String FILE_PATH = "Project\\data\\Notification_List.csv";
    
    // Prefix used for generating notification IDs
    private static final String PREFIX = "NOTI";

    /**
     * Private constructor to enforce the singleton pattern.
     *
     * @throws IOException If an error occurs during loading the notifications.
     */
    private NotificationRepository() throws IOException {
        super();
        load();
    }
    
    /**
     * Retrieves the singleton instance of the NotificationRepository.
     *
     * @return The singleton instance of NotificationRepository.
     * @throws RuntimeException If the repository fails to initialize.
     */
    public static NotificationRepository getInstance() {
        if (repo == null) {
            try {
                repo = new NotificationRepository();
            } catch (IOException e) {
                System.err.println("Failed to create NotificationRepository instance: " + e.getMessage());
                throw new RuntimeException("Failed to initialize NotificationRepository", e);
            }
        }
        return repo;
    }

    /**
     * Returns the prefix used for generating notification IDs.
     *
     * @return The prefix string "NOTI".
     */
    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns the file path for storing notification data.
     *
     * @return The file path string.
     */
    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Returns the class type of the Notification entity.
     *
     * @return The Notification class type.
     */
    @Override
    public Class<Notification> getEntityClass() {
        return Notification.class;
    }
}
