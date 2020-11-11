package logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
public class EventoTunelNivleLiquido implements Serializable  {
    @Id
    @GeneratedValue
    private Integer idEventoNivelLiquido;
    private String nivel;
    private Date fecha;

    public EventoTunelNivleLiquido() {
    }

    public EventoTunelNivleLiquido( String nivel, Date fecha) {
        this.nivel = nivel;
        this.fecha = fecha;
    }


    public Integer getIdEventoNivelLiquido() {
        return idEventoNivelLiquido;
    }

    public void setIdEventoNivelLiquido(Integer idEventoNivelLiquido) {
        this.idEventoNivelLiquido = idEventoNivelLiquido;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
