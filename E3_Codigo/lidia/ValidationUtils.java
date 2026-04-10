package utils;

import java.util.ArrayList;

public class ValidationUtils {
	private ValidationUtils() {}
	
	public static void validateArguments(Object[] objects, Class<?>[] expectedTypes, int nParameters) throws IllegalArgumentException {
    	if(objects.length != nParameters) {
    		throw new IllegalArgumentException("There must be " + nParameters + " objects");
    	}
    	
    	int j = 0;
    	for(int i = 0; i < nParameters; i++) {
    		if(expectedTypes[i] == ArrayList.class) {
    			j--;
    			ValidationUtils.checkList(objects[i], expectedTypes[j]);
    		} else {
    			ValidationUtils.checkField(objects[i], expectedTypes[i]);
    		}
    	}
	}
	
	private static <T> void checkList(Object object, Class<T> expectedType) throws IllegalArgumentException {
    	if(object == null || expectedType == null) {
    		throw new IllegalArgumentException("Arguments cannot be null");
    	}
    	
    	try {    		
    		ValidationUtils.checkField(object, ArrayList.class);
    		ArrayList<T> newList = new ArrayList<>();
    		
    		@SuppressWarnings("unchecked")
    		ArrayList<?> rawList = (ArrayList<?>)object;
    		for(Object o: rawList) {
    			ValidationUtils.checkField(o, expectedType);
    			newList.add(expectedType.cast(o));
    		}
    		
    	} catch (Exception e) {
    		System.err.println("Error validating list: " + e.getMessage());
    	}
    }
	
	private static void checkField(Object object, Class<?> expectedType) throws IllegalArgumentException {
    	if(object == null) {
    		throw new IllegalArgumentException("Object cannot be null");
    	}
    	
    	if(!expectedType.isInstance(object)) {
    		throw new IllegalArgumentException("Object must be of " + expectedType + " class");
    	}
    }
}
