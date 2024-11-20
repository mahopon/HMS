package observer;

import control.notification.NotificationController;
import entity.notification.Notification;
import entity.user.User;
import interfaces.observer.IObserver;
import java.util.ArrayList;
import java.util.List;
import utility.DateFormat;

/**
 * Implementation of the Observer pattern for managing notifications.
 * The NotificationObserver class observes and handles notifications for a specific user.
 */
public class NotificationObserver implements IObserver {
    
    // The user associated with this observer
    private final User user;

    // List to store the history of notifications as lists of strings
    private final List<List<String>> notificationHistory;

    /**
     * Constructor for NotificationObserver.
     *
     * @param user The user associated with this observer.
     */
    public NotificationObserver(User user) {
        this.notificationHistory = new ArrayList<>();
        this.user = user;
    }

    /**
     * Adds a new notification message to the history.
     *
     * @param message The notification message to be added.
     */
    @Override
    public void notify(List<String> message) {
        notificationHistory.add(message);
    }

    /**
     * Retrieves the notification history for the user.
     *
     * @return A list of lists, where each inner list represents a notification entry.
     */
    @Override
    public List<List<String>> getNotificationHistory() {
        return notificationHistory;
    }

    /**
     * Populates the notification history by fetching notifications from the NotificationController.
     * Converts each notification into a formatted list of strings and adds it to the history.
     */
    @Override
    public void setNotificationHistory() {
        List<Notification> notifications = NotificationController.getNotificationByUser(user);
        for (Notification noti : notifications) {
            List<String> newEntry = new ArrayList<>();
            newEntry.add(noti.getMessage());
            newEntry.add(DateFormat.formatWithTime(noti.getDatetime()));
            notify(newEntry);
        }
    }
}
