package swing2.view.gestor.empleados;

import javax.swing.*;

import swing2.controller.gestor.GestorEmpleadoController;

import java.awt.*;
import users.Staff;
import users.Employee;
import utils.Permission;
import utils.EmployeeRoles;

/**
 * Panel de visualización y edición avanzada para los detalles de un empleado.
 * Muestra información personal, de contacto, laboral, y permite la gestión directa
 * de estados (Activo/Inactivo), permisos y roles del empleado seleccionado.
 * 
 * @author Lidia Martín
 */
public class PanelDetallesEmpleado extends JPanel {
	private PanelGestionEmpleados panelPadre;
	private GestorEmpleadoController ctrl;
	private Staff empleado;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_INFO = new Color(40, 80, 140);
	private static final Color COLOR_LABEL = new Color(187, 192, 199);
	
	/**
	 * Constructor de la clase PanelDetallesEmpleado.
	 * Configura los parámetros iniciales de diseño del panel contenedor, tales como
	 * la distribución de bordes, color de fondo estructural y márgenes interiores.
	 * 
	 * @param panelPadre Panel de gestión de empleados que funciona como orquestador de navegación.
	 * @param ctrl       Controlador que gestiona la lógica de persistencia y estado de los empleados.
	 */
	public PanelDetallesEmpleado(PanelGestionEmpleados panelPadre, GestorEmpleadoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	/**
	 * Reconstruye dinámicamente la interfaz para cargar y mostrar los detalles actualizados
	 * de un miembro del personal, estructurando el contenido en cabecera, cuerpo y botonera.
	 * 
	 * @param empleado Instancia de Staff de la cual se va a extraer y representar la información.
	 */
	public void mostrarDetalles(Staff empleado) {
		this.empleado = empleado;
		
		// Limpiar panel anterior
		this.removeAll();
		
		// BARRA SUPERIOR: Título + botón volver
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// CONTENIDO CENTRAL: Información del empleado
		JPanel contenidoPrincipal = crearContenidoPrincipal();
		JScrollPane scroll = new JScrollPane(contenidoPrincipal);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		this.add(scroll, BorderLayout.CENTER);
		
		// BARRA INFERIOR: Botones de acción
		JPanel barraInferior = crearBarraInferior();
		this.add(barraInferior, BorderLayout.SOUTH);
		
		// Actualizar la vista
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Construye la barra superior que alberga el título informativo de la pantalla
	 * y el botón interactivo para regresar al listado general de personal.
	 * 
	 * @return Un JPanel formateado para la cabecera del módulo de detalles.
	 */
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
	
	/**
	 * Instancia, agrupa y organiza secuencialmente todas las subsecciones informativas
	 * y de edición asociadas al registro del empleado (personales, contacto, laborales, roles y permisos).
	 * 
	 * @return Un JPanel contenedor estructurado mediante un diseño de BoxLayout vertical.
	 */
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
		
		// SECCIÓN 3: INFORMACIÓN LABORAL
	    JPanel seccion3 = crearSeccion("INFORMACIÓN LABORAL");
	    seccion3.add(crearFilaInfo("Salario mensual:", "€" + String.format("%.2f", empleado.getSalary())));
	    
	    // ESTADO CON RADIO BUTTONS
	    if (empleado instanceof Employee) {
	        Employee emp = (Employee) empleado;
	        seccion3.add(crearFilaEstado("Estado:", emp.isEnabled()));
	    }
	    
	    panel.add(seccion3);
	    panel.add(Box.createVerticalStrut(20));
	    
	    // 3.1 - PERMISOS (checkboxes)
	    if (empleado instanceof Employee) {
	        Employee emp = (Employee) empleado;
	        JPanel seccionPermisos = crearSeccion("PERMISOS");
	        seccionPermisos.add(crearFilaPermisos(emp));
	        panel.add(seccionPermisos);
	        panel.add(Box.createVerticalStrut(20));
	    }

	    // 3.2 - ROL (checkboxes)
	    if (empleado instanceof Employee) {
	        Employee emp = (Employee) empleado;
	        JPanel seccionRol = crearSeccion("ROL");
	        seccionRol.add(crearFilaRol(emp));
	        panel.add(seccionRol);
	        panel.add(Box.createVerticalStrut(20));
	    }
		
		// 4.- DETALLES ADICIONALES
		JPanel seccion4 = crearSeccion("DETALLES ADICIONALES");
		seccion4.add(crearFilaInfo("ID de empleado:", String.valueOf(empleado.getUserId())));
		panel.add(seccion4);
		
		panel.add(Box.createVerticalGlue());
		
		return panel;
	}
	
	/**
	 * Crea un panel contenedor con esquinas lógicas para encapsular bajo un mismo
	 * bloque visual y un título descriptivo un conjunto homogéneo de información.
	 * 
	 * @param titulo Texto indicador de la categoría de datos que se listará en el bloque.
	 * @return Un JPanel formateado listo para la inserción de tuplas de información.
	 */
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
	
	/**
	 * Modela un componente de fila simple y alineado compuesto por una etiqueta 
	 * fija de metadato y su valor textual correspondiente en formato clave-valor.
	 * 
	 * @param etiqueta Texto que describe la propiedad o campo informativo.
	 * @param valor    Texto que representa el dato concreto del campo.
	 * @return Un JPanel alineado horizontalmente a la izquierda.
	 */
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
	
	/**
	 * Genera la barra inferior del módulo que contiene un control secundario
	 * de retorno a la lista de administración de empleados.
	 * 
	 * @return Un JPanel configurado para la sección sur del panel.
	 */
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
	
	/**
	 * Crea un componente de fila interactivo que mediante un grupo de botones de opción
	 * (JRadioButton) permite conmutar y persistir el estado habilitado del empleado.
	 * 
	 * @param etiqueta Texto del descriptor de estado de la fila.
	 * @param activo   Valor booleano representativo del estado inicial actual del empleado.
	 * @return Un JPanel con los controles interactivos integrados y vinculados al controlador.
	 */
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
	    JRadioButton btnActivo = new JRadioButton(" Activo");
	    btnActivo.setForeground(Color.WHITE);
	    btnActivo.setOpaque(false);
	    btnActivo.setFont(new Font("Arial", Font.PLAIN, 12));
	    
	    JRadioButton btnInactivo = new JRadioButton(" Inactivo");
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
	    
	    // AGREGAR LISTENERS
	    btnActivo.addActionListener(e -> {
	        boolean exito = ctrl.cambiarEstadoEmpleado(empleado, true);
	        if (exito) {
	            mostrarDetalles(empleado);
	        }
	    });
	    
	    btnInactivo.addActionListener(e -> {
	        boolean exito = ctrl.cambiarEstadoEmpleado(empleado, false);
	        if (exito) {
	            mostrarDetalles(empleado);
	        }
	    });
	    
	    // Agregar al panel
	    fila.add(lblEtiqueta);
	    fila.add(btnActivo);
	    fila.add(btnInactivo);
	    
	    return fila;
	}
	
