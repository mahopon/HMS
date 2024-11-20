package control.prescription;

import entity.appointment.Appointment;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import repository.medicine.PrescriptionRepository;

/**
 * Controller for managing prescription-related operations.
 */
public class PrescriptionController {

    private static final PrescriptionRepository prescriptionRepository = PrescriptionRepository.getInstance();

    /**
     * Creates a new Prescription for the specified appointment.
     *
     * @param apptId The ID of the appointment.
     * @return The created Prescription.
     * @throws InvalidInputException If the appointment ID is null or empty.
     */
    public static Prescription createPrescription(String apptId) throws InvalidInputException {
        if (apptId == null || apptId.isEmpty()) {
            throw new InvalidInputException("Appointment ID cannot be null or empty.");
        }

        Prescription newPrescription = new Prescription(prescriptionRepository.getNextClassId(), apptId, true);
        prescriptionRepository.add(newPrescription);
        prescriptionRepository.save();
        return newPrescription;
    }

    /**
     * Retrieves a Prescription by its ID.
     *
     * @param prescriptionId The ID of the prescription.
     * @return The Prescription object.
     * @throws EntityNotFoundException If the prescription is not found.
     * @throws InvalidInputException   If the prescription ID is invalid.
     */
    public static Prescription getPrescriptionById(String prescriptionId) throws EntityNotFoundException, InvalidInputException {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }

