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

public class RegistroController {
    private VentanaPrincipa ventana;
    private PanelRegistro panel;

    public RegistroController(VentanaPrincipa ventana, PanelRegistro panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Algoritmo oficial de validación de DNI español
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
