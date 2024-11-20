package entity.appointment;

import entity.EntityObject;
import java.time.LocalDateTime;
import utility.DateFormat;

/**
 * Represents an appointment with details such as patient ID, doctor ID, appointment date and time,
 * service type, status, diagnosis, and notes.
 */
public class Appointment extends EntityObject {

    /**
     * Enum representing the type of service provided in the appointment.
     */
    public enum Service {
        CONSULTATION,
        XRAY,
        LABTEST
    }

    private String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime apptDateTime;
    private Service service;

    /**
     * Enum representing the status of the appointment.
     */
    public enum Status { PENDING, CONFIRMED, COMPLETED, CANCELED }

    private Status status;
    private String diagnosis;
    private String notes;

    /**
     * Default constructor for Appointment.
     */
    public Appointment() {
    }

    /**
     * Constructs an Appointment with specified details.
     *
     * @param id         The ID of the appointment.
     * @param patient    The ID of the patient.
     * @param doctor     The ID of the doctor.
     * @param service    The type of service provided.
     * @param dateTime   The date and time of the appointment.
     */
    public Appointment(String id, String patient, String doctor, Service service, LocalDateTime dateTime) {
        this.id = id;
        this.patientId = patient;
        this.doctorId = doctor;
        this.service = service;
        this.apptDateTime = dateTime;
        this.status = Status.PENDING;
        this.diagnosis = "";
        this.notes = "";
    }

    /**
     * Returns the ID of the appointment.
     *
     * @return The appointment ID.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Returns the patient ID associated with the appointment.
     *
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Returns the doctor ID associated with the appointment.
     *
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Returns the date and time of the appointment.
     *
     * @return The appointment date and time.
     */
    public LocalDateTime getApptDateTime() {
        return apptDateTime;
    }

    /**
     * Returns the current status of the appointment.
     *
     * @return The appointment status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the service type of the appointment.
     *
     * @return The service type.
     */
    public Service getService() {
        return service;
    }

    /**
     * Returns the diagnosis of the appointment.
     *
     * @return The diagnosis.
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Returns the notes associated with the appointment.
     *
     * @return The notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the ID of the appointment.
     *
     * @param id The appointment ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the patient ID associated with the appointment.
     *
     * @param patientId The patient ID to set.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Sets the doctor ID associated with the appointment.
     *
     * @param doctorId The doctor ID to set.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Sets the date and time of the appointment.
     *
     * @param apptDateTime The appointment date and time to set.
     */
    public void setApptDate(LocalDateTime apptDateTime) {
        this.apptDateTime = apptDateTime;
    }

    /**
     * Sets the status of the appointment.
     *
     * @param set The status to set.
     */
    public void setStatus(Status set) {
        this.status = set;
    }

    /**
     * Sets the service type of the appointment.
     *
     * @param service The service type to set.
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Sets the diagnosis of the appointment.
     *
     * @param diagnosis The diagnosis to set.
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Sets the notes associated with the appointment.
     *
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return A formatted string containing the appointment details.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
               "Patient ID: " + patientId + "\n" +
               "Doctor ID: " + doctorId + "\n" +
               "Appointment Date & Time: " + DateFormat.formatWithTime(apptDateTime) + "\n" +
               "Service: " + service + "\n" +
               "Status: " + status + "\n" +
               "Diagnosis: " + diagnosis + "\n" +
               "Notes: " + notes;
    }
}
