package boundary.user.pharmacist;

import control.appointment.AppointmentController;
import control.billing.InvoiceController;
import control.medicine.MedicineController;
import control.notification.NotificationController;
import control.prescription.PrescriptionController;
import control.prescription.PrescriptionItemController;
import control.request.MedicineRequestController;
import control.user.SessionManager;
import entity.appointment.Appointment;
import entity.billing.Invoice;
import entity.medicine.Medicine;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import entity.user.HospitalStaff;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import exception.user.NoUserLoggedInException;
import interfaces.boundary.IUserInterface;
import interfaces.observer.IObserver;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import observer.NotificationObserver;
import utility.ClearConsole;
import utility.DateFormat;
import utility.InputHandler;
import utility.KeystrokeWait;

/**
 * Represents the home UI for a pharmacist in the hospital management system.
 * Provides options to manage prescriptions, medicines, replenishment requests, and notifications.
 */
public class PH_HomeUI implements IUserInterface {
    private final SessionManager session;
    private static final Scanner scanner = InputHandler.getInstance();
    private final NotificationController notificationController;
    private IObserver observer;

    /**
     * Initializes a new instance of PH_HomeUI for the given session.
     * 
     * @param session The session manager associated with the current user.
     */
    public PH_HomeUI(SessionManager session) {
        this.session = session;
        this.notificationController = NotificationController.getInstance();
        try {
            this.observer = new NotificationObserver(session.getCurrentUser());
            notificationController.registerObserver(session.getCurrentUser().getId(), observer);
            this.observer.setNotificationHistory();
        } catch (NoUserLoggedInException e) {
            System.out.println("No user logged in");
        }
    }

