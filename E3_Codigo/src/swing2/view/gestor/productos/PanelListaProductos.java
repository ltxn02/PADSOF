package swing2.view.gestor.productos;

import catalog.Comic;
import catalog.Figurine;
import catalog.Game;
import catalog.NewProduct;
import logic.Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import swing2.controller.gestor.GestorProductoController;

/**
 * Panel de listado de productos.
 * Muestra tabla con todos los productos del catálogo con soporte de búsqueda en tiempo real.
 */
public class PanelListaProductos extends JPanel {
	private PanelGestionProductos panelPadre;
	private GestorProductoController ctrl;
	
	// === COMPONENTES DE BÚSQUEDA Y MIGRACIÓN ===
	private JTextField campoBusqueda;
	private JPanel pnlFilas; // Convertido en atributo de clase para poder actualizarlo
	
	// Colores
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_CABECERA = new Color(30, 45, 80);
	private static final Color COLOR_FILA = Color.WHITE;
	private static final Color COLOR_TEXTO_FILA = new Color(30, 45, 80);
	private static final Color COLOR_BOTON = new Color(52, 152, 219);

	public PanelListaProductos(PanelGestionProductos panelPadre, GestorProductoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		this.setLayout(new BorderLayout());
		this.setBackground(COLOR_FONDO);

		crearInterfaz();
	}

