package boundary.user.patient;

import control.appointment.AppointmentController;
import control.billing.InvoiceController;
import control.medicine.MedicineController;
import control.notification.NotificationController;
import control.user.HospitalStaffController;
import control.user.PatientController;
import control.user.SessionManager;
import control.user.UnavailableDateController;
import control.user.UserController;
import entity.appointment.Appointment;
import entity.billing.Invoice;
import entity.medicine.Medicine;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import entity.user.Doctor;
import entity.user.HospitalStaff;
import entity.user.Patient;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import exception.user.NoUserLoggedInException;
import interfaces.boundary.IUserInterface;
import interfaces.observer.IObserver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import observer.NotificationObserver;
import utility.ClearConsole;
import utility.DateFormat;
import utility.DateTimeSelect;
import utility.InputHandler;
import utility.KeystrokeWait;

/**
 * The `P_HomeUI` class represents the user interface for a patient's home screen in a healthcare system.
 * It implements the `IUserInterface` interface and provides various methods for the patient to interact with their appointments,
 * view notifications, make payments, and more.
 *
 * This class handles operations such as scheduling, rescheduling, canceling appointments, viewing past appointments,
 * viewing and paying invoices, and viewing notifications. It interacts with controllers for managing appointments,
 * invoices, notifications, and user sessions.
 * 
 * It also provides methods to select dates, display doctor information, and handle payments for outstanding balances.
 * 
 * The patient interacts with this interface through various menu options that invoke these actions.
 */
public class P_HomeUI implements IUserInterface {
    private static final Scanner scanner = InputHandler.getInstance();
    private final SessionManager session;
    private final NotificationController notificationController;
    private IObserver observer;

    /**
     * Constructs a P_HomeUI instance to handle patient user interface actions.
     * Initializes the session and notification controller for the patient.
     * 
     * @param session the session manager to handle the current user session
     */
    public P_HomeUI(SessionManager session) {
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
     * Displays the main menu options for the patient interface.
     * Provides options like viewing medical records, updating information, scheduling appointments, etc.
     */
    @Override
    public void show_options() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n============== Patient Menu ==============");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. View and Pay Invoice");
            System.out.println("10. View Notifications");
            System.out.println("11. Logout");
            System.out.println("============================================");
    
            int choice = 0;
    
            while (true) {
                try {
                    System.out.print("Please select an option: ");
                    choice = scanner.nextInt();
                    scanner.nextLine();
    
                    if (choice >= 1 && choice <= 11) {
                        break;
                    } else {
                        System.out.println("Enter only a number between 1 and 11.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 11.");
                    scanner.nextLine();
                }
            }

            if (choice == 11) {
                exit = true;
            } else {
                ClearConsole.clearConsole();
                handle_option(choice);
            }
        }
    
