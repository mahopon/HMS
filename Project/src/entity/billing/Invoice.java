package entity.billing;

import entity.EntityObject;
import java.time.LocalDateTime;
import utility.DateFormat;

/**
 * Represents an invoice with details such as customer ID, appointment ID, total amount,
 * service fee, tax, balance, payment status, issue date, and status of the invoice.
 * This class is used to manage and display invoice information.
 */
public class Invoice extends EntityObject {

    /**
     * Enum representing the status of the invoice.
     * The possible statuses are:
     * - PENDING: Invoice is not paid yet.
     * - PAID: Invoice has been paid in full.
     * - PARTIAL: Invoice has been partially paid.
     * - CANCELED: Invoice has been canceled.
     */
    public enum InvoiceStatus {
        PENDING, PAID, PARTIAL, CANCELED
    }

    private String id;
    private String customerId;
    private String apptId;
    private double serviceFee;
    private double totalAmount;
    private double taxRate;
    private double balance;
    private double currentPaid;
    private double totalPayable;
    private LocalDateTime issueDate;
    private InvoiceStatus status;

    /**
     * Default constructor for Invoice. Initializes an empty Invoice object.
     */
    public Invoice() {
    }

    /**
     * Constructs an Invoice with specified details.
     * The total amount is calculated as the sum of the base amount and the service fee.
     * The total payable is calculated by applying the tax rate to the total amount.
     * 
     * @param invoiceId The ID of the invoice.
     * @param customerId The ID of the customer to whom the invoice is issued.
     * @param apptId The ID of the appointment related to the invoice.
     * @param serviceFee The service fee associated with the invoice.
     * @param totalAmount The total amount before tax and service fee.
     * @param taxRate The tax rate applied to the total amount.
     * @param issueDate The issue date of the invoice.
     */
    public Invoice(String invoiceId, String customerId, String apptId, double serviceFee, double totalAmount, double taxRate, LocalDateTime issueDate) {
        this.id = invoiceId;
        this.customerId = customerId;
        this.apptId = apptId;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount + serviceFee;
        this.taxRate = taxRate;
        this.totalPayable = this.totalAmount * (1 + taxRate);
        this.balance = serviceFee * (1 + taxRate);
        this.currentPaid = 0.0;
        this.issueDate = issueDate;
        this.status = InvoiceStatus.PENDING;
    }

    /**
     * Retrieves the total amount before tax and service fee.
     * 
     * @return The total amount of the invoice before tax and service fee.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount of the invoice before tax and service fee.
     * 
     * @param totalAmount The new total amount for the invoice.
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Retrieves the tax rate applied to the total amount.
     * 
     * @return The tax rate of the invoice.
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the tax rate applied to the total amount.
     * 
     * @param taxRate The new tax rate to apply to the invoice.
     */
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * Retrieves the total payable amount, which includes tax.
     * 
     * @return The total amount to be paid including tax.
     */
    public double getTotalPayable() {
        return totalPayable;
    }

    /**
     * Sets the total payable amount, which includes tax.
     * 
     * @param totalPayable The new total payable amount for the invoice.
     */
    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }

    /**
     * Retrieves the current balance of the invoice (unpaid amount).
     * 
     * @return The balance remaining on the invoice.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance remaining on the invoice.
     * 
     * @param balance The new balance for the invoice.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Retrieves the service fee applied to the invoice.
     * 
     * @return The service fee for the invoice.
     */
    public double getServiceFee() {
        return serviceFee;
    }

    /**
     * Sets the service fee for the invoice.
     * 
     * @param serviceFee The new service fee for the invoice.
     */
    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    /**
     * Retrieves the amount already paid towards the invoice.
     * 
     * @return The amount that has already been paid.
     */
    public double getCurrentPaid() {
        return currentPaid;
    }

    /**
     * Sets the amount already paid towards the invoice.
     * 
     * @param paid The new amount paid towards the invoice.
     */
    public void setCurrentPaid(double paid) {
        this.currentPaid = paid;
    }

    /**
     * Retrieves the unique ID of the invoice.
     * 
     * @return The ID of the invoice.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID for the invoice.
     * 
     * @param invoiceId The new ID for the invoice.
     */
    public void setId(String invoiceId) {
        this.id = invoiceId;
    }

    /**
     * Retrieves the ID of the customer associated with the invoice.
     * 
     * @return The customer ID for the invoice.
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID for the invoice.
     * 
     * @param customerId The new customer ID for the invoice.
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Retrieves the issue date of the invoice.
     * 
     * @return The issue date of the invoice.
     */
    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the issue date for the invoice.
     * 
     * @param issueDate The new issue date for the invoice.
     */
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Retrieves the current status of the invoice.
     * 
     * @return The current status of the invoice (e.g., PENDING, PAID, etc.).
     */
    public InvoiceStatus getStatus() {
        return status;
    }

    /**
     * Sets the status for the invoice.
     * 
     * @param status The new status for the invoice.
     */
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    /**
     * Retrieves the appointment ID associated with the invoice.
     * 
     * @return The appointment ID for the invoice.
     */
    public String getapptIdId() {
        return apptId;
    }

    /**
     * Sets the appointment ID associated with the invoice.
     * 
     * @param apptId The new appointment ID for the invoice.
     */
    public void setapptIdId(String apptId) {
        this.apptId = apptId;
    }

    /**
     * Returns a string representation of the invoice with all details,
     * including the status, issue date, amounts, and appointment ID.
     * 
     * @return A formatted string containing the invoice details.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
               "Appointment ID: " + apptId + "\n" +
               "Service Fee: $" + serviceFee + "\n" +
               "Total Amount: $" + totalAmount + "\n" +
               "Total Payable: $" + totalPayable + "\n" +
               "Current Balance: $" + balance + "\n" +
               "Current Paid: $" + currentPaid + "\n" +
               "Issue Date: " + DateFormat.format(issueDate) + "\n" +
               "Status: " + status;
    }
}
