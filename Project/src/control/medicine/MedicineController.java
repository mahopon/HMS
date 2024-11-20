package control.medicine;

import entity.medicine.Medicine;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.util.List;
import repository.medicine.MedicineRepository;

/**
 * Controller class responsible for managing operations related to medicines, including restocking,
 * retrieving, updating, and checking the availability and stock of medicines.
 */
public class MedicineController {

    private static final MedicineRepository medicineRepository = MedicineRepository.getInstance();

    /**
     * Restocks the given medicine with the specified quantity.
     * 
     * @param med The medicine to restock.
     * @param quantity The quantity to add to the stock.
     * @return {@code true} if restocking is successful.
     * @throws InvalidInputException If the quantity is non-positive.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean restockMedicine(Medicine med, int quantity) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
        med.restock(quantity);
        medicineRepository.save();
        return true;
    }

    /**
     * Retrieves a medicine by its ID.
     * 
     * @param id The ID of the medicine to retrieve.
     * @return The medicine object if found.
     * @throws InvalidInputException If the ID is null or empty.
     * @throws EntityNotFoundException If no medicine is found with the provided ID.
     */
    public static Medicine getMedicineById(String id) throws InvalidInputException, EntityNotFoundException {
        if (id == null || id.isEmpty()) {
            throw new InvalidInputException("Medicine ID cannot be null or empty.");
        }

        Medicine med = medicineRepository.get(id);
        if (med == null) {
            throw new EntityNotFoundException("Medicine", id);
        }
        return med;
    }

    /**
     * Retrieves all medicines.
     * 
     * @return A list of all medicines.
     */
    public static List<Medicine> getAllMedicines() {
        return medicineRepository.toList();
    }

    /**
     * Checks the stock quantity of a specific medicine.
     * 
     * @param medicineId The ID of the medicine to check.
     * @return The current stock quantity of the medicine.
     * @throws InvalidInputException If the ID is null or empty.
     * @throws EntityNotFoundException If no medicine is found with the provided ID.
     */
    public static Integer checkMedicineAmt(String medicineId) throws InvalidInputException, EntityNotFoundException {
        Medicine med = getMedicineById(medicineId);
        return med.getStockQuantity();
    }

    /**
     * Checks if the given medicine has low stock, based on its low stock threshold.
     * 
     * @param med The medicine to check.
     * @return {@code true} if the medicine has stock less than the low stock threshold.
     * @throws InvalidInputException If the medicine is null.
     */
    public static Boolean checkLowStock(Medicine med) throws InvalidInputException {
        if (med == null) {
            throw new InvalidInputException("Medicine cannot be null.");
        }
        return med.getStockQuantity() < med.getLowStockThreshold();
    }

    /**
     * Checks if the given medicine is available (in stock).
     * 
     * @param med The medicine to check.
     * @return {@code true} if the medicine is available.
     * @throws InvalidInputException If the medicine is null.
     */
    public static Boolean checkAvailability(Medicine med) throws InvalidInputException {
        if (med == null) {
            throw new InvalidInputException("Medicine cannot be null.");
        }
        return med.checkAvailability();
    }

