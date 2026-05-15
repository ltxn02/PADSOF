package swing2.view;

import swing2.controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PanelLoginn extends JPanel {

    private VentanaPrincipa ventanaPadre;
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private LoginController controller;

    public PanelLoginn(VentanaPrincipa ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        this.controller = new LoginController(ventanaPadre, this);

        this.setBackground(new Color(51, 66, 90));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();

        
        URL imgUrl = getClass().getResource("../../foto/logoVertical.png");
        if (imgUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgUrl);
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(220, -1, Image.SCALE_SMOOTH);
            gbcMain.gridy = 0;
            gbcMain.insets = new Insets(0, 0, 30, 0);
            this.add(new JLabel(new ImageIcon(imgEscalada)), gbcMain);
        }

        
        JPanel panelAzul = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 80, 140));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        panelAzul.setOpaque(false);
        panelAzul.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 10, 10, 10);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel titulo = new JLabel("ACCESO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        gbcForm.gridx = 0; gbcForm.gridy = 0; gbcForm.gridwidth = 2;
        panelAzul.add(titulo, gbcForm);

        
        gbcForm.gridwidth = 1;
        gbcForm.gridy = 1; gbcForm.gridx = 0;
        panelAzul.add(crearLabel("Usuario:"), gbcForm);
        campoUsuario = new JTextField(15);
        gbcForm.gridx = 1;
        panelAzul.add(campoUsuario, gbcForm);

        gbcForm.gridy = 2; gbcForm.gridx = 0;
        panelAzul.add(crearLabel("Contraseña:"), gbcForm);
        campoPassword = new JPasswordField(15);
        gbcForm.gridx = 1;
        panelAzul.add(campoPassword, gbcForm);

        
        botonLogin = new JButton("ENTRAR");
        botonLogin.setBackground(new Color(0, 160, 210));
        botonLogin.setForeground(Color.WHITE);
        gbcForm.gridy = 3; gbcForm.gridx = 0; gbcForm.gridwidth = 2;
        gbcForm.insets = new Insets(20, 10, 5, 10);
        panelAzul.add(botonLogin, gbcForm);

        
        JButton btnRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnRegistro.setForeground(new Color(200, 220, 255));
        btnRegistro.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setBorderPainted(false);
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbcForm.gridy = 4;
        gbcForm.insets = new Insets(0, 10, 10, 10);
        panelAzul.add(btnRegistro, gbcForm);

        

        
        botonLogin.addActionListener(e -> {
            controller.ejecutarLogin(campoUsuario.getText().trim(), new String(campoPassword.getPassword()));
        });

        
        btnRegistro.addActionListener(e -> {
            ventanaPadre.mostrarPantalla("REGISTRO");
        });

        gbcMain.gridy = 1;
        this.add(panelAzul, gbcMain);
    }

    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        return l;
    }

    public void limpiarCampos() {
        campoUsuario.setText("");
        campoPassword.setText("");
    }
}