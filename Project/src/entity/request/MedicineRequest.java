package entity.request;

import java.time.LocalDateTime;
import utility.DateFormat;

/**
 * Represents a request for a specific medicine, including details like the medicine ID,
 * requested quantity, requestor and approver information, and timestamps for creation and modification.
 */
public class MedicineRequest extends Request {

    private String medicineId; ///< The ID of the medicine being requested.
    private int quantity;      ///< The quantity of the medicine requested.

    /**
     * Default constructor for the MedicineRequest class.
     */
    public MedicineRequest() {
        super();
    }

    /**
     * Constructor to create a new MedicineRequest with specified details.
     * 
     * @param id The unique ID of the request.
     * @param requestorId The ID of the person requesting the medicine.
     * @param approverId The ID of the person approving the request.
     * @param status The status of the request (e.g., PENDING, APPROVED).
     * @param timeCreated The timestamp when the request was created.
     * @param timeModified The timestamp when the request was last modified.
     * @param medicineId The ID of the medicine being requested.
     * @param quantity The quantity of the medicine requested.
     */
    public MedicineRequest(String id, String requestorId, String approverId, STATUS status, LocalDateTime timeCreated, LocalDateTime timeModified, String medicineId, int quantity) {
        super(id, requestorId, approverId, status, timeCreated, timeModified);
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    /**
     * Gets the ID of the medicine being requested.
     * 
     * @return The medicine ID.
     */
    public String getMedicineId() {
        return medicineId;
    }

    /**
     * Sets the ID of the medicine being requested.
     * 
     * @param medicineId The new medicine ID to set.
     */
    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * Gets the quantity of the medicine requested.
     * 
     * @return The requested quantity of the medicine.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the medicine requested.
     * 
     * @param quantity The new quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Provides a string representation of the MedicineRequest, including all relevant details.
     * 
     * @return A string describing the MedicineRequest, including ID, requestor and approver IDs, status,
     *         creation and modification times, medicine ID, and quantity.
     */
    @Override
    public String toString() {
        return super.getId() + ", " +
               super.getRequestorId() + ", " +
               super.getApproverId() + ", " +
               super.getStatus() + ", " +
               DateFormat.formatWithTime(super.getTimeCreated()) + ", " +
               DateFormat.formatWithTime(super.getTimeModified()) + ", " +
               medicineId + ", " +
               quantity;
    }
}