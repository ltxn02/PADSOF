package swing2.controller.gestor;

import logic.Application;
import users.RegisteredUser;
import users.Staff;
import utils.EmployeeRoles;
import users.Employee;
import users.Manager;
import utils.Permission;
import swing2.view.VentanaPrincipa;
import swing2.view.gestor.empleados.PanelGestionEmpleados;

import javax.swing.JOptionPane;

import java.util.ArrayList;

/**
 * Controlador para gestionar empleados.
 * Separa la lógica de negocio de la interfaz gráfica.
 */
public class GestorEmpleadoController {
    private VentanaPrincipa ventana;
    private PanelGestionEmpleados panel;

    public GestorEmpleadoController(VentanaPrincipa ventana, PanelGestionEmpleados panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Obtener lista de todos los empleados (excluyendo managers)
     */
    public ArrayList<Staff> obtenerEmpleados() {
        ArrayList<Staff> empleados = new ArrayList<>();
        
        // Obtener todos los usuarios de Application
        ArrayList<RegisteredUser> todosLosUsuarios = Application.getUsers();
        
        if (todosLosUsuarios != null) {
            for (RegisteredUser usuario : todosLosUsuarios) {
                // Filtrar solo los que son Staff pero NO Manager
                if (usuario instanceof Staff && !(usuario instanceof Manager)) {
                    empleados.add((Staff) usuario);
                }
            }
        }
        
        return empleados;
    }

    /**
     * Buscar empleados por término
     */
    public ArrayList<Staff> buscarEmpleados(ArrayList<Staff> empleadosBase, String termino) {
        ArrayList<Staff> resultados = new ArrayList<>();
        String terminoMinuscula = termino.toLowerCase().trim();

        // Si el término está vacío, devolver todos
        if (terminoMinuscula.isEmpty()) {
            return new ArrayList<>(empleadosBase);
        }

        // Buscar en diferentes campos
        for (Staff empleado : empleadosBase) {
            if (empleado.getFullname().toLowerCase().contains(terminoMinuscula) ||
                empleado.getUsername().toLowerCase().contains(terminoMinuscula) ||
                empleado.getEmail().toLowerCase().contains(terminoMinuscula) ||
                empleado.getPhoneNumber().contains(terminoMinuscula)) {
                resultados.add(empleado);
            }
        }

        return resultados;
    }

    /**
     * Crear un nuevo empleado
     * @return true si se crea exitosamente, false si hay error
     */
    /**
     * Crear un nuevo empleado
     * @return true si se crea exitosamente, false si hay error
     */
    public boolean crearEmpleado(String nombre, String fecha, String dni, String usuario, 
                                  String email, String telefono, String password, 
                                  String confirmPassword, double salario) {
        
        // VALIDACIONES
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usuario == null || usuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El usuario es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El email es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El teléfono es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dni == null || dni.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El DNI es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (fecha == null || fecha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "La fecha de nacimiento es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "La contraseña es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (salario <= 0) {
            JOptionPane.showMessageDialog(panel, "El salario debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificar que el usuario no existe ya
        ArrayList<RegisteredUser> usuarios = Application.getUsers();
        for (RegisteredUser u : usuarios) {
            if (u.getUsername().equals(usuario)) {
                JOptionPane.showMessageDialog(panel, "El usuario '" + usuario + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        try {
            // Crear el nuevo empleado con salario
            Employee nuevoEmpleado = new Employee(
                usuario,
                password,
                nombre,
                dni,
                fecha,
                email,
                telefono,
                salario,  // SALARIO PERSONALIZADO
                true      // Activo por defecto
            );

            // Registrar en la aplicación
            Application.registerEmployee(nuevoEmpleado);

            // Mensaje de éxito
            JOptionPane.showMessageDialog(
                panel,
                "✅ Empleado '" + usuario + "' creado exitosamente.\n" +
                "Salario: €" + String.format("%.2f", salario),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                panel,
                "❌ Error al crear empleado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    /**
     * Obtener detalles de un empleado
     */
    public String obtenerDetallesEmpleado(Staff empleado) {
        if (empleado == null) {
            return "Empleado no encontrado";
        }

        String estado = empleado.isActive() ? "✅ Activo" : "❌ Inactivo";

        return "📋 DETALLES DEL EMPLEADO\n\n" +
               "Nombre completo: " + empleado.getFullname() + "\n" +
               "Usuario: " + empleado.getUsername() + "\n" +
               "Email: " + empleado.getEmail() + "\n" +
               "Teléfono: " + empleado.getPhoneNumber() + "\n" +
               "DNI: " + empleado.getMaskedDni() + "\n" +
               "Salario: €" + String.format("%.2f", empleado.getSalary()) + "\n" +
               "Estado: " + estado;
    }

    /**
     * Eliminar un empleado
     */
    public boolean eliminarEmpleado(Staff empleado) {
        try {
            // Aquí iría la lógica para eliminar
            // Por ahora solo mostramos un placeholder
            JOptionPane.showMessageDialog(
                panel,
                "❌ ELIMINAR EMPLEADO (PRÓXIMAMENTE)\n\n" +
                "Se eliminaría a: " + empleado.getUsername(),
                "Eliminar empleado",
                JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Editar un empleado
     */
    public boolean editarEmpleado(Staff empleado, String nombre, String email, String telefono) {
        try {
            // Validaciones
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Aquí iría la lógica para editar
            JOptionPane.showMessageDialog(
                panel,
                "✏️ EDITAR EMPLEADO (PRÓXIMAMENTE)\n\n" +
                "Se editaría a: " + empleado.getUsername(),
                "Editar empleado",
                JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al editar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Cambiar el estado de un empleado (Activo/Inactivo)
     */
    public boolean cambiarEstadoEmpleado(Staff empleado, boolean activo) {
        try {
            if (empleado instanceof Employee) {
                Employee emp = (Employee) empleado;
                
                if (activo) {
                    emp.activateEmployee();
                    JOptionPane.showMessageDialog(
                        panel,
                        "✅ Usuario " + emp.getUsername() + " activado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    emp.desactivateEmployee();
                    JOptionPane.showMessageDialog(
                        panel,
                        "❌ Usuario " + emp.getUsername() + " desactivado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
                
                // Guardar cambios
                Application.guardarDatos("rongero_data.dat");
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                panel,
                "❌ Error al cambiar estado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        return false;
    }

    /**
     * Obtener el estado actual de un empleado
     */
    public boolean obtenerEstadoEmpleado(Staff empleado) {
        if (empleado instanceof Employee) {
            return ((Employee) empleado).isEnabled();
        }
        return false;
    }
    
    public boolean agregarPermisoEmpleado(Staff empleado, Permission permiso) {
        try {
            if (empleado instanceof Employee) {
                Employee emp = (Employee) empleado;
                if (!emp.permissions.contains(permiso)) {
                    emp.add_permisions(permiso);
                    Application.guardarDatos("rongero_data.dat");
                }
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al añadir permiso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public boolean quitarPermisoEmpleado(Staff empleado, Permission permiso) {
        try {
            if (empleado instanceof Employee) {
                Employee emp = (Employee) empleado;
                if (emp.permissions.contains(permiso)) {
                    emp.delete_permisions(permiso);
                    Application.guardarDatos("rongero_data.dat");
                }
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al quitar permiso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public boolean cambiarRolEmpleado(Staff empleado, EmployeeRoles rol) {
        try {
            if (empleado instanceof Employee) {
                Employee emp = (Employee) empleado;
                emp.Rol.clear();
                emp.add_roles(rol);
                Application.guardarDatos("rongero_data.dat");
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error al cambiar rol: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}