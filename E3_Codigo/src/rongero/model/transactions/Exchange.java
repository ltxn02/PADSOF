package model.transactions;
import model.user.Employee;
import util.Permission;
/**
 * Clase para representar los intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */
/**
 * Clase que representa la ejecución final de un intercambio de productos de segunda mano.
 * Actúa como el registro oficial del intercambio una vez que ha sido aceptado por las partes
 * y validado por un empleado autorizado.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class Exchange {
    public static int lastExchangeId = 1;
    public int exchangeId;
    public ExchangeOffer validateOffer;
    public Employee validatedBy;
    /**
     * Constructor para formalizar un intercambio a partir de una oferta aceptada.
     * Asigna un identificador único y vincula la oferta correspondiente.
     * * @param offer La oferta de intercambio (ExchangeOffer) que ha sido aceptada por el receptor.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public Exchange(ExchangeOffer offer){
        this.validateOffer = offer;
        this.exchangeId = Exchange.lastExchangeId;
        Exchange.lastExchangeId++;
    }
    /**
     * Proceso de validación administrativa del intercambio.
     * Verifica que el empleado tenga los permisos necesarios y que la oferta esté
     * efectivamente aceptada antes de proceder al cambio físico y legal de propietarios.
     * * @param e El empleado que realiza la validación técnica y de seguridad.
     * @return true si el intercambio se ha completado con éxito; false si el empleado
     * no tiene permisos o la oferta no era válida para intercambio.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public boolean validateExchange(Employee e){
        if (!e.permissions.contains(Permission.EXCH_VALIDATE)){
            return  false;
        }
        if (!validateOffer.ofertaAceptada()){
            return false;
        }
        this.validatedBy = e;
        this.validateOffer.ejecutarIntercambio();
        return true;
    }



}
