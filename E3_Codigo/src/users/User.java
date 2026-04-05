package users;
import utils.*;
import transactions.*;
import catalog.*;
import java.util.*;

public abstract class User extends BaseElement {
	/** 
	 * La idea de las funciones de catalog es que el usuario pueda visualizar el catalogo, haciendo busquedas, filtrando
	 * o lo que sea, asi que realmente no sé si tendrá que devolver Catalog, Product[*], nada y simplemente mostrar por
	 * pantalla, etc. Who knows?
	 */
	
	public List<NewProduct> view_catalog(User currentUser, Catalog c) {
		if(currentUser instanceof Employee || currentUser instanceof Manager) {
			return c.visibleProducts();
		}
		return c.allProducts();
	}
	// public List<NewProduct> view_catalog(c: Catalog) {}
	
	public List<NewProduct> search_catalog(String s, Catalog c) {
		return c.searchProducts(s);
	}
	// public List<NewProduct> search_catalog(s: String, c: Catalog) {}
	
	public List<Game> filter_games_by_age(int min, int max, Catalog c) {
		return c.filterByAge(min, max);
	}
	// public List<NewProduct> filter_catalog(c: Catalog) {}
} 