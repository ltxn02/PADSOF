package utils;

public class AgeRange extends BaseElement {
	private String label;
    private int minAge;
    private int maxAge;

    public AgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        this.label = minAge + "-" + maxAge;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    
    public boolean equalTo(int min, int max) {
    	return this.minAge == min && this.maxAge == max;
    }
    
    public boolean equalTo(AgeRange ageRange) {
    	return this.minAge == ageRange.minAge && this.maxAge == ageRange.maxAge;
    }
    
    public boolean containedIn(int min, int max) {
    	return this.minAge >= min && this.maxAge <= max;
    }
    
    public boolean containedIn(AgeRange ageRange) {
    	return this.minAge >= ageRange.minAge  && this.maxAge <= ageRange.maxAge;
    }
    
    public void changeVisibility(boolean visible) {
    	super.markAs(visible);
    }
}
