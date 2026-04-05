package users;

import java.util.*;
import java.io.*;
import utils.*;
import transactions.*;
import catalog.*;

public class Employee extends Staff {
    private boolean enabled;
    public ArrayList<EmployeeRoles> Rol;
    public ArrayList<Permission> permissions;

    public Employee(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
        this.enabled = enabled;
        this.permissions= new ArrayList<>();
        this.Rol = new ArrayList<>();
    }
    
    public void editProduct(NewProduct p, String name, String description, double price, String picturePath, int stock) throws SecurityException {
    	if(this.checkPermission(Permission.PRODUCT_EDIT) == false) {
    		throw new SecurityException("Employee doesn't have permission to edit products");
    	}
    	p.editProductInfo(name, description, price, picturePath, stock);
    }

    public void loadProduct(ItemType itemType, Map<String, Object> data) throws SecurityException {
        // 1. Comprobamos permisos usando el método de Taha
        if(this.checkPermission(Permission.PRODUCT_LOAD) == false) {
            throw new SecurityException("[!] Acceso denegado: No tienes permiso para cargar productos.");
        }

        try {
            // 2. Extraemos los datos comunes
            String name = (String) data.get("name");
            String desc = (String) data.get("description");
            double price = Double.parseDouble(data.get("price").toString());
            String pic = (String) data.get("picturePath");
            int stock = Integer.parseInt(data.get("stock").toString());

            NewProduct nuevoProducto = null;

            // --- SOLUCIÓN: Creamos una categoría por defecto para cumplir con la regla de NewProduct ---
            ArrayList<Category> defaultCategories = new ArrayList<>();
            defaultCategories.add(new Category(itemType.toString(), new ArrayList<>()));

            // 3. Construimos el producto según su tipo pasándole la defaultCategory
            switch(itemType) {
                case COMIC:
                    int nPages = Integer.parseInt(data.get("nPages").toString());
                    String publisher = (String) data.get("publisher");
                    int pubYear = Integer.parseInt(data.get("publicationYear").toString());

                    nuevoProducto = new Comic(name, desc, price, pic, stock, defaultCategories, new ArrayList<>(), null, nPages, publisher, pubYear, new ArrayList<>());
                    break;

                case GAME:
                    int nPlayers = Integer.parseInt(data.get("nPlayers").toString());
                    nuevoProducto = new Game(name, desc, price, pic, stock, defaultCategories, new ArrayList<>(), null, nPlayers, new ArrayList<>(), null);
                    break;

                case FIGURINE:
                    double height = Double.parseDouble(data.get("height").toString());
                    double width = Double.parseDouble(data.get("width").toString());
                    double depth = Double.parseDouble(data.get("depth").toString());
                    String material = (String) data.get("material");
                    String franchise = (String) data.get("franchise");

                    nuevoProducto = new Figurine(name, desc, price, pic, stock, defaultCategories, new ArrayList<>(), null, height, width, depth, material, franchise);
                    break;

                case PACK:
                    System.out.println("[!] Los packs se gestionan desde el menú de Gestor.");
                    return;
            }

            // 4. Lo añadimos al catálogo global
            if (nuevoProducto != null) {
                logic.Application.getCatalog().add(nuevoProducto);
            }

        } catch (Exception e) {
            System.out.println("[!] Error construyendo el producto. Revisa los datos: " + e.getMessage());
        }
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
                    this.loadProduct(type, data);

                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para mapear las columnas extras según el tipo de producto.
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

    public void activateEmployee(){
        this.enabled = true;
    }

    public void desactivateEmployee(){
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }


}
