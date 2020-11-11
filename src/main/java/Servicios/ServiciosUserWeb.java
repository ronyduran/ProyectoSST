package Servicios;

import logica.GestionDB;
import logica.UserWeb;

public class ServiciosUserWeb extends GestionDB<UserWeb> {

    public ServiciosUserWeb(Class<UserWeb> clase) {
        super(clase);
    }
}
