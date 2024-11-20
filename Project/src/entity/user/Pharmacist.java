package entity.user;

import java.time.LocalDate;

/**
 * The Pharmacist class represents a pharmacist in the healthcare system. It extends the HospitalStaff
 * class and is associated with the role of a pharmacist.
 */
public class Pharmacist extends HospitalStaff {
    /**
     * Default constructor for the Pharmacist class.
     * Calls the default constructor of the superclass (HospitalStaff).
     */
    public Pharmacist() {
        super();
    }


    /**
     * Constructor to initialize a Pharmacist object with specific details.
     *
     * @param isPatient A boolean flag indicating if the user is a patient (false for pharmacists).
     * @param userId The unique identifier for the pharmacist.
     * @param name The name of the pharmacist.
     * @param gender The gender of the pharmacist.
     * @param dob The date of birth of the pharmacist.
     */
    public Pharmacist(boolean isPatient, String userId, String name, String gender, LocalDate dob) {
        super(isPatient, userId, name, gender, dob, Role.PHARMACIST);
    }
}