        Prescription prescription = prescriptionRepository.get(prescriptionId);
        if (prescription == null) {
            throw new EntityNotFoundException("Prescription", prescriptionId);
        }
        return prescription;
    }

    /**
     * Retrieves a Prescription associated with the specified appointment.
     *
     * @param appt The appointment to find the prescription for.
     * @return The Prescription object.
     * @throws EntityNotFoundException If the prescription is not found.
     * @throws InvalidInputException   If the appointment is null.
     */
    public static Prescription getPrescriptionByAppt(Appointment appt) throws EntityNotFoundException, InvalidInputException {
        if (appt == null) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }

        List<Prescription> prescription = prescriptionRepository.findByField("apptId", appt.getId());
        if (prescription.isEmpty()) {
            throw new EntityNotFoundException("Cannot find prescription for appointment " + appt.getId());
        }
        return prescription.getFirst();
    }

    /**
     * Retrieves a list of active prescriptions.
     *
     * @return A list of active prescriptions.
     */
    public static List<Prescription> getActivePrescriptions() {
        return prescriptionRepository.findByField("isActive", true);
    }

    /**
     * Retrieves all PrescriptionItems for the specified prescription.
     *
     * @param prescription The prescription to get items for.
     * @return A list of PrescriptionItems.
     * @throws InvalidInputException If the prescription is null.
     */
    public static List<PrescriptionItem> getPrescriptionItems(Prescription prescription) throws InvalidInputException {
        if (prescription == null) {
            throw new InvalidInputException("Prescription cannot be null.");
        }
        return PrescriptionItemController.getPrescriptionItems(prescription);
    }

    /**
     * Updates the active status of a prescription.
     *
     * @param prescription The prescription to update.
     * @param isActive     The new active status.
     * @return The updated active status.
     * @throws InvalidInputException If the prescription is null.
     */
    public static Boolean updatePrescriptionStatus(Prescription prescription, boolean isActive) throws InvalidInputException {
        if (prescription == null) {
            throw new InvalidInputException("Prescription cannot be null.");
        }
        prescription.setIsActive(isActive);
        prescriptionRepository.save();
        return isActive;
    }

    /**
     * Updates the prescription status to inactive if all items have been dispensed.
     *
     * @param prescriptionId The ID of the prescription.
     * @throws InvalidInputException   If the prescription ID is invalid.
     * @throws EntityNotFoundException If the prescription is not found.
     */
    public static void updatePrescriptionStatusIfCompleted(String prescriptionId) throws InvalidInputException, EntityNotFoundException {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }

        Prescription prescription = getPrescriptionById(prescriptionId);
        List<PrescriptionItem> itemList = getPrescriptionItems(prescription);
        boolean allDispensed = itemList.stream().allMatch(item -> item.getStatus() == PrescriptionItem.ItemStatus.DISPENSED);

        if (allDispensed) {
            prescription.setIsActive(false);
            prescriptionRepository.save();
        }
    }

    /**
     * Cancels the specified prescription by setting its active status to false.
     *
     * @param prescription The prescription to cancel.
     * @throws InvalidInputException If the prescription is null or already inactive.
     */
    public static void cancelPrescription(Prescription prescription) throws InvalidInputException {
        if (prescription == null) {
            throw new InvalidInputException("Prescription cannot be null.");
        }
        if (!prescription.getIsActive()) {
            throw new InvalidInputException("Prescription is already inactive.");
        }
        prescription.setIsActive(false);
        prescriptionRepository.save();
    }

    /**
     * Checks if all items in the prescription have been completed (no pending items).
     *
     * @param prescription The prescription to check.
     * @return True if all items are completed, false otherwise.
     * @throws InvalidInputException If the prescription is null.
     */
    public static Boolean checkCompleted(Prescription prescription) throws InvalidInputException {
        if (prescription == null) {
            throw new InvalidInputException("Prescription cannot be null.");
        }
        List<PrescriptionItem> itemList = PrescriptionItemController.getPrescriptionItems(prescription);
        return itemList.stream().noneMatch(item -> item.getStatus() == PrescriptionItem.ItemStatus.PENDING);
    }

    /**
     * Deletes the specified prescription.
     *
     * @param prescription The prescription to delete.
     * @throws EntityNotFoundException If the prescription is not found.
     */
    public static void deletePrescription(Prescription prescription) throws EntityNotFoundException {
        if (prescription == null) {
            throw new EntityNotFoundException("Prescription", "null");
        }
        prescriptionRepository.remove(prescription);
        prescriptionRepository.save();
    }
    /**
     * Updates the prescription for a specific appointment by adding or modifying prescribed medications.
     * If a prescription already exists for the appointment, it updates the prescribed items. 
     * If no prescription exists, a new prescription is created.
     * 
     * @param appt The appointment for which the prescription needs to be updated.
     * @param updatedPrescribedMedication A map containing the updated prescribed medications, where 
     *                                    each entry contains a medication ID as the key, and a list 
     *                                    of objects as the value. The list contains the medication 
     *                                    quantity at index 0 and the notes at index 1.
     * 
     * @throws InvalidInputException If the input data is invalid, such as null or empty appointment 
     *                               or medication data.
     * @throws EntityNotFoundException If the prescription or prescription items cannot be found.
     */
    public static void updatePrescription(Appointment appt, HashMap<String, List<Object>> updatedPrescribedMedication) {
        if (updatedPrescribedMedication != null && !updatedPrescribedMedication.isEmpty()) {
            Prescription prescription = null;
            
            // Try to fetch the existing prescription by appointment
            try {
                prescription = PrescriptionController.getPrescriptionByAppt(appt);
            } catch (EntityNotFoundException e) {
                System.out.println("No existing prescription found. A new prescription will be created.");
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
    
            // If the prescription doesn't exist, create a new one
            if (prescription == null) {
                try {
                    prescription = PrescriptionController.createPrescription(appt.getId());
                } catch (InvalidInputException e) {
                    System.out.println("Error creating new prescription: " + e.getMessage());
                    return; // Exit method if prescription creation fails
                }
            }
    
            // Now, proceed to update the prescription items
            for (Map.Entry<String, List<Object>> med : updatedPrescribedMedication.entrySet()) {
                String medicineId = med.getKey();
                int quantity = (int) med.getValue().get(0);
                String notes = (String) med.getValue().get(1);
    
                try {
                    // Update the prescription item with the new details
                    PrescriptionItemController.updatePrescriptionItem(prescription.getId(), medicineId, quantity, notes);
                } catch (EntityNotFoundException | InvalidInputException e) {
                    System.out.println("Error updating prescription item: " + e.getMessage());
                }
            }
        }
    }
    
}
