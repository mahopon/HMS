package entity.medicine;

import entity.EntityObject;

/**
 * Represents an item in a prescription, which is a specific medicine prescribed
 * along with its quantity, status, and associated prescription details.
 */
public class PrescriptionItem extends EntityObject {
    
    private String id;
    private String prescriptionId;
    private String medicineId;
    private int quantity;
    
    /**
     * Enum representing the possible statuses of a prescription item.
     * PENDING: The item has not been dispensed or canceled.
     * CANCELLED: The item has been canceled and will not be dispensed.
     * DISPENSED: The item has been dispensed to the patient.
     */
    public enum ItemStatus {PENDING, CANCELLED, DISPENSED};
    
    private ItemStatus status;
    private String notes;

    /**
     * Default constructor for the PrescriptionItem class.
     */
    public PrescriptionItem() {}

    /**
     * Constructor to create a new prescription item with specified details.
     * 
     * @param id The unique ID of the prescription item.
     * @param prescriptionId The ID of the associated prescription.
     * @param medicineId The ID of the prescribed medicine.
     * @param quantity The quantity of the medicine prescribed.
     * @param status The current status of the prescription item.
     * @param notes Additional notes regarding the prescription item.
     */
    public PrescriptionItem(String id, String prescriptionId, String medicineId, int quantity, ItemStatus status, String notes) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.status = status;
        this.notes = notes;
    }

    /**
     * Gets the unique identifier for the prescription item.
     * 
     * @return The prescription item ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the prescription item.
     * 
     * @param id The new prescription item ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the associated prescription for this item.
     * 
     * @return The prescription ID.
     */
    public String getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Sets the ID of the associated prescription for this item.
     * 
     * @param prescriptionId The new prescription ID to set.
     */
    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    /**
     * Gets the ID of the prescribed medicine.
     * 
     * @return The medicine ID.
     */
    public String getMedicineId() {
        return medicineId;
    }

    /**
     * Sets the ID of the prescribed medicine.
     * 
     * @param medicineId The new medicine ID to set.
     */
    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * Gets the quantity of the prescribed medicine.
     * 
     * @return The quantity of the prescribed medicine.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the prescribed medicine.
     * 
     * @param quantity The new quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the status of the prescription item.
     * 
     * @return The current status of the prescription item.
     */
    public ItemStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the prescription item.
     * 
     * @param status The new status of the prescription item.
     */
    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    /**
     * Gets the additional notes for the prescription item.
     * 
     * @return The notes for the prescription item.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the additional notes for the prescription item.
     * 
     * @param notes The new notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Provides a string representation of the prescription item, including its ID,
     * the associated prescription ID, the medicine ID, quantity, and status.
     * 
     * @return A string describing the prescription item.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
               "Prescription ID: " + prescriptionId + "\n" +
               "Medicine ID: " + medicineId + "\n" +
               "Quantity: " + quantity + "\n" +
               "Status: " + status;
    }
}