package Servicios;

import logica.GestionDB;
import logica.UserAppCliente;
import logica.UserWeb;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosAppCliente extends GestionDB<UserAppCliente> {

    public ServiciosAppCliente() {
        super(UserAppCliente.class);
    };

    public static ServiciosAppCliente inst;


    public static ServiciosAppCliente getInstancia(){
        if(inst==null){
            inst=new ServiciosAppCliente();
        }
        return inst;
    };

    public List<UserAppCliente> todos(){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM UserAppCliente", UserAppCliente.class);

        return query.getResultList();
    }

    public UserAppCliente buscarPorID(String idUser){

        EntityManager entityManager = getEntityManager();

        Query query = entityManager.createNativeQuery("SELECT * FROM UserAppCliente WHERE UserAppCliente.idCliente like :id", UserAppCliente.class);
        //query.setParameter("id",idUser);
        //query.setParameter("id", id);
        return (UserAppCliente) query.setParameter("id",idUser).getSingleResult();
    }
}
