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

        // Título
        JLabel titulo = new JLabel("CREAR NUEVA CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 3;
        this.add(titulo, g);

        g.gridwidth = 1;
        int r = 1;

        // Filas alineadas (DNI y Correo ahora estarán en línea perfecta)
        agregarFilaFormulario("Nombre Completo:", txtNombre = new JTextField(15), null, g, r++);
        agregarFilaFormulario("F. Nacimiento (DD/MM/AAAA):", txtFecha = new JTextField(15), null, g, r++);
        agregarFilaFormulario("DNI (8 nms + letra):", txtDni = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Nombre Usuario:", txtUser = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Correo Electrónico:", txtEmail = new JTextField(15), null, g, r++);
        agregarFilaFormulario("Teléfono:", txtTlf = new JTextField(15), null, g, r++);

        // Contraseña 1 con botón
        txtPass = new JPasswordField(15);
        JToggleButton btnVer1 = crearBotonOjo(txtPass);
        agregarFilaFormulario("Contraseña:", txtPass, btnVer1, g, r++);

        // Contraseña 2 con botón
        txtConfirm = new JPasswordField(15);
        JToggleButton btnVer2 = crearBotonOjo(txtConfirm);
        agregarFilaFormulario("Confirmar Pass:", txtConfirm, btnVer2, g, r++);

        // Botón Registrar
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
    }

    private void agregarFilaFormulario(String etiqueta, JTextField campo, JToggleButton boton, GridBagConstraints g, int fila) {
        g.gridy = fila;

        // Columna 0: Etiqueta alineada a la derecha
        g.gridx = 0;
        g.anchor = GridBagConstraints.EAST;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        this.add(lbl, g);

        // Columna 1: Campo de texto
        g.gridx = 1;
        g.anchor = GridBagConstraints.CENTER;
        this.add(campo, g);

        // Columna 2: Botón de ojo o espacio vacío para mantener alineación
        g.gridx = 2;
        if (boton != null) {
            this.add(boton, g);
        } else {
            // Añadimos un espacio invisible del mismo tamaño que el botón
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