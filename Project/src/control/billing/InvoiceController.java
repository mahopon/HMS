package control.billing;

import control.appointment.AppointmentController;
import control.medicine.MedicineController;
import control.prescription.PrescriptionController;
import control.prescription.PrescriptionItemController;
import entity.appointment.Appointment;
import entity.billing.Invoice;
import entity.medicine.Medicine;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import repository.billing.InvoiceRepository;

/**
 * Controller for managing invoices.
 * Handles creation, calculation, retrieval, and modification of invoices.
 */
public class InvoiceController {

    private static final InvoiceRepository invoiceRepository = InvoiceRepository.getInstance();

    /**
     * Creates a new invoice based on the specified prescription.
     *
     * @param customerId     The ID of the customer.
     * @param apptId         The ID of the appointment associated with the invoice.
     * @param taxRate        The tax rate to apply to the invoice.
     * @throws InvalidInputException   If any input is invalid (e.g., negative tax rate or empty customer ID).
     * @throws EntityNotFoundException If the prescription or appointment is not found.
     */
    public static void createInvoice(String customerId, String apptId, double taxRate) throws InvalidInputException, EntityNotFoundException {
        if (customerId == null || customerId.isEmpty()) {
            throw new InvalidInputException("Customer ID cannot be null or empty.");
        }
        if (taxRate < 0) {
            throw new InvalidInputException("Tax rate cannot be negative.");
        }
        Appointment appt = AppointmentController.getAppt(apptId);
        Prescription prescription = null;
        try {
            prescription = PrescriptionController.getPrescriptionByAppt(appt);
        } catch (NoSuchElementException e) {
            prescription = null;
        }
        double totalAmount;
        if (prescription == null) {
            totalAmount = 0.0;
        } else {
            List<PrescriptionItem> prescriptionItems = PrescriptionController.getPrescriptionItems(prescription);
            totalAmount = calculateTotalAmount(prescriptionItems);
        }

        Invoice newInvoice = new Invoice(
                invoiceRepository.getNextClassId(),
                customerId,
                apptId,
                30.0,
                totalAmount,
                taxRate,
                LocalDateTime.now()
        );

        invoiceRepository.add(newInvoice);
        invoiceRepository.save();
    }
    
