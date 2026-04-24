package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import users.RegisteredUser;
import logic.Application;

public class PanelLogin extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton botonLogin;

    public PanelLogin(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;

        // 1. CONFIGURACIÓN DEL FONDO (Gris oscuro)
        this.setBackground(new Color(51, 66, 90));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10);

        // --- FILA 0: LOGO ---
        URL imgUrl = getClass().getResource("../foto/logoVertical.png");
        if (imgUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgUrl);
            int anchoDeseado = 250;
            int altoProporcional = (iconoOriginal.getIconHeight() * anchoDeseado) / iconoOriginal.getIconWidth();
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(anchoDeseado, altoProporcional, Image.SCALE_SMOOTH);
            JLabel labelImagen = new JLabel(new ImageIcon(imgEscalada));

            gbcMain.gridx = 0;
            gbcMain.gridy = 0; // Fila superior exclusiva para el logo
            gbcMain.gridwidth = 1;
            gbcMain.insets = new Insets(0, 0, 20, 0);
            this.add(labelImagen, gbcMain);
        }

        // 2. EL "CUADRADO AZUL" (Sub-panel con bordes redondeados)
        // Usamos una clase anónima para dibujar el fondo redondeado
        JPanel panelAzul = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 80, 140)); // Color azul del cuadro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // 40 es el radio de la curva
                g2.dispose();
            }
        };
        panelAzul.setOpaque(false); // Para que se vea la redondez
        panelAzul.setBorder(BorderFactory.createEmptyBorder(20, 30, 25, 30)); // Margen interno

        // Layout interno del cuadro azul
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(8, 8, 8, 8);
        gbcForm.fill = GridBagConstraints.HORIZONTAL; // Para que los campos blancos tengan ancho

        // --- INTERNO FILA 0: TÍTULO ---
        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        gbcForm.gridx = 0; gbcForm.gridy = 0; gbcForm.gridwidth = 2;
        panelAzul.add(titulo, gbcForm);

        // --- INTERNO FILA 1: USUARIO ---
        gbcForm.gridwidth = 1; gbcForm.gridy = 1;
        JLabel labelUser = new JLabel("Usuario:");
        labelUser.setForeground(new Color(210, 220, 240));
        gbcForm.gridx = 0;
        panelAzul.add(labelUser, gbcForm);

        campoUsuario = new JTextField(15);
        campoUsuario.setBackground(Color.WHITE); // Fondo blanco
        campoUsuario.setOpaque(true);
        campoUsuario.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        gbcForm.gridx = 1;
        panelAzul.add(campoUsuario, gbcForm);

        // --- INTERNO FILA 2: CONTRASEÑA ---
        gbcForm.gridx = 0; gbcForm.gridy = 2;
        JLabel labelPassword = new JLabel("Contraseña:");
        labelPassword.setForeground(new Color(210, 220, 240));
        panelAzul.add(labelPassword, gbcForm);

        campoPassword = new JPasswordField(15);
        campoPassword.setBackground(Color.WHITE); // Fondo blanco
        campoPassword.setOpaque(true);
        campoPassword.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        gbcForm.gridx = 1;
        panelAzul.add(campoPassword, gbcForm);

        // --- INTERNO FILA 3: BOTÓN ---
        botonLogin = new JButton("ENTRAR");
        botonLogin.setBackground(new Color(0, 160, 210));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFocusPainted(false);
        gbcForm.gridx = 0; gbcForm.gridy = 3; gbcForm.gridwidth = 2;
        gbcForm.insets = new Insets(20, 0, 10, 0);
        panelAzul.add(botonLogin, gbcForm);

        // 3. AÑADIMOS EL CUADRO AZUL AL PANEL PRINCIPAL (Fila 1)
        gbcMain.gridy = 1;
        this.add(panelAzul, gbcMain);

        // Controlador
        botonLogin.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String user = campoUsuario.getText();
        String pass = new String(campoPassword.getPassword());
        try {
            RegisteredUser usuario = Application.login(user, pass);
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getUsername());
            ventanaPadre.mostrarPantalla("CATALOGO");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}