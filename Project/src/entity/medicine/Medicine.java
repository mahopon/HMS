package entity.medicine;

import entity.EntityObject;

/**
 * The Medicine class represents a medicine entity with details such as ID, name,
 * stock quantity, unit cost, dosage, and low stock threshold. It extends the 
 * {@link EntityObject} class.
 */
public class Medicine extends EntityObject {

    private String id;
    private String medicineName;
    private int stockQuantity;
    private double unitCost;
    private double dosage; // in mg
    private int lowStockThreshold;

    /**
     * Default constructor for the Medicine class.
     */
    public Medicine() {
        
    }

    /**
     * Constructs a Medicine object with specified attributes.
     * 
     * @param id               The unique identifier for the medicine.
     * @param medicineName     The name of the medicine.
     * @param stockQuantity    The quantity of the medicine in stock.
     * @param unitCost         The cost per unit of the medicine.
     * @param dosage           The dosage of the medicine (in mg).
     * @param lowStockThreshold The low stock threshold for the medicine.
     */
    public Medicine(String id, String medicineName, int stockQuantity, double unitCost, double dosage, int lowStockThreshold) {
        this.id = id;
        this.medicineName = medicineName;
        this.stockQuantity = stockQuantity;
        this.unitCost = unitCost;
        this.dosage = dosage;
        this.lowStockThreshold = lowStockThreshold;
    }

    /**
     * Restocks the medicine by adding a specified quantity.
     * 
     * @param quantity The quantity to add to the current stock.
     */
    public void restock(int quantity) {
        stockQuantity += quantity;
    }

    /**
     * Checks if the medicine is available in stock.
     * 
     * @return {@code true} if the stock quantity is greater than 0, {@code false} otherwise.
     */
    public boolean checkAvailability() {
        return stockQuantity > 0;
    }

    /**
     * Gets the unique ID of the medicine.
     * 
     * @return The ID of the medicine.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID of the medicine.
     * 
     * @param id The new ID of the medicine.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name of the medicine.
     * 
     * @param medicineName The name of the medicine.
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * Gets the current stock quantity of the medicine.
     * 
     * @return The current stock quantity.
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the current stock quantity of the medicine.
     * 
     * @param stockQuantity The new stock quantity.
     * @throws IllegalArgumentException if the new stock quantity is negative.
     */
    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("New quantity cannot be negative.");
        }
        this.stockQuantity = stockQuantity;
    }

    /**
     * Gets the dosage of the medicine.
     * 
     * @return The dosage of the medicine in mg.
     */
    public double getDosage() {
        return dosage;
    }
    
    /**
     * Sets the dosage of the medicine.
     * 
     * @param dosage The new dosage of the medicine in mg.
     */
    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    /**
     * Retrieves the low stock threshold for the medicine.
     * 
     * @return The stock level at which the medicine is considered to have low stock.
     */
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Sets the low stock threshold for the medicine.
     * Ensures the threshold is not negative.
     * 
     * @param newLevel The new low stock threshold for the medicine.
     * @throws IllegalArgumentException if the new threshold is negative.
     */
    public void setLowStockThreshold(int newLevel) {
        if (newLevel < 0) {
            throw new IllegalArgumentException("Low stock threshold cannot be negative.");
        }
        this.lowStockThreshold = newLevel;
    }


    /**
     * Gets the name of the medicine.
     * 
     * @return The name of the medicine.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Increases the stock quantity of the medicine.
     * 
     * @param value The quantity to increase.
     */
    public void incStock(int value) {
        this.stockQuantity += value;
    }

    /**
     * Decreases the stock quantity of the medicine.
     * 
     * @param value The quantity to decrease.
     * @throws IllegalArgumentException if the resulting stock quantity is negative.
     */
    public void decStock(int value) {
        if (stockQuantity - value < 0) {
            throw new IllegalArgumentException("New quantity cannot be negative.");
        }
        this.stockQuantity -= value;
    }

    /**
     * Gets the unit cost of the medicine.
     * 
     * @return The cost per unit of the medicine.
     */
    public double getUnitCost() {
        return unitCost;
    }

    /**
     * Sets the unit cost of the medicine.
     * 
     * @param unitCost The new unit cost of the medicine.
     */
    public void setUnitCost(float unitCost) {
        this.unitCost = unitCost;
    }

    /**
     * Returns a string representation of the Medicine object, including all its attributes.
     * A warning is added if the stock is below the low stock threshold.
     * 
     * @return A string representation of the medicine.
     */
    @Override
    public String toString() {
        String str = "ID: " + id + "\n" +
                     "Name: " + medicineName + "\n" +
                     "Stock Quantity: " + stockQuantity + "\n" +
                     "Unit Cost: $" + unitCost + "\n" +
                     "Dosage: " + dosage + " mg\n" +
                     "Low Stock Threshold: " + lowStockThreshold;
        if (lowStockThreshold > stockQuantity) {
            str = str + "\nWARNING: LOW STOCK!";
        }
        return str;
    }
}
