package swing2.view;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Application;
import swing2.view.empleado.PanelProductosEmpleado;
import swing2.view.gestor.PanelDashboard;
import users.*;

public class VentanaPrincipa extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contenedor = new JPanel(cardLayout);

    // El "Estado" de la aplicación: quién está usando el programa
    private RegisteredUser usuarioLogueado = null;
    
    // Referencias a paneles
    private PanelInicioo panelCliente = null;
    private PanelDashboard panelGestor = null;
    private PanelCarrito panelCarrito = null;
    public VentanaPrincipa() {
        // Carga inicial de datos desde el archivo binario
        Application.cargarDatos("rongero_data.dat");

        setTitle("RONGERO - Tienda Frikis");
        setSize(1400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Paneles principales
        panelCliente = new PanelInicioo(this, usuarioLogueado);
        panelGestor = new PanelDashboard(this, null);
        
        // Al inicio, cargamos las pantallas base.
        // PanelInicioo recibe 'null' porque empezamos como Invitados (Guest).
        contenedor.add(panelCliente, "INICIO");
        contenedor.add(new PanelIntercambios(this, usuarioLogueado), "INTERCAMBIOS");
        contenedor.add(new PanelLoginn(this), "LOGIN");
        contenedor.add(new PanelRegistro(this), "REGISTRO");
        contenedor.add(panelGestor, "GESTOR");
        
        add(contenedor);
        setVisible(true);
    }

    /**
     * Cambia el usuario actual y refresca el Panel de Inicio.
     * Esto permite que el sistema de recomendaciones actúe si el usuario es un Client.
     * 
     * 
     * !!!! MODIFICADA PARA QUE RECONOZCA EL TIPO DE USUARIO CON EL QUE SE ACCEDE (Client, Employee, Manager)
     * ~ Lidia, 20:10, 05/05/2026
     */
    public void cambiarSesion(RegisteredUser nuevoUsuario) {
        this.usuarioLogueado = nuevoUsuario;
        
        if (nuevoUsuario == null) {
        	mostrarPantalla("LOGIN");
        } else if (nuevoUsuario instanceof Manager) {
        	Component[] componentes = contenedor.getComponents();
        	for (Component c : componentes) {
        		if (c instanceof PanelDashboard) {
        			contenedor.remove(c);
        			break;
        		}
        	}
        	
        	// Crear nuevo panel con el manager
        	panelGestor = new PanelDashboard(this, (Manager)nuevoUsuario);
        	contenedor.add(panelGestor, "GESTOR");
        	
        	contenedor.revalidate();
        	contenedor.repaint();
        	
        	mostrarPantalla("GESTOR");
        } else if (nuevoUsuario instanceof Employee) {
            // ========================================================
            // USUARIO ES EMPLEADO
            // ========================================================
            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelProductosEmpleado) {
                    contenedor.remove(c);
                    break;
                }
            }

            // Creamos y añadimos el nuevo panel de administrador
            PanelProductosEmpleado panelEmp = new PanelProductosEmpleado(this, (Employee) nuevoUsuario);
            contenedor.add(panelEmp, "PRODUCTOS_EMPLEADO");

            contenedor.revalidate();
            contenedor.repaint();

            mostrarPantalla("PRODUCTOS_EMPLEADO");

        } else if (nuevoUsuario instanceof Client) {
            // ===== ES CLIENTE =====
            // Remover los paneles INICIOO e INTERCAMBIOS anteriores
            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelInicioo || c instanceof PanelIntercambios) {
                    contenedor.remove(c);
                }
            }

            // Crear NUEVOS paneles con el cliente actualizado
            panelCliente = new PanelInicioo(this, nuevoUsuario);
            contenedor.add(panelCliente, "INICIO");
            contenedor.add(new PanelIntercambios(this, nuevoUsuario), "INTERCAMBIOS");

            // Actualizar UI
            contenedor.revalidate();
            contenedor.repaint();

            // Mostrar panel de cliente
            mostrarPantalla("INICIO");
        }
        
    }

    /**
     * Cambia la visibilidad entre los paneles del CardLayout.
     */
    public void mostrarPantalla(String nombre) {
        if (nombre.equals("CARRITO")) {
            // 1. Eliminar el panel de carrito anterior si existe
            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelCarrito) {
                    contenedor.remove(c);
                    break;
                }
            }

            // 2. Comprobar que el usuario sea Cliente y tenga carrito
            if (usuarioLogueado instanceof Client) {
                Client cliente = (Client) usuarioLogueado;
                // Creamos el panel pasando el ShoppingCart del objeto Client
                panelCarrito = new PanelCarrito(cliente.getShoppingCart(), this);
                contenedor.add(panelCarrito, "CARRITO");
            } else {
                // Si por error alguien intenta entrar sin ser cliente
                cardLayout.show(contenedor, "LOGIN");
                return;
            }
        }

        // Refrescar y mostrar
        contenedor.revalidate();
        contenedor.repaint();
        cardLayout.show(contenedor, nombre);
    }

    public RegisteredUser getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        // Ejecución segura en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new VentanaPrincipa());
    }
}