	/**
	 * Diseña una matriz avanzada de checkboxes organizados en tres columnas temáticas
	 * (Productos, Pedidos, Intercambios) para visualizar y modificar de manera granular
	 * el conjunto de permisos del empleado.
	 * 
	 * @param emp Instancia del objeto Employee cuyos permisos van a ser editados.
	 * @return Un JPanel con la cuadrícula de permisos y su respectivo botón de guardado.
	 */
	private JPanel crearFilaPermisos(Employee emp) {
	    JPanel contenedor = new JPanel();
	    contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
	    contenedor.setOpaque(false);
	    contenedor.setAlignmentX(Component.LEFT_ALIGNMENT);

	    JLabel lbl = new JLabel("Permisos:");
	    lbl.setForeground(COLOR_LABEL);
	    lbl.setFont(new Font("Arial", Font.BOLD, 11));
	    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

	    contenedor.add(lbl);
	    contenedor.add(Box.createVerticalStrut(5));

	    // Panel con 3 columnas
	    JPanel columnas = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
	    columnas.setOpaque(false);

	    JPanel colProduct = new JPanel();
	    colProduct.setLayout(new BoxLayout(colProduct, BoxLayout.Y_AXIS));
	    colProduct.setOpaque(false);

	    JPanel colOrder = new JPanel();
	    colOrder.setLayout(new BoxLayout(colOrder, BoxLayout.Y_AXIS));
	    colOrder.setOpaque(false);

	    JPanel colExchange = new JPanel();
	    colExchange.setLayout(new BoxLayout(colExchange, BoxLayout.Y_AXIS));
	    colExchange.setOpaque(false);
	    
	    colProduct.setPreferredSize(new Dimension(170, 100));
	    colOrder.setPreferredSize(new Dimension(170, 100));
	    colExchange.setPreferredSize(new Dimension(170, 100));
	    
	    JCheckBox[] checks = new JCheckBox[Permission.values().length];

	    for (int i = 0; i < Permission.values().length; i++) {
	        Permission p = Permission.values()[i];
	        JCheckBox cb = new JCheckBox(p.name());
	        cb.setOpaque(false);
	        cb.setForeground(Color.WHITE);
	        cb.setFont(new Font("Arial", Font.PLAIN, 12));
	        cb.setSelected(emp.permissions.contains(p));
	        checks[i] = cb;

	        if (p.name().startsWith("PRODUCT")) {
	            colProduct.add(cb);
	        } else if (p.name().startsWith("ORDER")) {
	            colOrder.add(cb);
	        } else if (p.name().startsWith("EXCH")) {
	            colExchange.add(cb);
	        }
	    }

	    columnas.add(colProduct);
	    columnas.add(colExchange);
	    columnas.add(colOrder);
	    columnas.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    JButton btnGuardar = new JButton("Guardar permisos");
	    btnGuardar.addActionListener(e -> {
	        emp.permissions.clear();
	        for (int i = 0; i < checks.length; i++) {
	            if (checks[i].isSelected()) {
	                ctrl.agregarPermisoEmpleado(emp, Permission.values()[i]);
	            }
	        }
	        mostrarDetalles(emp);
	    });
	    btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    panelBtn.setOpaque(false);
	    panelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
	    panelBtn.add(btnGuardar);
	    
	    contenedor.add(columnas);
	    contenedor.add(Box.createVerticalStrut(5));
	    contenedor.add(panelBtn);

	    return contenedor;
	}

