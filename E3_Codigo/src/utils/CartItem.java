package utils;

import catalog.NewProduct;
import java.time.*;

public class CartItem implements java.io.Serializable{
	private NewProduct product;
	private int quantity;
	
	public CartItem(NewProduct product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public double fullPrice() {
		return this.quantity * this.product.getPrice();
	}
	
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
		return this.quantity + " x " + this.product.itemAuxPreview();
	}

	public NewProduct getProduct() {
		return this.product;
	}

	public int getQuantity() {
		return this.quantity;
	}
}