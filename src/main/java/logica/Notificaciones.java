package logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class Notificaciones implements Comparable< Notificaciones >, Serializable {

    @Id
    @Column(name = "IdNotificaciones")
    private String idNoti;
    @Column(name = "Mensaje")
    private String notificacion;
    @Column(name = "Fecha")
    private Date fecha;

    public Notificaciones(){

    }

    public Notificaciones (String idNoti,String notificacion,Date fecha) {
        this.idNoti=idNoti;
        this.notificacion = notificacion;
        this.fecha=fecha;


    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificaciones(String notificacion) {
        notificacion = notificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdNoti() {
        return idNoti;
    }

    public void setIdNoti(String idNoti) {
        this.idNoti = idNoti;
    }

    public int compareTo(Notificaciones n1) {
        return this.getIdNoti().compareTo(n1.getIdNoti());
    }
}
