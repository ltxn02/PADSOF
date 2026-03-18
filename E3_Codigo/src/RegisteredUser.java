import java.util.*;

public abstract class RegisteredUser extends User {
	private static int lastUserId;
	private int userId;
	private String username;
	private String password;
	private String fullname;
	private String dni;
	private Date birthdate;
	private String email;
	private String phoneNumber;
	private List<Notification> myNotifications;
	
	public RegisteredUser(String username, String password, String fullname, String dni, Date birthdate, String email, String phoneNumber) {
		this.userId = next_id();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.dni = dni;
		this.birthdate = birthdate;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	private int next_id() {
		int nextId = lastUserId;
		lastUserId++;
		return nextId;
	}
	
	public boolean login(String username, String password) {
		if (username == this.username && password == this.password) {
			return true;
		}
		return false;
	}
	
	// public 
	
	@Override
	public String toString() {
		return "ok";
	}
}