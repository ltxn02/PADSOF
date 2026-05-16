package swing2.view.gestor.clientes;

import javax.swing.*;

import swing2.controller.gestor.GestorClienteController;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import users.Client;

public class PanelDetallesCliente extends JPanel {
	private PanelGestionClientes panelPadre;
	private GestorClienteController ctrl;
	private Client cliente;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_INFO = new Color(40, 80, 140);
	private static final Color COLOR_LABEL = new Color(187, 192, 199);
	
	public PanelDetallesCliente(PanelGestionClientes panelPadre, GestorClienteController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	// Cargar y mostrar los detalles de un cliente
	public void mostrarDetalles(Client cliente) {
		this.cliente = cliente;
		
		// Limpiar panel anterior
		this.removeAll();
		
		// BARRA SUPERIOR: Título + botón volver
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// CONTENIDO CENTRAL: Información del cliente
		JPanel contenidoPrincipal = crearContenidoPrincipal();
		JScrollPane scroll = new JScrollPane(contenidoPrincipal);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		this.add(scroll, BorderLayout.CENTER);
		
		// BARRA INFERIOR: Botón volver
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
		JLabel titulo = new JLabel("DETALLES DEL CLIENTE");
		titulo.setForeground(Color.WHITE);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));
		
		barra.add(btnVolver, BorderLayout.WEST);
		barra.add(titulo, BorderLayout.CENTER);
		
		return barra;
	}
	
	// CONTENIDO PRINCIPAL: Información del cliente
	private JPanel crearContenidoPrincipal() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// 1.- INFORMACIÓN PERSONAL
		JPanel seccion1 = crearSeccion("INFORMACIÓN PERSONAL");
		seccion1.add(crearFilaInfo("Nombre completo:", cliente.getFullname()));
		seccion1.add(crearFilaInfo("Nombre de usuario:", cliente.getUsername()));
		seccion1.add(crearFilaInfo("DNI:", cliente.getMaskedDni()));
		seccion1.add(crearFilaInfo("Fecha de nacimiento:", cliente.getBirthdate()));
		
		panel.add(seccion1);
		panel.add(Box.createVerticalStrut(20));
		
		// 2.- CONTACTO
		JPanel seccion2 = crearSeccion("CONTACTO");
		seccion2.add(crearFilaInfo("Correo electrónico:", cliente.getEmail()));
		seccion2.add(crearFilaInfo("Teléfono:", cliente.getPhoneNumber()));
		
		panel.add(seccion2);
		panel.add(Box.createVerticalStrut(20));
		
		// 3.- INFORMACIÓN DE CLIENTE
		JPanel seccion3 = crearSeccion("INFORMACIÓN DE CLIENTE");
		seccion3.add(crearFilaInfo("Fecha de incorporación:", formatearFecha(cliente.getJoiningDate())));
		seccion3.add(crearFilaInfo("ID de cliente:", String.valueOf(cliente.getUserId())));
		
		panel.add(seccion3);
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
	
	// FILA DE INFORMACIÓN
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
	
	// BARRA INFERIOR: Botón volver
	private JPanel crearBarraInferior() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
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
		
		barra.add(btnVolver);
		
		return barra;
	}
	
	// Formatear fecha LocalDateTime a String
	private String formatearFecha(LocalDateTime fecha) {
		if (fecha == null) return "N/A";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return fecha.format(formatter);
	}
}