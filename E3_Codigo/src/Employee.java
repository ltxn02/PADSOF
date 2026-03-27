import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Staff{
    private boolean enabled;
    public ArrayList<EmployeeRoles> Rol;
    public ArrayList<Permission> permissions;
    public Employee(String username, String password, String fullname, String dni, Date birthdate, String email, String phoneNumber, double salary, boolean enabled){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
        this.enabled = enabled;
    }
}
