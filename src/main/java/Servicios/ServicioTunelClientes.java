package Servicios;

import logica.EventoTunelClientes;
import logica.GestionDB;

public class ServicioTunelClientes extends GestionDB<EventoTunelClientes> {
    public ServicioTunelClientes(Class<EventoTunelClientes> clase) {
        super(clase);
    }
}
