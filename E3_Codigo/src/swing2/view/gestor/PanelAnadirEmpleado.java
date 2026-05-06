package swing2.view.gestor;

import swing2.controller.gestor.GestorEmpleadoController;
import swing2.view.VentanaPrincipa;

import javax.swing.*;
import java.awt.*;

public class PanelAnadirEmpleado extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private PanelGestionEmpleados panelEmpleados;
	private GestorEmpleadoController ctrl;
	
	// Campos de registro
	private JTextField txtNombre, txtFecha, txtDni, txtUsername, txtEmail, txtTlf, txtSalario;
	private JPasswordField txtPwd, txtConfirmPwd;
	
	public PanelAnadirEmpleado(VentanaPrincipa ventanaPadre, PanelGestionEmpleados panelEmpleados) {
		this.ventanaPadre = ventanaPadre;
		this.panelEmpleados = panelEmpleados;
		this.ctrl = new GestorEmpleadoController(ventanaPadre, panelEmpleados);
		
		this.setBackground(new Color(51, 66, 90));
		this.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(8, 8, 8, 8);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		// Título
		JLabel titulo = new JLabel("CREAR NUEVA CUENTA", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 22));
		titulo.setForeground(Color.WHITE);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 3;
		this.add(titulo, g);
		
		g.gridwidth = 1;
		int r = 1;
		
		// Formulario
		agregarFilaFormulario("Nombre completo:", txtNombre = new JTextField(15), null, g, r++);
		agregarFilaFormulario("F. nacimiento (DD/MM/AAAA):", txtFecha = new JTextField(15), null, g, r++);
		agregarFilaFormulario("DNI (8 nms + letra):", txtDni = new JTextField(15), null, g, r++);
		agregarFilaFormulario("Nombre usuario:", txtUsername = new JTextField(15), null, g, r++);
		agregarFilaFormulario("Correo electrónico:", txtEmail = new JTextField(15), null, g, r++);
		agregarFilaFormulario("Teléfono:", txtTlf = new JTextField(15), null, g, r++);
		agregarFilaFormulario("Salario:", txtSalario = new JTextField(15), null, g, r++);
		
		// Contraseña 1 + botón (mostrar contraseña)
		txtPwd = new JPasswordField(15);
		JToggleButton btnVer1 = crearBotonVer(txtPwd);
		agregarFilaFormulario("Contraseña:", txtPwd, btnVer1, g, r++);
		
		// Contraseña 2 + botón (mostrar contraseña)
		txtConfirmPwd = new JPasswordField(15);
		JToggleButton btnVer2 = crearBotonVer(txtConfirmPwd);
		agregarFilaFormulario("Confirmar contraseña", txtConfirmPwd, btnVer2, g, r++);
		
		// Botón "Añadir nuevo empleado"
		JPanel panelBotones = crearPanelBotones(g, r);
		g.gridy = r;
		g.gridx = 0;
		g.gridwidth = 3;
		g.insets = new Insets(20, 8, 8, 8);
		this.add(panelBotones, g);
	}
	
	// Fila de formulario (etiqueta + campo + botón opcional)
	private void agregarFilaFormulario(String etiqueta, JTextField campo, JToggleButton btn, GridBagConstraints g, int fila) {
		g.gridy = fila;
		
		// COLUMNA 0: Etiqueta alineada a la dcha
		g.gridx = 0;
		g.anchor = GridBagConstraints.EAST;
		JLabel lbl = new JLabel(etiqueta);
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("Arial", Font.PLAIN, 11));
		this.add(lbl, g);
		
		// COLUMNA 1: Campo de texto
		g.gridx = 1;
		g.anchor = GridBagConstraints.CENTER;
		campo.setPreferredSize(new Dimension(200, 28));
		this.add(campo, g);
		
		// COLUMNA 2: Botón de ver o espacio vacío
		g.gridx = 2;
		if (btn != null) {
			this.add(btn, g);
		} else {
			this.add(Box.createHorizontalStrut(45), g);
		}
	}
	
	// Crear botón para mostrar/ocultar contraseña
	private JToggleButton crearBotonVer(JPasswordField campo) {
		JToggleButton btn = new JToggleButton("Ver");
		btn.setPreferredSize(new Dimension(45, 25));
		btn.setFont(new Font("Arial", Font.PLAIN, 10));
		btn.addActionListener(e -> campo.setEchoChar(btn.isSelected() ? (char)0 : '·'));
		return btn;
	}
	
	// Crear panel con botones guardar/cancelar
	private JPanel crearPanelBotones(GridBagConstraints g, int fila) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.setOpaque(false);
		
		// Botón Guardar
		JButton btnAnadir = new JButton("☑ Añadir empleado");
		btnAnadir.setPreferredSize(new Dimension(200, 40));
		btnAnadir.setBackground(new Color(46, 204, 113));
		btnAnadir.setForeground(Color.WHITE);
		btnAnadir.setFont(new Font("Arial", Font.BOLD, 14));
		btnAnadir.setBorder(null);
		btnAnadir.setFocusPainted(false);
		btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnadir.addActionListener(e -> anadirEmpleado());
		
		// Botón cancelar
		JButton btnCancelar = new JButton("☒ Cancelar");
		btnCancelar.setPreferredSize(new Dimension(200, 40));
		btnCancelar.setBackground(new Color(46, 204, 113));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancelar.setBorder(null);
		btnCancelar.setFocusPainted(false);
		btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCancelar.addActionListener(e -> volverAlListado());
		
		panel.add(btnAnadir);
		panel.add(btnCancelar);
		
		return panel;
	}
	
	// Añadir empleado (controlador)
	private void anadirEmpleado() {
		try {
			// Parsear salario
			double salario;
			try {
				salario = Double.parseDouble(txtSalario.getText().trim());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(
						this,
						"El salario debe ser un número válido (ej: 1200.50",
						"Error",
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			// Llama al controlador
			boolean exito = ctrl.crearEmpleado(
					txtNombre.getText(),
					txtFecha.getText(),
					txtDni.getText(),
					txtUsername.getText(),
					txtEmail.getText(),
					txtTlf.getText(),
					new String(txtPwd.getPassword()),
					new String(txtConfirmPwd.getPassword()),
					salario
			);
			
			if (exito) {
				volverAlListado();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					this,
					"Error inesperado: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	// Volver al listado de empleados
	private void volverAlListado() {
		// Mostrar panel de empleados nuevamente
		panelEmpleados.mostrarListado();
		
		// Limpiar campos
		limpiarCampos();
	}
	
	// Limpiar todos los campos
	private void limpiarCampos() {
		txtNombre.setText("");
        txtFecha.setText("");
        txtDni.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtTlf.setText("");
        txtSalario.setText("0.00");
        txtPwd.setText("");
        txtConfirmPwd.setText("");
	}	
}
