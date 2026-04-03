import java.util.*;

public abstract class RegisteredUser extends User {
	private static int lastUserId = 0;
	private int userId;
	private String username;
	private String password;
	private String fullname;
	private String dni;
	private String birthdate;
	private String email;
	private String phoneNumber;
	private List<Notification> myNotifications;
	
	public RegisteredUser(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.dni = dni;
		this.birthdate = birthdate;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.myNotifications = new ArrayList<>();
		this.userId = lastUserId;
		lastUserId++;
	}
	
	public boolean login(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }

	public String getUsername() {
		return username;
	}

	public String view_notifications() {
		String res = "My inbox:\n";
		for(Notification n: this.myNotifications) {
			res += "\t" + n.notificationPreview() + "\n";
		}
		return res;
	}

	public String read_notification(Notification notification) {
		String res = "" + notification;
		notification.markAsRead(true);
		return res;
	}
	
	public void change_visibility(Notification notification, boolean visible) {
		notification.markAs(visible);
	}
	
	@Override
	public String toString() {
		return this.username;
	}
}