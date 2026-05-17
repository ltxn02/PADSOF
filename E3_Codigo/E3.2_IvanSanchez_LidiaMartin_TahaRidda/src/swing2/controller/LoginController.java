package swing2.controller;

import logic.Application;
import users.RegisteredUser;
import swing2.view.VentanaPrincipa;
import swing2.view.PanelLoginn;
import javax.swing.JOptionPane;
import java.io.IOException;

/**
 * Controlador encargado de gestionar el proceso de autenticación e inicio de sesión.
 * Actúa como intermediario en el patrón MVC para validar las credenciales introducidas
 * en la interfaz gráfica y coordinar el cambio de estado de la sesión en la aplicación.
 * 
 * @author Taha Ridda
 */
public class LoginController {
    private VentanaPrincipa ventana;
    private PanelLoginn panel;

    /**
     * Constructor de la clase LoginController.
     * Vincula el controlador con la ventana principal y su panel de login asociado.
     * 
     * @param ventana Ventana principal de la aplicación que coordina el estado global de la vista.
     * @param panel   Panel específico que contiene el formulario de inicio de sesión.
     */
    public LoginController(VentanaPrincipa ventana, PanelLoginn panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Ejecuta la lógica de autenticación del usuario.
     * Verifica que los campos obligatorios no estén vacíos (contemplando la excepción de bypass 
     * para el usuario especial "sacha") e invoca el método de login en la capa de negocio 
     * para actualizar la sesión activa o reportar anomalías mediante mensajes emergentes.
     * 
     * @param user Nombre de cuenta o alias del usuario que intenta acceder.
     * @param pass Contraseña de seguridad asociada a la cuenta.
     */
    public void ejecutarLogin(String user, String pass) {
        if (user.isEmpty() || (pass.isEmpty() && !user.equalsIgnoreCase("sacha"))) {
            JOptionPane.showMessageDialog(panel, "Rellena todos los campos.");
            return;
        }

        try {
            RegisteredUser usuario = Application.login(user, pass);
            if (usuario != null) {
                
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