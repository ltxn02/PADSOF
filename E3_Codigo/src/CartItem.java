

public class CartItem {
	private NewProduct product;
	private int quantity;
	
	public CartItem(NewProduct product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public double fullPrice() {
		return this.quantity * this.product.getPrice();
	}
	
	public void addQuantity(int quantity) throws IllegalArgumentException {
		if(this.product.isEffectiveStockHigher(quantity) == false) {
			throw new IllegalArgumentException("Invalid quantity, there's not enough stock");
		}
		this.product.orderProduct(quantity);
	}
	
	@Override
	public String toString() {
		return "[" + this.product + " x " + this.quantity + "]";
	}
}