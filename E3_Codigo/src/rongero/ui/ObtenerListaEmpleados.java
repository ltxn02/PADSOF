package ui;

import logic.Application;
import model.user.Employee;
import model.user.RegisteredUser;

import java.util.ArrayList;

public class ObtenerListaEmpleados {
    public static ArrayList<Employee> ObtenerListaEmpleados() {
        ArrayList<RegisteredUser> usuarios = Application.getInstance().getUsers();
        ArrayList<Employee> empleados = new ArrayList<>();
        for (RegisteredUser u : usuarios) {
            if (u instanceof Employee) {
                empleados.add((Employee) u);
            }
        }
        return empleados;
    }

}
