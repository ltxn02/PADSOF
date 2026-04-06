package model.transactions;
import java.time.*;
import model.discounts.*;
import model.catalog.NewProduct;
public class CartItem {
	private NewProduct product;
	private int quantity;
	
	public CartItem(NewProduct product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public double fullPrice() {
		IDiscount d = product.getDiscount();

		if (d instanceof ICantidad && !d.isExpired()) {
			return ((ICantidad) d).applyCantidad(product.getPrice(), this.quantity);
		}

		return product.getPriceWithDiscount() * this.quantity;	}
	
	public void orderQuantity(int quantity) throws IllegalArgumentException {
		if(this.product.isEffectiveStockHigher(quantity) == false) {
			throw new IllegalArgumentException("Invalid quantity, there's not enough stock");
		}
		this.product.orderProduct(quantity);
		this.quantity = quantity;
	}
	
	public void removeQuantity(int quantity) throws IllegalArgumentException {
		if(quantity < 0) {
			throw new IllegalArgumentException("Invalid quantity, must be positive");
		}
		
		boolean isAll = false;
		int aux = quantity;
		
		if(aux >= this.quantity) {
			aux = this.quantity;
			this.quantity = 0;
			isAll = true;
		} else {
			this.quantity -= aux;
		}
		this.product.returnProduct(aux, isAll);
	}
	
	public boolean isExpired(Duration time) {
		return this.product.isExpired(time);
	}
	
	public boolean isEmpty() {
		return this.quantity == 0;
	}
	
	public boolean isProduct(NewProduct p) {
		return this.product == p;
	}
	
	@Override
	public String toString() {
		return "[" + this.product + " x " + this.quantity + "]";
	}

	public NewProduct getProduct() {
		return this.product;
	}

	public int getQuantity() {
		return this.quantity;
	}

	// Dentro de CartItem.java o ShoppingCart
	public double calcularTotalLinea() {
		IDiscount desc = product.getDiscount();
		if (desc instanceof ICantidad && !desc.isExpired()) {
			return ((ICantidad) desc).applyCantidad(product.getPrice(), this.quantity);
		}
		return product.getPriceWithDiscount() * this.quantity;
	}

}