public enum Status {
	OK(0, "OK"),
	ERROR(-1, "ERROR");
	
	private short statusCode;
    private String statusString;
	
	/**
	 * Constructor para crear una nueva línea con la información del color de la línea y el tiempo entre paradas.
	 * @param color Nombre del color de la linea
	 * @param minutosEntreParadas Tiempo entre paradas en minutos
	 */
	private Linea(short statusCode, String statusString) {
        this.statusCode=statusCode;
		this.statusString=statusString;
	}
	
	/**
	 * Método público para obtener el tiempo entre paradas en función de la línea.
	 * @return Tiempo entre paradas en minutos
	 */
	public int getMinutosEntreParadas() {
		return this.minutosEntreParadas;
	}
	
	/**
	 * Devuelve una representación de la línea de tren.
	 * @return Cadena con el nombre y el color de la línea de tren.
	 */
	public String toString() {
		return this.name() + " (" + this.color + ")";
	}
}