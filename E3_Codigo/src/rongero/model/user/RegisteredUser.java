package model.user;
import java.util.*;
import util.Notification;
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
		this.userId = RegisteredUser.lastUserId;
		RegisteredUser.lastUserId++;
	}
	
	public boolean login(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }

	public String getUsername() {
		return username;
	}

	public String view_notifications() {
		String res = "My inbox (" + countNewNotifications() + " new):\n";
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
	
	public String userPreview() {
		return this.fullname + " (@" + this.username + ") - " + maskEmail(this.email);
	}
	
	public String userProfile() {
		StringBuilder res = new StringBuilder();
		
		res.append("Name: " + this.fullname + "\n");
		res.append("Username: " + this.username + "\n");
		res.append("Email: " + maskEmail(this.email) + "\n");
		res.append("Phonenumber: " + this.phoneNumber + "\n");
		res.append("Datebirth: " + this.birthdate + "\n");
		res.append("DNI: " + maskDni(this.dni) + "\n");
		
		return res.toString();
	}
	
    // -- HELPERS ------------------------------------------------------------------
	
	private int countNewNotifications() {
		int i = 0;
		for(Notification n: this.myNotifications) {
			if(n.isRead() == false) {
				i++;
			}
		}
		return i;
	}
	
	private String maskEmail(String email) {
		if(email == null || !email.contains("@") || email.indexOf("@") <= 1) {
			return email;
		}
		
		int i, atIndex = email.indexOf("@");
		String aux = "";
		
		for(i = 1; i < atIndex; i++) {
			aux += "*";
		}
		
		return email.charAt(0) + aux + email.substring(atIndex);
	}
	
	private String maskDni(String dni) {
		if(dni == null || dni.length() < 4) {
			return dni;
		}
		
		return "****" + dni.substring(dni.length() - 4);
	}

	public List<Notification> getMyNotifications() {
		return this.myNotifications;
	}

	// Metodo para añadir una notificación a la bandeja de entrada
	public void addNotification(Notification n) {
		this.myNotifications.add(n);
	}
}