package swing2.view;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Application;
import users.*;

public class VentanaPrincipa extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contenedor = new JPanel(cardLayout);

    // El "Estado" de la aplicación: quién está usando el programa
    private RegisteredUser usuarioLogueado = null;
    
    // Referencias a paneles
    private PanelInicioo panelCliente = null;
    private PanelGestorDashboard panelGestor = null;

    public VentanaPrincipa() {
        // Carga inicial de datos desde el archivo binario
        Application.cargarDatos("rongero_data.dat");

        setTitle("RONGERO - Tienda Frikis");
        setSize(1400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Paneles principales
        panelCliente = new PanelInicioo(this, usuarioLogueado);
        panelGestor = new PanelGestorDashboard(this, null);
        
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
     */
    public void cambiarSesion(RegisteredUser nuevoUsuario) {
        this.usuarioLogueado = nuevoUsuario;
        
        if (nuevoUsuario == null) {
        	mostrarPantalla("LOGIN");
        } else if (nuevoUsuario instanceof Manager) {
        	Component[] componentes = contenedor.getComponents();
        	for (Component c : componentes) {
        		if (c instanceof PanelGestorDashboard) {
        			contenedor.remove(c);
        			break;
        		}
        	}
        	
        	// Crear nuevo panel con el manager
        	panelGestor = new PanelGestorDashboard(this, (Manager)nuevoUsuario);
        	contenedor.add(panelGestor, "GESTOR");
        	
        	contenedor.revalidate();
        	contenedor.repaint();
        	
        	mostrarPantalla("GESTOR");
        } else if (nuevoUsuario instanceof Employee) {
        	// ========================================================
            // USUARIO ES EMPLEADO → Por ahora, mostrar INICIO
            // ========================================================
            // (Puedes crear un panel especial para empleados después)
        	mostrarPantalla("INICIO");
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
        cardLayout.show(contenedor, nombre);
    }

    public RegisteredUser getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void main(String[] args) {
        // Ejecución segura en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new VentanaPrincipa());
    }
}