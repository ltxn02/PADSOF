package swing.view;
import javax.swing.*;
import java.awt.*;
public class login extends JFrame {
    public VentanaLogin() {
        setTitle("Rongero - Inicio de Sesión");
        setSize(1800, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 300));
        JLabel labelUsuario = new JLabel("Introduzca el nombre de usuario:");
        JTextField campoUsuario = new JTextField(20); panel.add(labelUsuario);
        panel.add(campoUsuario); JLabel labelcontrase = new JLabel("Introduzca la contraseña: ");
        JTextField campocontrase = new JTextField(20); panel.add(labelcontrase); panel.add(campocontrase);
        add(panel); setVisible(true);
    }
    public static void main(String[] args) {
        new login();
    }
}