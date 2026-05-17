package swing2.view;

import swing2.controller.RegistroController;
import javax.swing.*;
import java.awt.*;

public class PanelRegistro extends JPanel {
    private JTextField txtNombre, txtFecha, txtDni, txtUser, txtEmail, txtTlf;
    private JPasswordField txtPass, txtConfirm;

    public PanelRegistro(VentanaPrincipa ventana) {
        RegistroController controller = new RegistroController(ventana, this);

        this.setBackground(new Color(51, 66, 90));
        this.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel titulo = new JLabel("CREAR NUEVA CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 3;
        this.add(titulo, g);

        g.gridwidth = 1;
        int r = 1;

        
        agregarFilaFormulario("Nombre completo:", txtNombre = new JTextField(15), null, g, r++);
        agregarFilaFormulario("F. nacimiento (DD/MM/AAAA):", txtFecha = new JTextField(15), null, g, r++);
        agregarFilaFormulario("DNI (8 nms + letra):", txtDni = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Nombre usuario:", txtUser = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Correo electrónico:", txtEmail = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Teléfono:", txtTlf = new JTextField(15), null, g, r++);

        
        txtPass = new JPasswordField(15);
        JToggleButton btnVer1 = crearBotonOjo(txtPass);
        agregarFilaFormulario("Contraseña:", txtPass, btnVer1, g, r++);

        
        txtConfirm = new JPasswordField(15);
        JToggleButton btnVer2 = crearBotonOjo(txtConfirm);
        agregarFilaFormulario("Confirmar pass:", txtConfirm, btnVer2, g, r++);

        
        JButton btnFinalizar = new JButton("FINALIZAR REGISTRO");
        btnFinalizar.setBackground(new Color(46, 204, 113));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.addActionListener(e -> controller.procesarRegistro(
                txtNombre.getText(), txtFecha.getText(), txtDni.getText(),
                txtUser.getText(), txtEmail.getText(), txtTlf.getText(),
                new String(txtPass.getPassword()), new String(txtConfirm.getPassword())
        ));

        g.gridy = r; g.gridx = 0; g.gridwidth = 3;
        g.insets = new Insets(20, 8, 8, 8);
        this.add(btnFinalizar, g);
        // --- BOTÓN VOLVER AL INICIO ---
        JButton btnVolver = new JButton("¿Ya tienes cuenta? Inicia sesión o vuelve atrás");
        btnVolver.setForeground(new Color(200, 220, 255));
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Lo añadimos debajo del botón finalizar
        g.gridy = r + 1;
        g.gridx = 0;
        g.gridwidth = 3;
        g.insets = new Insets(0, 8, 8, 8);
        this.add(btnVolver, g);

        // Acción para volver
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            ventana.mostrarPantalla("INICIO");
        });

    }

    private void agregarFilaFormulario(String etiqueta, JTextField campo, JToggleButton boton, GridBagConstraints g, int fila) {
        g.gridy = fila;

        
        g.gridx = 0;
        g.anchor = GridBagConstraints.EAST;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        this.add(lbl, g);

        
        g.gridx = 1;
        g.anchor = GridBagConstraints.CENTER;
        this.add(campo, g);

        
        g.gridx = 2;
        if (boton != null) {
            this.add(boton, g);
        } else {
            
            this.add(Box.createHorizontalStrut(45), g);
        }
    }

    private JToggleButton crearBotonOjo(JPasswordField campo) {
        JToggleButton btn = new JToggleButton("👁");
        btn.setPreferredSize(new Dimension(45, 25));
        btn.addActionListener(e -> campo.setEchoChar(btn.isSelected() ? (char) 0 : '•'));
        return btn;
    }

    public void limpiarCampos() {
        txtNombre.setText(""); txtFecha.setText(""); txtDni.setText("");
        txtUser.setText(""); txtEmail.setText(""); txtTlf.setText("");
        txtPass.setText(""); txtConfirm.setText("");
    }
}