package Servicios;

import logica.GestionDB;
import logica.UserAppCliente;

public class ServiciosAppCliente extends GestionDB<UserAppCliente> {

    public ServiciosAppCliente(Class<UserAppCliente> clase) {
        super(clase);
    }
}
