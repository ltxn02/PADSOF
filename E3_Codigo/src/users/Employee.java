package users;

import java.util.*;
import java.io.*;
import utils.*;
import transactions.*;
import discounts.*;
import products.*;
import products.catalog.Catalog;

/**
 * Clase que representa a un Empleado (Employee) del sistema de la tienda.
 * 
 * Los empleados son usuarios con permisos administrativos limitados, heredados de {@link Staff}.
 * Tienen la responsabilidad de realizar operaciones específicas del sistema según los permisos
 * que les hayan sido asignados por un gestor ({@link Manager}). Sus funcionalidades principales incluyen:
 * <ul>
 *   <li>Cargar y editar productos en el catálogo (si tienen permiso)</li>
 *   <li>Cargar productos de forma masiva desde archivos CSV/TXT</li>
 *   <li>Cambiar la visibilidad de productos en el catálogo</li>
 *   <li>Valorar productos de segunda mano (si tienen permiso)</li>
 *   <li>Validar y cancelar intercambios entre clientes (si tienen permiso)</li>
 *   <li>Actualizar el estado de pedidos (si tienen permiso)</li>
 * </ul>
 * 
 * Cada empleado tiene un estado de activación (activo/inactivo) y una lista de permisos
 * específicos que determinan qué operaciones puede realizar en el sistema.
 * 
 * @author Lidia Martín
 * @version 2.5
 */
public class Employee extends Staff implements java.io.Serializable{
    private boolean enabled;
    public ArrayList<EmployeeRoles> Rol;
    public ArrayList<Permission> permissions;
    
    /**
    * Constructor para crear una nueva cuenta de empleado en el sistema.
    * Inicializa todos los atributos del empleado, incluyendo su estado de activación
    * y las listas de permisos y roles (inicialmente vacías).
    *
    * @param username       Nombre de usuario único para el inicio de sesión (ej: "empleado").
    * @param password       Contraseña cifrada o en texto plano según la política de seguridad.
    * @param fullname       Nombre completo del empleado (ej: "Empleado de Prueba").
    * @param dni            Documento Nacional de Identidad del empleado (ej: "87654321B").
    * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "15/05/1995").
    * @param email          Correo electrónico corporativo del empleado (ej: "empleado@rongero.es").
    * @param phoneNumber    Número de teléfono de contacto (ej: "600000000").
    * @param salary         Salario bruto anual del empleado en euros (ej: 1200.00).
    * @param enabled        {@code true} si la cuenta debe estar activa al crear, 
    *                       {@code false} si debe estar desactivada inicialmente.
    */
    public Employee(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
        this.enabled = enabled;
        this.permissions= new ArrayList<>();
        this.Rol = new ArrayList<>();
    }
    
