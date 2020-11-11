package logica;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {
    private String URL = "jdbc:h2:tcp://localhost/~/WebSST";
    private static ConexionDB conexionDB;
    private static Server tcp;
    private static Server webServer;
    /**
     * Implementado patron Singleton
     */
    public ConexionDB() throws SQLException {
        registroDriver();
        //InciarBD();
    }

    public static ConexionDB getInstance() throws SQLException {
        if(conexionDB==null){
            conexionDB = new ConexionDB();
        }
        return conexionDB;
    }

    public static void InciarDB() throws SQLException {
        //subiendo en modo servidor H2
        tcp = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon", "-ifNotExists").start();//sube eL modo servidor H2
        // webServer = Server.createWebServer("-webPort", "9090", "-webAllowOthers", "-webDaemon").start();
        //Abriendo el cliente web. El valor 0 representa puerto aleatorio.
        String status = Server.createWebServer( "-webPort", "9091", "-webAllowOthers", "-webDaemon").start().getStatus();
        System.out.println("Status Web: "+status);

    }
    public static void detenerDB() throws SQLException {
        // Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        tcp.stop();
    }
    /**
     * Registrar Driver. 1er paso para hacer una conectividad JDBS
     */
    private void registroDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**
         * No es necesario hacerlo En los Driver modernos porque se registran automatico.
         * pero se hace con fines educativos
         */
    }


    /**
     * Abrir Objeto de conexion. 2do paso.
     * URL = protocolo:subprotocolo://servidor:puerto/subnombre
     */
    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
            //Por defecto se le pasa password vacio, pero claramente se puede cambiar

        } catch (SQLException ex) {

        }
        return con;
    }


    /**
     * Abrir objeto Statement para poder trabjar. 3er paso
     */
    /**
     * Hacer la consulta de los elementos deseado. 4to paso
     */
    /**
     * Cerrar conexion. 5to paso.
     */

}
