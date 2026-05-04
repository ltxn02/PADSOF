package swing2.controller;

import logic.Application;
import swing2.view.VentanaPrincipa;
import swing2.view.PanelRegistro;
import users.Client;
import utils.Notification;
import users.RegisteredUser;

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

    public void procesarRegistro(String nombre, String fechaStr, String dni, String user, String email, String tlf, String pass, String confirmPass) {

        // 1. Validaciones básicas de campos vacíos
        if (nombre.trim().isEmpty() || user.trim().isEmpty() || dni.trim().isEmpty() ||
                email.trim().isEmpty() || pass.isEmpty() || tlf.trim().isEmpty() || fechaStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Todos los campos son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validación de coincidencia de contraseña
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. INTENTO DE REGISTRO
        try {
            // Enviamos la fecha tal cual la escribe el usuario (DD-MM-AAAA)
            // No usamos LocalDate.parse aquí para no chocar con la validación interna de Client
            Client nuevoCliente = new Client(
                    user.trim(),
                    pass,
                    nombre.trim(),
                    dni.trim(),
                    fechaStr.trim(), // Se envía como String para que Client lo valide
                    email.trim(),
                    tlf.trim()
            );

            // Intentamos registrar
            Application.registerClient(nuevoCliente);

            // --- Lógica de Notificación (Sincronizada con tu main) ---
            ArrayList<RegisteredUser> destinatarios = new ArrayList<>();
            destinatarios.add(nuevoCliente);
            Notification bienvenida = new Notification("¡Bienvenido a Rongero! Disfruta de la experiencia.", destinatarios);
            nuevoCliente.addNotification(bienvenida);

            mostrarConfirmacionExito();

        } catch (IllegalArgumentException e) {
            // Si la fecha, DNI o Email están mal según la lógica interna, saltará aquí
            // El mensaje dirá exactamente qué formato espera (ej: "Date must be DD-MM-AAAA")
            JOptionPane.showMessageDialog(panel, "ERROR DE VALIDACIÓN: " + e.getMessage(), "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            // Error de usuario duplicado
            JOptionPane.showMessageDialog(panel, "ERROR: " + e.getMessage(), "Usuario duplicado", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Error genérico para cualquier otro fallo
            JOptionPane.showMessageDialog(panel, "Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarConfirmacionExito() {
        Object[] opciones = {"Aceptar"};
        int seleccion = JOptionPane.showOptionDialog(
                panel,
                "Usuario registrado correctamente. Ya puedes iniciar sesión.",
                "Registro Completado",
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