package logica;
import javax.persistence.*;

@Entity
public class UserWeb {
    @Id
    private String IdUserWeb;
    private String userName;
    private String correoElectronico;
    private String password;
    private String rol;
    private String nombre;
    private String apellido;

    public UserWeb() {

    }

    public UserWeb(String idUserWeb, String userName, String correoElectronico, String password, String rol, String nombre, String apellido) {
        IdUserWeb = idUserWeb;
        this.userName = userName;
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
    }


    public String getIdUserWeb() {
        return IdUserWeb;
    }

    public void setIdUserWeb(String idUserWeb) {
        IdUserWeb = idUserWeb;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
