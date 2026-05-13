package swing2.view.gestor;

import javax.swing.*;

import swing2.controller.gestor.GestorEmpleadoController;

import java.awt.*;
import users.Staff;
import users.Employee;

public class PanelDetallesEmpleado extends JPanel {
	private PanelGestionEmpleados panelPadre;
	private GestorEmpleadoController ctrl;
	private Staff empleado;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_INFO = new Color(40, 80, 140);
	private static final Color COLOR_LABEL = new Color(187, 192, 199);
	
	public PanelDetallesEmpleado(PanelGestionEmpleados panelPadre, GestorEmpleadoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	// Cargar y mostrar los detalles de un empleado
	public void mostrarDetalles(Staff empleado) {
		this.empleado = empleado;
		
		// Limpiar panel anterior
		this.removeAll();
		
		// BARRA SUPERIOR: Título + botón volver
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior);
		
		// CONTENIDO CENTRAL: Información del empleado
		JPanel contenidoPrincipal = crearContenidoPrincipal();
		this.add(new JScrollPane(contenidoPrincipal), BorderLayout.CENTER);
		
		// BARRA INFERIOR: Botones de acción
		JPanel barraInferior = crearBarraInferior();
		this.add(barraInferior, BorderLayout.SOUTH);
		
		// Actualizar la vista
		this.revalidate();
		this.repaint();
	}
	