    /**
     * Searches for medicines by their name.
     * 
     * @param name The name of the medicine to search for.
     * @return A list of medicines with the specified name.
     * @throws InvalidInputException If the name is null or empty.
     */
    public static List<Medicine> searchMedicineByName(String name) throws InvalidInputException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Medicine name cannot be null or empty.");
        }
        return medicineRepository.findByField("medicineName", name);
    }

    /**
     * Updates the details of a medicine, including its name, dosage, and low stock threshold.
     * 
     * @param med The medicine to update.
     * @param newName The new name of the medicine.
     * @param newDosage The new dosage of the medicine.
     * @param newLowStockThreshold The new low stock threshold.
     * @return {@code true} if the update is successful.
     * @throws InvalidInputException If any of the input values are invalid.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean updateMedicineDetails(Medicine med, String newName, double newDosage, int newLowStockThreshold) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (newName == null || newName.isEmpty()) {
            throw new InvalidInputException("Medicine name cannot be null or empty.");
        }
        if (newDosage <= 0) {
            throw new InvalidInputException("Dosage must be greater than zero.");
        }
        if (newLowStockThreshold <= 0) {
            throw new InvalidInputException("Low stock threshold must be greater than zero.");
        }
        med.setMedicineName(newName);
        med.setDosage(newDosage);
        med.setLowStockThreshold(newLowStockThreshold);
        medicineRepository.save();
        return true;
    }

    /**
     * Increases the stock of a specific medicine by a given amount.
     * 
     * @param med The medicine to update.
     * @param amount The amount by which to increase the stock.
     * @return {@code true} if the stock increase is successful.
     * @throws InvalidInputException If the amount is non-positive.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean incMedStock(Medicine med, int amount) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than zero.");
        }
        med.incStock(amount);
        medicineRepository.save();
        return true;
    }

    /**
     * Decreases the stock of a specific medicine by a given amount.
     * 
     * @param med The medicine to update.
     * @param amount The amount by which to decrease the stock.
     * @return {@code true} if the stock decrease is successful.
     * @throws InvalidInputException If the amount is non-positive or other errors occur.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean decMedStock(Medicine med, int amount) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than zero.");
        }
        try {
            med.decStock(amount);
            medicineRepository.save();
            return true;
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    /**
     * Updates the stock quantity of a medicine to a specified value.
     * 
     * @param med The medicine to update.
     * @param amount The new stock quantity.
     * @return {@code true} if the update is successful.
     * @throws InvalidInputException If the quantity is negative or other errors occur.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean updateMedStock(Medicine med, int amount) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (amount < 0) {
            throw new InvalidInputException("Stock quantity cannot be negative.");
        }
        try {
            med.setStockQuantity(amount);
            medicineRepository.save();
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Sets a new low stock threshold for a medicine.
     * 
     * @param med The medicine to update.
     * @param newThreshold The new low stock threshold.
     * @return {@code true} if the update is successful.
     * @throws InvalidInputException If the threshold is non-positive or other errors occur.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean setLowStockThreshold(Medicine med, int newThreshold) throws InvalidInputException, EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        if (newThreshold <= 0) {
            throw new InvalidInputException("Low stock threshold must be greater than zero.");
        }
        try {
            med.setLowStockThreshold(newThreshold);
            medicineRepository.save();
            return true;
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    /**
     * Removes the specified medicine from the inventory.
     * 
     * @param med The medicine to remove.
     * @return {@code true} if the removal is successful.
     * @throws EntityNotFoundException If the medicine is null.
     */
    public static Boolean removeMedicine(Medicine med) throws EntityNotFoundException {
        if (med == null) {
            throw new EntityNotFoundException("Medicine", "null");
        }
        med.setStockQuantity(0);
        medicineRepository.save();
        return true;
    }
    
    
    /**
     * Adds a new medicine to the inventory.
     *
     * @param name             The name of the medicine.
     * @param stock            The current stock of the medicine.
     * @param quantity         The quantity per dose.
     * @param unitCost         The cost per unit of the medicine.
     * @param dosage           The dosage details of the medicine.
     * @param lowStockThreshold The low stock threshold for the medicine.
     * @return {@code true} if the addition is successful.
     * @throws EntityNotFoundException If any of the required parameters are invalid.
     */
   public static Boolean addMedicine(String name, int stock, int quantity, double unitCost, double dosage, int lowstockthreshold) throws EntityNotFoundException {

        Medicine med = new Medicine(medicineRepository.getNextClassId(), name, stock, unitCost,dosage,lowstockthreshold);
       medicineRepository.add(med);
       medicineRepository.save();
       return true;
   }
}
