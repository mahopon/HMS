package interfaces.observer;

import java.util.List;

/**
 * Interface for implementing the Observer pattern.
 * Observers can receive notifications and manage a history of notifications.
 */
public interface IObserver {

    /**
     * Sends a notification to the observer with the specified message.
     *
     * @param message A list of strings representing the notification message.
     */
    public void notify(List<String> message);

    /**
     * Retrieves the history of notifications received by the observer.
     *
     * @return A list of lists, where each inner list represents a notification message.
     */
    public List<List<String>> getNotificationHistory();

    /**
     * Initializes or updates the notification history for the observer.
     * Typically used to synchronize the observer with existing notifications.
     */
    public void setNotificationHistory();
}