        System.out.println("You have successfully logged out. Goodbye!");
    }

    /**
     * Handles the selected menu option by invoking the corresponding method.
     * Each case corresponds to a specific patient action, such as viewing medical records or scheduling an appointment.
     * 
     * @param choice the menu option selected by the patient
     */
    @Override
    public void handle_option(int choice) {
        switch (choice) {
            case 1 -> viewMedicalRecord();
            case 2 -> updatePersonalInformation();
            case 3 -> viewAvailableAppointmentSlots();
            case 4 -> scheduleAppointment();
            case 5 -> rescheduleAppointment();
            case 6 -> cancelAppointment();
            case 7 -> viewScheduledAppointments();
            case 8 -> viewPastAppointmentOutcomeRecords();
            case 9 -> viewAndPayInvoice();
            case 10 -> viewNotifications();
            case 11 -> {
                System.out.println("Logging out...");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    /**
     * Displays the patient's medical record, including past appointment outcomes.
     */
    private void viewMedicalRecord() {
        try {
            Patient patient = (Patient) session.getCurrentUser();

            List<Object> list = AppointmentController.getAppointmentOutcomes(patient);
            List<Appointment> pastAppts = (List<Appointment>) list.get(0);
            HashMap<String, Prescription> pastPrescriptions = (HashMap<String, Prescription>) list.get(1);
            HashMap<String, List<PrescriptionItem>> prescriptionItems = (HashMap<String, List<PrescriptionItem>>) list.get(2);

            System.out.println("==========================================");
            System.out.println("View Medical Record");
            System.out.println("==========================================");
            System.out.println(patient.toString());
            System.out.println("----------------------------------");
            System.out.println("Past Appointment Outcome Records:");
            System.out.println("----------------------------------");
            
            for (Appointment appt : pastAppts) {
                System.out.println("\tAppointment Date: " + appt.getApptDateTime());
                System.out.println("\tID: " + appt.getId());
                System.out.println("\tService: " + appt.getService());
                System.out.println("\tDiagnosis: " + appt.getDiagnosis());
                System.out.println("\tConsultation Notes: " + appt.getNotes());
                System.out.println("----------------------------------");
                System.out.println("Prescription items:");
                Prescription prescription = pastPrescriptions.get(appt.getId());
                if (prescription == null) {
                    System.out.println("No prescribed items.");
                } else {
                    for (PrescriptionItem item : prescriptionItems.get(prescription.getId())) {
                        Medicine med = null;
                        try {
                            med = MedicineController.getMedicineById(item.getMedicineId());
                        } catch (EntityNotFoundException ex) {
                        }
                        System.out.println("\tName: " + med.getMedicineName());
                        System.out.println("\tDosage: " + med.getDosage());
                        System.out.println("\tQuantity: " + item.getQuantity());
                        System.out.println("\tStatus: " + item.getStatus());
                        System.out.println("----------------------------------");
                    }
                }
            }
            System.out.println("==========================================");
        } catch (NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
                    e.printStackTrace();
                } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Provides the patient with options to update their personal information, such as email, contact number, or password.
     */
    private void updatePersonalInformation() {
        try {
            Patient patient = (Patient) session.getCurrentUser();
            
            while (true) {
                System.out.println("\nWhat would you like to update?");
                System.out.println("1. Update Email");
                System.out.println("2. Update Contact Number");
                System.out.println("3. Change password");
                System.out.println("4. Go Back");
                System.out.print("Enter your choice: ");
                
                String choice = scanner.nextLine();
                ClearConsole.clearConsole();
                
                switch (choice) {
                    case "1" -> updateEmail(patient);
                    case "2" -> updateContactNumber(patient);
                    case "3" -> changePassword(patient);
                    case "4" -> {
                        System.out.println("Returning to the previous menu.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows the patient to update their email address.
     * 
     * @param patient the current logged-in patient
     */
    private void updateEmail(Patient patient) {
        while (true) {
            try {
                System.out.println("Update email");
                System.out.println("==================");
                System.out.print("Enter new email (enter nothing to go back to previous menu): ");
                String email = scanner.nextLine();
                if (email.isEmpty()) {
                    return;
                }
    
                PatientController.changeEmail(patient, email);
                System.out.println("Email updated successfully.");
                break;
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }
    }

    /**
     * Allows the patient to update their contact number.
     * 
     * @param patient the current logged-in patient
     */
    private void updateContactNumber(Patient patient) {
        while (true) {
            try {
                System.out.println("Update contact number");
                System.out.println("==================");
                System.out.print("Enter new contact number (enter nothing to go back to previous menu): ");
                String contactNumber = scanner.nextLine();
                if (contactNumber.isEmpty()) {
                    return;
                }
    
                PatientController.changeContactNumber(patient, contactNumber);
                System.out.println("Contact number updated successfully.");
                break;
            } catch (InvalidInputException | EntityNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }
    }
    
    /**
     * Allows the patient to change their password.
     * 
     * @param patient the current logged-in patient
     */
    private void changePassword(Patient patient) {
        String newPassword;
        String confirmPassword;

        System.out.println("Change Password");
        System.out.println("==================");
        while (true) {
            try {
                System.out.print("Enter your new password (Enter nothing to go back to previous menu): ");
                newPassword = scanner.nextLine().trim();

                if (newPassword.isEmpty()) {
                    return;
                }

                if (newPassword.isEmpty() || newPassword.length() < 8) {
                    throw new InvalidInputException("Password must be at least 8 characters long.");
                }

                System.out.print("Confirm your new password: ");
                confirmPassword = scanner.nextLine().trim();

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("Passwords do not match. Please try again.");
                } else {
                    UserController.passwordChange(patient, newPassword);
                    System.out.println("Password changed successfully.");
                    KeystrokeWait.waitForKeyPress();
                    ClearConsole.clearConsole();
                    break;
                }
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }
    }
    
    /**
     * Displays the available appointment slots for the patient to choose from.
     */
    private void viewAvailableAppointmentSlots() {
        while (true) {
            try {
                System.out.println("==================");
            System.out.println("List of Doctors:");
            List<HospitalStaff> list = HospitalStaffController.getAllStaff()
                .stream()
                .filter(staff -> staff.getId().startsWith("D"))
                .sorted(Comparator.comparing(HospitalStaff::getId))
                .collect(Collectors.toList());
    
            if (list.isEmpty()) {
                System.out.println("No doctors available.");
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
                return;
            }
    
            for (HospitalStaff s : list) {
                System.out.println("==================");
                System.out.println("ID: " + s.getId());
                System.out.println("Name: " + s.getName());
            }
            System.out.println("==================");
                System.out.print("Enter doctor ID to view available slots (enter nothing to go back to previous menu): ");
                String doctorId = scanner.nextLine().trim();
    
                if (doctorId.isEmpty()) {
                    return;
                }
    
                Doctor doc = (Doctor) HospitalStaffController.findById(doctorId);
    
                LocalDate selectedDate;
                while (true) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    System.out.print("Enter the date (dd-MM-yyyy) to view available slots (today or future dates only, enter nothing to go back): ");
                    String dateInput = scanner.nextLine().trim();
                    
                    if (dateInput.isEmpty()) {
                        return;
                    }
    
                    try {
                        selectedDate = LocalDate.parse(dateInput, formatter);
                        if (selectedDate.isBefore(LocalDate.now())) {
                            System.out.println("Invalid date. The date must be today or a future date.");
                        } else {
                            break;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
                    }
                }
    
                List<LocalDateTime> availableSlots = AppointmentController.getAvailableTimeSlots(doc, selectedDate);
    
                if (availableSlots.isEmpty()) {
                    System.out.println("No available slots for this doctor on " + DateFormat.format(selectedDate) + ".");
                    return;
                }
    
                System.out.println("Available slots for Doctor " + doctorId + " on " + DateFormat.format(selectedDate) + ":");
                for (int i = 0; i < availableSlots.size(); i++) {
                    System.out.println((i + 1) + ". " +  DateFormat.formatWithTime(availableSlots.get(i)));
                }
                return;
    
            } catch (EntityNotFoundException e) {
                System.out.println("Error: Doctor not found. Please enter a valid doctor ID.");
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                KeystrokeWait.waitForKeyPress();
                ClearConsole.clearConsole();
            }
        }
    }
    
    /**
     * Schedules a new appointment with a doctor.
     * This method allows the patient to view a list of doctors, select one, 
     * choose an appointment time, and specify a service type for the appointment.
     */
    private void scheduleAppointment() {
        try {
            System.out.println("==================");
            System.out.println("List of Doctors:");
            List<HospitalStaff> list = HospitalStaffController.getAllStaff()
                .stream()
                .filter(staff -> staff.getId().startsWith("D"))
                .sorted(Comparator.comparing(HospitalStaff::getId))
                .collect(Collectors.toList());
    
            if (list.isEmpty()) {
                System.out.println("No doctors available.");
                return;
            }
    
            for (HospitalStaff s : list) {
                System.out.println("==================");
                System.out.println("ID: " + s.getId());
                System.out.println("Name: " + s.getName());
            }
            System.out.println("==================");
            System.out.print("Enter doctor ID to schedule an appointment (enter nothing to go back): ");
            String doctorId = scanner.nextLine().trim();
    
            if (doctorId.isEmpty()) {
                return;
            }
    
            Doctor doc = (Doctor) HospitalStaffController.findById(doctorId);
            LocalDateTime selectedSlot = DateTimeSelect.selectNewDateAndTime(doc);
    
            if (selectedSlot == null) {
                return;
            }
    
            System.out.println("\nChoose a service type:");
            Appointment.Service[] services = Appointment.Service.values();
            for (int i = 0; i < services.length; i++) {
                System.out.println((i + 1) + ". " + services[i].name());
            }
    
            int serviceChoice;
            while (true) {
                System.out.print("Enter the number corresponding to the service: ");
                try {
                    serviceChoice = Integer.parseInt(scanner.nextLine());
                    if (serviceChoice < 1 || serviceChoice > services.length) {
                        System.out.println("Invalid choice. Please enter a number between 1 and " + services.length);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
    
            Appointment.Service service = services[serviceChoice - 1];
            AppointmentController.scheduleAppointment(doc, (Patient) session.getCurrentUser(), service, selectedSlot);
            notificationController.notifyObserver(doctorId, "Patient " + session.getCurrentUser().getId() + " has scheduled an appointment with you on " + utility.DateFormat.formatWithTime(selectedSlot));
            System.out.println("Appointment scheduled successfully.");
            KeystrokeWait.waitForKeyPress();
        } catch (EntityNotFoundException | InvalidInputException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Reschedules an existing appointment for the patient.
     * This method allows the patient to view their current appointments, select one,
     * and choose a new date and time.
     */
    private void rescheduleAppointment() {
        try {
            List<Appointment> list = AppointmentController.getScheduledAppointments((Patient) session.getCurrentUser());
            list.sort(Comparator.comparing(Appointment::getId));
    
            if (list.isEmpty()) {
                System.out.println("You have no scheduled appointments.");
                return;
            }
    
            System.out.println("Your Scheduled Appointments:");
            for (Appointment appt : list) {
                System.out.println("=============================");
                System.out.println(appt.toString());
            }
            System.out.println("=============================");
    
            Appointment selectedAppointment = null;
            while (selectedAppointment == null) {
                System.out.print("Enter the appointment ID to reschedule (enter nothing to go back): ");
                String apptId = scanner.nextLine().trim();
    
                if (apptId.isEmpty()) {
                    return;
                }
    
                selectedAppointment = list.stream()
                    .filter(appt -> appt.getId().equals(apptId))
                    .findFirst()
                    .orElse(null);
    
                if (selectedAppointment == null) {
                    System.out.println("Invalid appointment ID. Please enter a valid appointment ID from the list.");
                }
            }
            Doctor doc = (Doctor) HospitalStaffController.findById(selectedAppointment.getDoctorId());
            LocalDateTime newDateTime = DateTimeSelect.selectNewDateAndTime(doc);
    
            if (newDateTime == null) {
                return;
            }
            
            UnavailableDateController.removeUnavailability(selectedAppointment.getApptDateTime());
            AppointmentController.rescheduleAppointment(selectedAppointment, newDateTime);
            System.out.println("Appointment rescheduled successfully.");
        } catch (InvalidInputException | EntityNotFoundException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Cancels an existing appointment.
     * This method allows the patient to select an appointment to cancel from a list of scheduled appointments.
     */
    private void cancelAppointment() {
        try {
            List<Appointment> list = AppointmentController.getScheduledAppointments((Patient) session.getCurrentUser());
            list.sort(Comparator.comparing(Appointment::getId));
    
            if (list.isEmpty()) {
                System.out.println("You have no scheduled appointments.");
                return;
            }
    
            System.out.println("Scheduled Appointments:");
            for (Appointment appt : list) {
                System.out.println("=============================");
                System.out.println(appt.toString());
            }
            System.out.println("=============================");
            
            Appointment selectedAppointment = null;
            while (selectedAppointment == null) {
                System.out.print("Enter appointment ID to cancel (enter nothing to go back): ");
                String apptId = scanner.nextLine().trim();
    
                if (apptId.isEmpty()) {
                    return;
                }
    
                selectedAppointment = list.stream()
                    .filter(appt -> appt.getId().equals(apptId))
                    .findFirst()
                    .orElse(null);
    
                if (selectedAppointment == null) {
                    System.out.println("Invalid appointment ID. Please enter a valid appointment ID from the list.");
                }
            }
    
            AppointmentController.cancelAppointment(selectedAppointment);
            UnavailableDateController.removeUnavailability(selectedAppointment.getApptDateTime());
            System.out.println("Appointment canceled successfully.");
        } catch (InvalidInputException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }
    
    /**
     * Displays a list of all scheduled appointments for the patient.
     */
    private void viewScheduledAppointments() {
        try {
            List<Appointment> list = AppointmentController.getScheduledAppointments((Patient) session.getCurrentUser());
            list.sort(Comparator.comparing(Appointment::getId));
            System.out.println("Scheduled Appointments:");
            System.out.println("-----------------------------");
            if (list.isEmpty()) {
                System.out.println("No scheduled appointments.");
            } else {
                for (Appointment appt : list) {
                    System.out.println(appt.toString());
                    System.out.println("-----------------------------");
                }
            }
            System.out.println("=============================");
        } catch (InvalidInputException | NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Displays the outcome records for past appointments, including prescriptions and prescribed items.
     */
    private void viewPastAppointmentOutcomeRecords() {
        try {
            System.out.println("==========================================");
            System.out.println("Past Appointment Outcome Records:");
            List<Object> list = AppointmentController.getAppointmentOutcomes((Patient) session.getCurrentUser());
            List<Appointment> pastAppts = (List<Appointment>) list.get(0);
            HashMap<String, Prescription> pastPrescriptions = (HashMap<String, Prescription>) list.get(1);
            HashMap<String, List<PrescriptionItem>> prescriptionItems = (HashMap<String, List<PrescriptionItem>>) list.get(2);
            for (Appointment appt : pastAppts) {
                System.out.println("==========================================");
                System.out.println("ID: " + appt.getId());
                System.out.println("Service: " + appt.getService());
                System.out.println("Diagnosis: " + appt.getDiagnosis());
                System.out.println("Consultation Notes: " + appt.getNotes());
                System.out.println("----------------------------------");
                System.out.println("Prescription items:");
                Prescription prescription = pastPrescriptions.get(appt.getId());
                if (prescription == null) {
                    System.out.println("No prescribed items.");
                } else {
                    for (PrescriptionItem item : prescriptionItems.get(prescription.getId())) {
                        Medicine med = MedicineController.getMedicineById(item.getMedicineId());
                        System.out.println("\tName: " + med.getMedicineName());
                        System.out.println("\tDosage: " + med.getDosage());
                        System.out.println("\tQuantity: " + item.getQuantity());
                        System.out.println("\tStatus: " + item.getStatus());
                        System.out.println("----------------------------------");
                    }
                }
            }
            System.out.println("==========================================");

        } catch (InvalidInputException | NoUserLoggedInException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
     * Displays and allows the user to pay outstanding invoices.
     * If the invoice is unpaid and has a balance, the patient can make a payment.
     */
    private void viewAndPayInvoice() {
        try {
            List<Invoice> invList = InvoiceController.getInvoiceByCustomer(session.getCurrentUser().getId());
    
            if (invList.isEmpty()) {
                System.out.println("You have no invoices.");
                return;
            }
    
            System.out.println("Your invoices:");
            System.out.println("==============");
            for (Invoice inv : invList) {
                System.out.println(inv);
                System.out.println("==============");
            }
    
            System.out.print("Enter the Invoice ID to select: ");
            String invoiceId = scanner.nextLine();
    
            Invoice selectedInvoice = invList.stream()
                                             .filter(inv -> inv.getId().equals(invoiceId))
                                             .findFirst()
                                             .orElse(null);
    
            if (selectedInvoice == null) {
                System.out.println("Invalid Invoice ID. Please select a valid invoice from the list.");
                return;
            }
    
            System.out.println("Selected Invoice Details:");
            System.out.println(selectedInvoice);

            if (selectedInvoice.getStatus() == Invoice.InvoiceStatus.PAID) {
            System.out.println("This invoice is already marked as PAID.");
            return;
        }
    
            if (selectedInvoice.getBalance() <= 0) {
                System.out.println("This invoice is already paid in full.");
                return;
            }
    
            System.out.println("Outstanding Balance: " + selectedInvoice.getBalance());
    
            double payment;
            while (true) {
                try {
                    System.out.print("Enter payment amount: ");
                    payment = Double.parseDouble(scanner.nextLine());
    
                    if (payment <= 0) {
                        System.out.println("Payment must be greater than zero. Please try again.");
                        continue;
                    }
    
                    if (payment > selectedInvoice.getBalance()) {
                        System.out.println("Payment exceeds the outstanding balance. Please enter a valid amount.");
                        continue;
                    }
    
                    // If the input is valid, break the loop
                    break;
    
                } catch (NumberFormatException e) {
                    System.out.println("Invalid payment amount. Please enter a valid number.");
                }
            }
    
            InvoiceController.payBalance(selectedInvoice, payment);
            System.out.println("Payment successful. Remaining balance: " + selectedInvoice.getBalance());
    
        } catch (NoUserLoggedInException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            KeystrokeWait.waitForKeyPress();
            ClearConsole.clearConsole();
        }
    }

    /**
 * Displays and marks all notifications as read.
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