    /**
     * Recalculates the cost of an existing invoice based on prescription items.
     *
     * @param apptId The ID of the appointment for which the invoice cost needs to be recalculated.
     * @throws InvalidInputException   If the appointment ID is invalid.
     * @throws EntityNotFoundException If the invoice or prescription is not found.
     */
    public static void recalculateInvoiceCost(String apptId) throws InvalidInputException, EntityNotFoundException {
        if (apptId == null || apptId.isEmpty()) {
            throw new InvalidInputException("Prescription ID cannot be null or empty.");
        }

        Invoice invoice = InvoiceController.getInvoiceByAppt(apptId);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice of", apptId);
        }
        Appointment appt = AppointmentController.getAppt(apptId);
        try {
            Prescription prescription = PrescriptionController.getPrescriptionByAppt(appt);
            try {
                List<PrescriptionItem> items = PrescriptionItemController.getPrescriptionItems(prescription);
                
                double totalCost = 0.0;
                for (PrescriptionItem item : items) {
                    Medicine med = MedicineController.getMedicineById(item.getMedicineId());
                    if (med != null) {
                        totalCost += item.getQuantity() * med.getUnitCost();
                    }
                }
                invoice.setTotalAmount(totalCost + invoice.getServiceFee());
                invoice.setTotalPayable(invoice.getTotalAmount()*(1+invoice.getTaxRate()));
                if (invoice.getTotalPayable() - invoice.getCurrentPaid() < 0) {
                    invoice.setCurrentPaid(invoice.getCurrentPaid() - invoice.getTotalPayable());
                    invoice.setBalance(0);
                } else {
                    invoice.setBalance(invoice.getTotalPayable() - invoice.getCurrentPaid());
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (EntityNotFoundException e) {
            PrescriptionController.createPrescription(appt.getId());
        }
        invoiceRepository.save();
    }

    /**
     * Calculates the total amount for an invoice based on the prescription items.
     *
     * @param items The list of prescription items.
     * @return The total amount for the prescription items.
     * @throws EntityNotFoundException If a medicine ID in the prescription item list is not found.
     * @throws InvalidInputException   If any input is invalid.
     */
    private static double calculateTotalAmount(List<PrescriptionItem> items) throws EntityNotFoundException, InvalidInputException {
        double totalAmount = 0.0;

        for (PrescriptionItem item : items) {
            Medicine medicine = MedicineController.getMedicineById(item.getMedicineId());
            if (medicine == null) {
                throw new EntityNotFoundException("Medicine", item.getMedicineId());
            }
            totalAmount += medicine.getUnitCost() * item.getQuantity();
        }

        return totalAmount;
    }
    
    /**
     * Increases the balance of a given invoice by a specified cost.
     *
     * @param inv  The invoice to update.
     * @param cost The cost to add to the invoice's balance.
     * @throws InvalidInputException If the cost is negative.
     */
    public static void incBalance(Invoice inv, double cost) throws InvalidInputException {
        if (cost < 0) {
            throw new InvalidInputException("Cost cannot be negative.");
        }
        double newBalance = inv.getBalance() + cost;
        inv.setBalance(newBalance);
        invoiceRepository.save();
    }
    
    /**
     * Makes a payment towards the balance of a given invoice.
     *
     * @param inv      The invoice to make a payment against.
     * @param payment  The payment amount.
     * @throws InvalidInputException If the payment is less than or equal to zero, or exceeds the current balance.
     */
    public static void payBalance(Invoice inv, double payment) throws InvalidInputException {
        if (payment <= 0) {
            throw new InvalidInputException("Payment must be greater than zero.");
        }
        double currentBalance = inv.getBalance();
        if (payment > currentBalance) {
            throw new InvalidInputException("Payment exceeds the current balance.");
        }
        inv.setBalance(currentBalance - payment);
        inv.setCurrentPaid(inv.getCurrentPaid() + payment);
        if (inv.getCurrentPaid() == inv.getTotalPayable()) inv.setStatus(Invoice.InvoiceStatus.PAID);
        else inv.setStatus(Invoice.InvoiceStatus.PARTIAL);
        invoiceRepository.save();
    }
    
    /**
     * Retrieves an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice.
     * @return The Invoice object.
     * @throws InvalidInputException   If the invoice ID is invalid.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    public static Invoice getInvoiceById(String invoiceId) throws InvalidInputException, EntityNotFoundException {
        if (invoiceId == null || invoiceId.isEmpty()) {
            throw new InvalidInputException("Invoice ID cannot be null or empty.");
        }

        Invoice invoice = invoiceRepository.findByField("id", invoiceId).stream().findFirst().orElse(null);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice", invoiceId);
        }
        return invoice;
    }

    /**
     * Retrieves a list of invoices for a specified customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of invoices for the customer, sorted by invoice ID.
     */
    public static List<Invoice> getInvoiceByCustomer(String customerId) {
        List<Invoice> list = invoiceRepository.findByField("customerId", customerId);
        list.sort(Comparator.comparing(Invoice::getId));
        return list;
    }

    /**
     * Retrieves a list of all invoices.
     *
     * @return A list of all invoices, sorted by invoice ID.
     */
    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.toList();
        invoices.sort(Comparator.comparing(Invoice::getId));
        return invoices;
    }

    /**
     * Cancels the specified invoice.
     *
     * @param invoiceId The ID of the invoice to cancel.
     * @return True if the operation is successful.
     * @throws InvalidInputException   If the invoice ID is invalid.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    public static Boolean cancelInvoice(String invoiceId) throws InvalidInputException, EntityNotFoundException {
        Invoice invoice = getInvoiceById(invoiceId);

        if (invoice.getStatus() == Invoice.InvoiceStatus.CANCELED) {
            return true;
        }

        invoice.setStatus(Invoice.InvoiceStatus.CANCELED);
        invoiceRepository.save();
        return true;
    }

    /**
     * Deletes the specified invoice.
     *
     * @param invoiceId The ID of the invoice to delete.
     * @throws InvalidInputException   If the invoice ID is invalid.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    public static void deleteInvoice(String invoiceId) throws InvalidInputException, EntityNotFoundException {
        Invoice invoice = getInvoiceById(invoiceId);
        invoiceRepository.remove(invoice);
        invoiceRepository.save();
    }

    /**
     * Retrieves an invoice by its associated appointment ID.
     *
     * @param apptId The ID of the appointment.
     * @return The corresponding invoice.
     */
    public static Invoice getInvoiceByAppt(String apptId) {
        Invoice inv = invoiceRepository.findByField("apptId", apptId).getFirst();
        return inv;
    }
}
