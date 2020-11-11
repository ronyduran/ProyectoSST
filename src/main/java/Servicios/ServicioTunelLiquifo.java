package Servicios;

import logica.EventoTunelNivleLiquido;
import logica.GestionDB;

public class ServicioTunelLiquifo  extends GestionDB<EventoTunelNivleLiquido> {

    private static ServicioTunelLiquifo inst;
    public ServicioTunelLiquifo() {
        super(EventoTunelNivleLiquido.class);
    }

    public static ServicioTunelLiquifo getIntacia(){
        if(inst==null){
            inst=new ServicioTunelLiquifo();
        }
        return inst;
    };
}
