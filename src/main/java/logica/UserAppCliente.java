package logica;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAppCliente implements Serializable {
    @Id
    private String idCliente;
    private String nombreCompleto;
    private String sexo;
    private Integer edad;
    private String correoElectronico;
    private String Password;
    private String username;
    private String idNFC;
   /* @OneToMany
    private List<Notificaciones> listNotificaciones;
*/
    public UserAppCliente() {

    }

    public UserAppCliente(String idCliente, String nombreCompleto, String sexo, Integer edad, String correoElectronico, String password, String username, String idNFC) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.sexo = sexo;
        this.edad = edad;
        this.correoElectronico = correoElectronico;
        this.Password = password;
        this.username = username;
        this.idNFC=idNFC;
        //this.listNotificaciones= new ArrayList<Notificaciones>();
    }


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdNFC() {
        return idNFC;
    }

    public void setIdNFC(String idNFC) {
        this.idNFC = idNFC;
    }

    /*public List<Notificaciones> getListNotificaciones() {
        return listNotificaciones;
    }

    public void setListNotificaciones(List<Notificaciones> listNotificaciones) {
        this.listNotificaciones = listNotificaciones;
    }*/


}
