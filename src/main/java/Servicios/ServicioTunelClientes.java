package Servicios;

import logica.EventoTunelClientes;
import logica.GestionDB;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


public class ServicioTunelClientes extends GestionDB<EventoTunelClientes> {
    public ServicioTunelClientes() {
        super(EventoTunelClientes.class);
    }

    public static ServicioTunelClientes inst;


    public static ServicioTunelClientes getInstancia(){
        if(inst==null){
            inst=new ServicioTunelClientes();
        }
        return inst;
    };

    public List<EventoTunelClientes> todos(){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM EventoTunelClientes", EventoTunelClientes.class);

        return query.getResultList();
    }
}
