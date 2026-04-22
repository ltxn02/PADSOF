package utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import products.*;
import transactions.*;
import users.RegisteredUser;

/**
 * Clase que representa una notificación dentro del sistema.
 * Sirve para enviar avisos y mensajes a los usuarios registrados (por ejemplo,
 * mensajes de bienvenida, alertas de stock o actualizaciones de pedidos).
 * Hereda de {@link BaseElement} para utilizar sus capacidades base si fuera necesario.
 *
 * @author Iván Sánchez Bonacasa
 * @version 1.0
 */
public class Notification extends BaseElement implements java.io.Serializable{
    private int notificationId;
    private static int lastNotificationId = 1;
    private String message;
    private Instant receivedAt;
    private boolean read;
    private ArrayList<RegisteredUser> receivedUsers;

    /**
     * Constructor para crear una nueva notificación.
     * Al crearse, se asigna automáticamente la fecha y hora actuales y se marca como "no leída".
     *
     * @param message       El contenido de texto de la notificación.
     * @param receivedUsers Lista de usuarios ({@link RegisteredUser}) que reciben esta notificación.
     */
    public Notification(String message, ArrayList<RegisteredUser> receivedUsers) {
        this.message = message;
        this.receivedAt = Instant.now();
        this.read = false;
        this.receivedUsers = receivedUsers;

        // Asignamos el ID actual y sumamos 1 para la siguiente notificación generada
        this.notificationId = lastNotificationId++;
    }

    /**
     * Comprueba si la notificación ha sido leída por el usuario.
     *
     * @return true si ya fue leída, false si sigue pendiente.
     */
    public boolean isRead() {
        return this.read;
    }

    /**
     * Cambia el estado de lectura de la notificación.
     *
     * @param read true para marcarla como leída, false para marcarla como no leída.
     */
    public void markAsRead(boolean read){
        this.read = read;
    }

    /**
     * Genera una vista previa corta de la notificación (ideal para bandejas de entrada).
     * Muestra solo los primeros 20 caracteres del mensaje.
     *
     * @return Cadena de texto formateada con el estado, fecha y el mensaje acortado.
     */
    public String notificationPreview() {
        return this.buildString(20);
    }

    /**
     * Genera la vista completa de la notificación.
     *
     * @return Cadena de texto formateada con el estado, fecha y el mensaje completo.
     */
    @Override
    public String toString() {
        return this.buildString(-1);
    }

    // -- HELPERS ------------------------------------------------------------------

    /**
     * Metodo auxiliar interno para construir la cadena de texto de la notificación.
     * Permite acortar el mensaje si es demasiado largo o mostrarlo entero.
     *
     * @param maxChars Número máximo de caracteres a mostrar del mensaje. Si es -1, no hay límite.
     * @return Cadena de texto formateada.
     * @throws IllegalArgumentException Si maxChars es un valor menor a -1.
     */
    private String buildString(int maxChars) throws IllegalArgumentException {
        if (maxChars < -1) {
            throw new IllegalArgumentException("Invalid max characters");
        }

        if (this.message == null) {
            return "";
        }

        StringBuilder res = new StringBuilder();
        String endString = "";
        int end = maxChars;

        if(maxChars == -1 || maxChars >= this.message.length()) {
            end = this.message.length();
        } else {
            endString = "...";
        }

        res.append(this.read ? "   " : " ! ");
        res.append("[" + formatInstant(this.receivedAt) + "] ");
        res.append(notificationId + ": ");
        res.append(this.message.substring(0, end) + endString);

        return res.toString();
    }

    /**
     * Metodo auxiliar para formatear la marca de tiempo (Instant) a un formato legible por humanos.
     *
     * @param instant La marca de tiempo a formatear.
     * @return Cadena con el formato "dd/MM/yy HH:mm". Si recibe null, devuelve "N/A".
     */
    private String formatInstant(Instant instant) {
        if(instant == null) return "N/A";

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm").withZone(ZoneId.systemDefault());
        return fmt.format(instant);
    }
}