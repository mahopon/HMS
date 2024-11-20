package entity.user;

public class StaffFactory {

    /**
     * Creates an instance of the appropriate subclass of HospitalStaff based on the given ID prefix.
     * The method checks the prefix of the ID to determine if the staff member is a Doctor, Pharmacist, or Administrator.
     * 
     * @param id The ID of the staff member.
     * @return The appropriate HospitalStaff subclass (Doctor, Pharmacist, or Administrator).
     * @throws IllegalArgumentException if the ID does not start with 'D', 'P', or 'A'.
     */
    public static HospitalStaff createStaffByPrefix(String id) {
        if (id.startsWith("D")) {
            return new Doctor();
        } else if (id.startsWith("P")) {
            return new Pharmacist();
        } else if (id.startsWith("A")) {
            return new Administrator();
        } else {
            throw new IllegalArgumentException("Invalid prefix in ID: " + id);
        }
    }

    /**
     * Creates an instance of the appropriate subclass of HospitalStaff based on the given role.
     * 
     * @param role The role of the staff member (DOCTOR, PHARMACIST, or ADMINISTRATOR).
     * @return The appropriate HospitalStaff subclass (Doctor, Pharmacist, or Administrator).
     * @throws IllegalArgumentException if the role is null or does not match one of the valid roles.
     */
    public static HospitalStaff createStaffByRole(HospitalStaff.Role role) {
        if (null == role) {
            throw new IllegalArgumentException("Invalid role");
        } else switch (role) {
            case DOCTOR:
                return new Doctor();
            case PHARMACIST:
                return new Pharmacist();
            case ADMINISTRATOR:
                return new Administrator();
            default:
                throw new IllegalArgumentException("Invalid role");
        }
    }
}