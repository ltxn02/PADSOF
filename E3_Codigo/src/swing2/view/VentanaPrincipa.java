package swing2.view;

import logic.Application;
import users.RegisteredUser;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipa extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contenedor = new JPanel(cardLayout);

    // El "Estado" de la aplicación: quién está usando el programa
    private RegisteredUser usuarioLogueado = null;

    public VentanaPrincipa() {
        // Carga inicial de datos desde el archivo binario
        Application.cargarDatos("rongero_data.dat");

        setTitle("RONGERO - Tienda Frikis");
        setSize(1400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Al inicio, cargamos las pantallas base.
        // PanelInicioo recibe 'null' porque empezamos como Invitados (Guest).
        contenedor.add(new PanelInicioo(this, usuarioLogueado), "INICIO");
        contenedor.add(new PanelIntercambios(this, usuarioLogueado), "INTERCAMBIOS");
        contenedor.add(new PanelLoginn(this), "LOGIN");
        contenedor.add(new PanelRegistro(this), "REGISTRO");
        add(contenedor);
        setVisible(true);
    }

    /**
     * Cambia el usuario actual y refresca el Panel de Inicio.
     * Esto permite que el sistema de recomendaciones actúe si el usuario es un Client.
     */
    public void cambiarSesion(RegisteredUser nuevoUsuario) {
        this.usuarioLogueado = nuevoUsuario;

        // 1. Eliminamos la versión anterior del panel de inicio
        Component[] componentes = contenedor.getComponents();
        for (Component c : componentes) {
            // Buscamos el panel de inicio para reemplazarlo
            if (c instanceof PanelInicioo) {
                contenedor.remove(c);
            }
        }

        // 2. Añadimos el nuevo panel con el usuario actualizado
        // Si el usuario es Client, CatalogoController activará las recomendaciones.
        contenedor.add(new PanelInicioo(this, usuarioLogueado), "INICIO");
        contenedor.add(new PanelIntercambios(this, usuarioLogueado), "INTERCAMBIOS");

        // 3. Forzamos a la UI a actualizarse
        contenedor.revalidate();
        contenedor.repaint();

        // 4. Volvemos a la pantalla principal
        mostrarPantalla("INICIO");
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