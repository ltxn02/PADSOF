package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import users.RegisteredUser;
import logic.Application;

public class PanelLogin extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton botonLogin;

    public PanelLogin(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;

        // Usamos un GridBagLayout para centrar fácilmente el formulario
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes entre componentes

        // 1. Título
        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        this.add(titulo, gbc);

        // 2. Campo Usuario
        gbc.gridwidth = 1; gbc.gridy = 1;
        this.add(new JLabel("Usuario:"), gbc);

        campoUsuario = new JTextField(15);
        gbc.gridx = 1;
        this.add(campoUsuario, gbc);

        // 3. Campo Contraseña (JPasswordField para ocultar texto)
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(new JLabel("Contraseña:"), gbc);

        campoPassword = new JPasswordField(15);
        gbc.gridx = 1;
        this.add(campoPassword, gbc);

        // 4. Botón de Login
        botonLogin = new JButton("Entrar");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        this.add(botonLogin, gbc);

        // --- CONTROLADOR (EVENTO DEL BOTÓN) ---
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });
    }

    private void intentarLogin() {
        String user = campoUsuario.getText();
        String pass = new String(campoPassword.getPassword()); // Extraer texto del JPasswordField

        try {
            // ¡Llamamos al Modelo (backend) que ya programaste!
            RegisteredUser usuarioLogueado = Application.login(user, pass);

            // Si funciona, mostramos un aviso y cambiamos de pantalla
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuarioLogueado.getUsername(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Aquí en el futuro haremos: ventanaPadre.mostrarPantalla("MENU_CLIENTE");
            // Por ahora, solo limpiaremos los campos
            campoUsuario.setText("");
            campoPassword.setText("");

        } catch (IOException ex) {
            // Si falla, mostramos un pop-up de error (JOptionPane)
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
        }
    }
}