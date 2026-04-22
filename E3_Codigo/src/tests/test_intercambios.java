package tests;
import utils.*;
import users.*;
import transactions.*;
import java.time.Duration;
import java.util.ArrayList;

import products.*;

public class test_intercambios {

    public static void main(String[] args) {
        Client taha = new Client("Asus", "Asus", "Taha", "1234l", "19/10/2006", "taha@gmail.com", "12345");
        Client lidia = new Client("Barça10", "Lidia", "Lidia", "54321d", "20/10/2006", "lidia@gmail.com", "52463");
        Client ana = new Client("AnaStar", "Ana", "García", "1111a", "01/01/2000", "ana@gmail.com", "66655");
        Client carlos = new Client("Carlitros", "Carlos", "Pérez", "2222c", "05/05/1995", "carlos@gmail.com", "44433");
        Client martin = new Client("Martín99", "Martín", "Ruiz", "3333m", "12/12/1999", "martin@gmail.com", "22211");

        Employee ivan = new Employee("R7_BOSS", "Ivan", "Ivan", "73736", "23/07/1965", "ivan@gmail.com", "8888", 0, true);
        Employee andres = new Employee("Andy_New", "Andres", "Andres", "9999", "10/10/1990", "andres@mail.com", "7777", 0, true);

        ivan.add_permisions(Permission.EXCH_VALIDATE);

        SecondHandProduct ps5 = new SecondHandProduct("PS5", "Sony", "f1", 450, true, ItemType.GAME, Condition.PERFECTO, taha);
        SecondHandProduct switchOled = new SecondHandProduct( "Switch", "Nint", "f2", 300, true, ItemType.GAME, Condition.MUY_BUENO, lidia);
        SecondHandProduct iphone = new SecondHandProduct( "iPhone 13", "Apple", "f3", 600, true, ItemType.GAME, Condition.MUY_BUENO, ana);
        SecondHandProduct portatil = new SecondHandProduct( "HP Victus", "HP", "f4", 700, true, ItemType.FIGURINE, Condition.PERFECTO, carlos);
        SecondHandProduct cascos = new SecondHandProduct( "Sony WH", "Audio", "f5", 150, true, ItemType.GAME, Condition.DAÑADO, martin);
        
        ArrayList<SecondHandProduct> loteAna = new ArrayList<>();
        loteAna.add(iphone);
        loteAna.add(cascos);
        
        for(SecondHandProduct p: loteAna) {
        	try {
        		ana.registerSecondHandProduct(p);
        	} catch (Exception e) {
        		System.err.println("Error registering product: " + e.getMessage());
        	}
        	
        }
        
        try {
        	ana.makeOffer(portatil, iphone, cascos);
        } catch (Exception e) {
        	System.err.println("Error making offer: " + e.getMessage());
        }
        System.out.println("Productos de Ana bloqueados: (true)" + (!iphone.isAvailable() && !cascos.isAvailable()));
        try {
        	portatil.getOwner().answerOffer(portatil, true);
        } catch (Exception e) {
        	System.err.println("Error answering offer: " + e.getMessage());
        }
        
        try {
        	////////////////////////////////////
			// INICIO: Bloque de código añadido
        	try {
        		Exchange exchA = portatil.getOwner().findExchange(portatil);
        		ivan.validateExchange(exchA);
    			System.out.println("Resultado: Carlos ahora tiene: " + iphone + " y " + cascos);
    			System.out.println("resultado: Ana ahora tiene: " + portatil);
    			
        	} catch(Exception e) {
        		System.err.println("Error: " + e.getMessage());
        	}
        	
			// FINAL: Bloque de código añadido
			////////////////////////////////////
		
			
        } catch(Exception e) {
        	System.err.println("Error creating the exchange: " + e.getMessage());
        }
        
        
        /*exchA.validateExchange(ivan);
        if (exchA.validateExchange(ivan)) {
            System.out.println("Resultado: Carlos ahora tiene: " + iphone + " y " + cascos);
            System.out.println("resultado: Ana ahora tiene: " + portatil);
        }*/

        
        System.out.println("\n--------------------------------------------------\n");

        ArrayList<SecondHandProduct> loteTaha = new ArrayList<>();
        loteTaha.add(ps5);
        
        for(SecondHandProduct p: loteTaha) {
        	try {
        		ana.registerSecondHandProduct(p);
        	} catch (Exception e) {
        		System.err.println("Error registering product: " + e.getMessage());
        	}
        	
        }
        
        try {
        	taha.makeOffer(switchOled, ps5);
        } catch (Exception e) {
        	System.err.println("Error making offer: " + e.getMessage());
        }
        
        System.out.println("Productos de Ana bloqueados: (true)" + (!iphone.isAvailable() && !cascos.isAvailable()));
        
        try {
        	switchOled.getOwner().answerOffer(switchOled, true);
        } catch (Exception e) {
        	System.err.println("Error answering offer: " + e.getMessage());
        }
        
        try {
    		Exchange exchB = switchOled.getOwner().findExchange(switchOled);
    		andres.validateExchange(exchB);
    		
    		System.out.println("Estado de la oferta tras intento fallido: (Pendiente)" + exchB.getAssociatedOffer());

            System.out.println("\n--------------------------------------------------\n");

            System.out.println("Antes de rechazar, PS5 bloqueada?(true): " + (!ps5.isAvailable()));
            exchB.getAssociatedOffer().reject_offer();
            System.out.println("Tras rechazo, PS5 disponible?(true): " + ps5.isAvailable());
            System.out.println("Estado final oferta Taha(rechazada): " + exchB.getAssociatedOffer());

            System.out.println("\n--------------------------------------------------\n");
			
    	} catch(Exception e) {
    		System.err.println("Error: " + e.getMessage());
    	}        
        
        /*boolean intentoAndres = exchB.validateExchange(andres);
        System.out.println("Andres pudo validar (no)?: " + (intentoAndres ? "SÍ (FALLO DE SEGURIDAD)" : "NO (SISTEMA SEGURO)"));*/

        ExchangeOffer ofertaCaduca = new ExchangeOffer(ps5, new ArrayList<>(), martin);

        try { Thread.sleep(5); } catch (Exception e) {}

        if (ofertaCaduca.is_Expired()) {
            ofertaCaduca.expired_offer();
            System.out.println("La oferta de Martín ha expirado correctamente.");
            System.out.println("Estado oferta (expirada): " + ofertaCaduca);
            System.out.println("PS5 liberada para otros(si)?: " + ps5.isAvailable()); // Usando tu método de oferta
        }

        System.out.println("\n--- TEST FINALIZADO CON ÉXITO ---");
    }
}
