package format;

import java.util.List;

import users.*;

public class MenuBuilder {
	
	
	public static String welcomeMenu() {		
		return TextFormat.welcomeInitMenu(null, MenuOptions.GUEST);
	}
	
	public static String initMenu(User user) {
		List<String> menu = menuOptions(user);
		return TextFormat.initMenu(user, menu);
	}
	
	public static String invalidOption(User user) {
		return TextFormat.invalidOption(user, menuOptions(user).size());
	}
	
	public static String closingApp() {
		return TextFormat.closeAppMessage();
	}
	
	private static List<String> menuOptions(User user) {
		return  (user instanceof Client) ? MenuOptions.CLIENT :
				(user instanceof Employee) ? MenuOptions.EMPLOYEE :
				(user instanceof Manager) ? MenuOptions.MANAGER :
				MenuOptions.GUEST;
	}
}