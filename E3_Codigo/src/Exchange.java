/**
 * Clase para representar los intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */

public class Exchange {
    public static int exchangeíd = 1;
    public Exchangeoffer validateOffer;
    public Employee validatedBy;
    /**
     * Constructor para el intercambio
     * @param  offer la oferta de intercamcbio
     * */
    public Exchange( Exchangeoffer offer){
        this.exchangeíd++;
        this.validateOffer = offer;
        offer.getComprador().registrarOfertaRealizada(this);
        offer.getRecibidor().registrarOfertaRecibida(this);

    }
    /**
     * Funcion para que el empleado valide un intercambio ofertado, ya aceptado
     * @param e El empleado que hace la validación del intercambio
     * @return retorna true si se valida el intercamio y false si no se intercambia
     * */
    public boolean validateExchange(Employee e){
        if (!e.permissions.contains(Permission.EXCH_VALIDATE)){
            return  false;
        }
        if (!validateOffer.ofertaaceptada()){
            return false;
        }
        this.validatedBy = e;
        this.validateOffer.intercambiar_propietarios();
        return true;
    }



}
