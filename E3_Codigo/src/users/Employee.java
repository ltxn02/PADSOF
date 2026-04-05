package users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    
    
    /* FALTA COMPROBAR QUE TIPO DE PRODUCTO ES ENTRE Game, Comic, Figurine Y Pack (NO SE PUEDE CREAR OBJETO DE TIPO NEWPRODUCT O PRODUCT)*/
    /*public void loadProduct(Catalog catalog, String name, String description, double price, String picturePath, int stock, ArrayList<Category> categories, ArrayList<Review> reviews) {
    	catalog.addProductOnSale(name, description, price, picturePath, stock, categories, reviews);
    }
    
    public void loadProduct(Catalog catalog, String name, String description, double price, String picturePath, int stock) {
    	this.loadProduct(catalog, name, description, price, picturePath, stock, new ArrayList<Category>(), new ArrayList<Review>());
    }*/
    
    // public void loadProduct(FILE)
    
    
    
    
    
    
    
    
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
