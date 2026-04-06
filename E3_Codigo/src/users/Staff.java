package users;
import utils.*;
import transactions.*;
import catalog.*;

public abstract class Staff extends RegisteredUser implements java.io.Serializable{
    private double salary;

    public Staff(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary) {
        super(username, password, fullname, dni, birthdate, email, phoneNumber);
        this.salary = salary;
    }
}
