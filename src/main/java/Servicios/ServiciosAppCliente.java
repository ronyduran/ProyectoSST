package Servicios;

import logica.GestionDB;
import logica.UserAppCliente;
import logica.UserWeb;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
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


    public void EliminarPorID(String idUser){
        try {

            EntityManager entityManager = getEntityManager();
            Query query = entityManager.createNativeQuery("DELETE FROM UserAppCliente WHERE UserAppCliente.idCliente like :id", UserAppCliente.class);
            query.setParameter("id",idUser);
            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        }catch (Exception e){
            System.out.println(e);
            System.out.println("No se puede borrar");

        }
    }

    /*public UserAppCliente buscarPorID(String idUser){

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM UserAppCliente WHERE UserAppCliente.idCliente like :id", UserAppCliente.class);

        //query.setParameter("id",idUser);
        //query.setParameter("id", id);
        return  (UserAppCliente) query.setParameter("id",idUser).getSingleResult();
    }*/

    public UserAppCliente buscarPorID(String idUser){

        EntityManager entityManager = getEntityManager();
        UserAppCliente user = entityManager.find(UserAppCliente.class, idUser);
        return user;
    }


}
