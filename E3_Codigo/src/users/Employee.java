package users;

import java.util.*;
import java.io.*;
import utils.*;
import transactions.*;
import catalog.*;
import discounts.*;

public class Employee extends Staff implements java.io.Serializable{
    private boolean enabled;
    public ArrayList<EmployeeRoles> Rol;
    public ArrayList<Permission> permissions;

    public Employee(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
        this.enabled = enabled;
        this.permissions= new ArrayList<>();
        this.Rol = new ArrayList<>();
    }
    
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

    public void loadProduct(Catalog catalog, ItemType itemType, Map<String, Object> data) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_LOAD) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to load products");
    	}
    	catalog.addProductOnSale(itemType, data);
    }
    
    public void loadProductBulk(String filePath, Catalog catalog) throws SecurityException {
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
    
    public void changeVisibilityProduct(NewProduct product, boolean visible) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_EDIT) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to edit products");
    	}
    	product.changeVisibilityProduct(visible);
    }
    
    public void appraiseSecondHandProduct(Client client, SecondHandProduct product, Condition c, double valuedOn) throws SecurityException, IllegalArgumentException {
    	if(this.checkPermission(Permission.EXCH_PRODUCT_APPRAISE) == false) {
    		throw new SecurityException("Employee " + this.getUsername() + " doesn't have permission to appraise products from a client's wallet");
    	}
    	
    	if(client.hasSecondHandProduct(product) == false) {
    		throw new IllegalArgumentException("Invalid product, it doesn't exist in the client's wallet");
    	}
    	
    	product.appraiseSecondHand(this, c, valuedOn);
    }
    
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
     * Metodo auxiliar para mapear las columnas extras según el tipo de producto.
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
    
    private boolean checkPermission(Permission p) {
    	return this.permissions.contains(p);
    }

    public void add_permisions(Permission e){
        this.permissions.add(e);
    }

    public void delete_permisions(Permission e){
        if (this.permissions.contains(e)) {
            this.permissions.remove(e);
        }
    }

    public ArrayList<Permission> permisosEmpleado(){
        return this.permissions;
    }

    public void add_roles(EmployeeRoles Rol){
        this.Rol.add(Rol);
    }

    public void delete_roles(EmployeeRoles Rol){
        this.Rol.remove(Rol);
    }
    
    public void activateEmployee() {
        this.enabled = true;
    }

    public void desactivateEmployee() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) throws SecurityException {
        // 1. Verificamos que el empleado tiene el permiso específico
        if(this.checkPermission(Permission.ORDER_STATUS_UPDATE) == false) {
            throw new SecurityException("[!] Acceso denegado: No tienes el permiso ORDER_STATUS_UPDATE para cambiar el estado de los pedidos.");
        }

        // 2. Si lo tiene, actualizamos el estado del pedido
        order.setOrderStatus(newStatus);
    }
    
    
    
    // -- HELPERS ------------------------------------------------------------------
    
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
    
    private void checkField(Object object, Class<?> expectedType) throws IllegalArgumentException {
    	if(object == null) {
    		throw new IllegalArgumentException("Object cannot be null");
    	}
    	
    	if(!expectedType.isInstance(object)) {
    		throw new IllegalArgumentException("Object must be of " + expectedType + " class");
    	}
    }
}
