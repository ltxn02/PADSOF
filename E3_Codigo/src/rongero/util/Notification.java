package util;
import model.catalog.BaseElement;
import model.user.RegisteredUser;
import java.time.Instant;
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
    	String res = this.buildString(20);
    	return res;
    }
    
    @Override
    public String toString() {
    	String res = this.buildString(-1);
    	return res; 
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
    	res.append("[" + this.receivedAt.toString() + "] ");
    	res.append(notificationId + ": ");
    	res.append(this.message.substring(0, end) + endString);
    	
    	return res.toString();
    }
}
