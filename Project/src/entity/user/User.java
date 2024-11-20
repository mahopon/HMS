package entity.user;

import entity.EntityObject;
import java.time.LocalDate;
import utility.Password_hash;

/**
 * Abstract class representing a user in the system.
 * The User class holds common properties for all types of users, such as patients, 
 * and provides methods to manage their details, including password handling.
 */
public abstract class User extends EntityObject {

    private boolean isPatient;
    private String id;
    private String password;
    private String name;
    private String gender;
    private LocalDate dob;

    /**
     * Default constructor for the User class. Initializes an empty User object.
     */
    public User() {
    }

    /**
     * Constructor to initialize a User object with basic attributes.
     * The password is automatically hashed to a default value "password".
     * 
     * @param isPatient A boolean value indicating whether the user is a patient.
     * @param id The unique identifier of the user.
     * @param name The name of the user.
     * @param gender The gender of the user.
     * @param dob The date of birth of the user.
     */
    public User(boolean isPatient, String id, String name, String gender, LocalDate dob){
        this.isPatient = isPatient;
        this.id = id;
        this.password = Password_hash.hashPassword("password");  // Default password is hashed
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }

    // Getters

    /**
     * Retrieves whether the user is a patient or not.
     * 
     * @return true if the user is a patient, false otherwise.
     */
    public boolean getIsPatient() {
        return this.isPatient;
    }

    /**
     * Retrieves the unique ID of the user.
     * 
     * @return The unique identifier for this user.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Retrieves the name of the user.
     * 
     * @return The name of the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the hashed password of the user.
     * 
     * @return The hashed password of the user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Retrieves the gender of the user.
     * 
     * @return The gender of the user.
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Retrieves the date of birth of the user.
     * 
     * @return The date of birth of the user.
     */
    public LocalDate getDob() {
        return this.dob;
    }

    // Setters

    /**
     * Sets whether the user is a patient.
     * 
     * @param isPatient true if the user is a patient, false otherwise.
     */
    public void setIsPatient(boolean isPatient) {
        this.isPatient = isPatient;
    }

    /**
     * Sets the unique ID for the user.
     * 
     * @param id The new unique identifier for the user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the name of the user.
     * 
     * @param name The new name for the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the password for the user. This method is private to ensure
     * password changes are made only via specific methods.
     * 
     * @param password The password to set for the user.
     */
    private void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the gender of the user.
     * 
     * @param gender The new gender for the user.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Changes the user's password by accepting a plain text password,
     * hashing it, and setting it as the new password.
     * 
     * @param input The plain text password to set for the user.
     */
    public void changePassword(String input){
        setPassword(Password_hash.hashPassword(input));
    }

    /**
     * Sets the date of birth for the user.
     * 
     * @param date The new date of birth for the user.
     */
    public void setDob(LocalDate date) {
        this.dob = date;
    }
}
