package utils;

import java.time.*;

/**
 * Clase abstracta que sirve como clase base para los elementos del sistema.
 * Proporciona un mecanismo estándar de auditoría y control de estado,
 * gestionando si un elemento está activo o inactivo, así como las marcas
 * de tiempo de su creación y su última actualización.
 * * @author Lidia Martin
 * @version 2.1
 */
public abstract class BaseElement implements java.io.Serializable {
	private boolean active;
	private Instant createdAt;
	private Instant updatedAt;

	/**
	 * Constructor por defecto.
	 * Al instanciar un elemento, este nace automáticamente en estado "activo"
	 * y registra el instante exacto (fecha y hora) de su creación.
	 */
	public BaseElement() {
		this.active = true;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	/**
	 * Cambia el estado del elemento (activo o inactivo) y actualiza
	 * automáticamente la marca de tiempo de su última modificación.
	 * * @param active true para marcar el elemento como activo, false para darlo de baja/inactivarlo.
	 */
	public void markAs(boolean active/*, RegisteredUser by*/) {
		this.active = active;
		this.updatedAt = Instant.now();
	}

	/**
	 * Comprueba si el elemento se encuentra actualmente activo en el sistema.
	 * * @return true si el elemento está activo, false si está inactivo.
	 */
	public boolean isActive() {
		return this.active == true;
	}

	/**
	 * Devuelve una representación en formato texto del estado de auditoría del elemento,
	 * incluyendo si está activo, cuándo fue creado y cuándo fue modificado por última vez.
	 * * @return Cadena de texto con la información de estado y marcas de tiempo.
	 */
	@Override
	public String toString() {
		String str = "Status: ";

		if (this.active == true) {
			str += "active. ";
		} else {
			str += "inactive. ";
		}

		return str + "Created at: " + this.createdAt + ". Last updated: " + this.updatedAt;
	}
}