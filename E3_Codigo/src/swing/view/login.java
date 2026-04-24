package swing.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class login extends JFrame {
    public login() {
        setTitle("Rongero - Inicio de Sesión");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(51, 66, 90));

        GridBagConstraints gbc = new GridBagConstraints();
        Font fuenteEtiquetas = new Font("SansSerif", Font.BOLD, 16);

        // --- FILA 0: IMAGEN (Escalado Proporcional) ---
        URL imgUrl = getClass().getResource("../../foto/logoVertical.png");

        if (imgUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgUrl);

            // --- LÓGICA DE REDIMENSIÓN PROPORCIONAL ---
            int anchoDeseado = 300; // Puedes cambiar este valor (ej. 200, 400)
            int altoOriginal = iconoOriginal.getIconHeight();
            int anchoOriginal = iconoOriginal.getIconWidth();

            // Calculamos el alto proporcional para que no se aplaste
            int altoProporcional = (altoOriginal * anchoDeseado) / anchoOriginal;

            // Image.SCALE_SMOOTH es el que mejor calidad da
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(anchoDeseado, altoProporcional, Image.SCALE_SMOOTH);
            JLabel labelImagen = new JLabel(new ImageIcon(imgEscalada));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(0, 0, 30, 0);
            panel.add(labelImagen, gbc);
        } else {
            JLabel errorImg = new JLabel("IMAGEN NO ENCONTRADA");
            errorImg.setForeground(Color.RED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(errorImg, gbc);
        }

        // --- FILA 1: USUARIO ---
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 10, 15, 10);

        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setForeground(Color.WHITE);
        labelUsuario.setFont(fuenteEtiquetas);
        panel.add(labelUsuario, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JTextField campoUsuario = new JTextField(15);
        panel.add(campoUsuario, gbc);

        // --- FILA 2: CONTRASEÑA ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 10, 0, 10);

        JLabel labelContrase = new JLabel("Contraseña: ");
        labelContrase.setForeground(Color.WHITE);
        labelContrase.setFont(fuenteEtiquetas);
        panel.add(labelContrase, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField campoContrase = new JPasswordField(15);
        panel.add(campoContrase, gbc);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new login();
    }
}