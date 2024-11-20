package control.user;

import entity.user.HospitalStaff;
import entity.user.UnavailableDate;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import repository.user.UnavailableDateRepository;

/**
 * Controller for managing unavailable dates for hospital staff.
 */
public class UnavailableDateController {

    private static final UnavailableDateRepository repo = UnavailableDateRepository.getInstance();

    /**
     * Adds an unavailable date for the specified hospital staff.
     *
     * @param staff The hospital staff for whom the date is being added.
     * @param date  The date to be marked as unavailable.
     * @return A success message if the date is added successfully.
     * @throws InvalidInputException If the staff or date is null, or if the date is already marked as unavailable.
     */
    public static String addUnavailability(HospitalStaff staff, LocalDateTime date) throws InvalidInputException {
        if (staff == null) {
            throw new InvalidInputException("Hospital staff cannot be null.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }
        if (isDateUnavailable(staff.getId(), date)) {
            throw new InvalidInputException("The specified date is already marked as unavailable.");
        }

        String newId = repo.getNextClassId();
        repo.add(new UnavailableDate(newId, staff.getId(), date));
        repo.save();
        return "Added successfully.";
    }

    /**
     * Removes an unavailable date for the specified hospital staff.
     *
     * @param unavailableDate The unavailable date to be removed.
     * @return True if the date is removed successfully.
     * @throws EntityNotFoundException If the unavailable date is not found.
     * @throws InvalidInputException   If the unavailable date is null.
     */
    public static Boolean removeUnavailability(LocalDateTime dateTime) throws EntityNotFoundException, InvalidInputException {
        if (dateTime == null) {
            throw new InvalidInputException("Date and time cannot be null.");
        }
    
        // Search for the unavailable date in the repository
        UnavailableDate unavailableDate = repo.findByField("date", dateTime).stream()
                .filter(date -> date.getDate().isEqual(dateTime))
                .findFirst()
                .orElse(null);
    
        if (unavailableDate == null) {
            throw new EntityNotFoundException("No unavailable date found for the specified date and time.");
        }
    
        // Remove the unavailable date
        repo.remove(unavailableDate);
        repo.save(); // Save changes to the file
        return true;
    }
    

    /**
     * Retrieves a list of unavailable dates for the specified hospital staff.
     *
     * @param staff The hospital staff for whom the unavailable dates are being retrieved.
     * @return A list of unavailable dates for the staff.
     * @throws InvalidInputException If the staff is null.
     */
    public static List<UnavailableDate> getUnavailableDates(HospitalStaff staff) throws InvalidInputException {
        if (staff == null) {
            throw new InvalidInputException("Hospital staff cannot be null.");
        }
        return repo.findByField("staffId", staff.getId());
    }

    /**
     * Retrieves all unavailable slots (dates and times) for the specified doctor.
     *
     * @param staff The hospital staff for whom the unavailable slots are being retrieved.
     * @return A list of unavailable slots (dates and times) for the staff.
     * @throws InvalidInputException If the staff is null.
     */
    public static List<LocalDateTime> getUnavailableSlotsByDate(HospitalStaff staff, LocalDate date) throws InvalidInputException {
        if (staff == null) {
            throw new InvalidInputException("Hospital staff cannot be null.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }
    
        // Retrieve all unavailable dates for the staff, then filter by the specified date.
        return repo.findByField("staffId", staff.getId()).stream()
                .map(UnavailableDate::getDate)
                .filter(dateTime -> dateTime.toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of unavailable dates for the specified hospital staff on a given date.
     *
     * @param staff The hospital staff for whom the unavailable dates are being retrieved.
     * @param date  The date to check for unavailability.
     * @return A list of unavailable dates matching the given date.
     * @throws InvalidInputException If the staff or date is null.
     */
    public static List<UnavailableDate> getUnavailableDatesByDate(HospitalStaff staff, LocalDate date) throws InvalidInputException {
        if (staff == null) {
            throw new InvalidInputException("Hospital staff cannot be null.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }

        return repo.findByField("staffId", staff.getId()).stream()
                .filter(unavailableDate -> unavailableDate.getDate().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a specific date is marked as unavailable for the given staff ID.
     *
     * @param staffId The ID of the hospital staff.
     * @param date    The date to check for unavailability.
     * @return True if the date is unavailable, false otherwise.
     * @throws InvalidInputException If the staff ID is null or empty, or if the date is null.
     */
    public static boolean isDateUnavailable(String staffId, LocalDateTime date) throws InvalidInputException {
        if (staffId == null || staffId.isEmpty()) {
            throw new InvalidInputException("Staff ID cannot be null or empty.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }

        List<UnavailableDate> unavailableDates = repo.findByField("staffId", staffId);
        return unavailableDates.stream().anyMatch(unavailable -> unavailable.getDate().isEqual(date));
    }
}
