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
		if(username == this.username && password == this.password) {
			return true;
		}
		return false;
	}

	// public boolean logout() {}
	// public Notification (?) read_notifications() {}
	// public boolean hide_notification() {}
	
	@Override
	public String toString() {
		return this.username;
	}
	/*@Override
	public String toString() {
<<<<<<< Updated upstream
		return "ok";
	}
=======
		
	}*/
}