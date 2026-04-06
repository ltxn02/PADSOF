package users;
import utils.*;
import transactions.*;
import catalog.*;
import java.util.Date;

public class Manager extends Staff implements java.io.Serializable{

    public Manager (String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
    }






}
