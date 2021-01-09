package Servicios;

import logica.GestionDB;
import logica.Notificaciones;
import logica.UserAppCliente;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosNotificaciones  extends GestionDB<Notificaciones> {


    public ServiciosNotificaciones() {
        super(Notificaciones.class);
    }

    public static ServiciosNotificaciones inst;


    public static ServiciosNotificaciones getInstancia(){
        if(inst==null){
            inst=new ServiciosNotificaciones();
        }
        return inst;
    };

    public List<Notificaciones> todos(){

        EntityManager entityManager = getEntityManager();

        Query query = entityManager.createNativeQuery("SELECT * FROM Notificaciones", Notificaciones.class);

        return query.getResultList();
    }

}
