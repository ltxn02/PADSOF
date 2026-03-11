
public enum Permission {
	PRODUCT_LOAD,
	PRODUCT_EDIT,
	PRODUCT_SOFT_DELETE,
	ORDER_STATUS_UPDATE,
	EXCH_PRODUCT_APPRAISE,
	EXCH_VALIDATE;
	
	public String toString() {
		return this.name();
	}
}
