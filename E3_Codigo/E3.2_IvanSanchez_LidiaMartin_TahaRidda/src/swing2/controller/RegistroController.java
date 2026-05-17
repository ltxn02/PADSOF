package swing2.controller;

import logic.Application;
import swing2.view.VentanaPrincipa;
import swing2.view.PanelRegistro;
import users.Client;
import utils.Notification;
import users.RegisteredUser;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controlador encargado de gestionar el proceso de registro de nuevos usuarios.
 * Valida los datos introducidos en la interfaz (como la obligatoriedad de campos, 
 * coincidencia de contraseñas, mayoría de edad y algoritmo oficial del DNI español) 
 * antes de proceder a la creación y persistencia del nuevo cliente en el sistema.
 * 
 * @author Taha Ridda
 */
public class RegistroController {
    private VentanaPrincipa ventana;
    private PanelRegistro panel;

    /**
     * Constructor de la clase RegistroController.
     * Vincula el controlador con la ventana principal y el panel de registro de la interfaz.
     * 
     * @param ventana Ventana principal de la aplicación que gestiona el cambio de pantallas.
     * @param panel   Panel que contiene el formulario físico de inserción de datos.
     */
    public RegistroController(VentanaPrincipa ventana, PanelRegistro panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Aplica el algoritmo oficial de validación del DNI español.
     * Verifica que el formato general coincida con 8 dígitos seguidos de una letra
     * y comprueba de forma matemática que la letra proporcionada corresponda con el 
     * residuo del cálculo del número entre 23.
     * 
     * @param dni Cadena de texto que representa el DNI completo a evaluar.
     * @return true si el DNI es sintáctica y matemáticamente válido; false en caso contrario.
     */
    private boolean validarDniAlgoritmo(String dni) {
        
        if (dni == null || !dni.matches("^[0-9]{8}[A-Z]$")) {
            return false;
        }

        try {
            String numerosStr = dni.substring(0, 8);
            char letraUsuario = dni.charAt(8);

            
            int numeroDni = Integer.parseInt(numerosStr);
            int resto = numeroDni % 23;

            
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            char letraCorrecta = letras.charAt(resto);

            return letraUsuario == letraCorrecta;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Procesa las peticiones de alta de un nuevo usuario cliente en el sistema.
     * Ejecuta de forma secuencial controles sobre los campos obligatorios, el formato de fecha, 
     * la edad mínima permitida por la configuración del sistema, la integridad de las claves 
     * de acceso y la validez legal del documento de identidad provisto.
     * 
     * @param nombre      Nombre completo del aspirante a registrarse.
     * @param fechaStr    Fecha de nacimiento en formato de texto estándar (DD/MM/AAAA).
     * @param dni         Documento Nacional de Identidad para la validación algorítmica.
     * @param user        Nombre de cuenta único o nickname para el acceso al entorno.
     * @param email       Correo electrónico para notificaciones y contacto.
     * @param tlf         Teléfono de contacto asociado al usuario.
     * @param pass        Contraseña de seguridad de la cuenta.
     * @param confirmPass Duplicado exacto para la verificación de la contraseña.
     */
    public void procesarRegistro(String nombre, String fechaStr, String dni, String user, String email, String tlf, String pass, String confirmPass) {

        
        if (nombre.trim().isEmpty() || user.trim().isEmpty() || dni.trim().isEmpty() ||
                email.trim().isEmpty() || pass.isEmpty() || tlf.trim().isEmpty() || fechaStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Todos los campos son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaStr.trim(), formatter);
            LocalDate hoy = LocalDate.now();

            
            int edad = Period.between(fechaNacimiento, hoy).getYears();

            
            int edadPermitida = Application.getEdadMinimaRegistro();

            if (edad < edadPermitida) {
                JOptionPane.showMessageDialog(panel,
                        "Lo sentimos, debes tener al menos " + edadPermitida + " años para registrarte.",
                        "Edad insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Formato de fecha incorrecto (DD-MM-AAAA).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        String dniLimpio = dni.trim().toUpperCase();
        if (!validarDniAlgoritmo(dniLimpio)) {
            JOptionPane.showMessageDialog(panel,
                    "El DNI introducido no es válido.\nRecuerde: 8 números y la letra correspondiente (sin espacios).",
                    "DNI Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        try {
            Client nuevoCliente = new Client(
                    user.trim(),
                    pass,
                    nombre.trim(),
                    dniLimpio, 
                    fechaStr.trim(),
                    email.trim(),
                    tlf.trim()
            );

            
            Application.registerClient(nuevoCliente);

            
            ArrayList<RegisteredUser> destinatarios = new ArrayList<>();
            destinatarios.add(nuevoCliente);
            Notification bienvenida = new Notification("¡Bienvenido a Rongero! Disfruta de la experiencia.", destinatarios);
            nuevoCliente.addNotification(bienvenida);

            mostrarConfirmacionExito();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(panel, "ERROR DE VALIDACIÓN: " + e.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "ERROR: " + e.getMessage(), "Usuario duplicado", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Muestra un cuadro de diálogo informativo que indica que el registro concluyó 
     * de manera exitosa, vacía el contenido residual de los campos del formulario 
     * y redirige el foco de la interfaz gráfica de vuelta hacia la pantalla de Login.
     */
    private void mostrarConfirmacionExito() {
        Object[] opciones = {"Aceptar"};
        int seleccion = JOptionPane.showOptionDialog(
                panel,
                "Usuario registrado correctamente. Ya puedes iniciar sesión.",
                "Registro completado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == JOptionPane.OK_OPTION || seleccion == JOptionPane.CLOSED_OPTION) {
            panel.limpiarCampos();
            ventana.mostrarPantalla("LOGIN");
        }
    }
}