    /**
     * Displays the pharmacist menu and processes user input to navigate options.
     */
    @Override
    public void show_options() {
        boolean exit = false;
    
        while (!exit) {
            System.out.println("\n=== Pharmacist Menu ===");
            System.out.println("1. View Active Prescriptions");
            System.out.println("2. View Prescription Items");
            System.out.println("3. Dispense Prescription Item");
            System.out.println("4. Create Replenishment Request");
            System.out.println("5. View All Medicines");
            System.out.println("6. View Notifications");
            System.out.println("7. Logout");
            System.out.println("====================");
    
            int choice = 0;
    
            while (true) {
                try {
                    System.out.print("Please select an option: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 7) break;
                    System.out.println("Enter only a number between 1 and 7.");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    scanner.nextLine();
                }
            }
    
            if (choice == 7) exit = true;
            else {
                ClearConsole.clearConsole();
                handle_option(choice);
            }
        }
    
        System.out.println("You have successfully logged out. Goodbye!");
    }

    /**
     * Handles the specific option chosen by the user from the pharmacist menu.
     * 
     * @param choice The option selected by the user.
     */
    @Override
    public void handle_option(int choice) {
        switch (choice) {
            case 1 -> viewActivePrescriptions();
            case 2 -> viewPrescriptionItems();
            case 3 -> dispensePrescriptionItem();
            case 4 -> createReplenishmentRequest();
            case 5 -> viewAllMedicines();
            case 6 -> viewNotifications();
            case 7 -> {
                System.out.println("Logging out...");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    /**
     * Displays all active prescriptions.
     */
    private void viewActivePrescriptions() {
        try {
            List<Prescription> activePrescriptions = PrescriptionController.getActivePrescriptions();
    
            if (activePrescriptions.isEmpty()) {
                System.out.println("No active prescriptions.");
            } else {
                System.out.println("Active prescriptions:");
                for (Prescription prescription : activePrescriptions) {
                    System.out.println("=============================");
                    System.out.println("Prescription ID: " + prescription.getId());
                    
                    // Get associated appointment details
                    Appointment appointment = AppointmentController.getAppt(prescription.getApptId());
                    if (appointment != null) {
                        System.out.println("\tAppointment ID: " + appointment.getId());
                        System.out.println("\tAppointment Date: " + DateFormat.formatWithTime(appointment.getApptDateTime()));
                        System.out.println("\tPrescribed by: " + appointment.getDoctorId());
                        System.out.println("\tService: " + appointment.getService());
                        System.out.println("\tDiagnosis: " + appointment.getDiagnosis());
                        System.out.println("\tConsultation Notes: " + appointment.getNotes());
                    } else {
                        System.out.println("\tNo associated appointment found.");
                    }
    
                    // Prescription items and details
                    // System.out.println("\tPrescription items:");
                    // List<Object> list = AppointmentController.getAppointmentOutcomesById(prescription.getApptId());
                    // HashMap<String, List<PrescriptionItem>> prescriptionItemList = (HashMap<String, List<PrescriptionItem>>) list.get(2);
                    // if (prescriptionItemList.isEmpty()) {
                    //     System.out.println("\tNo prescribed items.");
                    // } else {
                    //     for (PrescriptionItem item : prescriptionItemList.get(prescription.getId())) {
                    //         Medicine med = MedicineController.getMedicineById(item.getMedicineId());
                    //         if (med != null) {
                    //             System.out.println("\t\tMedicine Name: " + med.getMedicineName());
                    //             System.out.println("\t\tDosage: " + med.getDosage());
                    //             System.out.println("\t\tQuantity: " + item.getQuantity());
                    //             System.out.println("\t\tStatus: " + item.getStatus());
                    //             System.out.println("----------------------------------");
                    //         }
                    //     }
                    // }
                }

                System.out.println("=============================");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }
    
    /**
     * Displays the items of a specific prescription.
     */
    private void viewPrescriptionItems() {
        try {
            List<Prescription> activePrescriptions = PrescriptionController.getActivePrescriptions();
    
            if (activePrescriptions.isEmpty()) {
                System.out.println("No active prescriptions.");
            } else {
                System.out.println("Active prescriptions:");
                System.out.println("----------------------------");
                for (Prescription pre : activePrescriptions) {
                    System.out.println("Prescription ID: " + pre.getId());
                }
                System.out.println("=============================");
            }
            System.out.print("Enter prescription ID to view items: ");
            String prescriptionId = scanner.nextLine();
            Prescription prescription = PrescriptionController.getPrescriptionById(prescriptionId);
            
            if (prescription == null) {
                System.out.println("Invalid prescription ID.");
                return;
            }
            
            List<PrescriptionItem> prescriptionItems = PrescriptionItemController.getPrescriptionItems(prescription);
            if (prescriptionItems.isEmpty()) {
                System.out.println("No items found for this prescription.");
            } else {
                System.out.println("Prescription Items");
                for (PrescriptionItem item : prescriptionItems) {
                    System.out.println("=============================");
                    System.out.println(item.toString());
                }
                System.out.println("=============================");
            }
        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Dispenses prescription items for a specific prescription.
     */
    private void dispensePrescriptionItem() {
        try {
            System.out.print("Enter prescription ID to view pending items: ");
            String prescriptionId = scanner.nextLine();

            List<PrescriptionItem> pendingItems = PrescriptionItemController.getPendingPrescriptionItems(prescriptionId);
            if (pendingItems.isEmpty()) {
                System.out.println("No pending items for this prescription.");
                return;
            } else {
                System.out.println("Pending Prescription Items:");
                for (PrescriptionItem item : pendingItems) {
                    System.out.println("=============================");
                    System.out.println("Item ID: " + item.getId());
                    System.out.println("Medicine ID: " + item.getMedicineId());
                    System.out.println("Quantity: " + item.getQuantity());
                    System.out.println("Notes: " + item.getNotes());
                    System.out.println("Status: " + item.getStatus());
                }
                System.out.println("=============================");
            }
    
            System.out.print("Enter prescription item IDs to dispense (comma-separated): ");
            String input = scanner.nextLine();
            String[] itemIds = input.split(",");
    
            for (String id : itemIds) {
                String itemId = id.trim();
                try {
                    PrescriptionItem itemToDispense = PrescriptionItemController.getPrescriptionItemById(itemId);
                    Medicine med = MedicineController.getMedicineById(itemToDispense.getMedicineId());
    
                    if (itemToDispense == null || !itemToDispense.getPrescriptionId().equals(prescriptionId)) {
                        System.out.println("Invalid prescription item ID: " + itemId);
                        continue;
                    }

                    if (itemToDispense.getStatus() == PrescriptionItem.ItemStatus.DISPENSED) {
                        System.out.println("Prescription item " + itemId + " has already been dispensed.");
                        continue;
                    }
    
                    boolean isDispensed = PrescriptionItemController.dispensePrescriptionItem(itemToDispense);
    
                    if (isDispensed) {
                        System.out.println("Item dispensed successfully: " + itemId);
                        Prescription prescription = PrescriptionController.getPrescriptionById(itemToDispense.getPrescriptionId());
                        Appointment appointment = AppointmentController.getAppt(prescription.getApptId());
                        Invoice invoice = InvoiceController.getInvoiceByAppt(appointment.getId());
                        double itemCost = itemToDispense.getQuantity() * med.getUnitCost();
                        InvoiceController.incBalance(invoice, itemCost);
                    } else {
                        System.out.println("Failed to dispense item: " + itemId);
                    }

                    if (MedicineController.checkLowStock(med)) {
                        try {
                            notificationController.notifyObserver(session.getCurrentUser().getId(), "URGENT: Need to request replenishment to restock " + med.getMedicineName() + " !");
                            notificationController.notifyAdmins("URGENT: Medicine " + med.getMedicineName() + " is low in stock!");
                        } catch (NoUserLoggedInException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (EntityNotFoundException | InvalidInputException e) {
                    System.out.println("Error dispensing item with ID " + itemId + ": " + e.getMessage());
                }
            }
    
            try {
                PrescriptionController.updatePrescriptionStatusIfCompleted(prescriptionId);
            } catch (EntityNotFoundException | InvalidInputException e) {
                System.out.println("Error updating prescription status: " + e.getMessage());
            }
    
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Creates a replenishment request for medicine stock.
     */
    private void createReplenishmentRequest() {
        try {
            System.out.print("Enter medicine ID to replenish: ");
            String medicineId = scanner.nextLine();
            System.out.print("Enter quantity to replenish: ");
            int amount = scanner.nextInt();
            scanner.nextLine();
            
            String requestId = MedicineRequestController.createReplenishmentRequest((HospitalStaff) session.getCurrentUser(), medicineId, amount);
            System.out.println("Replenishment request created. Request ID: " + requestId);
        } catch (InvalidInputException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Displays all medicines and their details.
     */
    private void viewAllMedicines() {
        List<Medicine> medicines = MedicineController.getAllMedicines();

        if (medicines.isEmpty()) {
            System.out.println("No medicines available.");
        } else {
            System.out.println("Medicines");
            for (Medicine medicine : medicines) {
                System.out.println("=============================");
                System.out.println(medicine.toString());
            }
            System.out.println("=============================");
        }
        KeystrokeWait.waitForKeyPress();
        ClearConsole.clearConsole();
    }

    /**
     * Displays the notification history for the logged-in user.
     */
    public void viewNotifications() {
        try {
            List<List<String>> notiList = observer.getNotificationHistory();
            System.out.println("Notifications");
            System.out.println("=============");
            for (List<String> noti : notiList) {
                System.out.println("Message: " + noti.get(0) + " | Time sent: " + noti.get(1));
            }
            
            // Mark all notifications as read
            notificationController.markNotificationsRead(session.getCurrentUser().getId());
            
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        } catch (NoUserLoggedInException ex) {
        }
    }
}
