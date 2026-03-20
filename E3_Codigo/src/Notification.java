import java.time.Instant;
import java.util.ArrayList;

public class Notification {
    private int notificationId;
    private static int lastNotificationId = 1;
    private String message;
    private Instant receivedAt;
    private boolean read;
    private ArrayList<RegisteredUser> receivedUsers;

    public Notification(String message, ArrayList<RegisteredUser> receivedUsers) {
        this.message = message;
        this.receivedAt = Instant.now();
        this.read = false;
        this.receivedUsers = receivedUsers;
        this.notificationId = lastNotificationId++;
        Notification.lastNotificationId++;
    }

    public void markAs(boolean read){
        this.read = read;
    }
}
