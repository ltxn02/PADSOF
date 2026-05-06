package swing2.view.gestor;

import javax.swing.*;

import swing2.view.PanelInicioo;
import swing2.view.VentanaPrincipa;

import java.awt.*;
import users.Manager;
import users.RegisteredUser;

public class PanelDashboard extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private CardLayout layoutContenido;
	private JPanel panelContenido;
	private JPanel menuNavegacion;
	private Manager usuarioActual;
	
	private JButton btnEstadisticas, btnDescuentos, btnProductos, btnEmpleados, btnPedidos, btnLogout;
	private JButton btnActivo = null;
	
	private static final Font FUENTE_TITULOS = new Font("Arial", Font.BOLD, 14);
	
	private static final Color COLOR_FONDO_CONTENIDO = new Color(23, 48, 79);
	private static final Color COLOR_MENU_NORMAL = new Color(34, 50, 83);
	private static final Color COLOR_TEXTO_NORMAL = Color.WHITE;
	private static final Color COLOR_MENU_ACTIVO = Color.WHITE;
	private static final Color COLOR_TEXTO_ACTIVO = new Color(34, 50, 83);
	
	public PanelDashboard (VentanaPrincipa ventanaPadre, Manager usuarioActual) {
		this.ventanaPadre = ventanaPadre;
		this.usuarioActual = usuarioActual;
		this.setLayout(new BorderLayout(0, 0));
		
		// 1.- Barra superior fija
		this.menuNavegacion = crearMenuNavegacion();
		this.add(menuNavegacion, BorderLayout.NORTH);
		
		// 2.- Panel central
		layoutContenido = new CardLayout();
		panelContenido = new JPanel(layoutContenido);
		this.panelContenido.setBackground(COLOR_FONDO_CONTENIDO);
		this.add(panelContenido, BorderLayout.CENTER);
		
		// Agregar los paneles de contenido
		panelContenido.add(crearPanelPrueba("ESTADÍSTICAS"), "ESTADISTICAS");
		panelContenido.add(crearPanelPrueba("DESCUENTOS"), "DESCUENTOS");
		panelContenido.add(crearPanelPrueba("PRODUCTOS"), "PRODUCTOS");
		panelContenido.add(new PanelGestionEmpleados(ventanaPadre), "EMPLEADOS");
		//panelContenido.add(crearPanelPrueba("EMPLEADOS"), "EMPLEADOS");
		panelContenido.add(crearPanelPrueba("PEDIDOS"), "PEDIDOS");
		
		/*
		// Descomentar cuando tengas los paneles reales:
		panelContenido.add(new PanelGestorEstadisticas(ventanaPadre), "ESTADISTICAS");
		panelContenido.add(new PanelGestorDescuentos(ventanaPadre), "DESCUENTOS");
		panelContenido.add(new PanelGestorProductos(ventanaPadre), "PRODUCTOS");
		panelContenido.add(new PanelGestorEmpleados(ventanaPadre), "EMPLEADOS");
		panelContenido.add(new PanelGestorPedidos(ventanaPadre), "PEDIDOS");
		*/
		
		this.add(panelContenido, BorderLayout.CENTER);
		
		// Mostrar el panel por defecto
		mostrarSeccion("ESTADISTICAS");
		marcarActivo(btnEstadisticas);
	}
	
	private JPanel crearMenuNavegacion() {
		JPanel menu = new JPanel(new BorderLayout());
		menu.setBackground(COLOR_MENU_NORMAL);
		menu.setPreferredSize(new Dimension(1000, 80));
		
		// --- PARTE IZQUIERDA: Logo + Botones ---
		JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		nav.setOpaque(false);
		
		// Botones del menú
		btnEstadisticas = crearBotonMenu("ESTADÍSTICAS");
		btnDescuentos = crearBotonMenu("DESCUENTOS");
		btnProductos = crearBotonMenu("PRODUCTOS");
		btnEmpleados = crearBotonMenu("EMPLEADOS");
		btnPedidos = crearBotonMenu("PEDIDOS");
		
		// Agregar listeners a los botones
		btnEstadisticas.addActionListener(e -> { marcarActivo(btnEstadisticas); mostrarSeccion("ESTADÍSTICAS"); });
		btnDescuentos.addActionListener(e -> { marcarActivo(btnDescuentos); mostrarSeccion("DESCUENTOS"); });
		btnProductos.addActionListener(e -> { marcarActivo(btnProductos); mostrarSeccion("PRODUCTOS"); });
		btnEmpleados.addActionListener(e -> { marcarActivo(btnEmpleados); mostrarSeccion("EMPLEADOS"); });
		btnPedidos.addActionListener(e -> { marcarActivo(btnPedidos); mostrarSeccion("PEDIDOS"); });
		
		// Agregar componentes a la parte izquierda
		nav.add(PanelInicioo.crearPanelLogo());
		nav.add(btnEstadisticas);
		nav.add(btnDescuentos);
		nav.add(btnProductos);
		nav.add(btnEmpleados);
		nav.add(btnPedidos);
		
		// --- PARTE DERECHA: Usuario + Logout ---
		JPanel navDcha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
		navDcha.setOpaque(false);
		
		navDcha.add(crearPanelUsuarioGestor(this.usuarioActual));
		navDcha.add(crearBotonLogout());
		
		menu.add(nav, BorderLayout.WEST);
		menu.add(navDcha, BorderLayout.EAST);
		
		return menu;
	}
	
	private JButton crearBotonMenu(String str) {
		JButton b = new JButton(str);
		
		b.setPreferredSize(new Dimension(140, 80));
		b.setForeground(COLOR_TEXTO_NORMAL);
		b.setFont(FUENTE_TITULOS);
		b.setBackground(COLOR_MENU_NORMAL);
		b.setBorder(null);
		b.setFocusPainted(false);
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		return b;
	}
	
	private JButton crearBotonLogout() {
		btnLogout = new JButton("LOGOUT");
		
		btnLogout.setForeground(COLOR_TEXTO_NORMAL);
		btnLogout.setFont(FUENTE_TITULOS);
		btnLogout.setBackground(COLOR_MENU_NORMAL);
		btnLogout.setBorder(null);
		btnLogout.setFocusPainted(false);
		btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLogout.addActionListener(e -> {
			int confirma = JOptionPane.showConfirmDialog(
					this,
					"¿Estás seguro que deseas cerrar sesión?",
					"Confirmar logout",
					JOptionPane.YES_NO_OPTION
			);
			if (confirma == JOptionPane.YES_OPTION) {
				ventanaPadre.cambiarSesion(null);
				ventanaPadre.mostrarPantalla("INICIO");
			}
		});
		return btnLogout;
	}
	
    private void marcarActivo(JButton b) {
    	marcarInactivo(btnEstadisticas, btnDescuentos, btnProductos, btnEmpleados, btnPedidos);
        b.setForeground(COLOR_TEXTO_ACTIVO);
        b.setBackground(COLOR_MENU_ACTIVO);
    }
    
    private void marcarInactivo(JButton... btns) {
    	for(JButton b : btns) {
    		b.setBackground(COLOR_MENU_NORMAL);
        	b.setForeground(COLOR_TEXTO_NORMAL);
    	}
    }
    
    private JPanel crearPanelUsuarioGestor(Manager user) {
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
    	p.setOpaque(false);
    	
    	JButton btnPerfil = new JButton();
    	btnPerfil.setContentAreaFilled(false);
    	btnPerfil.setBorderPainted(false);
    	btnPerfil.setFocusPainted(false);
    	btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	
    	btnPerfil.setText("GESTOR");
    	btnPerfil.setFont(FUENTE_TITULOS);
    	btnPerfil.setForeground(COLOR_TEXTO_NORMAL);
    	
    	btnPerfil.addActionListener(e -> {
    		JOptionPane.showMessageDialog(this,  "Próximamente: Perfil de " + user.getUsername());
    	});
    	
    	p.add(btnPerfil);
    	
    	return p;
    }
    
    private JPanel crearPanelPrueba(String nombre) {
    	JPanel panel = new JPanel();
    	panel.setBackground(COLOR_FONDO_CONTENIDO);
    	panel.setLayout(new GridBagLayout());
    	
    	JLabel lbl = new JLabel("Panel: " + nombre);
    	lbl.setFont(new Font("Arial", Font.BOLD, 32));
    	lbl.setForeground(COLOR_TEXTO_NORMAL);
    	panel.add(lbl);
    	
    	return panel;
    }
    
    private void mostrarSeccion(String nombreSeccion) {
    	layoutContenido.show(panelContenido, nombreSeccion);
    }
}
