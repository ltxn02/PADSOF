package ui;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    public VentanaLogin() {
        setTitle("Rongero - Inicio de Sesión");
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel labelUsuario = new JLabel("Introduzca el nombre de usuario:");
        JTextField campoUsuario = new JTextField(20);

        add(labelUsuario);
        add(campoUsuario);
        setVisible(true);
    }
    public static void main(String[] args) {
        new VentanaLogin();
    }
}