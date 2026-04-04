import java.util.Date;

public abstract class Staff extends RegisteredUser{
    private double salary;

    public Staff(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary) {
        super(username, password, fullname, dni, birthdate, email, phoneNumber);
        this.salary = salary;
    }
}
