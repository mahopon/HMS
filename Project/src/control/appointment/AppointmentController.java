package control.appointment;

import control.prescription.PrescriptionController;
import control.prescription.PrescriptionItemController;
import control.user.HospitalStaffController;
import control.user.PatientController;
import control.user.UnavailableDateController;
import entity.appointment.Appointment;
import entity.medicine.Prescription;
import entity.medicine.PrescriptionItem;
import entity.user.Doctor;
import entity.user.HospitalStaff;
import entity.user.Patient;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import repository.appointment.AppointmentRepository;

/**
 * Controller for managing appointments
 */
public class AppointmentController {

    private static final AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();

   /**
     * Schedules a new appointment.
     *
     * @param doc The doctor for the appointment.
     * @param patient The patient for the appointment.
     * @param service The service type of the appointment.
     * @param selectedSlot The date and time for the appointment.
     * @throws InvalidInputException If any of the parameters are null.
     */
    public static void scheduleAppointment(Doctor doc, Patient patient, Appointment.Service service, LocalDateTime selectedSlot) throws InvalidInputException {
        if (doc == null || patient == null || service == null || selectedSlot == null) {
            throw new InvalidInputException("Invalid input: Doctor, patient, service, and selected slot must not be null.");
        }

        Appointment newAppointment = new Appointment(appointmentRepository.getNextClassId(), patient.getId(), doc.getId(), service, selectedSlot);
        appointmentRepository.add(newAppointment);
        appointmentRepository.save();

        // Mark the selected slot as unavailable for the doctor
        UnavailableDateController.addUnavailability(doc, selectedSlot);
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param apptId The ID of the appointment.
     * @return The appointment object.
     * @throws InvalidInputException If the ID is null or empty.
     * @throws EntityNotFoundException If the appointment is not found.
     */
    public static Appointment getAppt(String apptId) throws InvalidInputException, EntityNotFoundException {
        if (apptId == null || apptId.isEmpty()) {
            throw new InvalidInputException("Appointment ID cannot be null or empty.");
        }

        Appointment appt = appointmentRepository.findByField("id", apptId).stream().findFirst().orElse(null);
        if (appt == null) {
            throw new EntityNotFoundException("Appointment", apptId);
        }
        return appt;
    }
    
    /**
     * Retrieves all appointments.
     *
     * @return A list of all appointments.
     */
    public static List<Appointment> getAllAppts() {
        return appointmentRepository.toList();
    }

    /**
     * Retrieves all appointments for a specific doctor.
     *
     * @param doc The doctor whose appointments are to be retrieved.
     * @return A list of appointments for the specified doctor.
     * @throws InvalidInputException If the doctor is null.
     */
    public static List<Appointment> getDoctorAppts(Doctor doc) throws InvalidInputException {
        if (doc == null) {
            throw new InvalidInputException("Doctor cannot be null.");
        }
        return appointmentRepository.findByField("doctorId", doc.getId());
    }

    /**
     * Retrieves all pending appointments for a specific doctor.
     *
     * @param doc The doctor whose pending appointments are to be retrieved.
     * @return A list of pending appointments for the specified doctor.
     * @throws InvalidInputException If the doctor is null.
     */
    public static List<Appointment> getPendingAppts(Doctor doc) throws InvalidInputException {
        if (doc == null) {
            throw new InvalidInputException("Doctor cannot be null.");
        }
        return appointmentRepository.findByField("doctorId", doc.getId()).stream()
                .filter(appt -> appt.getStatus() == Appointment.Status.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all scheduled future appointments for a specific patient.
     *
     * @param patient The patient whose scheduled appointments are to be retrieved.
     * @return A list of scheduled appointments for the specified patient.
     * @throws InvalidInputException If the patient is null.
     */
    public static List<Appointment> getScheduledAppointments(Patient patient) throws InvalidInputException {
        if (patient == null) {
            throw new InvalidInputException("Patient cannot be null.");
        }

        return appointmentRepository.findByField("patientId", patient.getId()).stream()
                .filter(appt -> appt.getApptDateTime().isAfter(LocalDateTime.now().minusDays(1)) && (appt.getStatus() == Appointment.Status.PENDING || appt.getStatus() == Appointment.Status.CONFIRMED))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all past appointments for a specific patient.
     *
     * @param patient The patient whose past appointments are to be retrieved.
     * @return A list of past appointments for the specified patient.
     * @throws InvalidInputException If the patient is null.
     */
    public static List<Appointment> getPastAppointments(Patient patient) throws InvalidInputException {
        if (patient == null) {
            throw new InvalidInputException("Patient cannot be null.");
        }

        return appointmentRepository.findByField("patientId", patient.getId()).stream()
                .filter(appt -> appt.getApptDateTime().isBefore(LocalDateTime.now().plusDays(1)) &&
                                (appt.getStatus() == Appointment.Status.COMPLETED))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the outcomes of a completed appointment based on its appointment ID.
     *
     * @param appointmentId The ID of the appointment whose outcomes are to be retrieved.
     * @return A list containing the appointment, prescription, and prescription items.
     * @throws InvalidInputException If the appointment ID is invalid or does not exist.
     */
    public static List<Object> getAppointmentOutcomesById(String appointmentId) throws InvalidInputException, EntityNotFoundException {
        if (appointmentId == null || appointmentId.isEmpty()) {
            throw new InvalidInputException("Appointment ID cannot be null or empty.");
        }

        // Fetch the appointment based on appointmentId
        Appointment appointment = getAppt(appointmentId);
        if (appointment == null) {
            throw new InvalidInputException("Appointment with the given ID does not exist.");
        }

        // Check if the appointment is completed
        if (appointment.getStatus() != Appointment.Status.COMPLETED) {
            throw new InvalidInputException("Appointment is not completed yet.");
        }

        // Create result containers
        HashMap<String, Prescription> pastPrescriptions = new HashMap<>();
        HashMap<String, List<PrescriptionItem>> prescriptionItems = new HashMap<>();

        // Fetch the prescription associated with the appointment
        Prescription prescription = PrescriptionController.getPrescriptionByAppt(appointment);
        pastPrescriptions.put(appointmentId, prescription);

        // Fetch the prescription items associated with the prescription
        if (prescription != null) {
            prescriptionItems.put(prescription.getId(), PrescriptionItemController.getPrescriptionItems(prescription));
        }

        // Return the results in a list
        List<Object> result = new ArrayList<>();
        result.add(appointment); // Adding the appointment object
        result.add(pastPrescriptions); // Adding the prescription details
        result.add(prescriptionItems); // Adding the prescription items

        return result;
    }

    /**
     * Retrieves the outcomes of completed appointments for a specific patient.
     *
     * @param patient The patient whose appointment outcomes are to be retrieved.
     * @return A list containing past appointments, prescriptions, and prescription items.
     * @throws InvalidInputException If the patient is null.
     */
    public static List<Object> getAppointmentOutcomes(Patient patient) throws InvalidInputException {
        if (patient == null) {
            throw new InvalidInputException("Patient cannot be null.");
        }
    
        List<Appointment> pastAppts = getPastAppointments(patient)
                                      .stream()
                                      .filter(appt -> appt.getStatus() == Appointment.Status.COMPLETED)
                                      .toList();
    
        HashMap<String, Prescription> pastPrescriptions = new HashMap<>();
        HashMap<String, List<PrescriptionItem>> prescriptionItems = new HashMap<>();
    
        for (Appointment appt : pastAppts) {
            try {
                Prescription prescription = PrescriptionController.getPrescriptionByAppt(appt);
                pastPrescriptions.put(appt.getId(), prescription);
    
                prescriptionItems.put(prescription.getId(), PrescriptionItemController.getPrescriptionItems(prescription));
            } catch (EntityNotFoundException | InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
                pastPrescriptions.put(appt.getId(), null);
            }
        }
    
        List<Object> result = new ArrayList<>();
        result.add(pastAppts);
        result.add(pastPrescriptions);
        result.add(prescriptionItems);
    
        return result;
    }
    /**
     * Retrieves available time slots for a given hospital staff (doctor) on a specific date.
     * The method checks the default time slots and filters out the unavailable ones based on the 
     * existing unavailability records for the doctor on the specified date.
     *
     * @param doc The hospital staff (doctor) for whom the available time slots are to be retrieved.
     * @param date The date for which to retrieve available time slots.
     * @return A list of available time slots as {@link LocalDateTime} objects.
     * @throws InvalidInputException If either the {@code doc} or {@code date} is null.
     */
    public static List<LocalDateTime> getAvailableTimeSlots(HospitalStaff doc, LocalDate date) throws InvalidInputException {
        
        if (doc == null) {
            throw new InvalidInputException("Hospital staff cannot be null.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }

        List<LocalDateTime> defaultSlots = List.of(
                date.atTime(9, 0),
                date.atTime(10, 0),
                date.atTime(11, 0),
                date.atTime(14, 0),
                date.atTime(15, 0),
                date.atTime(16, 0)
        );
            
        // Fetch the unavailable slots for the specified staff and date
        List<LocalDateTime> unavailableSlots = UnavailableDateController.getUnavailableSlotsByDate(doc, date);

        // Filter out unavailable slots from the default time slots
        return defaultSlots.stream()
            .filter(slot -> !unavailableSlots.contains(slot))
            .collect(Collectors.toList());
    }
    
    /**
     * Reschedules an existing appointment.
     *
     * @param appt The appointment to be rescheduled.
     * @param newDateTime The new date and time for the appointment.
     * @throws InvalidInputException If the appointment or new date/time is null.
     * @throws EntityNotFoundException If the doctor is not found or the time slot is unavailable.
     */
    public static void rescheduleAppointment(Appointment appt, LocalDateTime newDateTime) throws InvalidInputException, EntityNotFoundException {
        if (appt == null || newDateTime == null) {
            throw new InvalidInputException("Appointment and new date/time cannot be null.");
        }

        Doctor doc = (Doctor) HospitalStaffController.findById(appt.getDoctorId());
        if (checkOverlappingAppointments(doc, newDateTime)) {
            throw new InvalidInputException("The new time slot is not available.");
        }

        appt.setApptDate(newDateTime);
        appt.setStatus(Appointment.Status.PENDING);
        appointmentRepository.save();

        // Mark the selected slot as unavailable for the doctor
        UnavailableDateController.addUnavailability(doc, newDateTime);
    }

    /**
     * Makes a decision on an appointment request.
     *
     * @param appt The appointment request to be decided.
     * @param isAccepted True if the appointment is accepted, false if it is rejected.
     * @throws InvalidInputException If the appointment is null or already confirmed/canceled.
     */
    public static void apptRequestDecision(Appointment appt, boolean isAccepted) throws InvalidInputException {
        if (appt == null) {
            throw new InvalidInputException("Appointment cannot be null.");
        }
    
        if (appt.getStatus() == Appointment.Status.CONFIRMED) {
            throw new InvalidInputException("Appointment is already confirmed.");
        }
    
        if (appt.getStatus() == Appointment.Status.CANCELED) {
            throw new InvalidInputException("Appointment is already canceled.");
        }
    
        appt.setStatus(isAccepted ? Appointment.Status.CONFIRMED : Appointment.Status.CANCELED);
        appointmentRepository.save();
    }
    
    /**
     * Completes an appointment by setting its diagnosis, consultation notes, and prescribed medication.
     *
     * @param appt The appointment to be completed.
     * @param diagnosis The diagnosis for the appointment.
     * @param consultationNotes The consultation notes.
     * @param prescribedMedication The prescribed medication details.
     * @throws InvalidInputException If any required input is null or empty.
     * @throws EntityNotFoundException If prescription creation fails.
     */
    public static void completeAppointment(Appointment appt, String diagnosis, String consultationNotes) throws InvalidInputException, EntityNotFoundException {
        if (appt == null) {
            throw new InvalidInputException("Appointment cannot be null.");
        }
        if (diagnosis == null || diagnosis.isEmpty()) {
            throw new InvalidInputException("Diagnosis cannot be empty");
        }
        if (consultationNotes == null || consultationNotes.isEmpty()) {
            throw new InvalidInputException("Consultation notes cannot be empty");
        }

        appt.setDiagnosis(diagnosis);
        appt.setNotes(consultationNotes);
        appt.setStatus(Appointment.Status.COMPLETED);
        appointmentRepository.save();
    }

    /**
     * Cancels an appointment.
     *
     * @param appt The appointment to be canceled.
     * @throws InvalidInputException If the appointment is null.
     */
    public static void cancelAppointment(Appointment appt) throws InvalidInputException {
        if (appt == null) {
            throw new InvalidInputException("Appointment cannot be null.");
        }

        appt.setStatus(Appointment.Status.CANCELED);
        appointmentRepository.save();
    }

    /**
     * Checks if there are any overlapping appointments for a doctor at a given date and time.
     *
     * @param doc The doctor to check for overlapping appointments.
     * @param dateTime The date and time to check.
     * @return True if there is an overlapping appointment, false otherwise.
     * @throws InvalidInputException If the doctor or date/time is null.
     */
    public static boolean checkOverlappingAppointments(Doctor doc, LocalDateTime dateTime) throws InvalidInputException {
        if (doc == null || dateTime == null) {
            throw new InvalidInputException("Doctor and date/time cannot be null.");
        }

        List<Appointment> doctorAppointments = getDoctorAppts(doc);
        return doctorAppointments.stream().anyMatch(appt -> appt.getApptDateTime().isEqual(dateTime));
    }

    /**
     * Retrieves appointments for a specific doctor on a given date.
     *
     * @param doc The doctor whose appointments are to be retrieved.
     * @param date The date for which to retrieve appointments.
     * @return A list of appointments for the specified doctor on the given date.
     * @throws InvalidInputException If the doctor or date is null.
     */
    public static List<Appointment> getApptByDate(Doctor doc, LocalDate date) throws InvalidInputException {
        if (doc == null) {
            throw new InvalidInputException("Doctor cannot be null.");
        }
        if (date == null) {
            throw new InvalidInputException("Date cannot be null.");
        }
    
        List<Appointment> doctorAppointments = getDoctorAppts(doc);
    
        return doctorAppointments.stream()
                .filter(appt -> appt.getApptDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of unique patients assigned to a specific doctor.
     *
     * @param doc The doctor whose patients are to be retrieved.
     * @return A list of unique patients assigned to the specified doctor.
     * @throws InvalidInputException If the doctor is null.
     */
    public static List<Patient> getDoctorsPatients(Doctor doc) throws InvalidInputException {
        if (doc == null) {
            throw new InvalidInputException("Doctor cannot be null.");
        }

        List<Appointment> doctorAppointments = appointmentRepository.findByField("doctorId", doc.getId());

        return doctorAppointments.stream()
                .map(appt -> {
                    try {
                        return (Patient) PatientController.getById(appt.getPatientId());
                    } catch (EntityNotFoundException e) {
                        System.out.println("Error retrieving patient: " + e.getMessage());
                        return null;
                    }
                })
                .filter(patient -> patient != null)
                .distinct()
                .collect(Collectors.toList());
    }  
    
    /**
     * Updates the outcomes of a completed appointment, including diagnosis, prescriptions, and consultation notes.
     *
     * @param appt The appointment to be updated.
     * @param updatedDiagnosis The updated diagnosis, or null if no update is required.
     * @param updatedConsultationNotes The updated consultation notes, or null if no update is required.
     * @param updatedPrescribedMedication The updated prescribed medications (key: medicine ID, value: list [quantity, notes]), or null if no update is required.
     * @throws InvalidInputException If the appointment is null or not completed, or if input values are invalid.
     * @throws EntityNotFoundException If the prescription or prescription items cannot be updated.
     */
    public static void updateAppointmentOutcomes(Appointment appt, String updatedDiagnosis, String updatedConsultationNotes) throws InvalidInputException, EntityNotFoundException {
        if (appt == null) {
            throw new InvalidInputException("Appointment cannot be null.");
        }

        if (appt.getStatus() != Appointment.Status.COMPLETED) {
            throw new InvalidInputException("Only completed appointments can be updated.");
        }

        if (updatedDiagnosis != null && !updatedDiagnosis.isEmpty()) {
            appt.setDiagnosis(updatedDiagnosis);
        }

        if (updatedConsultationNotes != null && !updatedConsultationNotes.isEmpty()) {
            appt.setNotes(updatedConsultationNotes);
        }
        appointmentRepository.save();
    }

}