	// BARRA SUPERIOR
	private JPanel crearBarraSuperior() {
		JPanel barra = new JPanel(new BorderLayout());
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		// Botón volver
		JButton btnVolver = new JButton("< Volver");
		btnVolver.setPreferredSize(new Dimension(150, 35));
		btnVolver.setBackground(new Color(52, 73, 94));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
		btnVolver.setBorder(null);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.addActionListener(e -> panelPadre.mostrarListado());
		
		// Título
		JLabel titulo = new JLabel("DETALLES DEL EMPLEADO");
		titulo.setForeground(Color.WHITE);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));
		
		barra.add(btnVolver, BorderLayout.WEST);
		barra.add(titulo, BorderLayout.CENTER);
		
		return barra;
	}
	
	// CONTENIDO PRINCIPAL: Información del empleado
	private JPanel crearContenidoPrincipal() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// 1.- INFORMACIÓN PERSONAL
		JPanel seccion1 = crearSeccion("INFORMACIÓN PERSONAL");
		seccion1.add(crearFilaInfo("Nombre completo:", empleado.getFullname()));
		seccion1.add(crearFilaInfo("Nombre de usuario:", empleado.getUsername()));
		seccion1.add(crearFilaInfo("DNI:", empleado.getMaskedDni()));
		seccion1.add(crearFilaInfo("F. nacimiento:", empleado.getBirthdate()));
		
		panel.add(seccion1);
		panel.add(Box.createVerticalStrut(20));
		
		// 2.- CONTACTO
		JPanel seccion2 = crearSeccion("CONTACTO");
		seccion2.add(crearFilaInfo("Correo electrónico:", empleado.getEmail()));
		seccion2.add(crearFilaInfo("Teléfono:", empleado.getPhoneNumber()));
		
		panel.add(seccion2);
		panel.add(Box.createVerticalStrut(20));
		
		// ===== SECCIÓN 3: INFORMACIÓN LABORAL =====
	    JPanel seccion3 = crearSeccion("INFORMACIÓN LABORAL");
	    seccion3.add(crearFilaInfo("Salario Mensual:", "€" + String.format("%.2f", empleado.getSalary())));
	    
	    // === ESTADO CON RADIO BUTTONS ===
	    if (empleado instanceof Employee) {
	        Employee emp = (Employee) empleado;
	        seccion3.add(crearFilaEstado("Estado:", emp.isEnabled()));
	    }
	    
	    panel.add(seccion3);
	    panel.add(Box.createVerticalStrut(20));
		
		// 4.- DETALLES ADICIONALES
		JPanel seccion4 = crearSeccion("DETALLES ADICIONALES");
		seccion4.add(crearFilaInfo("ID de empleado:", String.valueOf(empleado.getUserId())));
		panel.add(seccion4);
		
		panel.add(Box.createVerticalGlue());
		
		return panel;
	}
	
	// SECCIÓN CON TÍTULO
	private JPanel crearSeccion(String titulo) {
		JPanel seccion = new JPanel();
		seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
		seccion.setBackground(COLOR_PANEL_INFO);
		seccion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		
		// Título de sección
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		seccion.add(lblTitulo);
		seccion.add(Box.createVerticalStrut(10));
		
		return seccion;
	}
	
	// FILA DE INFORMACION
	private JPanel crearFilaInfo(String etiqueta, String valor) {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		fila.setOpaque(false);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fila.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Etiqueta
		JLabel lblEtiqueta = new JLabel(etiqueta);
		lblEtiqueta.setForeground(COLOR_LABEL);
		lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 11));
		lblEtiqueta.setPreferredSize(new Dimension(180, 25));
		
		// Valor
		JLabel lblValor = new JLabel(valor);
		lblValor.setForeground(Color.WHITE);
		lblValor.setFont(new Font("Arial", Font.PLAIN, 12));
		
		fila.add(lblEtiqueta);
		fila.add(lblValor);
		
		return fila;
	}
	
	// BARRA INFERIOR: Botones de acción
	private JPanel crearBarraInferior() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		// Botón editar
		JButton btnEditar = new JButton("EDITAR");
		btnEditar.setPreferredSize(new Dimension(150, 40));
		btnEditar.setBackground(new Color(52, 152, 219));
		btnEditar.setForeground(Color.WHITE);
		btnEditar.setFont(new Font("Arial", Font.BOLD, 12));
		btnEditar.setBorder(null);
		btnEditar.setFocusPainted(false);
		btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEditar.addActionListener(e -> {
			JOptionPane.showMessageDialog(
					this,
					"EDITAR EMPLEADO (PRÓXIMAMENTE)" +
					"Se podrá editar los datos de: " + empleado.getUsername(),
					"Editar empleado",
					JOptionPane.INFORMATION_MESSAGE
			);
		});
		
		// Botón volver
		JButton btnVolver = new JButton("< Volver");
		btnVolver.setPreferredSize(new Dimension(150, 40));
		btnVolver.setBackground(new Color(149, 165, 166));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
		btnVolver.setBorder(null);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.addActionListener(e -> panelPadre.mostrarListado());
		
		barra.add(btnEditar);
		barra.add(btnVolver);
		
		return barra;
	}
	
	// FILA DE ESTADO
	private JPanel crearFilaEstado(String etiqueta, boolean activo) {
	    JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
	    fila.setOpaque(false);
	    fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    fila.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    // Etiqueta
	    JLabel lblEtiqueta = new JLabel(etiqueta);
	    lblEtiqueta.setForeground(COLOR_LABEL);
	    lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 11));
	    lblEtiqueta.setPreferredSize(new Dimension(180, 25));
	    
	    // Botones de radio
	    JRadioButton btnActivo = new JRadioButton("Activo");
	    btnActivo.setForeground(Color.WHITE);
	    btnActivo.setOpaque(false);
	    btnActivo.setFont(new Font("Arial", Font.PLAIN, 12));
	    
	    JRadioButton btnInactivo = new JRadioButton("Inactivo");
	    btnInactivo.setForeground(Color.WHITE);
	    btnInactivo.setOpaque(false);
	    btnInactivo.setFont(new Font("Arial", Font.PLAIN, 12));
	    
	    // Grupo excluyente
	    ButtonGroup grupo = new ButtonGroup();
	    grupo.add(btnActivo);
	    grupo.add(btnInactivo);
	    
	    // Seleccionar según estado
	    if (activo) {
	        btnActivo.setSelected(true);
	    } else {
	        btnInactivo.setSelected(true);
	    }
	    
	    // Agregar al panel
	    fila.add(lblEtiqueta);
	    fila.add(btnActivo);
	    fila.add(btnInactivo);
	    
	    return fila;
	}
}
