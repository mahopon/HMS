package control.user;

import entity.user.Patient;
import entity.user.User;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import repository.user.PatientRepository;

/**
 * The PatientController class provides methods for managing patient entities.
 * It allows for retrieving patient information and updating their contact details (email and phone number).
 * This class interacts with the PatientRepository to perform operations on patient records.
 */
public class PatientController {

    private static final PatientRepository repo = PatientRepository.getInstance();
    /**
     * Retrieves a patient by their ID.
     * 
     * @param patientId the ID of the patient to retrieve
     * @return the patient object corresponding to the provided ID
     * @throws EntityNotFoundException if the patient with the given ID is not found
     */
    public static Patient getById(String patientId) throws EntityNotFoundException {
        Patient patient = repo.get(patientId);
        if (patient == null) {
            throw new EntityNotFoundException("Patient", patientId);
        }
        return patient;
    }

    /**
     * Changes the email address of a user, validating the format.
     * If the email is invalid or the patient is not found, appropriate exceptions are thrown.
     *
     * @param user the user whose email is being changed
     * @param email the new email address to set
     * @throws InvalidInputException if the provided email format is invalid
     * @throws EntityNotFoundException if the patient corresponding to the user ID is not found
     */
    public static void changeEmail(User user, String email) throws InvalidInputException, EntityNotFoundException {
        // Validate email format
        if (email == null || !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new InvalidInputException("Invalid email format.");
        }
        // Find patient by user ID
        Patient patient = repo.findByField("id", user.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Patient", user.getId()));
        
        // Set new email and save to repository
        patient.setEmail(email);
        repo.save();
    }

    /**
     * Changes the contact number of a user, validating the format.
     * If the contact number is invalid or the patient is not found, appropriate exceptions are thrown.
     *
     * @param user the user whose contact number is being changed
     * @param contactNumber the new contact number to set
     * @throws InvalidInputException if the provided contact number format is invalid
     * @throws EntityNotFoundException if the patient corresponding to the user ID is not found
     */
    public static void changeContactNumber(User user, String contactNumber) throws InvalidInputException, EntityNotFoundException {
        // Validate contact number format (can include an optional "+" and must be between 10-15 digits)
        if (contactNumber == null || !contactNumber.matches("^[89][0-9]{7}$")) {
            throw new InvalidInputException("Invalid Singaporean mobile number format. It must start with 8 or 9 and have exactly 8 digits.");
        }
        // Find patient by user ID
        Patient patient = repo.findByField("id", user.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Patient", user.getId()));
        
        // Set new contact number and save to repository
        patient.setContactNumber(contactNumber);
        repo.save();
    }
}