	/**
	 * Configura una fila estructurada con controles de selección múltiple (JCheckBox) 
	 * alineados horizontalmente para gestionar los diferentes tipos de roles empresariales
	 * asignables al empleado.
	 * 
	 * @param emp Instancia del objeto Employee cuyos roles funcionales van a ser modificados.
	 * @return Un JPanel con la botonera de roles y el disparador de sincronización de datos.
	 */
	private JPanel crearFilaRol(Employee emp) {
	    JPanel contenedor = new JPanel();
	    contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
	    contenedor.setOpaque(false);
	    contenedor.setAlignmentX(Component.LEFT_ALIGNMENT);

	    JLabel lbl = new JLabel("Rol:");
	    lbl.setForeground(COLOR_LABEL);
	    lbl.setFont(new Font("Arial", Font.BOLD, 11));
	    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

	    contenedor.add(lbl);
	    contenedor.add(Box.createVerticalStrut(5));

	    // Panel con checkboxes
	    JPanel rolesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
	    rolesPanel.setOpaque(false);
	    rolesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

	    JCheckBox[] checks = new JCheckBox[EmployeeRoles.values().length];

	    for (int i = 0; i < EmployeeRoles.values().length; i++) {
	        EmployeeRoles r = EmployeeRoles.values()[i];
	        JCheckBox cb = new JCheckBox(r.name());
	        cb.setOpaque(false);
	        cb.setForeground(Color.WHITE);
	        cb.setFont(new Font("Arial", Font.PLAIN, 12));
	        cb.setSelected(emp.Rol.contains(r));
	        checks[i] = cb;
	        rolesPanel.add(cb);
	    }

	    JButton btnGuardar = new JButton("Guardar rol");
	    btnGuardar.addActionListener(e -> {
	        emp.Rol.clear();
	        for (int i = 0; i < checks.length; i++) {
	            if (checks[i].isSelected()) {
	                emp.Rol.add(EmployeeRoles.values()[i]);
	            }
	        }
	        mostrarDetalles(emp);
	    });
	    btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    panelBtn.setOpaque(false);
	    panelBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
	    panelBtn.add(btnGuardar);
	    
	    contenedor.add(rolesPanel);
	    contenedor.add(Box.createVerticalStrut(5));
	    contenedor.add(panelBtn);

	    return contenedor;
	}
}