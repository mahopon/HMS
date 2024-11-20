package control.user;

import entity.user.HospitalStaff;
import entity.user.StaffFactory;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import repository.user.StaffRepository;

/**
 * The HospitalStaffController class provides methods for managing hospital staff entities.
 * It allows for adding, removing, finding, and listing staff members. It interacts with 
 * the StaffRepository to perform data operations on hospital staff records.
 */
public class HospitalStaffController {
    private static final StaffRepository repo = StaffRepository.getInstance();
    /**
     * Retrieves all hospital staff from the repository.
     *
     * @return a list of all hospital staff
     */
    public static List<HospitalStaff> getAllStaff() {
        return repo.toList();
    }

    /**
     * Adds a new staff member to the repository after validating the input parameters.
     * If the input is invalid, an InvalidInputException is thrown.
     *
     * @param name the name of the staff member
     * @param gender the gender of the staff member (male or female)
     * @param role the role of the staff member (Doctor, Pharmacist, etc.)
     * @param dob the date of birth of the staff member (must be between 18 and 100)
     * @return true if the staff member was successfully added, false otherwise
     * @throws InvalidInputException if any of the input parameters are invalid
     */
    public static HospitalStaff addStaff(String name, String gender, HospitalStaff.Role role, LocalDate dob) throws InvalidInputException {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be null or empty.");
        }
        if (name.length() < 3) {
            throw new InvalidInputException("Name must be at least 3 characters long.");
        }
    
        if (gender == null || (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female"))) {
            throw new InvalidInputException("Invalid gender. Must be 'male' or 'female'.");
        }
    
        if (role == null) {
            throw new InvalidInputException("Role cannot be null.");
        }
    
        if (dob == null) {
            throw new InvalidInputException("Date of birth cannot be null.");
        }
    
        LocalDate today = LocalDate.now();
        if (!dob.isBefore(today)) {
            throw new InvalidInputException("Date of birth must be before today.");
        }
    
        int age = Period.between(dob, today).getYears();
        if (age < 18 || age > 100) {
            throw new InvalidInputException("Age derived from date of birth must be between 18 and 100 years.");
        }
    
        try {
            HospitalStaff newStaff = StaffFactory.createStaffByRole(role);
            newStaff.setIsPatient(false);
            newStaff.changePassword("password");
            newStaff.setName(name);
            newStaff.setId(repo.getNextId(role));
            newStaff.setGender(gender);
            newStaff.setRole(role);
            newStaff.setDob(dob);
    
            repo.add(newStaff);
            repo.save();
            return newStaff;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Updates the details of an existing staff member.
     * Ensures that all input parameters are valid before updating the staff record.
     *
     * @param staff the staff member to update
     * @param name the new name of the staff member
     * @param gender the new gender of the staff member
     * @param role the new role of the staff member
     * @param dob the new date of birth of the staff member
     * @throws InvalidInputException if any of the input parameters are invalid
     */
    public static void updateStaff(HospitalStaff staff, String name, String gender, HospitalStaff.Role role, LocalDate dob) throws InvalidInputException {
        if (name == null || name.trim().length() < 3) {
            throw new InvalidInputException("Name must be at least 3 characters long.");
        }

        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            throw new InvalidInputException("Gender must be 'Male' or 'Female'.");
        }

        if (role == null) {
            throw new InvalidInputException("Role cannot be null. Please select a valid role.");
        }

        if (dob == null) {
            throw new InvalidInputException("Date of birth cannot be null.");
        }

        LocalDate today = LocalDate.now();
        if (!dob.isBefore(today)) {
            throw new InvalidInputException("Date of birth must be before today.");
        }

        int age = Period.between(dob, today).getYears();
        if (age < 18 || age > 100) {
            throw new InvalidInputException("Age derived from date of birth must be between 18 and 100 years.");
        }

        staff.setName(name);
        staff.setGender(gender);
        staff.setRole(role);
        staff.setDob(dob);
        repo.save();
    }


    /**
     * Removes a staff member from the repository.
     * If the staff member does not exist in the repository, an EntityNotFoundException is thrown.
     *
     * @param staff the staff member to remove
     * @return true if the staff member was successfully removed, false otherwise
     * @throws EntityNotFoundException if the staff member cannot be found
     */
    public static void removeStaff(HospitalStaff staff) throws EntityNotFoundException {
        if (staff == null) {
            throw new EntityNotFoundException("Staff", "null");
        }
        if (repo.get(staff.getId()) == null) {
            throw new EntityNotFoundException("Staff", staff.getId());
        }
        repo.remove(staff);
        repo.save();
    }

    /**
     * Finds a staff member by their ID.
     * If the staff member does not exist, an EntityNotFoundException is thrown.
     *
     * @param id the ID of the staff member
     * @return the staff member with the specified ID
     * @throws EntityNotFoundException if the staff member cannot be found
     */
    public static HospitalStaff findById(String id) throws EntityNotFoundException {
        HospitalStaff staff = repo.get(id);
        if (staff == null) {
            throw new EntityNotFoundException("Staff", id);
        }
        return staff;
    }

    /**
     * Finds staff members by a specific field and its value.
     * If the field or value is invalid, an InvalidInputException is thrown.
     *
     * @param field the name of the field to search by
     * @param value the value to search for
     * @return a list of staff members matching the field and value
     * @throws InvalidInputException if the field or value is invalid
     */
    public static List<HospitalStaff> findByField(String field, String value) throws InvalidInputException {
        if (field == null || field.isEmpty() || value == null || value.isEmpty()) {
            throw new InvalidInputException("Field and value cannot be null or empty.");
        }
        return repo.findByField(field, value);
    }
}
