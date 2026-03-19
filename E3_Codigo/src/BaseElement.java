import java.time.*;

public abstract class BaseElement {
	private boolean active;
	private Instant createdAt;
	private Instant updatedAt;
	
	public BaseElement() {
		this.active = true;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}
	
	public void markAs(boolean active/*, RegisteredUser by*/) {
		this.active = active;
		this.updatedAt = Instant.now();
	}
	
	@Override
	public String toString() {
		String str = "Status: ";
		
		if (this.active == true) {
			str += "active. ";
		} else {
			str += "inactive. ";
		}

		return str + "Created at: " + this.createdAt + ". Last updated: " + this.updatedAt;
	}
}