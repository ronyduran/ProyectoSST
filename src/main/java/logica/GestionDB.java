package logica;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class GestionDB<T> {
    private static EntityManagerFactory entityManagerFactory;
    private Class<T> claseEntidad;

    public GestionDB(Class<T> clase) {
        if(entityManagerFactory == null) {

                entityManagerFactory = Persistence.createEntityManagerFactory("WebSST");

        }
        this.claseEntidad = claseEntidad;
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }


    /**
     * Permite obtener el valor del campo
     */
    private Object getValorCampo(T entidad) {
        Object valorCampo = null;
        if (entidad != null) {
            for (Field campo : entidad.getClass().getDeclaredFields()) {
                if (campo.isAnnotationPresent(Id.class)) {
                    try {
                        campo.setAccessible(true);
                        valorCampo = campo.get(entidad);
                        System.out.println("Nombre del campo: " + campo.getName());
                        System.out.println("Tipo del campo: " + campo.getType().getName());
                        System.out.println("Valor del campo: " + valorCampo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valorCampo;
    }

    /**
     * CRUD BASICO IMPLEMENTANDO ORM
     *
     * @param entidad
     * @return
     * @throws IllegalArgumentException
     * @throws EntityExistsException
     * @throws PersistenceException
     */
    public boolean crearObjeto(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException {
        boolean estado = false;
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entidad);
            entityManager.getTransaction().commit();
            estado = true;
            //System.out.println("SE  PUEDO CREAR LA ENTIDAD");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\n Hubo un error al intentar agregar una entidad----");
            System.err.println(e);

        } finally {
            entityManager.close();
        }
        return estado;
    }

    public boolean eliminarObjeto(Object entidadId) throws PersistenceException {
        boolean estado = false;
        T entidad;
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entidad = entityManager.find(this.claseEntidad, entidadId);
            entityManager.remove(entidad);
            entityManager.getTransaction().commit();
            estado = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            entityManager.close();
            System.out.println("Se cerro");
        }

        return estado;
    }

    public boolean editarCampo(T entidad) throws PersistenceException {
        boolean estado = false;
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
            estado = true;
        } finally {

            entityManager.close();
        }

        return estado;
    }

    public T buscar(Object id) throws PersistenceException {
        EntityManager entityManager = getEntityManager();
        T entidad = null;
        try {
            entidad = entityManager.find(this.claseEntidad, id);
            System.out.println("La busqueda ha sidoe existosa!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return entidad;
    }

    public List<T> buscarTodo() throws PersistenceException {
        EntityManager entityManager = getEntityManager();
        List<T> entida = null;
        try {
            CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(this.claseEntidad);
            criteriaQuery.select(criteriaQuery.from(this.claseEntidad));
            entida = entityManager.createQuery(criteriaQuery).getResultList();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return entida;
    }

}

