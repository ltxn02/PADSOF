package swing2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import logic.Application;
import users.RegisteredUser;
import users.Staff;
import users.Employee;
import swing2.controller.GestorEmpleadoController;

public class PanelGestorEmpleados extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorEmpleadoController ctrl;
	
	// TABLA
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;
	
	// BÚSQUEDA Y FILTRADO
	private JTextField campoBusqueda;
	private ArrayList<Staff> empleadosFiltrados;
	private ArrayList<Staff> empleadosActuales;
	
	// PAGINACIÓN
	private int paginaActual = 0;
	private int empleadosPorPagina = 10;
	private int totalPaginas = 1;
	private JLabel labelPaginacion;
	private JButton btnAnterior, btnSiguiente;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_TABLE = new Color(40, 80, 140);
	private static final Color COLOR_HEADER = new Color(20, 50, 100);
	
	public PanelGestorEmpleados(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorEmpleadoController(ventanaPadre, this);
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1.- Barra superior (búsqueda + botón "Añadir empleado")
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// 2.- Tabla con scroll
		crearTabla();
		scrollPane = new JScrollPane(tabla);
		scrollPane.setBackground(COLOR_FONDO);
		scrollPane.getViewport().setBackground(COLOR_FONDO);
		this.add(scrollPane, BorderLayout.CENTER);
		
		// 3.- Barra de paginación (abajo)
		JPanel barraPaginacion = crearBarraPaginacion();
		this.add(barraPaginacion, BorderLayout.SOUTH);
		
		// 4.- Cargar datos iniciales
		cargarEmpleados();
	}
	
	// 1.- Barra superior
	private JPanel crearBarraSuperior() {
		JPanel barra = new JPanel(new BorderLayout(10, 0));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		// IZDA: Búsqueda
		JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelBusqueda.setOpaque(false);
		
		JLabel labelBusqueda = new JLabel("🔍︎ Buscar empleados");
		labelBusqueda.setBackground(Color.WHITE);
		labelBusqueda.setForeground(new Color(187, 192, 199));
		labelBusqueda.setFont(new Font("Arial", Font.BOLD, 12));
		
		campoBusqueda = new JTextField(25);
		campoBusqueda.setPreferredSize(new Dimension(250, 30));
		campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
		
		// Evento búsqueda en tiempo real
		campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				buscarEmpleados(campoBusqueda.getSelectedText());
				paginaActual = 0;
				actualizarTabla();
				actualizarPaginacion();
			}
		});
		
		panelBusqueda.add(labelBusqueda);
		panelBusqueda.add(campoBusqueda);
		
		// DCHA: Botón "Añadir empleado"
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panelBotones.setOpaque(false);
		
		JButton btnAnadir = new JButton("+ Añadir empleado");
		btnAnadir.setPreferredSize(new Dimension(180, 35));
		btnAnadir.setBackground(Color.WHITE);
		btnAnadir.setForeground(COLOR_FONDO);
		btnAnadir.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		btnAnadir.setBorder(null);
		btnAnadir.setFocusPainted(false);
		btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnadir.addActionListener(e -> abrirDialogoNuevoEmpleado());
		
		panelBotones.add(btnAnadir);
		
		barra.add(panelBusqueda, BorderLayout.WEST);
		barra.add(panelBotones, BorderLayout.EAST);
		
		return barra;
	}
	
	// 2.- Crear la tabla
	private void crearTabla() {
		// Nombre columnas
		String[] columnas = {"Nombre", "Usuario", "Correo", "Teléfono", "Estado", ""};
		
		// Modelo de tabla (sin filas por ahora)
		modeloTabla = new DefaultTableModel(columnas, 0) {
			// Hacer ciertas columnas no editables
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tabla = new JTable(modeloTabla);
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(Color.BLACK);
		tabla.setFont(new Font("Arial", Font.PLAIN, 11));
		tabla.setRowHeight(30);
		tabla.setSelectionBackground(new Color(100, 150, 255));
		tabla.setGridColor(new Color(200, 200, 200));
		
		// Personalizar header
		JTableHeader header = tabla.getTableHeader();
		header.setBackground(COLOR_HEADER);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Arial", Font.BOLD, 12));
		header.setReorderingAllowed(false);
		
		// Ajustar ancho de columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(150);	// Nombre
		tabla.getColumnModel().getColumn(1).setPreferredWidth(100);	// Usuario
		tabla.getColumnModel().getColumn(2).setPreferredWidth(150);	// Correo
		tabla.getColumnModel().getColumn(3).setPreferredWidth(100);	// Teléfono
		tabla.getColumnModel().getColumn(4).setPreferredWidth(80);	// Estado
		tabla.getColumnModel().getColumn(5).setPreferredWidth(80); 	// Más
		
		// EVENTO: Click en una fila para ver detalles
		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int fila = tabla.rowAtPoint(evt.getPoint());
				int columna = tabla.columnAtPoint(evt.getPoint());
				if (fila >= 0 && columna == 1) {	// Columna "Usuario"
					verDetallesEmpleado(fila);
				}
			}
		});
	}
	
	// 3.- Crear barra paginación
	private JPanel crearBarraPaginacion() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		// Botón anterior
		btnAnterior = new JButton("< Anterior");
		btnAnterior.setPreferredSize(new Dimension(120, 30));
		btnAnterior.setBackground(COLOR_FONDO);
		btnAnterior.setForeground(Color.WHITE);
		btnAnterior.setBorder(null);
		btnAnterior.setFocusPainted(false);
		btnAnterior.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnterior.addActionListener(e -> irPaginaAnterior());
		
		// Etiqueta de paginación
		labelPaginacion = new JLabel("Página 1 de 1");
		labelPaginacion.setForeground(Color.WHITE);
		labelPaginacion.setFont(new Font("Arial", Font.BOLD, 12));
		
		// Botón siguiente
		btnSiguiente = new JButton("Siguiente >");
		btnSiguiente.setPreferredSize(new Dimension(120, 30));
		btnSiguiente.setBackground(COLOR_FONDO);
		btnSiguiente.setForeground(Color.WHITE);
		btnSiguiente.setBorder(null);
		btnSiguiente.setFocusPainted(false);
		btnSiguiente.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSiguiente.addActionListener(e -> irPaginaSiguiente());
		
		barra.add(btnAnterior);
		barra.add(labelPaginacion);
		barra.add(btnSiguiente);
		
		return barra;
	}
	
	// 4.- Cargar empleados (controlador)
	private void cargarEmpleados() {
		empleadosFiltrados = ctrl.obtenerEmpleados();
		empleadosActuales = new ArrayList<>(empleadosFiltrados);
		calcularPaginas();
		actualizarTabla();
		actualizarPaginacion();
	}
	
	// 5.- Búsqueda de empleados (controlador)
	private void buscarEmpleados(String termino) {
		empleadosActuales = ctrl.buscarEmpleados(empleadosFiltrados,  termino);
		calcularPaginas();
	}
	
	// 6.- Actualizar tabla
	private void actualizarTabla() {
		modeloTabla.setRowCount(0);
		
		int inicio = paginaActual * empleadosPorPagina;
		int fin = Math.min(inicio + empleadosPorPagina, empleadosActuales.size());
		
		for (int i = inicio; i < fin; i++) {
			Staff empleado = empleadosActuales.get(i);
			String estado = null;
			if (empleado instanceof Employee) {
				estado = ((Employee)empleado).isEnabled() ? "Alta" : "Baja";
			} else {
				estado = "Activo";
			}
			
			Object[] fila = {
					empleado.getFullname(),
					empleado.getUsername(),
					empleado.getEmail(),
					empleado.getPhoneNumber(),
					estado,
					"Ver más"
			};
			
			modeloTabla.addRow(fila);
		}
	}
	
	// 7.- Paginación
	private void calcularPaginas() {
		totalPaginas = (int) Math.ceil((double) empleadosActuales.size() / empleadosPorPagina);
		if (totalPaginas == 0) totalPaginas = 1;
	}
	
	private void actualizarPaginacion() {
		labelPaginacion.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
		btnAnterior.setEnabled(paginaActual > 0);
		btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
		btnAnterior.setText(paginaActual == 0 ? "" : "< Anterior");
		btnSiguiente.setText(paginaActual == totalPaginas - 1 ? "" : "Siguiente >");
	}
	
	private void irPaginaAnterior() {
		if (paginaActual > 0) {
			paginaActual--;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	private void irPaginaSiguiente() {
		if (paginaActual < totalPaginas - 1) {
			paginaActual++;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	// 8.- Detalles (controlador)
	private void verDetallesEmpleado(int fila) {
		int inicio = paginaActual * empleadosPorPagina;
		Staff empleado = empleadosActuales.get(inicio + fila);
		
		String detalles = ctrl.obtenerDetallesEmpleado(empleado);
		
		JOptionPane.showMessageDialog(
				this,
				detalles,
				"Detalles del empleado",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	// 9.- Diálogo para añadir nuevo empleado
	private void abrirDialogoNuevoEmpleado() {
	    // Crear diálogo
	    JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Crear Nuevo Empleado", true);
	    dialogo.setSize(450, 600);
	    dialogo.setLocationRelativeTo((JFrame) SwingUtilities.getWindowAncestor(this));
	    dialogo.setLayout(new GridBagLayout());
	    dialogo.setResizable(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 15, 10, 15);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 0;

	    // ===== CAMPOS DEL FORMULARIO =====
	    
	    JLabel lblNombre = new JLabel("👤 Nombre Completo:");
	    lblNombre.setForeground(Color.BLACK);
	    lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblNombre, gbc);
	    
	    gbc.gridy++;
	    JTextField txtNombre = new JTextField(20);
	    txtNombre.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtNombre, gbc);

	    // --- Fecha ---
	    gbc.gridy++;
	    JLabel lblFecha = new JLabel("📅 Fecha Nacimiento (DD/MM/YYYY):");
	    lblFecha.setForeground(Color.BLACK);
	    lblFecha.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblFecha, gbc);
	    
	    gbc.gridy++;
	    JTextField txtFecha = new JTextField(20);
	    txtFecha.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtFecha, gbc);

	    // --- DNI ---
	    gbc.gridy++;
	    JLabel lblDni = new JLabel("🆔 DNI (8 números + letra):");
	    lblDni.setForeground(Color.BLACK);
	    lblDni.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblDni, gbc);
	    
	    gbc.gridy++;
	    JTextField txtDni = new JTextField(20);
	    txtDni.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtDni, gbc);

	    // --- Usuario ---
	    gbc.gridy++;
	    JLabel lblUsuario = new JLabel("👤 Usuario:");
	    lblUsuario.setForeground(Color.BLACK);
	    lblUsuario.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblUsuario, gbc);
	    
	    gbc.gridy++;
	    JTextField txtUsuario = new JTextField(20);
	    txtUsuario.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtUsuario, gbc);

	    // --- Email ---
	    gbc.gridy++;
	    JLabel lblEmail = new JLabel("📧 Email:");
	    lblEmail.setForeground(Color.BLACK);
	    lblEmail.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblEmail, gbc);
	    
	    gbc.gridy++;
	    JTextField txtEmail = new JTextField(20);
	    txtEmail.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtEmail, gbc);

	    // --- Teléfono ---
	    gbc.gridy++;
	    JLabel lblTelefono = new JLabel("☎️ Teléfono:");
	    lblTelefono.setForeground(Color.BLACK);
	    lblTelefono.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblTelefono, gbc);
	    
	    gbc.gridy++;
	    JTextField txtTelefono = new JTextField(20);
	    txtTelefono.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtTelefono, gbc);

	    // --- Salario (NUEVO) ---
	    gbc.gridy++;
	    JLabel lblSalario = new JLabel("💰 Salario Mensual (€):");
	    lblSalario.setForeground(Color.BLACK);
	    lblSalario.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblSalario, gbc);
	    
	    gbc.gridy++;
	    JTextField txtSalario = new JTextField(20);
	    txtSalario.setPreferredSize(new Dimension(300, 30));
	    txtSalario.setText("1200.00");  // Valor por defecto
	    dialogo.add(txtSalario, gbc);

	    // --- Contraseña ---
	    gbc.gridy++;
	    JLabel lblPassword = new JLabel("🔐 Contraseña:");
	    lblPassword.setForeground(Color.BLACK);
	    lblPassword.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblPassword, gbc);
	    
	    gbc.gridy++;
	    JPasswordField txtPassword = new JPasswordField(20);
	    txtPassword.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtPassword, gbc);

	    // --- Confirmar Contraseña ---
	    gbc.gridy++;
	    JLabel lblConfirm = new JLabel("🔐 Confirmar Contraseña:");
	    lblConfirm.setForeground(Color.BLACK);
	    lblConfirm.setFont(new Font("Arial", Font.BOLD, 11));
	    dialogo.add(lblConfirm, gbc);
	    
	    gbc.gridy++;
	    JPasswordField txtConfirm = new JPasswordField(20);
	    txtConfirm.setPreferredSize(new Dimension(300, 30));
	    dialogo.add(txtConfirm, gbc);

	    // ===== BOTONES =====
	    gbc.gridy++;
	    gbc.insets = new Insets(20, 15, 10, 15);
	    
	    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
	    panelBotones.setOpaque(false);

	    JButton btnGuardar = new JButton("✅ Guardar");
	    btnGuardar.setPreferredSize(new Dimension(120, 35));
	    btnGuardar.setBackground(new Color(46, 204, 113));
	    btnGuardar.setForeground(Color.WHITE);
	    btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));
	    btnGuardar.setBorder(null);
	    btnGuardar.setFocusPainted(false);
	    btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    JButton btnCancelar = new JButton("❌ Cancelar");
	    btnCancelar.setPreferredSize(new Dimension(120, 35));
	    btnCancelar.setBackground(new Color(231, 76, 60));
	    btnCancelar.setForeground(Color.WHITE);
	    btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
	    btnCancelar.setBorder(null);
	    btnCancelar.setFocusPainted(false);
	    btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

	    panelBotones.add(btnGuardar);
	    panelBotones.add(btnCancelar);

	    dialogo.add(panelBotones, gbc);

	    // ===== EVENTOS DE LOS BOTONES =====
	    btnGuardar.addActionListener(e -> {
	        try {
	            // Parsear el salario
	            double salario = Double.parseDouble(txtSalario.getText().trim());
	            
	            boolean exito = ctrl.crearEmpleado(
	                txtNombre.getText(),
	                txtFecha.getText(),
	                txtDni.getText(),
	                txtUsuario.getText(),
	                txtEmail.getText(),
	                txtTelefono.getText(),
	                new String(txtPassword.getPassword()),
	                new String(txtConfirm.getPassword()),
	                salario  // ← PASAR SALARIO
	            );
	            
	            if (exito) {
	                dialogo.dispose();
	                cargarEmpleados();  // Recargar tabla
	                paginaActual = 0;
	                actualizarTabla();
	                actualizarPaginacion();
	                campoBusqueda.setText("");  // Limpiar búsqueda
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(
	                dialogo,
	                "El salario debe ser un número válido (ej: 1200.50)",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    });

	    btnCancelar.addActionListener(e -> dialogo.dispose());

	    dialogo.setVisible(true);
	}
}
