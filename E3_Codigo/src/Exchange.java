/**
 * Clase para representar los intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */

public class Exchange {
    public int exchangeíd;
    public Exchangeoffer validateOffer;
    public Employee validatedBy;
    /**
     * Constructor para el intercambio
     * @param exchangeid Id del intercambio
     * @param  offer la oferta de intercamcbio
     * */
    public Exchange(int exchangeid, Exchangeoffer offer){
        this.exchangeíd = exchangeid;
        this.validateOffer = offer;
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
        this.validatedBy = e;
        this.validateOffer.requestedProduct.owner= this.validateOffer.comprador;
        this.validateOffer.requestedProduct.isOffered= false;

        for (SecondHandProduct p: this.validateOffer.offeredProducts){
            p.owner = this.validateOffer.recibidor;
            p.isOffered = false;
        }
        this.validateOffer.Estado = ExchangeStatus.ACEPTADA;
        return true;
    }



}
