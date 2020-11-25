package logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class EventoTunelClientes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEventoCTunelCliente;
    private String idCliente;
    private String estadoMascarilla;
    private String temperatura;
    private Date fecha;

    //El contructor debe de estar vacio
    public EventoTunelClientes(){
    }

    public EventoTunelClientes( String idCliente, String estadoMascarilla, String temperatura, Date fecha) {
        this.idCliente = idCliente;
        this.estadoMascarilla = estadoMascarilla;
        this.temperatura = temperatura;
        this.fecha = fecha;
    }


    public Integer getIdEventoCTunelCliente() {
        return idEventoCTunelCliente;
    }

    public void setIdEventoCTunelCliente(Integer idEventoCTunelCliente) {
        this.idEventoCTunelCliente = idEventoCTunelCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstadoMascarilla() {
        return estadoMascarilla;
    }

    public void setEstadoMascarilla(String estadoMascarilla) {
        this.estadoMascarilla = estadoMascarilla;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
