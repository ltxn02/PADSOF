package swing2.controller;

import logic.Application;
import users.RegisteredUser;
import swing2.view.VentanaPrincipa;
import swing2.view.PanelLoginn;
import javax.swing.JOptionPane;
import java.io.IOException;

public class LoginController {
    private VentanaPrincipa ventana;
    private PanelLoginn panel;

    public LoginController(VentanaPrincipa ventana, PanelLoginn panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    public void ejecutarLogin(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Rellena todos los campos.");
            return;
        }

        try {
            RegisteredUser usuario = Application.login(user, pass);
            if (usuario != null) {
                // Notificamos el éxito para que el Inicio se regenere con recomendaciones
                ventana.cambiarSesion(usuario);
                panel.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(panel, "Usuario o contraseña incorrectos.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Error al acceder a los datos.");
        }
    }
}