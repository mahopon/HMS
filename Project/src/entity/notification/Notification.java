package entity.notification;

import entity.EntityObject;
import java.time.LocalDateTime;

/**
 * Represents a notification for a user.
 * This class stores information about a specific notification, including the message,
 * the user it's associated with, the date and time of creation, and the read status.
 */
public class Notification extends EntityObject {
    
    private String id;
    private String userId;
    private String message;
    private LocalDateTime datetime;
    private Boolean read;

    /**
     * Default constructor for the Notification class. Initializes an empty notification object.
     */
    public Notification() {

    }

    /**
     * Constructor to initialize a Notification object with the specified attributes.
     * The date and time of creation is set to the current date and time, and the read status
     * is initialized to false.
     * 
     * @param id The unique identifier of the notification.
     * @param userId The ID of the user the notification is associated with.
     * @param message The message of the notification.
     */
    public Notification(String id, String userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.datetime = LocalDateTime.now(); // Sets the current date and time
        this.read = false; // Notification is initially unread
    }

    /**
     * Retrieves the unique ID of the notification.
     * 
     * @return The unique identifier for this notification.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID for the notification.
     * 
     * @param id The new unique identifier for the notification.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the user ID associated with the notification.
     * 
     * @return The ID of the user the notification is intended for.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for the notification.
     * 
     * @param userId The ID of the user the notification is intended for.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the message of the notification.
     * 
     * @return The message contained in the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message for the notification.
     * 
     * @param message The message to set for the notification.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the date and time when the notification was created.
     * 
     * @return The date and time of the notification's creation.
     */
    public LocalDateTime getDatetime() {
        return datetime;
    }

    /**
     * Sets the date and time when the notification was created.
     * 
     * @param datetime The new date and time for the notification.
     */
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    /**
     * Retrieves the read status of the notification.
     * 
     * @return true if the notification has been read, false if it is unread.
     */
    public Boolean getReadStatus() {
        return read;
    }

    /**
     * Sets the read status of the notification.
     * 
     * @param read The read status to set for the notification (true for read, false for unread).
     */
    public void setReadStatus(Boolean read) {
        this.read = read;
    }
}
