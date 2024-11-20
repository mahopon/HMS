package boundary.user.administrator;

import control.appointment.AppointmentController;
import control.medicine.MedicineController;
import control.notification.NotificationController;
import control.request.MedicineRequestController;
import control.user.HospitalStaffController;
import control.user.SessionManager;
import entity.appointment.Appointment;
import entity.medicine.Medicine;
import entity.request.MedicineRequest;
import entity.user.HospitalStaff;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import exception.user.NoUserLoggedInException;
import interfaces.boundary.IUserInterface;
import interfaces.observer.IObserver;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import observer.NotificationObserver;
import utility.ClearConsole;
import utility.InputHandler;
import utility.KeystrokeWait;
/**
 * The Administrator Home User Interface (A_HomeUI) class provides an interface for administrators to
 * carry out their available functions.
 */
public class A_HomeUI implements IUserInterface {
    private static final Scanner scanner = InputHandler.getInstance();
    private final SessionManager session;
    private final NotificationController notificationController;
    private IObserver observer;
    /**
     * Constructs the Administrator Home User Interface.
     * 
     * @param session The current session manager for handling user login information.
     */
    public A_HomeUI(SessionManager session) {
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
     * Displays the administrator menu and handles menu options.
     */
    @Override
    public void show_options() {
        boolean exit = false;
    
        while (!exit) {
            System.out.println("\n=== Administrator Menu ===");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. View Notifications");
            System.out.println("6. Logout");
            System.out.println("====================");
    
            int choice = 0;
    
            while (true) {
                try {
                    System.out.print("Please select an option: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 6) break;
                    System.out.println("Enter only a number between 1 and 6.");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 6.");
                    scanner.nextLine();
                }
            }
            if (choice == 6) exit = true;
            else {
                ClearConsole.clearConsole();
                handle_option(choice);
            }
        }
        System.out.println("You have successfully logged out. Goodbye!");
    }

    /**
     * Handles the selected menu option.
     * 
     * @param choice The choice made by the user from the administrator menu.
     */
    @Override
    public void handle_option(int choice) {
        switch (choice) {
            case 1 -> viewAndManageHospitalStaff();
            case 2 -> viewAppointmentsDetails();
            case 3 -> medicationInventoryOptions();
            case 4 -> manageReplenishmentRequests();
            case 5 -> viewNotifications();
            case 6 -> {
                System.out.println("Logging out...");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    /**
     * Displays and manages hospital staff.
     */
    private void viewAndManageHospitalStaff() {
        boolean exit = false;

        while (!exit) {
            System.out.println("View and Manage Hospital Staff");
            System.out.println("====================================");
            System.out.println("1. View/update/delete Hospital Staff");
            System.out.println("2. Add Hospital Staff");
            System.out.println("3. Return to Previous Menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            ClearConsole.clearConsole();
            switch (choice) {
                case 1 -> viewHospitalStaff();
                case 2 ->  addHospitalStaff();
                case 3 -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid choice. Please select an option from 1 to 3.");
                }
            }
        }
    }

    /**
     * Displays the list of hospital staff and provides options to update or delete staff.
     */
    private void viewHospitalStaff() {
        List<HospitalStaff> list = HospitalStaffController.getAllStaff()
                                .stream()
                                .filter(staff -> staff.getRole() == HospitalStaff.Role.DOCTOR || staff.getRole() == HospitalStaff.Role.PHARMACIST)
                                .collect(Collectors.toCollection(ArrayList::new));

        list.sort(Comparator.comparing(HospitalStaff::getId));
        System.out.println("View Hospital Staff");
    
        if (list.isEmpty()) {
            System.out.println("No hospital staff found.");
            return;
        }
    
        for (HospitalStaff staff : list) {
            System.out.println("==================");
            System.out.println(staff.toString());
        }
        System.out.println("==================");
    
        try {
            HospitalStaff targetStaff = null;
            while (targetStaff == null) {
                System.out.print("Enter hospital staff ID to update or delete (enter nothing to go back to previous menu): ");
                String staffId = scanner.nextLine().trim();
        
                if (staffId.isEmpty()) {
                    ClearConsole.clearConsole();
                    return;
                }
        
                targetStaff = list.stream()
                    .filter(staff -> staff.getId().equals(staffId))
                    .findFirst()
                    .orElse(null);
        
                if (targetStaff == null) {
                    System.out.println("Error: Hospital staff not found. Please enter a valid ID.");
                }
            }
        
            while (true) {
                System.out.println("Selected Staff: " + targetStaff.getId());
                System.out.println("1. Update Staff");
                System.out.println("2. Delete Staff");
                System.out.println("3. Return to Previous Menu");
                System.out.print("Enter your choice: ");
        
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> {
                        ClearConsole.clearConsole();
                        updateHospitalStaff(targetStaff);
                        KeystrokeWait.waitForKeyPress();
                        ClearConsole.clearConsole();
                        return;
                    }
                    case "2" -> {
                        ClearConsole.clearConsole();
                        HospitalStaffController.removeStaff(targetStaff);
                        System.out.println("Hospital staff deleted successfully.");
                        KeystrokeWait.waitForKeyPress();
                        ClearConsole.clearConsole();
                        return;
                    }
                    case "3" -> {
                        ClearConsole.clearConsole();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Updates the details of a hospital staff member.
     * 
     * @param staff The hospital staff member to update.
     */
    private void updateHospitalStaff(HospitalStaff staff) {
        System.out.println("Update Hospital Staff");
        System.out.println("=====================");
        System.out.println(staff.toString());
    
        String name;
        while (true) {
            System.out.print("Enter new name (press Enter to keep current: " + staff.getName() + "): ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = staff.getName();
                break;
            }
            if (name.length() > 2) {
                break;
            } else {
                System.out.println("Error: Name must be at least 3 characters long.");
            }
        }
    
        String gender;
        while (true) {
            System.out.print("Select gender (1 for Male, 2 for Female, press Enter to keep current: " + staff.getGender() + "): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                gender = staff.getGender();
                break;
            }
            if (input.equals("1")) {
                gender = "Male";
                break;
            } else if (input.equals("2")) {
                gender = "Female";
                break;
            } else {
                System.out.println("Error: Invalid choice. Please enter 1 for Male or 2 for Female.");
            }
        }
    
        HospitalStaff.Role role = staff.getRole();
        while (true) {
            System.out.println("Select role (1 for DOCTOR, 2 for PHARMACIST, press Enter to keep current: " + staff.getRole() + "): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                break;
            }
            if (input.equals("1")) {
                role = HospitalStaff.Role.DOCTOR;
                break;
            } else if (input.equals("2")) {
                role = HospitalStaff.Role.PHARMACIST;
                break;
            } else {
                System.out.println("Error: Invalid choice. Please enter 1 or 2.");
            }
        }
    
        LocalDate dob = staff.getDob();
        while (true) {
            System.out.print("Enter new date of birth (dd-MM-yyyy, press Enter to keep current: " + staff.getDob() + "): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                break;
            }
            try {
                dob = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please enter date in dd-MM-yyyy format.");
            }
        }
    
        try {
            HospitalStaffController.updateStaff(staff, name, gender, role, dob);
            System.out.println("Hospital staff updated successfully.");
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        KeystrokeWait.waitForKeyPress();
        ClearConsole.clearConsole();
    }

    
    /**
     * Adds a new hospital staff member.
     */
    private void addHospitalStaff() {
        System.out.println("Add Hospital Staff");
        System.out.println("Enter any blank input to go back to previous menu");
        System.out.println("====================================");

        String name;
        while (true) {
            System.out.print("Enter staff name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                ClearConsole.clearConsole();
                return;
            }
            if (name.length() > 2) {
                break;
            } else {
                System.out.println("Error: Name must be at least 3 characters long.");
            }
        }

        String gender;
        while (true) {
            System.out.print("Select gender (1 for Male, 2 for Female): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                ClearConsole.clearConsole();
                return;
            }
            if (input.equals("1")) {
                gender = "Male";
                break;
            } else if (input.equals("2")) {
                gender = "Female";
                break;
            } else {
                System.out.println("Error: Invalid choice. Please enter 1 for Male or 2 for Female.");
            }
        }

        HospitalStaff.Role role = null;
        boolean exit = false;
        while (!exit) {
            System.out.println("Select staff role:");
            System.out.println("1. DOCTOR");
            System.out.println("2. PHARMACIST");
            System.out.print("Enter your choice: ");
            String roleInput = scanner.nextLine().trim();

            if (roleInput.isEmpty()) {
                ClearConsole.clearConsole();
                return;
            }

            switch (roleInput) {
                case "1" -> {
                    role = HospitalStaff.Role.DOCTOR;
                    exit = true;
                }
                case "2" -> {
                    role = HospitalStaff.Role.PHARMACIST;
                    exit = true;
                }
                default -> System.out.println("Error: Invalid choice. Please enter 1 or 2");
            }
        }

        LocalDate dob;
        while (true) {
            System.out.print("Enter date of birth (dd-MM-yyyy): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                ClearConsole.clearConsole();
                return;
            }
            try {
                dob = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please enter date in dd-MM-yyyy format.");
            }
        }

        try {
            HospitalStaffController.addStaff(name, gender, role, dob);
            System.out.println("Hospital staff added successfully.");
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }

        KeystrokeWait.waitForKeyPress();
        ClearConsole.clearConsole();
    }

    /**
     * Displays the list of appointments.
     */
    private void viewAppointmentsDetails() {
        List<Appointment> list = AppointmentController.getAllAppts().stream().collect(Collectors.toCollection(ArrayList::new));
        list.sort(Comparator.comparing(Appointment::getId));

        System.out.println("Appointments");
        for (Appointment appt : list) {
            System.out.println("==================");
            System.out.println(appt.toString());
        }
        System.out.println("==================");
        KeystrokeWait.waitForKeyPress();
        ClearConsole.clearConsole();
    }

    /**
     * Provides options for managing the medication inventory.
     */
    private void medicationInventoryOptions() {
        int choice = -1;
        while (choice != 3) {
            System.out.println("Select an option:");
            System.out.println("1. Add Medicine");
            System.out.println("2. View and Manage Medication");
            System.out.println("3. Go Back to Previous Menu");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addMedication();
                    case 2 -> manageMedicationInventory();
                    case 3 -> {
                        ClearConsole.clearConsole();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
                ClearConsole.clearConsole();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Adds a new medicine to the inventory.
     */
    private void addMedication() {
        System.out.println("Add Medicine");
        System.out.println("==================");
    
        String name;
        do {
            System.out.print("Enter medicine name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Medicine name cannot be empty. Please try again.");
            }
        } while (name.isEmpty());
    
        int stock;
        while (true) {
            System.out.print("Enter current stock (non-negative integer): ");
            try {
                stock = Integer.parseInt(scanner.nextLine());
                if (stock < 0) {
                    System.out.println("Stock cannot be negative. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    
        int quantity;
        while (true) {
            System.out.print("Enter quantity per dose (positive integer): ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0) {
                    System.out.println("Quantity per dose must be greater than 0. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    
        double unitCost;
        while (true) {
            System.out.print("Enter unit cost (positive number): ");
            try {
                unitCost = Double.parseDouble(scanner.nextLine());
                if (unitCost <= 0) {
                    System.out.println("Unit cost must be greater than 0. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    
        double dosage;
        while (true) {
            System.out.print("Enter dosage (positive number): ");
            try {
                dosage = Double.parseDouble(scanner.nextLine());
                if (dosage <= 0) {
                    System.out.println("Dosage must be greater than 0. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    
        int lowStockThreshold;
        while (true) {
            System.out.print("Enter low stock threshold (non-negative integer): ");
            try {
                lowStockThreshold = Integer.parseInt(scanner.nextLine());
                if (lowStockThreshold < 0) {
                    System.out.println("Low stock threshold cannot be negative. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    
        try {
            MedicineController.addMedicine(name, stock, quantity, unitCost, dosage, lowStockThreshold);
            System.out.println("Medicine added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add medicine: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Manages the list of medications in the inventory.
     */
    private void manageMedicationInventory() {
        List<Medicine> medList = MedicineController.getAllMedicines();
        if (medList.isEmpty()) {
            System.out.println("No medicines.");
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
            return;
        }

        System.out.println("Medicine Inventory");
        for (Medicine med : medList) {
            System.out.println("==================");
            System.out.println(med.toString());
        }
        System.out.println("==================");
        System.out.println("Enter medicine ID to manage (enter nothing to go back to previous menu): ");
        try {
            String medId = scanner.nextLine();

            if (medId.isEmpty()) {
                ClearConsole.clearConsole();
                return;
            }
            Medicine med = MedicineController.getMedicineById(medId);
            ClearConsole.clearConsole();
            manageMedicationInventory(med);

        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Provides options to manage a specific medication in the inventory.
     * 
     * @param med The medication to manage.
     */
    private void manageMedicationInventory(Medicine med) {
        boolean exit = false;
    
        while (!exit) {
            System.out.println("Manage Medication Inventory");
            System.out.println("===========================");
            System.out.println(med.toString());
            System.out.println("===========================");
            System.out.println("1. Increase Medicine Stock");
            System.out.println("2. Decrease Medicine Stock");
            System.out.println("3. Update Medicine Stock");
            System.out.println("4. Update low stock threshold");
            System.out.println("5. Delete medicine");
            System.out.println("6. Return to Previous Menu");
    
            int choice = 0;
    
            while (true) {
                try {
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 6) break;
                    System.out.println("Enter only a number between 1 and 6.");
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 6.");
                    scanner.nextLine();
                }
            }
    
            ClearConsole.clearConsole();
    
            switch (choice) {
                case 1 -> increaseMedicineStock(med);
                case 2 -> decreaseMedicineStock(med);
                case 3 -> updateMedicineStock(med);
                case 4 -> updateMedicineThreshold(med);
                case 5 -> deleteMedicine(med);
                case 6 -> exit = true;
                default -> System.out.println("Invalid choice. Please select an option from 1 to 4.");
            }
        }
    }

    /**
     * Deletes a specific medication from the inventory.
     * 
     * @param med The medication to delete.
     */
    private void deleteMedicine(Medicine med) {
        try {
            MedicineController.removeMedicine(med);
            System.out.println("Removed all stock rom inventory.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }
    
    /**
     * Updates the low stock threshold for a specific medication.
     * 
     * @param medicine The medication whose threshold needs to be updated.
     */
    private void updateMedicineThreshold(Medicine medicine) {
        try {
            int newThreshold;
            while (true) {
                System.out.print("Enter the new threshold value (0 to go back to previous menu): ");
                try {
                    newThreshold = scanner.nextInt();
                    scanner.nextLine();
                    if (newThreshold == 0) {
                        System.out.print("Going back to previous menu... ");
                        return;
                    } else if (newThreshold > 0) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input. Please enter a positive number.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
    
            if (MedicineController.setLowStockThreshold(medicine, newThreshold)) {
                System.out.printf("Updated threshold for medicine ID %s to %d units.%n", medicine.getId(), newThreshold);
            }
        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }
    
    /**
     * Increases the stock quantity of a specific medication.
     * 
     * @param medicine The medication to update.
     */
    private void increaseMedicineStock(Medicine medicine) {
        try {
            int quantity;
            while (true) {
                System.out.print("Enter the quantity to increase (0 to go back to previous menu): ");
                try {
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    if (quantity == 0) {
                        System.out.print("Going back to previous menu... ");
                        return;
                    } else if (quantity > 0) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input. Please enter a positive number.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            
            if (MedicineController.incMedStock(medicine, quantity)) {
                System.out.printf("Increased stock for medicine ID %s by %d units.%n", medicine.getId(), quantity);
            }
        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Decreases the stock quantity of a specific medication.
     * 
     * @param medicine The medication to update.
     */
    private void decreaseMedicineStock(Medicine medicine) {
        try {
            int quantity;
            while (true) {
                System.out.print("Enter the quantity to decrease (0 to go back to previous menu): ");
                try {
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    if (quantity == 0) {
                        System.out.print("Going back to previous menu... ");
                        return;
                    } else if (quantity > 0) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input. Please enter a positive number.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                } 
            }
            if (MedicineController.decMedStock(medicine, quantity)) {
                System.out.printf("Decreased stock for medicine ID %s by %d units.%n", medicine.getId(), quantity);
            }

        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Updates the stock quantity of a specific medication.
     * 
     * @param medicine The medication to update.
     */
    private void updateMedicineStock(Medicine medicine) {
        try {
            int newQuantity;
            while (true) {
                System.out.print("Enter the new stock quantity (-1 to go back to previous menu): ");
                try {
                    newQuantity = scanner.nextInt();
                    scanner.nextLine();
                    if (newQuantity == -1) {
                        System.out.print("Going back to previous menu... ");
                        return;
                    } else if (newQuantity >= 0) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input. Please enter a number greater than or equal to 0.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            if (MedicineController.updateMedStock(medicine, newQuantity)) {
                System.out.printf("Updated stock for medicine ID %s to %d units.%n", medicine.getId(), newQuantity);
            }
        } catch (InvalidInputException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Manages replenishment requests for medications.
     */
    private void manageReplenishmentRequests() {
        while (true) {
            try {
                List<MedicineRequest> reqList = MedicineRequestController.getPendingRequests();
        
                if (reqList.isEmpty()) {
                    System.out.println("No pending replenishment requests.");
                    return;
                }
        
                System.out.println("Pending Replenishment Requests:");
                for (MedicineRequest req : reqList) {
                    System.out.println("=============================");
                    Medicine med = MedicineController.getMedicineById(req.getMedicineId());
                    System.out.println("Request ID: " + req.getId());
                    System.out.println("Medicine ID: " + req.getMedicineId());
                    System.out.println("Medicine Name: " + med.getMedicineName());
                    System.out.println("Requestor ID: " + req.getRequestorId());
                    System.out.println("Current Quantity: " + med.getStockQuantity());
                    System.out.println("Requested Quantity: " + req.getQuantity());
                }
                System.out.println("=============================");
        
                MedicineRequest selectedRequest = null;
                while (selectedRequest == null) {
                    System.out.print("Enter a valid request ID to process (Enter nothing to go back to previous menu): ");
                    String choice = scanner.nextLine();
                    if (choice.isEmpty()) {
                        ClearConsole.clearConsole();
                        return;
                    }
        
                    selectedRequest = reqList.stream()
                        .filter(req -> req.getId().equals(choice))
                        .findFirst()
                        .orElse(null);
        
                    if (selectedRequest == null) {
                        System.out.println("Invalid request ID. Please try again.");
                    }
                }
                ClearConsole.clearConsole();
                processReplenishmentRequest(selectedRequest);
            } catch (InvalidInputException | EntityNotFoundException  e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }
    }

    /**
     * Processes a specific replenishment request.
     * 
     * @param selectedRequest The replenishment request to process.
     */
    private void processReplenishmentRequest(MedicineRequest selectedRequest) {
        try {
            Medicine selectedMedicine = MedicineController.getMedicineById(selectedRequest.getMedicineId());
            boolean actionExit = false;
    
            while (!actionExit) {
                System.out.println("Processing Replenishment Request:");
                System.out.println("=============================");
                System.out.println("Request ID: " + selectedRequest.getId());
                System.out.println("Medicine ID: " + selectedRequest.getMedicineId());
                System.out.println("Requestor ID: " + selectedRequest.getRequestorId());
                System.out.println("Medicine Name: " + selectedMedicine.getMedicineName());
                System.out.println("Current Quantity: " + selectedMedicine.getStockQuantity());
                System.out.println("Requested Quantity: " + selectedRequest.getQuantity());
                System.out.println("=============================");
                System.out.println("1. Approve request");
                System.out.println("2. Reject request");
                System.out.println("3. Go back to the previous menu");
                System.out.print("Enter your choice: ");
    
                int actionChoice = scanner.nextInt();
                scanner.nextLine();
                ClearConsole.clearConsole();
    
                switch (actionChoice) {
                    case 1 -> {
                        MedicineRequestController.approveReplenishmentRequest((HospitalStaff) session.getCurrentUser(), selectedRequest);
                        System.out.println("Request approved. Stock has been updated.");
                        KeystrokeWait.waitForKeyPress();
                        actionExit = true;
                    }
                    case 2 -> {
                        MedicineRequestController.rejectReplenishmentRequest((HospitalStaff) session.getCurrentUser(), selectedRequest);
                        System.out.println("Request rejected.");
                        KeystrokeWait.waitForKeyPress();
                        actionExit = true;
                    }
                    case 3 -> actionExit = true;
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
                ClearConsole.clearConsole();
            }
        } catch (InvalidInputException | EntityNotFoundException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Displays notifications for the administrator and marks them as read.
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
