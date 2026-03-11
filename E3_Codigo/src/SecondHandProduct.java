import java.util.List;
/**
 * Clase para representar los productos de segunda mano
 * @author Taha Ridda
 * @version 1.0
 *
 */
public class SecondHandProduct {
	private int secondHandId;
	private boolean isAppraised;
	private boolean isOffered;
	//private Client Owner;
	private ItemType Itemtype;
	private Condition condition;
	
	public SecondHandProduct(int secondHandId, boolean isAppraised, boolean isOffered, ItemType ItemType, Condition condition /**Client Owner*/) {

		this.secondHandId = secondHandId;
		this.isAppraised = isAppraised;
		this.isOffered = isOffered;
		this.Itemtype = ItemType;
		this.condition = condition;
		//this.Owner = Owner;

	}
	
	
	
}
