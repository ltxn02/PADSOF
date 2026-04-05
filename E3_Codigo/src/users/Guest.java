package users;
import utils.*;
import transactions.*;
import catalog.*;
import java.util.*;

public class Guest extends User {
	public Client createClientAccount(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		Client account = new Client(username, password, fullname, dni, birthdate, email, phoneNumber);
		return account;
	}
}