	private void crearInterfaz() {
		// Panel de acciones (botones superiores)
		JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlAcciones.setOpaque(false);
		pnlAcciones.setBorder(new EmptyBorder(20, 40, 10, 40));
		
		// --- Inicialización de la Barra de Búsqueda ---
		campoBusqueda = new JTextField();
		aplicarPlaceholder();
		campoBusqueda.setPreferredSize(new Dimension(500, 35));
		campoBusqueda.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (campoBusqueda.getText().equals("Buscar productos")) {
					campoBusqueda.setText("");
					campoBusqueda.setForeground(Color.BLACK);
					campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (campoBusqueda.getText().trim().isEmpty()) {
					aplicarPlaceholder();
				}
			}
		});

		campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				String texto = campoBusqueda.getText();
				if (texto.equals("Buscar productos")) return;
				actualizarTabla(texto); // Filtra los productos en tiempo real
			}
		});

		pnlAcciones.add(campoBusqueda);
		this.add(pnlAcciones, BorderLayout.NORTH);

		// Panel de tabla (inicializa la estructura visual)
		JPanel pnlTabla = crearTablaProductos();
		this.add(pnlTabla, BorderLayout.CENTER);
		
		// Cargar todos los productos inicialmente (filtro vacío)
		actualizarTabla("");
	}
	
	private void aplicarPlaceholder() {
		campoBusqueda.setText("Buscar productos");
		campoBusqueda.setForeground(Color.GRAY);
		campoBusqueda.setFont(new Font("Arial", Font.ITALIC, 12));
	}

	private JPanel crearTablaProductos() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(new EmptyBorder(0, 40, 20, 40));

		// === CABECERA ===
		JPanel pnlCabecera = new JPanel(new GridLayout(1, 7, 10, 0)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_CABECERA);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2.dispose();
			}
		};
		pnlCabecera.setOpaque(false);
		pnlCabecera.setPreferredSize(new Dimension(0, 50));
		pnlCabecera.setBorder(new EmptyBorder(0, 20, 0, 20));

		String[] headers = {"ID", "Nombre", "Tipo", "Marca", "Stock", "Foto", "Precio"};
		for (String h : headers) {
			JLabel lblHeader = new JLabel(h, SwingConstants.CENTER);
			lblHeader.setForeground(Color.WHITE);
			lblHeader.setFont(new Font("Arial", Font.BOLD, 14));
			pnlCabecera.add(lblHeader);
		}
		panel.add(pnlCabecera, BorderLayout.NORTH);

		// === FILAS DE PRODUCTOS (Estructura base del contenedor) ===
		pnlFilas = new JPanel();
		pnlFilas.setLayout(new BoxLayout(pnlFilas, BoxLayout.Y_AXIS));
		pnlFilas.setOpaque(false);
		pnlFilas.setBackground(COLOR_FONDO);

		JScrollPane scroll = new JScrollPane(pnlFilas);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.getViewport().setBackground(COLOR_FONDO);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		panel.add(scroll, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Lógica encargada de vaciar el contenedor e insertar únicamente 
	 * aquellos productos que coincidan con la búsqueda.
	 */
	private void actualizarTabla(String filtro) {
		pnlFilas.removeAll(); // Borramos lo que haya actualmente en pantalla

		ArrayList<NewProduct> catalogo = Application.getCatalog();
		boolean hayResultados = false;

		if (catalogo != null && !catalogo.isEmpty()) {
			for (NewProduct p : catalogo) {
				int idReal = 0;
				if (p instanceof catalog.Product) {
					idReal = ((catalog.Product) p).getProductId();
				}

				// Obtener Tipo y Marca para poder buscar también por estos criterios
				String tipo = "Desconocido", marca = "-";
				if (p instanceof Comic) {
					tipo = "Cómic";
					marca = ((Comic) p).getPublisher();
				} else if (p instanceof Figurine) {
					tipo = "Figura";
					marca = ((Figurine) p).getFranchise();
				} else if (p instanceof Game) {
					tipo = "Juego";
					marca = "Mecánica";
				}

				// Comprobamos si coincide con el filtro (por Nombre, Tipo, Marca o ID)
				boolean coincide = filtro.isEmpty() || filtro.equals("Buscar productos") ||
						p.getName().toLowerCase().contains(filtro.toLowerCase()) ||
						tipo.toLowerCase().contains(filtro.toLowerCase()) ||
						marca.toLowerCase().contains(filtro.toLowerCase()) ||
						String.valueOf(idReal).contains(filtro);

				if (coincide) {
					pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));
					pnlFilas.add(crearFilaProducto(p, idReal));
					hayResultados = true;
				}
			}
		}

		// Mensaje en caso de que no haya coincidencias o el catálogo esté vacío
		if (!hayResultados) {
			String mensaje = (filtro.isEmpty() || filtro.equals("Buscar productos")) ? 
					"No hay productos en el catálogo" : "No se encontraron productos coincidentes";
			
			JLabel lblVacio = new JLabel(mensaje);
			lblVacio.setFont(new Font("Arial", Font.BOLD, 18));
			lblVacio.setForeground(new Color(150, 150, 150));
			lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
			
			pnlFilas.add(Box.createVerticalGlue());
			pnlFilas.add(lblVacio);
			pnlFilas.add(Box.createVerticalGlue());
		}

		// Forzamos a la interfaz a redibujarse inmediatamente
		pnlFilas.revalidate();
		pnlFilas.repaint();
	}

	private JPanel crearFilaProducto(NewProduct p, int id) {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JPanel fila = new JPanel(new GridLayout(1, 7, 10, 0)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_FILA);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
				g2.dispose();
			}
		};
		fila.setOpaque(false);
		fila.setBorder(new EmptyBorder(5, 20, 5, 20));

		// ID
		fila.add(crearLabelFila(String.valueOf(id)));

		// Nombre
		String nombre = p.getName().length() > 20 ? p.getName().substring(0, 17) + "..." : p.getName();
		fila.add(crearLabelFila(nombre));

		// Tipo y Marca
		String tipo = "Desconocido", marca = "-";
		if (p instanceof Comic) {
			tipo = "Cómic";
			marca = ((Comic) p).getPublisher();
		} else if (p instanceof Figurine) {
			tipo = "Figura";
			marca = ((Figurine) p).getFranchise();
		} else if (p instanceof Game) {
			tipo = "Juego";
			marca = "Mecánica";
		}

		fila.add(crearLabelFila(tipo));
		fila.add(crearLabelFila(marca.length() > 15 ? marca.substring(0, 12) + "..." : marca));

		// Stock
		fila.add(crearLabelFila(String.valueOf((int) p.getStock())));

		// Foto
		JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
		cargarImagenPequena(p, lblFoto);
		fila.add(lblFoto);

		// Precio
		fila.add(crearLabelFila(String.format("%.2f€", p.getPrice())));

		wrapper.add(fila, BorderLayout.CENTER);
		return wrapper;
	}

	private JLabel crearLabelFila(String texto) {
		JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
		lbl.setFont(new Font("Arial", Font.BOLD, 13));
		lbl.setForeground(COLOR_TEXTO_FILA);
		return lbl;
	}

	private void cargarImagenPequena(NewProduct p, JLabel imgLabel) {
		if (p.getFotos() != null && !p.getFotos().isEmpty()) {
			String nombreArchivo = new File(p.getFotos().get(0)).getName();
			String[] rutas = {
					"E3_Codigo/src/imgProductos/" + nombreArchivo,
					"src/imgProductos/" + nombreArchivo,
					"../src/imgProductos/" + nombreArchivo
			};
			File f = encontrarArchivo(rutas);
			if (f != null) {
				ImageIcon icon = new ImageIcon(f.getAbsolutePath());
				Image scaled = icon.getImage().getScaledInstance(35, 45, Image.SCALE_SMOOTH);
				imgLabel.setIcon(new ImageIcon(scaled));
			} else {
				imgLabel.setText("No img");
				imgLabel.setFont(new Font("Arial", Font.PLAIN, 10));
				imgLabel.setForeground(COLOR_TEXTO_FILA);
			}
		}
	}

	private File encontrarArchivo(String[] rutas) {
		for (String r : rutas) {
			File f = new File(r);
			if (f.exists()) return f;
		}
		return null;
	}
	
	public void refrescar() {
	    if (campoBusqueda != null) {
	        aplicarPlaceholder();
	    }
	    actualizarTabla("");
	}
}