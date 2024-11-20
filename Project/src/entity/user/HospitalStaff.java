package entity.user;

import java.time.LocalDate;

/**
* The HospitalStaff class represents a staff member of the hospital, who is a user of the system with a specific role (e.g., Doctor, Pharmacist, Administrator).
* This class extends the User class and adds additional properties like role and age.
*/
public class HospitalStaff extends User{
    
    /**
     * Enumeration representing the possible roles of a hospital staff member.
     * The roles can be DOCTOR, PHARMACIST, or ADMINISTRATOR.
     */
    public enum Role {DOCTOR, PHARMACIST, ADMINISTRATOR}

    private Role role;

    /**
     * Default constructor for the HospitalStaff class.
     * This calls the default constructor of the superclass (User).
     */
    public HospitalStaff() {
        super();
    }
    /**
     * Constructor to initialize a HospitalStaff member with specific details.
     *
     * @param isPatient A boolean indicating whether the user is a patient (false for staff).
     * @param userId The unique identifier of the user.
     * @param name The name of the staff member.
     * @param gender The gender of the staff member.
     * @param role The role of the staff member (Doctor, Pharmacist, Administrator).
     */
    public HospitalStaff(boolean isPatient, String userId, String name, String gender, LocalDate dob, Role role) {
        super(isPatient, userId, name, gender, dob);
        this.role = role;
    }
    
    //Getters

    /**
     * Gets the role of the staff member.
     *
     * @return The role of the staff member (DOCTOR, PHARMACIST, or ADMINISTRATOR).
     */
    public Role getRole() {
        return role;
    }
    //Setters

    /**
     * Sets the role of the staff member.
     *
     * @param role The role to set for the staff member.
     */
    public void setRole(Role Role) {
        this.role = Role;
    }

    /**
     * Returns a string representation of the HospitalStaff member, including user details.
     *
     * @return A formatted string displaying the ID, name, gender, role, and age of the staff member.
     */
    @Override
    public String toString() {
        return "User ID: " + super.getId() + "\n" +
               "Name: " + super.getName() + "\n" +
               "Gender: " + super.getGender() + "\n" +
               "Date of birth: " + utility.DateFormat.format(super.getDob()) + "\n" +
               "Role: " + role;
    }
    
    
}
