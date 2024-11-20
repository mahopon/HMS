package control.request;

import control.medicine.MedicineController;
import entity.medicine.Medicine;
import entity.request.MedicineRequest;
import entity.request.Request;
import entity.request.Request.STATUS;
import entity.user.HospitalStaff;
import exception.EntityNotFoundException;
import exception.InvalidInputException;
import java.time.LocalDateTime;
import java.util.List;
import repository.request.MedicineRequestRepository;

/**
 * Controller for managing medicine replenishment requests.
 */
public class MedicineRequestController {

    private static final MedicineRequestRepository repo = MedicineRequestRepository.getInstance();

    /**
     * Creates a new medicine replenishment request.
     *
     * @param staff      The hospital staff requesting replenishment.
     * @param medicineId The ID of the medicine to be replenished.
     * @param quantity   The quantity of the medicine requested.
     * @return The ID of the created request.
     * @throws InvalidInputException If any input is invalid.
     */
    public static String createReplenishmentRequest(HospitalStaff staff, String medicineId, int quantity) throws InvalidInputException {
        if (staff.getId() == null || staff.getId().isEmpty()) {
            throw new InvalidInputException("Requestor ID cannot be null or empty.");
        }
        if (medicineId == null || medicineId.isEmpty()) {
            throw new InvalidInputException("Medicine ID cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }

        MedicineRequest req = new MedicineRequest();
        req.setId(repo.getNextClassId());
        req.setRequestorId(staff.getId());
        req.setApproverId(null);
        req.setStatus(STATUS.PENDING);
        req.setTimeCreated(LocalDateTime.now());
        req.setMedicineId(medicineId);
        req.setQuantity(quantity);
        repo.add(req);
        repo.save();
        return req.getId();
    }

    /**
     * Retrieves a MedicineRequest by its ID.
     *
     * @param requestId The ID of the request.
     * @return The MedicineRequest object.
     * @throws EntityNotFoundException If the request is not found.
     * @throws InvalidInputException   If the request ID is invalid.
     */
    public static MedicineRequest getRequestById(String requestId) throws EntityNotFoundException, InvalidInputException {
        if (requestId == null || requestId.isEmpty()) {
            throw new InvalidInputException("Request ID cannot be null or empty.");
        }

        MedicineRequest req = repo.get(requestId);
        if (req == null) {
            throw new EntityNotFoundException("MedicineRequest", requestId);
        }
        return req;
    }

    /**
     * Retrieves all pending medicine requests.
     *
     * @return A list of pending MedicineRequest objects.
     */
    public static List<MedicineRequest> getPendingRequests() {
        return repo.findByField("status", Request.STATUS.PENDING);
    }

    /**
     * Approves a replenishment request and updates the medicine stock.
     *
     * @param user The hospital staff approving the request.
     * @param req  The MedicineRequest to be approved.
     * @throws InvalidInputException   If the approver or request is invalid.
     * @throws EntityNotFoundException If the medicine is not found.
     */
    public static void approveReplenishmentRequest(HospitalStaff user, MedicineRequest req) throws InvalidInputException, EntityNotFoundException {
        if (user == null) {
            throw new InvalidInputException("Approver cannot be null.");
        }
        if (req == null || req.getStatus() != Request.STATUS.PENDING) {
            throw new InvalidInputException("Request is either null or not in PENDING status.");
        }

        req.setStatus(Request.STATUS.APPROVED);
        req.setApproverId(user.getId());
        req.setTimeModified(LocalDateTime.now());

        Medicine med = MedicineController.getMedicineById(req.getMedicineId());
        if (med == null) {
            throw new EntityNotFoundException("Medicine", req.getMedicineId());
        }

        MedicineController.incMedStock(med, req.getQuantity());
        repo.save();
    }

    /**
     * Rejects a replenishment request.
     *
     * @param user The hospital staff rejecting the request.
     * @param req  The MedicineRequest to be rejected.
     * @throws InvalidInputException If the approver or request is invalid.
     */
    public static void rejectReplenishmentRequest(HospitalStaff user, MedicineRequest req) throws InvalidInputException {
        if (user == null) {
            throw new InvalidInputException("Approver cannot be null.");
        }
        if (req == null || req.getStatus() != Request.STATUS.PENDING) {
            throw new InvalidInputException("Request is either null or not in PENDING status.");
        }

        req.setStatus(Request.STATUS.REJECTED);
        req.setApproverId(user.getId());
        req.setTimeModified(LocalDateTime.now());
        repo.save();
    }

    /**
     * Updates the status of a replenishment request.
     *
     * @param requestId The ID of the request.
     * @param newStatus The new status to set.
     * @throws InvalidInputException   If the request ID is invalid.
     * @throws EntityNotFoundException If the request is not found.
     */
    public static void updateRequestStatus(String requestId, Request.STATUS newStatus) throws InvalidInputException, EntityNotFoundException {
        if (requestId == null || requestId.isEmpty()) {
            throw new InvalidInputException("Request ID cannot be null or empty.");
        }

        MedicineRequest req = getRequestById(requestId);
        req.setStatus(newStatus);
        req.setTimeModified(LocalDateTime.now());
        repo.save();
    }

    /**
     * Removes a replenishment request by its ID.
     *
     * @param requestId The ID of the request to be removed.
     * @throws InvalidInputException   If the request ID is invalid.
     * @throws EntityNotFoundException If the request is not found.
     */
    public static void removeReplenishmentRequest(String requestId) throws InvalidInputException, EntityNotFoundException {
        if (requestId == null || requestId.isEmpty()) {
            throw new InvalidInputException("Request ID cannot be null or empty.");
        }

        MedicineRequest req = getRequestById(requestId);
        repo.remove(req);
        repo.save();
    }
}
