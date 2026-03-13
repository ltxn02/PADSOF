/**
 * Enum para representar los roles de los empleados
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum EmployeeRoles {
	ORDERS_EMPLOYEE,
	EXCHANGES_EMPLOYEE,
	PRODUCTS_EMPLOYEE;
	
	public String toString() {
		return this.name();
	}
}
