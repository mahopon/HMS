package entity.user;
import entity.EntityObject;
import java.time.LocalDateTime;

/**
 * Represents a specific date and time during which a staff member is unavailable.
 * This class is used to manage unavailable slots for scheduling purposes.
 * 
 * Features:
 * - Tracks the unique identifier, staff member's ID, and the unavailable date/time.
 * - Provides methods to validate durations and manipulate data.
 */
public class UnavailableDate extends EntityObject {
    private String id;
    private String staffId;
    private LocalDateTime date;

    /**
     * Default constructor for UnavailableDate.
     * Initializes the UnavailableDate object without setting values.
     */
    public UnavailableDate() {

    }

    /**
     * Constructor to initialize an UnavailableDate with specified values.
     * 
     * @param id The unique identifier for the unavailable date entry.
     * @param staffId The ID of the staff member who is unavailable.
     * @param date The date and time when the staff member is unavailable.
     */
    public UnavailableDate(String id, String staffId, LocalDateTime date) {
        this.id = id;
        this.staffId = staffId;
        this.date = date;
    }

    /**
     * Checks if the given time duration is valid. The duration is valid if the start time is before or at the same time as the end time.
     * 
     * @param start The start time of the duration.
     * @param end The end time of the duration.
     * @return true if the start time is before or equal to the end time; false otherwise.
     */
    public static boolean isValidDuration(LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end)) return false;
        return true;
    }

    /**
     * Gets the date and time of the unavailable slot.
     * 
     * @return The date and time when the staff member is unavailable.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Retrieves the unique ID of the UnavailableDate.
     * 
     * @return The unique ID of the UnavailableDate.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID for the UnavailableDate.
     * 
     * @param id The new unique identifier for the UnavailableDate.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the staff ID for which this unavailable date is recorded.
     * 
     * @return The staff member's ID associated with this unavailable date.
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * Sets the staff ID for which this unavailable date is recorded.
     * 
     * @param staffId The staff member's ID to associate with this unavailable date.
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * Sets the date and time when the staff member is unavailable.
     * 
     * @param date The new date and time when the staff member is unavailable.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
