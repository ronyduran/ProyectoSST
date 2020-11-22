package Servicios;

import logica.EventoTunelNivleLiquido;
import logica.GestionDB;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ServicioTunelLiquifo  extends GestionDB<EventoTunelNivleLiquido> {

    private static ServicioTunelLiquifo inst;
    public static List<EventoTunelNivleLiquido> listaEventosNivelLiquido = new ArrayList<>();

    public ServicioTunelLiquifo() {
        super(EventoTunelNivleLiquido.class);
    }

    public static ServicioTunelLiquifo getIntacia(){
        if(inst==null){
            inst=new ServicioTunelLiquifo();
        }
        return inst;
    };

    public List<EventoTunelNivleLiquido> todos(){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM EventoTunelNivleLiquido", EventoTunelNivleLiquido.class);

        return query.getResultList();
    }

    public String ultimoEvento(){
        String ulti="0";
        listaEventosNivelLiquido = todos();
        if(listaEventosNivelLiquido.size()>0){
            ulti= listaEventosNivelLiquido.get(listaEventosNivelLiquido.size()-1).getNivel();
        };

        return ulti;
    }
}
