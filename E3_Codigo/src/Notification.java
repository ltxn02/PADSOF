import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Notification extends BaseElement {
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
    
    public boolean isRead() {
    	return this.read;
    }
    
    public void markAsRead(boolean read){
        this.read = read;
    }
    
    public String notificationPreview() {
    	return this.buildString(20);
    }
    
    @Override
    public String toString() {
    	return this.buildString(-1);
    }
    
    // -- HELPERS ------------------------------------------------------------------
    
    private String buildString(int maxChars) throws IllegalArgumentException {
    	if (maxChars < -1) {
    		throw new IllegalArgumentException("Invalid max characters");
    	}
    	
    	if (this.message == null) {
    		return "";
    	}
    	
    	StringBuilder res = new StringBuilder();
    	String endString = "";
    	int end = maxChars;
    	
    	if(maxChars == -1 || maxChars >= this.message.length()) {
    		end = this.message.length();
    	} else {
    		endString = "...";
    	}
    	
    	res.append(this.read ? "   " : " ! ");
    	res.append("[" + formatInstant(this.receivedAt) + "] ");
    	res.append(notificationId + ": ");
    	res.append(this.message.substring(0, end) + endString);
    	
    	return res.toString();
    }
    
    private String formatInstant(Instant instant) {
    	if(instant == null) return "N/A";
    	
    	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm").withZone(ZoneId.systemDefault());
    	return fmt.format(instant);
    }
}
