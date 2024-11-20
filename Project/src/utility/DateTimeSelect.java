package utility;

import control.appointment.AppointmentController;
import entity.user.Doctor;
import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Utility class for selecting a new date and time for appointments.
 * Provides functionality to check available slots for a doctor based on existing appointments
 * and unavailable dates.
 */
public class DateTimeSelect {

    /**
     * Allows a user to select a new date and time for a doctor.
     * Filters out unavailable slots based on the doctor's appointments and blocked dates.
     *
     * @param doctor The {@link Doctor} for whom the new date and time are to be selected.
     * @return The selected {@link LocalDateTime}, or null if the user opts to cancel the selection.
     */
    public static LocalDateTime selectNewDateAndTime(Doctor doctor) {
        List<LocalDateTime> availableSlots;
        LocalDate selectedDate;

        while (true) {
            try {

                // Loop until a valid date is entered
                while (true) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    System.out.print("Enter the date (dd-MM-yyyy) to view available slots (today or future dates only, enter nothing to go back): ");
                    String dateInput = InputHandler.getInstance().nextLine().trim();

                    if (dateInput.isEmpty()) {
                        return null; // User chooses to go back
                    }

                    try {
                        selectedDate = LocalDate.parse(dateInput, formatter);
                        if (selectedDate.isBefore(LocalDate.now())) {
                            System.out.println("Invalid date. The new date must be today or in the future.");
                        } else {
                            break;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
                    }
                }

                final LocalDate selectDate = selectedDate;

                // Retrieve the doctor's appointments and unavailable dates
                availableSlots = AppointmentController.getAvailableTimeSlots(doctor, selectDate);

                if (!availableSlots.isEmpty()) {
                    break; // Exit the loop if there are available slots
                }

                System.out.println("No available time slots for the selected date. Please choose another date.");
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Display available slots to the user
        System.out.println("Available slots for Doctor " + doctor.getId() + " on " + selectedDate + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + DateFormat.formatWithTime(availableSlots.get(i)));
        }

        int slotChoice;

        // Loop until a valid slot number is selected
        while (true) {
            System.out.print("Choose an available slot number: ");
            try {
                slotChoice = Integer.parseInt(InputHandler.getInstance().nextLine());
                if (slotChoice < 1 || slotChoice > availableSlots.size()) {
                    System.out.println("Invalid choice. Please choose a valid slot number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid slot number.");
            }
        }

        return availableSlots.get(slotChoice - 1);
    }
}
