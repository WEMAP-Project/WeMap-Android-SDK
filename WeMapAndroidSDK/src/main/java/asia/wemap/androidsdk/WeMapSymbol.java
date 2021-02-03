package asia.wemap.androidsdk;

import com.mapbox.mapboxsdk.plugins.annotation.Symbol;

public class WeMapSymbol {
    private Symbol symbol;

    public WeMapSymbol(){
    }

    public WeMapSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    public Symbol getSymbol(){
        return symbol;
    }
}
