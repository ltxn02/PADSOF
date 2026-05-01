package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class PanelRegistro extends JPanel {

    private VentanaPrincipal ventanaPadre;

    // Declaración de los campos
    private JTextField campoNombreCompleto;
    private JTextField campoDNI;
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JTextField campoFechaNacimiento;
    private JTextField campoEmail;
    private JTextField campoTelefono;

    private JButton botonRegistrar;

    public PanelRegistro(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;

        // 1. CONFIGURACIÓN DEL FONDO (Gris oscuro)
        this.setBackground(new Color(51, 66, 90));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(5, 10, 5, 10);

        // --- FILA 0: LOGO ---
        URL imgUrl = getClass().getResource("../foto/logoVertical.png");
        if (imgUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgUrl);
            int anchoDeseado = 180; // Un poco más pequeño para dejar sitio a los campos
            int altoProporcional = (iconoOriginal.getIconHeight() * anchoDeseado) / iconoOriginal.getIconWidth();
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(anchoDeseado, altoProporcional, Image.SCALE_SMOOTH);
            JLabel labelImagen = new JLabel(new ImageIcon(imgEscalada));

            gbcMain.gridx = 0; gbcMain.gridy = 0; gbcMain.gridwidth = 1;
            gbcMain.insets = new Insets(0, 0, 10, 0);
            this.add(labelImagen, gbcMain);
        }

        // 2. EL "CUADRADO AZUL" (Sub-panel con bordes redondeados)
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
        panelAzul.setBorder(BorderFactory.createEmptyBorder(15, 30, 20, 30));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 8, 5, 8); // Márgenes más ajustados por haber más campos
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Registro de Cliente");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        gbcForm.gridx = 0; gbcForm.gridy = 0; gbcForm.gridwidth = 2;
        panelAzul.add(titulo, gbcForm);

        // --- CAMPOS DE TEXTO ---
        // Fila 1: Nombre
        agregarEtiqueta("Nombre completo:", 1, gbcForm, panelAzul);
        campoNombreCompleto = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoNombreCompleto, gbcForm);

        // Fila 2: DNI
        agregarEtiqueta("DNI:", 2, gbcForm, panelAzul);
        campoDNI = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoDNI, gbcForm);

        // Fila 3: Usuario
        agregarEtiqueta("Nombre de usuario:", 3, gbcForm, panelAzul);
        campoUsuario = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoUsuario, gbcForm);

        // Fila 4: Contraseña (PasswordField)
        agregarEtiqueta("Contraseña:", 4, gbcForm, panelAzul);
        campoPassword = new JPasswordField(15);
        campoPassword.setBackground(Color.WHITE); campoPassword.setOpaque(true);
        campoPassword.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        gbcForm.gridx = 1; panelAzul.add(campoPassword, gbcForm);

        // Fila 5: Fecha
        agregarEtiqueta("Fecha nac. (DD/MM/AAAA):", 5, gbcForm, panelAzul);
        campoFechaNacimiento = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoFechaNacimiento, gbcForm);

        // Fila 6: Email
        agregarEtiqueta("Email:", 6, gbcForm, panelAzul);
        campoEmail = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoEmail, gbcForm);

        // Fila 7: Teléfono
        agregarEtiqueta("Número de teléfono:", 7, gbcForm, panelAzul);
        campoTelefono = crearCampoTexto();
        gbcForm.gridx = 1; panelAzul.add(campoTelefono, gbcForm);

        // --- BOTÓN REGISTRAR ---
        botonRegistrar = new JButton("REGISTRARSE");
        botonRegistrar.setBackground(new Color(0, 160, 210));
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);
        gbcForm.gridx = 0; gbcForm.gridy = 8; gbcForm.gridwidth = 2;
        gbcForm.insets = new Insets(15, 0, 5, 0);
        panelAzul.add(botonRegistrar, gbcForm);

        // --- BOTÓN VOLVER (Como enlace) ---
        JButton botonVolver = new JButton("Volver al Login");
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 12));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcForm.gridy = 9; gbcForm.anchor = GridBagConstraints.EAST; gbcForm.fill = GridBagConstraints.NONE;
        panelAzul.add(botonVolver, gbcForm);

        // 3. AÑADIMOS EL CUADRO AZUL AL PANEL PRINCIPAL
        gbcMain.gridy = 1;
        this.add(panelAzul, gbcMain);

        // --- EVENTOS ---
        botonVolver.addActionListener(e -> ventanaPadre.mostrarPantalla("LOGIN"));

        botonRegistrar.addActionListener(e -> {
            // 1. Extraer los datos de los campos de texto
            String nombre   = campoNombreCompleto.getText().trim();
            String dni      = campoDNI.getText().trim();
            String usuario  = campoUsuario.getText().trim();
            String pass     = new String(campoPassword.getPassword()).trim();
            String fecha    = campoFechaNacimiento.getText().trim();
            String email    = campoEmail.getText().trim();
            String telefono = campoTelefono.getText().trim();

            // 2. Validar que no haya campos en blanco
            if (nombre.isEmpty() || dni.isEmpty() || usuario.isEmpty() ||
                    pass.isEmpty() || fecha.isEmpty() || email.isEmpty() || telefono.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Por favor, rellena todos los campos para poder registrarte.",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return; // Cortamos la ejecución aquí si faltan datos
            }

            try {
                // 3. Creamos el objeto Client usando tu constructor exacto
                users.Client nuevoCliente = new users.Client(usuario, pass, nombre, dni, fecha, email, telefono);

                // 4. Registramos al cliente en la "base de datos" en memoria
                logic.Application.registerClient(nuevoCliente);

                // 5. ¡PERSISTENCIA! Guardamos los datos en el archivo físico
                logic.Application.guardarDatos("rongero_data.dat");

                // 6. Avisamos al usuario y le mandamos al login
                JOptionPane.showMessageDialog(this,
                        "¡Registro completado con éxito, " + nombre + "!\nYa puedes iniciar sesión.",
                        "Bienvenido a Rongero",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiamos los campos para el próximo que venga
                campoNombreCompleto.setText("");
                campoDNI.setText("");
                campoUsuario.setText("");
                campoPassword.setText("");
                campoFechaNacimiento.setText("");
                campoEmail.setText("");
                campoTelefono.setText("");

                ventanaPadre.mostrarPantalla("LOGIN");

            } catch (IOException ex) {
                // Si Application.registerClient lanza excepción (ej. "El nombre de usuario ya está en uso")
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Por si hay algún otro fallo inesperado
                JOptionPane.showMessageDialog(this,
                        "Ocurrió un error inesperado: " + ex.getMessage(),
                        "Error crítico",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Métodos auxiliares para no repetir código de estilo
    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(15);
        campo.setBackground(Color.WHITE);
        campo.setOpaque(true);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return campo;
    }

    private void agregarEtiqueta(String texto, int fila, GridBagConstraints gbc, JPanel panel) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(210, 220, 240));
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        panel.add(etiqueta, gbc);
    }
}