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
 * Controlador para la gestión y administración de empleados.
 * Actúa como intermediario del patrón MVC, aislando por completo las reglas
 * de negocio de la aplicación de los componentes de la interfaz gráfica.
 * 
 * @author Lidia Martín
 */
public class GestorEmpleadoController {
    private VentanaPrincipa ventana;
    private PanelGestionEmpleados panel;

    /**
     * Constructor de la clase GestorEmpleadoController.
     * Vincula el controlador con la ventana principal y su panel de gestión asociado.
     * 
     * @param ventana Ventana principal de la interfaz gráfica.
     * @param panel   Panel contenedor donde se renderizan las vistas de los empleados.
     */
    public GestorEmpleadoController(VentanaPrincipa ventana, PanelGestionEmpleados panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Recupera del sistema la lista global de trabajadores que pertenecen al personal,
     * aplicando un filtro específico para excluir a aquellos con privilegios de Manager.
     * 
     * @return Un ArrayList con los objetos Staff calificados.
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
     * Filtra una lista base de empleados evaluando si el término de búsqueda coincide
     * de forma parcial o total con su nombre, nombre de usuario, correo o teléfono.
     * 
     * @param empleadosBase Lista original sobre la cual realizar la búsqueda.
     * @param termino       Cadena de texto o patrón a buscar (sensible a mayúsculas/minúsculas).
     * @return Un nuevo ArrayList con los empleados que cumplen alguna coincidencia.
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
     * Realiza las validaciones de consistencia de los campos de registro e instruye
     * la creación y persistencia de un nuevo Employee dentro del sistema de datos.
     * 
     * @param nombre          Nombre completo del nuevo trabajador.
     * @param fecha           Fecha de nacimiento en formato string.
     * @param dni             Documento Nacional de Identidad formateado.
     * @param usuario         Nombre de cuenta único para el acceso.
     * @param email           Correo electrónico institucional o de contacto.
     * @param telefono        Número telefónico.
     * @param password        Contraseña de acceso elegida.
     * @param confirmPassword Duplicado de verificación de la contraseña.
     * @param salario         Asignación económica base mensual del puesto.
     * @return true si el empleado fue creado y registrado con éxito; false en caso contrario.
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
     * Construye un resumen formateado en formato String con toda la información
     * detallada y el estado actual de un miembro del personal seleccionado.
     * 
     * @param empleado Instancia de Staff a consultar.
     * @return Una cadena de texto estructurada con los metadatos del empleado.
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
     * Solicita la remoción física o lógica de un empleado del registro central del sistema.
     * NOTA: Actualmente implementa un mensaje temporal de desarrollo (Placeholder).
     * 
     * @param empleado Instancia del empleado que se pretende eliminar.
     * @return true si la operación se procesó simuladamente con éxito; false ante anomalías.
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
     * Modifica los atributos personales editables de un empleado tras validar la 
     * integridad elemental del nuevo bloque de datos enviado.
     * NOTA: Actualmente implementa un mensaje temporal de desarrollo (Placeholder).
     * 
     * @param empleado El objeto Staff de destino a ser modificado.
     * @param nombre   Nuevo nombre completo a asignar.
     * @param email    Nuevo correo electrónico de contacto.
     * @param telefono Nuevo número de contacto.
     * @return true si los cambios fueron validados y procesados; false si hay error.
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
     * Cambia de manera interactiva el estado funcional (Activo/Inactivo) de un empleado,
     * reflejando su capacidad de logueo, e inicia el volcado de persistencia en disco duro.
     * 
     * @param empleado El miembro del personal afectado por la actualización.
     * @param activo   true para activar la cuenta del usuario, false para deshabilitarla.
     * @return true si el estado fue conmutado y serializado con éxito; false ante fallos.
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
     * Interroga al modelo para conocer si un determinado empleado posee el flag de
     * habilitación de operaciones activo en su cuenta.
     * 
     * @param empleado Instancia de Staff a evaluar.
     * @return true si el empleado está activo y habilitado; false en caso contrario.
     */
    public boolean obtenerEstadoEmpleado(Staff empleado) {
        if (empleado instanceof Employee) {
            return ((Employee) empleado).isEnabled();
        }
        return false;
    }
    
    /**
     * Agrega un nuevo privilegio de operación atómico a la lista de permisos del
     * empleado especificado y escribe de inmediato los cambios en el fichero local.
     * 
     * @param empleado Miembro del personal al que se le concederá el derecho.
     * @param permiso  Enumeración descriptiva del permiso funcional a agregar.
     * @return true si el permiso fue anexado con éxito al perfil del operario.
     */
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
    
    /**
     * Revoca un privilegio operativo específico de la lista de permisos asignados
     * a un empleado y resincroniza el almacenamiento local.
     * 
     * @param empleado Miembro del personal al que se le sustraerá el derecho.
     * @param permiso  Enumeración descriptiva del permiso funcional a retirar.
     * @return true si la operación de borrado y persistencia se completó satisfactoriamente.
     */
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
    
    /**
     * Reasigna de forma exclusiva y limpia el rol funcional o de cargo de un empleado, 
     * reemplazando cualquier rol previo y guardando la nueva configuración en el sistema.
     * 
     * @param empleado Miembro del personal cuyo rol será modificado.
     * @param rol      El nuevo EmployeeRoles corporativo que asumirá.
     * @return true si la actualización de roles concluyó exitosamente.
     */
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