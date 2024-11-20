package entity.request;

import entity.EntityObject;
import java.time.LocalDateTime;

/**
 * Abstract class representing a request within the system, which can be approved, rejected, or pending.
 * This class contains common properties shared by all request types, such as requestor, approver, status, 
 * and timestamps for creation and modification.
 */
public abstract class Request extends EntityObject {
    
    /** Enum representing the possible statuses of a request. */
    public enum STATUS {
        PENDING,  ///< Request is awaiting approval.
        REJECTED, ///< Request has been rejected.
        APPROVED  ///< Request has been approved.
    }

    private String id;             ///< The unique ID of the request.
    private String requestorId;    ///< The ID of the person requesting the item or action.
    private String approverId;     ///< The ID of the person approving or rejecting the request.
    private STATUS status;         ///< The current status of the request.
    private LocalDateTime timeCreated; ///< The timestamp when the request was created.
    private LocalDateTime timeModified; ///< The timestamp when the request was last modified.

    /**
     * Default constructor for the Request class.
     */
    public Request() {

    }

    /**
     * Constructor to initialize a new Request with specified details.
     * 
     * @param id The unique ID of the request.
     * @param requestorId The ID of the person making the request.
     * @param approverId The ID of the person who will approve or reject the request.
     * @param status The current status of the request (PENDING, APPROVED, REJECTED).
     * @param timeCreated The timestamp when the request was created.
     * @param timeModified The timestamp when the request was last modified.
     */
    public Request(String id, String requestorId, String approverId, STATUS status, LocalDateTime timeCreated, LocalDateTime timeModified) {
        this.id = id;
        this.requestorId = requestorId;
        this.approverId = approverId;
        this.status = status;
        this.timeCreated = timeCreated;
        this.timeModified = timeModified;
    }

    /**
     * Gets the unique ID of the request.
     * 
     * @return The request's ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID of the request.
     * 
     * @param id The new ID to set for the request.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of the person making the request.
     * 
     * @return The requestor's ID.
     */
    public String getRequestorId() {
        return requestorId;
    }

    /**
     * Sets the ID of the person making the request.
     * 
     * @param requestorId The new requestor ID to set.
     */
    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    /**
     * Gets the ID of the person who will approve or reject the request.
     * 
     * @return The approver's ID.
     */
    public String getApproverId() {
        return approverId;
    }

    /**
     * Sets the ID of the person who will approve or reject the request.
     * 
     * @param approverId The new approver ID to set.
     */
    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    /**
     * Gets the timestamp when the request was created.
     * 
     * @return The creation time of the request.
     */
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    /**
     * Sets the timestamp when the request was created.
     * 
     * @param timeCreated The new creation timestamp to set.
     */
    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    /**
     * Gets the timestamp when the request was last modified.
     * 
     * @return The modification time of the request.
     */
    public LocalDateTime getTimeModified() {
        return timeModified;
    }

    /**
     * Sets the timestamp when the request was last modified.
     * 
     * @param timeModified The new modification timestamp to set.
     */
    public void setTimeModified(LocalDateTime timeModified) {
        this.timeModified = timeModified;
    }

    /**
     * Gets the current status of the request.
     * 
     * @return The current status of the request (PENDING, APPROVED, REJECTED).
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * Sets the current status of the request.
     * 
     * @param status The new status to set.
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }
}