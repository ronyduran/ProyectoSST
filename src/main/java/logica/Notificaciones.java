package logica;

import java.util.Date;

public class Notificaciones {


    private String idNoti;
    private String notificacion;
    private Date fecha;


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
}
