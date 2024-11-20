package control.prescription;

import control.medicine.MedicineController;
import entity.medicine.Medicine;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.util.List;
import repository.medicine.PrescriptionItemRepository;

/**
 * Controller for handling operations related to prescription items.
 */
public class PrescriptionItemController {

    private static final PrescriptionItemRepository prescriptionItemRepository = PrescriptionItemRepository.getInstance();

    /**
     * Creates a new PrescriptionItem.
     *
     * @param prescriptionId The ID of the prescription.
     * @param medicineId     The ID of the medicine.
     * @param quantity       The quantity of the medicine.
     * @param notes          Notes for the prescription item.
     * @return The created PrescriptionItem.
     * @throws InvalidInputException If any input is invalid.
     */
    public static PrescriptionItem createPrescriptionItem(String prescriptionId, String medicineId, int quantity, String notes) throws InvalidInputException {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }
        if (medicineId == null || medicineId.isEmpty()) {
            throw new InvalidInputException("Medicine ID cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }

        PrescriptionItem item = new PrescriptionItem();
        item.setId(prescriptionItemRepository.getNextClassId());
        item.setPrescriptionId(prescriptionId);
        item.setMedicineId(medicineId);
        item.setQuantity(quantity);
        item.setStatus(PrescriptionItem.ItemStatus.PENDING);
        item.setNotes(notes);
        prescriptionItemRepository.add(item);
        prescriptionItemRepository.save();
        return item;
    }

    /**
     * Retrieves a PrescriptionItem by its ID.
     *
     * @param itemId The ID of the prescription item.
     * @return The PrescriptionItem.
     * @throws EntityNotFoundException If the item is not found.
     * @throws InvalidInputException   If the item ID is invalid.
     */
    public static PrescriptionItem getPrescriptionItemById(String itemId) throws EntityNotFoundException, InvalidInputException {
        if (itemId == null || itemId.isEmpty()) {
            throw new InvalidInputException("Item ID cannot be null or empty.");
        }

        PrescriptionItem item = prescriptionItemRepository.findByField("id", itemId).stream().findFirst().orElse(null);
        if (item == null) {
            throw new EntityNotFoundException("PrescriptionItem", itemId);
        }
        return item;
    }

    /**
     * Retrieves a list of PrescriptionItems for the given prescription.
     *
     * @param prescription The prescription to get items for.
     * @return A list of PrescriptionItems.
     * @throws InvalidInputException If the prescription is null.
     */
    public static List<PrescriptionItem> getPrescriptionItems(Prescription prescription) throws InvalidInputException {
        if (prescription == null) {
            throw new InvalidInputException("Prescription cannot be null.");
        }
        return prescriptionItemRepository.findByField("prescriptionId", prescription.getId());
    }

    /**
     * Dispenses a PrescriptionItem and updates the medicine stock.
     *
     * @param item The PrescriptionItem to dispense.
     * @return True if the item was successfully dispensed, false otherwise.
     * @throws InvalidInputException   If the item is null.
     * @throws EntityNotFoundException If the medicine is not found.
     */
    public static Boolean dispensePrescriptionItem(PrescriptionItem item) throws InvalidInputException, EntityNotFoundException {
        if (item == null) {
            throw new InvalidInputException("Prescription item cannot be null.");
        }

        Medicine med = MedicineController.getMedicineById(item.getMedicineId());
        if (med == null) {
            throw new EntityNotFoundException("Medicine", item.getMedicineId());
        }

        if (MedicineController.decMedStock(med, item.getQuantity())) {
            item.setStatus(PrescriptionItem.ItemStatus.DISPENSED);
            prescriptionItemRepository.save();
            
            return true;
        }
        return false;
    }

    /**
     * Retrieves all pending PrescriptionItems for a given prescription ID.
     *
     * @param prescriptionId The ID of the prescription.
     * @return A list of pending PrescriptionItems.
     * @throws InvalidInputException If the prescription ID is invalid.
     */
    public static List<PrescriptionItem> getPendingPrescriptionItems(String prescriptionId) throws InvalidInputException {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }

        List<PrescriptionItem> items = prescriptionItemRepository.findByField("prescriptionId", prescriptionId);
        return items.stream()
                .filter(item -> item.getStatus() == PrescriptionItem.ItemStatus.PENDING)
                .toList();
    }

    /**
     * Deletes a PrescriptionItem.
     *
     * @param item The PrescriptionItem to delete.
     * @throws EntityNotFoundException If the item is not found.
     */
    public static void deletePrescriptionItem(PrescriptionItem item) throws EntityNotFoundException {
        if (item == null) {
            throw new EntityNotFoundException("PrescriptionItem", "null");
        }
        prescriptionItemRepository.remove(item);
        prescriptionItemRepository.save();
    }

    /**
     * Updates an existing PrescriptionItem with new quantity and notes.
     *
     * @param prescriptionId The ID of the prescription.
     * @param medicineId     The ID of the medicine in the prescription item.
     * @param quantity       The new quantity of the medicine.
     * @param notes          The updated notes for the prescription item.
     * @throws EntityNotFoundException If the PrescriptionItem is not found.
     * @throws InvalidInputException   If the inputs are invalid.
     */
    public static void updatePrescriptionItem(String prescriptionId, String medicineId, int quantity, String notes) throws EntityNotFoundException, InvalidInputException {
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }
        if (medicineId == null || medicineId.isEmpty()) {
            throw new InvalidInputException("Medicine ID cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }

        List<PrescriptionItem> items = prescriptionItemRepository.findByField("prescriptionId", prescriptionId);
        PrescriptionItem item = items.stream()
                .filter(i -> i.getMedicineId().equals(medicineId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = createPrescriptionItem(prescriptionId, medicineId, quantity, notes);
        } else {
            if (notes.isEmpty()) {
                notes = item.getNotes();
            }
        }

        item.setQuantity(quantity);
        item.setNotes(notes);

        prescriptionItemRepository.save();
    }
}
