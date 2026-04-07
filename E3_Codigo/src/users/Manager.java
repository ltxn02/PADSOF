package users;
import utils.*;
import transactions.*;
import catalog.*;
import java.util.ArrayList;
import java.util.Date;

public class Manager extends Staff implements java.io.Serializable {

    public Manager (String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
    }

    public Employee createEmployeeAccount(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled) {
    	Employee account = new Employee(username, password, fullname, dni, birthdate, email, phoneNumber, salary, enabled);
    	return account;
    }

    public void changeEmployeeStatus(Employee employee, boolean enable) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	
    	if(enable) {
    		employee.activateEmployee();
    	} else {
    		employee.desactivateEmployee();
    	}
    }

    public void addPermissionTo(Employee employee, Permission p) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	employee.add_permisions(p);
    }
    
    public void removePermissionTo(Employee employee, Permission p) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	employee.delete_permisions(p);
    }
    
    public Category createCategory(String name) throws IllegalArgumentException {
    	if(name == null || name.isEmpty()) {
    		throw new IllegalArgumentException("Must receive a valid string");
    	}
    	Category category = new Category(name);
    	return category;
    }
    
    public void renameCategory(Category category, String name) throws IllegalArgumentException {
    	if(name == null || name.isEmpty()) {
    		throw new IllegalArgumentException("Must receive a valid string");
    	}
    	
    	if(category == null) {
    		throw new IllegalArgumentException("Must receive a valid category");
    	}
    	category.rename(name);
    }
    
    public void addItemToCategory(Category category, Item item) {
    	try {
    		category.addItem(item);
    	} catch (Exception e) {
    		System.err.println("Error adding item: " + e.getMessage());
    	}
    }
    
    public void changeVisibilityCategory(Category category, boolean visible) throws IllegalArgumentException {
    	if(category == null) {
    		throw new IllegalArgumentException("Must receive a valid category");
    	}
    	category.markAs(visible);
    }
    
    public Pack createPack(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, ArrayList<NewProduct> initialProducts) {
    	Pack pack = new Pack(name, description, price, image, stock, categories, reviews, initialProducts);
    	return pack;
    }
    
    public void editPack() {
    	
    }
}
