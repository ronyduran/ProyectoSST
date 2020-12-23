package Servicios;

import logica.EventoTunelClientes;
import logica.GestionDB;
import logica.UserWeb;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosUserWeb extends GestionDB<UserWeb> {

    public ServiciosUserWeb() {
        super(UserWeb.class);
    }

    public static ServiciosUserWeb inst;


    public static ServiciosUserWeb getInstancia(){
        if(inst==null){
            inst=new ServiciosUserWeb();
        }
        return inst;
    };

    public List<UserWeb> todos(){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM UserWeb", UserWeb.class);

        return query.getResultList();
    }

    public List<UserWeb> buscarPorID(String id){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM UserWeb WHERE UserWeb.IdUserWeb like id", UserWeb.class);
        query.setParameter("id",id);
        //query.setParameter("id", id);
        return query.getResultList();
    }
}
