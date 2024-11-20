package entity.medicine;

import entity.EntityObject;

/**
 * Represents a prescription linked to an appointment, containing the prescription ID,
 * the associated appointment ID, and the status of the prescription (active or not).
 */
public class Prescription extends EntityObject {

    private String id;
    private String apptId;
    private boolean isActive;

    /**
     * Default constructor for the Prescription class.
     */
    public Prescription() {
        
    }

    /**
     * Constructor to create a new prescription with specified details.
     * 
     * @param id The unique ID of the prescription.
     * @param apptId The ID of the associated appointment.
     * @param isActive Indicates whether the prescription is currently active.
     */
    public Prescription(String id, String apptId, boolean isActive) {
        this.id = id;
        this.apptId = apptId;
        this.isActive = isActive;
    }

    /**
     * Gets the unique identifier for the prescription.
     * 
     * @return The prescription ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Gets the ID of the associated appointment for this prescription.
     * 
     * @return The appointment ID.
     */
    public String getApptId() { 
        return apptId; 
    }

    /**
     * Sets the appointment ID for this prescription.
     * 
     * @param apptId The new appointment ID to set.
     */
    public void setApptId(String apptId) { 
        this.apptId = apptId; 
    }

    /**
     * Gets the status of the prescription (whether it is active or not).
     * 
     * @return true if the prescription is active, false otherwise.
     */
    public boolean getIsActive() { 
        return isActive; 
    }

    /**
     * Sets the active status of the prescription.
     * 
     * @param isActive The new active status of the prescription.
     */
    public void setIsActive(boolean isActive) { 
        this.isActive = isActive; 
    }

    /**
     * Provides a string representation of the prescription, including its ID,
     * the associated appointment ID, and the active status.
     * 
     * @return A string describing the prescription.
     */
    @Override
    public String toString() {
        return "Prescription ID: " + id + "\n" +
               "Appointment ID: " + apptId + "\n" +
               "Active: " + isActive;
    }
}