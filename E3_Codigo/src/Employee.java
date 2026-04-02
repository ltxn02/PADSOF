import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee extends Staff{
    private boolean enabled;
    public ArrayList<EmployeeRoles> Rol;
    public ArrayList<Permission> permissions;
    public Employee(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
        this.enabled = enabled;
        this.permissions= new ArrayList<>();
    }
    public void add_permisions(Permission e){
        this.permissions.add(e);
    }

    public void delete_permisions(Permission e){
        if (this.permissions.contains(e)) {
            this.permissions.remove(e);
        }
    }

    public ArrayList<Permission> permisosEmpleado(){
        return this.permissions;
    }

    public void add_roles(EmployeeRoles Rol){
        this.Rol.add(Rol);
    }

    public void delete_roles(EmployeeRoles Rol){
        this.Rol.remove(Rol);
    }

    public void activateEmployee(){
        this.enabled = true;
    }

    public void desactivateEmployee(){
        this.enabled = false;
    }



}