    /**
     * Edita un producto existente en el catálogo con nuevos valores.
     * 
     * Este método valida que el empleado tenga el permiso {@link Permission#PRODUCT_EDIT}
     * antes de permitir cualquier modificación. Además, detecta automáticamente el tipo
     * específico de producto (Pack, Comic, Game o Figurine) y aplica las validaciones
     * y cambios particulares de cada tipo.
     *
     * @param p                  El producto ({@link NewProduct}) a editar.
     * @param name               Nuevo nombre del producto (o el actual si no cambia).
     * @param description        Nueva descripción del producto (o la actual si no cambia).
     * @param price              Nuevo precio en euros (o el actual si no cambia).
     * @param picturePath        Nueva ruta de la imagen (o la actual si no cambia).
     * @param stock              Nuevo stock disponible (o el actual si no cambia).
     * @param specificArguments  Argumentos variables específicos según el tipo de producto:
     *                           <ul>
     *                             <li><strong>Pack:</strong> {@code ArrayList<NewProduct>} (productos del pack)</li>
     *                             <li><strong>Comic:</strong> {@code IDiscount, Integer, String, Integer, ArrayList<String>}
     *                                 (descuento, páginas, editorial, año, autores)</li>
     *                             <li><strong>Game:</strong> {@code IDiscount, Integer, ArrayList<String>, AgeRange}
     *                                 (descuento, jugadores, mecánicas, rango edad)</li>
     *                             <li><strong>Figurine:</strong> {@code IDiscount, Double, Double, Double, String, String}
     *                                 (descuento, altura, ancho, profundidad, material, franquicia)</li>
     *                           </ul>
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#PRODUCT_EDIT}.
     */
    public void editProduct(NewProduct p, String name, String description, double price, String picturePath, int stock, Object...specificArguments) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_EDIT) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to edit products");
    	}
    	p.editProductInfo(name, description, price, picturePath, stock);
    	
    	if(p instanceof Pack) {
    		this.editPack((Pack)p, specificArguments);
    	} else if (p instanceof Comic) {
    		this.editComic((Comic)p, specificArguments);
    	} else if (p instanceof Game) {
    		this.editGame((Game)p, specificArguments);
    	} else if (p instanceof Figurine) {
    		this.editFigurine((Figurine)p, specificArguments);
    	}
    }    
    
    /**
     * Carga un único producto en el catálogo con validación de permisos.
     * 
     * El empleado debe tener el permiso {@link Permission#PRODUCT_LOAD} para poder
     * añadir nuevos productos al catálogo. El tipo de producto y sus atributos
     * específicos se proporcionan a través de un mapa de datos flexible.
     *
     * @param catalog  El catálogo ({@link Catalog}) al que se añadirá el producto.
     * @param itemType El tipo de producto a crear ({@link ItemType}).
     * @param data     Mapa con los atributos del producto:
     *                 <ul>
     *                   <li><strong>Comunes:</strong> "name", "description", "price", "stock", "picturePath", "categories", "reviews"</li>
     *                   <li><strong>Comic:</strong> "nPages", "publisher", "publicationYear", "writtenBy"</li>
     *                   <li><strong>Game:</strong> "nPlayers", "mechanics", "ageRange"</li>
     *                   <li><strong>Figurine:</strong> "height", "width", "depth", "material", "franchise"</li>
     *                   <li><strong>Pack:</strong> "products" (ArrayList de productos)</li>
     *                 </ul>
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#PRODUCT_LOAD}.
     */
    public void loadProduct(Catalog catalog, ItemType itemType, Map<String, Object> data) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_LOAD) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to load products");
    	}
    	catalog.addProductOnSale(itemType, data);
    }
    
    /**
     * Carga múltiples productos en el catálogo desde un archivo CSV/TXT.
     * 
     * El archivo debe tener un formato específico con columnas separadas por punto y coma (;):
     * <pre>
     * TIPO;NOMBRE;DESCRIPCION;PRECIO;STOCK;RUTA_IMAGEN;[DATOS_ESPECÍFICOS]
     * 
     * Ejemplos:
     * COMIC;One Piece Vol.1;El inicio de Luffy;7.99;50;img/onepiece1.jpg;208;Planeta Cómic;1997;Eiichiro Oda
     * GAME;Catan;Juego de estrategia;45.00;30;img/catan.jpg;4;Gestión,Dados;10-99
     * FIGURINE;Goku;Figura anime;35.50;15;img/goku.jpg;15.0;5.0;5.0;PVC;Anime
     * </pre>
     * 
     * El empleado debe tener el permiso {@link Permission#PRODUCT_LOAD} para ejecutar
     * esta operación. Los errores de parseo se registran pero no detienen el proceso.
     *
     * @param filePath El path absoluto o relativo del archivo a cargar (ej: "fileLoadBulkTest").
     * @param catalog  El catálogo ({@link Catalog}) al que se añadirán los productos.
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#PRODUCT_LOAD}.
     * @throws IOException       Si hay errores al leer el archivo.
     */
    public void loadProductBulk(String filePath, Catalog catalog) throws SecurityException, IOException {
        String line;
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] dataArray = line.split(cvsSplitBy);
                
                try {
                    // 1. Determinar el tipo (primera columna)
                    ItemType type = ItemType.valueOf(dataArray[0].toUpperCase());
                    
                    // 2. Construir el mapa de datos
                    Map<String, Object> data = new HashMap<>();
                    
                    // Datos comunes (posiciones 1 a 5)
                    data.put("name", dataArray[1]);
                    data.put("description", dataArray[2]);
                    data.put("price", Double.parseDouble(dataArray[3]));
                    data.put("stock", Integer.parseInt(dataArray[4]));
                    data.put("picturePath", dataArray[5]);
                    
                    // 3. Datos específicos según el tipo
                    fillSpecificData(catalog, type, data, dataArray);

                    // 4. Llamar a tu función existente
                    this.loadProduct(catalog, type, data);

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
        }
    }
    
    /**
     * Cambia la visibilidad de un producto en el catálogo.
     * 
     * Un producto invisible no aparecerá en el catálogo públicamente, pero seguirá
     * existiendo en la base de datos y podrá ser reactivado en cualquier momento.
     * El empleado debe tener el permiso {@link Permission#PRODUCT_EDIT} para realizar
     * esta acción.
     *
     * @param product El producto ({@link NewProduct}) cuya visibilidad será modificada.
     * @param visible {@code true} para hacer visible el producto, {@code false} para ocultarlo.
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#PRODUCT_EDIT}.
     */
    public void changeVisibilityProduct(NewProduct product, boolean visible) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_EDIT) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to edit products");
    	}
    	product.changeVisibilityProduct(visible);
    }
    
    /**
     * Valora un producto de segunda mano perteneciente a un cliente.
     * 
     * Este método es fundamental en el proceso de intercambio. El empleado examina
     * el producto, establece su condición ({@link Condition}) y le asigna un valor
     * monetario. Solo después de esta valoración, el producto puede ser ofrecido
     * en intercambios. El empleado debe tener el permiso {@link Permission#EXCH_PRODUCT_APPRAISE}.
     *
     * @param client   El cliente propietario del producto a valorar.
     * @param product  El producto de segunda mano ({@link SecondHandProduct}) a valorar.
     * @param c        La condición ({@link Condition}) del producto tras la inspección.
     * @param valuedOn El valor monetario asignado al producto en euros.
     * 
     * @throws SecurityException           Si el empleado no tiene el permiso 
     *                                     {@link Permission#EXCH_PRODUCT_APPRAISE}.
     * @throws IllegalArgumentException    Si el producto no pertenece al cliente especificado.
     */
    public void appraiseSecondHandProduct(Client client, SecondHandProduct product, Condition c, double valuedOn) throws SecurityException, IllegalArgumentException {
    	if(this.checkPermission(Permission.EXCH_PRODUCT_APPRAISE) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to appraise products from a client's wallet");
    	}
    	
    	if(client.hasSecondHandProduct(product) == false) {
    		throw new IllegalArgumentException("Invalid product, it doesn't exist in the client's wallet");
    	}
    	
    	product.appraiseSecondHand(this, c, valuedOn);
    }
    
    /**
     * Valida un intercambio entre dos clientes.
     * 
     * Un empleado con el permiso {@link Permission#EXCH_VALIDATE} puede confirmar
     * que un intercambio cumple con todos los requisitos del sistema y proceder con
     * la transferencia de productos. Una vez validado, el intercambio no puede ser
     * revertido sin cancelación explícita.
     *
     * @param exchange El intercambio ({@link Exchange}) a validar.
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#EXCH_VALIDATE}.
     */
    public void validateExchange(Exchange exchange) throws SecurityException {
    	if(this.checkPermission(Permission.EXCH_VALIDATE) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to validate exchanges");
    	}
    	
    	try {
    		exchange.validateExchange(this);
    	} catch (Exception e) {
    		System.err.println("Error validating exchange: " + e.getMessage());
    	}
    }
    
    /**
     * Cancela un intercambio previamente validado o en proceso.
     * 
     * Solo un empleado con el permiso {@link Permission#EXCH_VALIDATE} puede cancelar
     * intercambios. Esta acción revierte los cambios de propiedad y disponibilidad
     * de los productos involucrados.
     *
     * @param exchange El intercambio ({@link Exchange}) a cancelar.
     * 
     * @throws SecurityException Si el empleado no tiene el permiso {@link Permission#EXCH_VALIDATE}.
     */
    public void cancelExchange(Exchange exchange) throws SecurityException {
    	if(this.checkPermission(Permission.EXCH_VALIDATE) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to cancel exchanges");
    	}
    	
    	try {
    		exchange.cancelExchange(this);
    	} catch (Exception e) {
    		System.err.println("Error cancelling exchange: " + e.getMessage());
    	}
    }
    
    /**
     * Añade un nuevo permiso al empleado.
     * 
     * Los permisos duplicados no se añaden (la lista funciona como un conjunto).
     * Este método es llamado típicamente por el gestor ({@link Manager}).
     *
     * @param e El permiso ({@link Permission}) a asignar al empleado.
     */
    public void add_permisions(Permission e){
        this.permissions.add(e);
    }
    
    /**
     * Revoca un permiso previamente asignado al empleado.
     * 
     * Si el empleado no poseía ese permiso, el método no produce efecto.
     *
     * @param e El permiso ({@link Permission}) a revocar del empleado.
     */
    public void delete_permisions(Permission e){
        if (this.permissions.contains(e)) {
            this.permissions.remove(e);
        }
    }
    
    /**
     * Obtiene la lista de todos los permisos actualmente asignados al empleado.
     *
     * @return Un {@code ArrayList} con los permisos ({@link Permission}) del empleado.
     *         La lista puede estar vacía si no hay permisos asignados.
     */
    public ArrayList<Permission> permisosEmpleado(){
        return this.permissions;
    }
    
    /**
     * Añade un nuevo rol al empleado para clasificar su responsabilidad organizacional.
     *
     * @param Rol El rol ({@link EmployeeRoles}) a asignar al empleado.
     */
    public void add_roles(EmployeeRoles Rol){
        this.Rol.add(Rol);
    }
    
    /**
     * Revoca un rol previamente asignado al empleado.
     *
     * @param Rol El rol ({@link EmployeeRoles}) a eliminar del empleado.
     */
    public void delete_roles(EmployeeRoles Rol){
        this.Rol.remove(Rol);
    }
    
    /**
     * Activa la cuenta del empleado permitiéndole acceder al sistema y realizar operaciones.
     */
    public void activateEmployee() {
        this.enabled = true;
    }
    
    /**
     * Desactiva la cuenta del empleado, impidiendo que acceda al sistema.
     * Sus datos y registro histórico se mantienen en la base de datos.
     */
    public void desactivateEmployee() {
        this.enabled = false;
    }
    
    /**
     * Comprueba si la cuenta del empleado está activa.
     *
     * @return {@code true} si la cuenta está activa, {@code false} si está desactivada.
     */
    public boolean isEnabled() {
        return this.enabled;
    }
    
    /**
     * Actualiza el estado de un pedido con validación de permisos.
     * 
     * Solo un empleado con el permiso {@link Permission#ORDER_STATUS_UPDATE}
     * puede cambiar el estado de un pedido (ej: de "pendiente" a "enviado").
     * Este seguimiento es fundamental para la gestión de logística y la
     * comunicación con clientes sobre sus órdenes.
     *
     * @param order     El pedido ({@link Order}) cuyo estado será actualizado.
     * @param newStatus El nuevo estado ({@link OrderStatus}) a asignar al pedido.
     * 
     * @throws SecurityException Si el empleado no tiene el permiso 
     *                           {@link Permission#ORDER_STATUS_UPDATE}.
     */
    public void updateOrderStatus(Order order, OrderStatus newStatus) throws SecurityException {
        // 1. Verificamos que el empleado tiene el permiso específico
        if(this.checkPermission(Permission.ORDER_STATUS_UPDATE) == false) {
            throw new SecurityException("[!] Acceso denegado: No tienes el permiso ORDER_STATUS_UPDATE para cambiar el estado de los pedidos.");
        }

        // 2. Si lo tiene, actualizamos el estado del pedido
        order.setOrderStatus(newStatus);
    }

    
    
    
	// ═══════════════════════════════════════════════════════════
    // HELPERS / MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════════════════════

	
    /**
     * Método auxiliar que mapea los datos específicos de cada tipo de producto
     * a partir de las columnas adicionales en el archivo CSV.
     * 
     * Este método es usado internamente por {@link #loadProductBulk(String, Catalog)}
     * para procesar automáticamente los atributos específicos de cómics, juegos,
     * figuras y packs.
     *
     * @param catalog El catálogo de referencia (usado especialmente para packs).
     * @param type    El tipo de producto ({@link ItemType}).
     * @param data    Mapa donde se insertarán los datos específicos procesados.
     * @param row     Array de strings con las columnas del CSV (desde la 7ª en adelante).
     */
    private void fillSpecificData(Catalog catalog, ItemType type, Map<String, Object> data, String[] row) {
        switch (type) {
            case COMIC:
                data.put("nPages", Integer.parseInt(row[6]));
                data.put("publisher", row[7]);
                data.put("publicationYear", Integer.parseInt(row[8]));
                // Para listas, asumiendo formato "Autor1,Autor2,Autor3"
                data.put("writtenBy", new ArrayList<>(Arrays.asList(row[9].split(","))));
                break;

            case GAME:
                data.put("nPlayers", Integer.parseInt(row[6]));
                data.put("mechanics", new ArrayList<>(Arrays.asList(row[7].split(","))));
                data.put("ageRange", AgeRange.stringToAgeRange(row[8]));
                break;

            case FIGURINE:
                data.put("height", Double.parseDouble(row[6]));
                data.put("width", Double.parseDouble(row[7]));
                data.put("depth", Double.parseDouble(row[8]));
                data.put("material", row[9]);
                data.put("franchise", row[10]);
                break;

            case PACK:
                // Para el PACK, podrías recibir una lista de IDs o nombres de productos
                // Aquí deberías implementar una lógica para buscar esos productos en el catálogo
                data.put("products", catalog.packProducts(row[6])); 
                break;
        }
    }
    
    /**
     * Verifica si el empleado tiene un permiso específico.
     *
     * @param p El permiso ({@link Permission}) a verificar.
     * @return {@code true} si el empleado tiene el permiso, {@code false} en caso contrario.
     */
    private boolean checkPermission(Permission p) {
    	return this.permissions.contains(p);
    }
    
    /**
     * Método auxiliar privado para editar los atributos específicos de un Pack.
     * 
     * Valida y aplica cambios a la lista de productos que componen el pack,
     * asegurando que se mantienen las restricciones de multiplicidad (mínimo 2 productos).
     *
     * @param pack    El pack a editar.
     * @param objects Argumentos variables que deben contener exactamente 1 argumento:
     *                un {@code ArrayList<NewProduct>} con los nuevos productos del pack.
     */
    private void editPack(Pack pack, Object...objects) {
    	try {
    		Class<?> [] types = new Class<?>[] {
    			ArrayList.class,	// Tipo del ArrayList en types[-1]
    			NewProduct.class	// Tipo del primer ArrayList
    		};
    		this.validateArguments(objects, types, 1);
    	
    		@SuppressWarnings("unchecked")
    		ArrayList<NewProduct> list = (ArrayList<NewProduct>)objects[0];
    		
    		pack.editPackInfo(list);
    	} catch (Exception e) {
    		System.err.println("Error editing Pack: " + e.getMessage());
    	}
    }
    
    /**
     * Método auxiliar privado para editar los atributos específicos de un Comic.
     * 
     * Valida y aplica cambios a propiedades como el número de páginas, editorial,
     * año de publicación y autores del cómic.
     *
     * @param comic   El cómic a editar.
     * @param objects Argumentos variables que deben contener exactamente 5 argumentos:
     *                {@code IDiscount, Integer (nPages), String (publisher), Integer (year), ArrayList<String> (authors)}
     */
    private void editComic(Comic comic, Object...objects) {
    	try {
    		Class<?> [] types = new Class<?>[] {
    			IDiscount.class,
    			Integer.class,
    			String.class,
    			Integer.class,
    			ArrayList.class,
    			String.class
    		};
    		this.validateArguments(objects, types, 5);
    		
    		IDiscount discount = (IDiscount)objects[0];
    		int nPages = (Integer)objects[1];
    		String publisher = (String)objects[2];
    		int publicationYear = (Integer)objects[3];
    		@SuppressWarnings("unchecked")
    		ArrayList<String> writtenBy = (ArrayList<String>)objects[4];
    		
    		comic.editComicInfo(discount, nPages, publisher, publicationYear, writtenBy);
    	} catch (Exception e) {
    		System.err.println("Error editing Comic: " + e.getMessage());
    	}
    }
    
    /**
     * Método auxiliar privado para editar los atributos específicos de un Game.
     * 
     * Valida y aplica cambios a propiedades como el número de jugadores, mecánicas
     * del juego y el rango de edad recomendado.
     *
     * @param game    El juego a editar.
     * @param objects Argumentos variables que deben contener exactamente 4 argumentos:
     *                {@code IDiscount, Integer (nPlayers), ArrayList<String> (mechanics), AgeRange}
     */
    private void editGame(Game game, Object...objects) {
    	try {
    		Class<?> [] types = new Class<?>[] {
    			IDiscount.class,
    			Integer.class,
    			ArrayList.class,	// Tipo del ArrayList en types[-1]
    			AgeRange.class,
    			String.class		// Tipo del primer ArrayList
    		};
    		this.validateArguments(objects, types, 4);
    		
    		IDiscount discount = (IDiscount)objects[0];
    		int nPlayers = (Integer)objects[1];
    		@SuppressWarnings("unchecked")
    		ArrayList<String> mechanics = (ArrayList<String>)objects[2];
    		AgeRange ageRange = (AgeRange)objects[3];
    		
    		game.editGameInfo(discount, nPlayers, mechanics, ageRange);
    	} catch (Exception e) {
    		System.err.println("Error editing Game: " + e.getMessage());
    	}
    }
    
    /**
     * Método auxiliar privado para editar los atributos específicos de una Figurine.
     * 
     * Valida y aplica cambios a propiedades físicas como altura, ancho, profundidad,
     * material y franquicia de la figura.
     *
     * @param figurine El figura a editar.
     * @param objects  Argumentos variables que deben contener exactamente 6 argumentos:
     *                 {@code IDiscount, Double (height), Double (width), Double (depth), String (material), String (franchise)}
     */
    private void editFigurine(Figurine figurine, Object...objects) {
    	try {
    		Class<?> [] types = new Class<?>[] {
    			IDiscount.class,
    			Double.class,
    			Double.class,
    			Double.class,
    			String.class,
    			String.class
    		};
    		this.validateArguments(objects, types, 6);
    		
    		IDiscount discount = (IDiscount)objects[0];
    		double height = (Double)objects[1], width = (Double)objects[2], depth = (Double)objects[3];
    		String material = (String)objects[4], franchise = (String)objects[5];
    		
    		figurine.editFigurineInfo(discount, height, width, depth, material, franchise);
    	} catch (Exception e) {
    		System.err.println("Error editing Figurine: " + e.getMessage());
    	}
    }
    
    /**
     * Valida que los argumentos variables coincidan en cantidad y tipos esperados.
     * 
     * Este método realiza una validación rigurosa de los parámetros varargs,
     * especialmente útil para detectar tipos de lista y sus elementos genéricos.
     *
     * @param objects         Array de objetos a validar.
     * @param expectedTypes   Array de clases esperadas (ArrayList.class como marcador especial).
     * @param nParameters     Número exacto de parámetros esperados.
     * 
     * @throws IllegalArgumentException Si el número de parámetros no coincide,
     *                                  o si los tipos no son los esperados.
     */
    private void validateArguments(Object[] objects, Class<?>[] expectedTypes, int nParameters) throws IllegalArgumentException {
    	if(objects.length != nParameters) {
    		throw new IllegalArgumentException("There must be " + nParameters + " objects");
    	}
    	
    	int j = 0;
    	for(int i = 0; i < nParameters; i++) {
    		if(expectedTypes[i] == ArrayList.class) {
    			j--;
    			this.checkList(objects[i], expectedTypes[j]);
    		} else {
    			this.checkField(objects[i], expectedTypes[i]);
    		}
    	}
    }
    
    /**
     * Valida que un objeto sea un ArrayList y que todos sus elementos sean del tipo esperado.
     * 
     * @param <T>           El tipo genérico de elementos esperados en la lista.
     * @param object        El objeto a validar (debe ser un ArrayList).
     * @param expectedType  La clase de los elementos esperados en el ArrayList.
     * 
     * @return Un nuevo {@code ArrayList<T>} con los elementos validados y convertidos.
     * @throws IllegalArgumentException Si el objeto no es un ArrayList,
     *                                  o si algún elemento no es del tipo esperado.
     */
    private <T> ArrayList<T> checkList(Object object, Class<T> expectedType) throws IllegalArgumentException {
    	if(object == null || expectedType == null) {
    		throw new IllegalArgumentException("Arguments cannot be null");
    	}
    	
    	try {    		
    		this.checkField(object, ArrayList.class);
    		ArrayList<T> newList = new ArrayList<>();
    		
    		@SuppressWarnings("unchecked")
    		ArrayList<?> rawList = (ArrayList<?>)object;
    		for(Object o: rawList) {
    			this.checkField(o, expectedType);
    			newList.add(expectedType.cast(o));
    		}
    		return newList;
    		
    	} catch (Exception e) {
    		System.err.println("Error validating list: " + e.getMessage());
    	}
    	return null;
    }
    
    /**
     * Valida que un objeto sea una instancia de una clase esperada.
     * 
     * @param object       El objeto a validar.
     * @param expectedType La clase esperada.
     * 
     * @throws IllegalArgumentException Si el objeto es nulo o no es del tipo esperado.
     */
    private void checkField(Object object, Class<?> expectedType) throws IllegalArgumentException {
    	if(object == null) {
    		throw new IllegalArgumentException("Object cannot be null");
    	}
    	
    	if(!expectedType.isInstance(object)) {
    		throw new IllegalArgumentException("Object must be of " + expectedType + " class");
    	}
    }
}
