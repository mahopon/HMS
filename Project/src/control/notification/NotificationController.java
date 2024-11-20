package control.notification;

import entity.notification.Notification;
import entity.user.HospitalStaff;
import entity.user.User;
import interfaces.observer.IObserver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import repository.notification.NotificationRepository;
import repository.user.StaffRepository;
import utility.DateFormat;

/**
 * Controller for managing notifications related to users. It provides functionality
 * to create, retrieve, mark as read, and notify users or admins about various events.
 */
public class NotificationController {

    private static final NotificationRepository notificationRepository = NotificationRepository.getInstance();
    private static NotificationController instance;
    private final Map<String, IObserver> observers;

    /**
     * Private constructor to prevent instantiation. Initializes the observers map.
     */
    public NotificationController() {
        this.observers = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the NotificationController.
     *
     * @return The singleton instance of NotificationController.
     */
    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    /**
     * Creates a new notification for a specific user with a message.
     *
     * @param userId The ID of the user to receive the notification.
     * @param message The content of the notification.
     * @return The created Notification object.
     */
    public static Notification createNotification(String userId, String message) {
        Notification newNotification = new Notification(notificationRepository.getNextClassId(), userId, message);
        notificationRepository.add(newNotification);
        notificationRepository.save();
        return newNotification;
    }

    /**
     * Retrieves the list of notifications for a given user, sorted by the notification's datetime.
     * Admin users (whose IDs start with "A") receive both general and admin-specific notifications.
     *
     * @param user The user whose notifications are to be retrieved.
     * @return A sorted list of notifications for the user.
     */
    public static List<Notification> getNotificationByUser(User user) {
        if (user.getId().startsWith("A")) {
            // Admin users get both their own and the 'ADMIN' notifications.
            return notificationRepository.toList()
                .stream()
                .filter(noti -> noti.getUserId().equals(user.getId()) || noti.getUserId().equals("ADMIN")
                && Boolean.FALSE.equals(noti.getReadStatus())) // Filter unread notifications
                .sorted(Comparator.comparing(Notification::getDatetime).reversed())
                .toList();
        }

        // Regular users only get their specific notifications.
        return notificationRepository.findByField("userId", user.getId())
                .stream()
                .filter(noti -> Boolean.FALSE.equals(noti.getReadStatus())) // Filter unread notifications
                .sorted(Comparator.comparing(Notification::getDatetime).reversed())
                .toList();
    }

    /**
     * Marks all notifications for a specific user as read.
     *
     * @param userId The ID of the user whose notifications are to be marked as read.
     */
    public void markNotificationsRead(String userId) {
        List<Notification> userNotifications = notificationRepository.findByField("userId", userId);
        userNotifications.forEach(notification -> notification.setReadStatus(true));
        notificationRepository.save();
    }

    /**
     * Notifies all admin users about a specific message.
     *
     * @param message The message to notify the admins.
     */
    public void notifyAdmins(String message) {
        List<HospitalStaff> admins = getAllAdmins(); // Retrieves all admin users
        for (User admin : admins) {
            Notification newNoti = createNotification(admin.getId(), message);
            IObserver observer = observers.get(admin.getId());
            if (observer != null) {
                List<String> newEntry = new ArrayList<>();
                newEntry.add(newNoti.getMessage());
                newEntry.add(DateFormat.formatWithTime(newNoti.getDatetime()));
                observer.notify(newEntry);
            }
        }
    }

    /**
     * Retrieves all admin users by filtering those whose IDs start with "A".
     *
     * @return A list of all admin users.
     */
    private List<HospitalStaff> getAllAdmins() {
        // Retrieve all users from the StaffRepository and filter by admin IDs
        return StaffRepository.getInstance().toList()
                .stream()
                .filter(user -> user.getId().startsWith("A"))
                .toList();
    }

    /**
     * Registers an observer to receive notifications for a specific user ID.
     *
     * @param id The ID of the user to register the observer for.
     * @param observer The observer to register.
     */
    public void registerObserver(String id, IObserver observer) {
        observers.put(id, observer);
    }

    /**
     * Removes an observer from the list of observers for a specific user ID.
     *
     * @param id The ID of the user to remove the observer for.
     */
    public void removeObserver(String id) {
        observers.remove(id);
    }

    /**
     * Notifies a specific observer with a new notification message.
     *
     * @param id The ID of the user to notify.
     * @param message The message content to send to the observer.
     */
    public void notifyObserver(String id, String message) {
        Notification newNoti = createNotification(id, message);
        IObserver observer = observers.get(id);
        if (observer != null) {
            List<String> newEntry = new ArrayList<>();
            newEntry.add(newNoti.getMessage());
            newEntry.add(DateFormat.formatWithTime(newNoti.getDatetime()));
            observer.notify(newEntry);
        }
    }

}
