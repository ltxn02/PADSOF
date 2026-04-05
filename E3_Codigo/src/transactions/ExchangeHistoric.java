package transactions;
import utils.*;
import users.*;
import catalog.*;

import java.util.ArrayList;

public class ExchangeHistoric {
    private ArrayList<Exchange> exchanges;

    public ExchangeHistoric() {
        this.exchanges = new ArrayList<>();
    }

    public ExchangeHistoric(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public ArrayList<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public void addExchange(Exchange exchange) {
        this.exchanges.add(exchange);
    }

    public void removeExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
    }
    
    public boolean hasExchange(Exchange exchange) {
    	return this.exchanges.contains(exchange);
    }
}
