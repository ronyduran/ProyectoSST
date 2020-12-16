package logica;

import Servicios.ServicioTunelClientes;
import Servicios.ServicioTunelLiquifo;
import com.google.gson.Gson;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;

import java.sql.Time;
import java.util.Calendar;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import logica.EventoTunelNivleLiquido;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {
    private static String modoConexion = "";
    public static List<Session> usuariosConectados = new ArrayList<>();
    public static List<EventoTunelClientes> listaEventosClientes = new ArrayList<>();


    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST/");
        } ).start(7000);

        if(modoConexion.isEmpty()) {
            ConexionDB.getInstance().InciarDB();
            System.out.println("Inicio");

        }
        /*for(int i=0;i<50;i++){
            ServicioTunelLiquifo.getIntacia().crearObjeto(new EventoTunelNivleLiquido("55",fecha()));
        }*/


        //JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        ModelWeb mode =new ModelWeb();


       // ManejoRemover mr = new ManejoRemover(mode);

       // mr.hilo.start();
        //-----------------------------
        //---------------------------------------------------------------------------------------
        //Prueba WebSockect-----------------------------------------------------------------------
        
        app.ws("/WSEnvio",ws->{
            ws.onConnect(ctx -> {
                System.out.println("Conexión Iniciada - " + ctx.getSessionId());
                usuariosConectados.add(ctx.session);
                enviarMensajeAClientesConectados("estado:"+"Encendido");
                enviarMensajeAClientesConectados("cont:"+contClientesdelActual());
                enviarMensajeAClientesConectados("nivel:"+ServicioTunelLiquifo.getIntacia().ultimoEvento());
                /*if(mode.getNotificaciones().size()==0){
                    enviarMensajeAClientesConectados("noficiaciones:En Espera de Notificaciones");
                }else{
                    for (String n:mode.getNotificaciones()) {
                        enviarMensajeAClientesConectados("noficiaciones:"+n);
                    }
                } */

                enviarMensajeAClientesConectados("conMasc:"+contMasc());
                enviarMensajeAClientesConectados("sinMasc:"+sintMasc());

                diasUserGraf();

            });
            ws.onMessage(ctx -> {

            });

            ws.onClose(ctx -> {
                System.out.println("Conexión Cerrada - "+ctx.getSessionId());
                usuariosConectados.remove(ctx.session);
            });
            ws.onError(ctx -> {
                System.out.println("Ocurrió un error en el WS");
            });

        });

        //---------------------------------------------------------------------------------------

        app.routes(() -> {
            path( "/", () -> {
                get("/",ctx -> {
                    ctx.redirect("/index.html");
                        });
                get("/tableEventosClientes",ctx -> {
                    List<EventoTunelClientes> list;
                    HashMap map = new HashMap();

                    list = ServicioTunelClientes.getInstancia().todos();
                    map.put("data", list);
                    Gson g = new Gson();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);

                    //ctx.json(map);

                });
                /*get("/",ctx -> {
                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("esta",mode.getEstado());
                    modelo.put("numero",mode.getContador());
                    modelo.put("taman",mode.getNivel());
                    modelo.put("notifi",mode.getNotificaciones());

                    ctx.render("/Web SST/index.html", modelo);});*/

                post("/noti",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.insertNoti(noti);
                    System.out.println(noti);
                    String mensaje="[Tiger Nixon,System Architect,Edinburgh,5421,2011/04/25,$3,120]";
                    enviarMensajeAClientesConectados("noti:"+mensaje);




                });
                post("/cliente",ctx -> {
                    String id=ctx.formParam("id",String.class).get();
                    String masc= ctx.formParam("masc",String.class).get();
                    String temp= ctx.formParam("temp",String.class).get();

                    System.out.println("Id:"+id+"--"+"Mascarilla:"+masc+"Temperatura:"+temp);
                    ServicioTunelClientes.getInstancia().crearObjeto(new EventoTunelClientes(id,masc,temp,fecha()));


                    enviarMensajeAClientesConectados("cont:"+contClientesdelActual());
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                    diasUserGraf();

                });
                post("/mascarilla",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.conMascarilla(noti);
                    mode.insertNoti(noti);
                    System.out.println(noti+"--"+"Cantidad:"+mode.getUserMascarillla().size());
                    enviarMensajeAClientesConectados("noficiaciones:"+mode.fecha()+"--"+noti);
                    enviarMensajeAClientesConectados("conMas:"+mode.getUserMascarillla().size());

                });post("/sinmascarilla",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.sinMascarilla(noti);
                    mode.insertNoti(noti);
                    System.out.println(noti+"--"+"Cantidad:"+mode.getUserSinMascarillla().size());
                    enviarMensajeAClientesConectados("noficiaciones:"+mode.fecha()+"--"+noti);
                    enviarMensajeAClientesConectados("sinMas:"+mode.getUserSinMascarillla().size());
                });
                post("/nivel",ctx -> {
                    String nivel= ctx.formParam("taman",String.class).get();
                    ServicioTunelLiquifo.getIntacia().crearObjeto(new EventoTunelNivleLiquido(nivel,fecha()));
                    //mode.setNivel(ctx.formParam("taman",Integer.class).get());
                    System.out.println(ctx.formParam("taman",String.class).get());
                    enviarMensajeAClientesConectados("nivel:"+nivel);
                });


                post("/estado",ctx -> {
                    mode.setEstado(ctx.formParam("esta",String.class).get());
                    System.out.println(ctx.formParam("esta",String.class).get());
                    enviarMensajeAClientesConectados("estado:"+mode.getEstado());
                });
                post("/contador",ctx -> {
                    mode.setContador(ctx.formParam("numero",String.class).get());
                    System.out.println(ctx.formParam("numero",String.class).get());
                    enviarMensajeAClientesConectados("cont:"+mode.getContador());
                });

            });
        });
    }
    public static void enviarMensajeAClientesConectados(String mensaje){
        for(Session sesionConectada : usuariosConectados){
            try {
                sesionConectada.getRemote().sendString(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   /* public Integer crearIdEventosTunelClientes(){
       int cantActEventoClientes= listaEventosClientes.size();
       int nuevoId=cantActEventoClientes++;
         return nuevoId;
    };

    public void crearEventoTunelClientes(@NotNull String idCliente, String estadoMascarilla, String temperatura){

        if(!idCliente.contentEquals("") && (estadoMascarilla.contentEquals("True") || estadoMascarilla.contentEquals("False")) && !temperatura.contentEquals("") ) {
            //EventoTunelClientes ec1 = new EventoTunelClientes(crearIdEventosTunelClientes(),idCliente,estadoMascarilla,temperatura,fecha());
           // listaEventosClientes.add(ec1);
        };

    };*/


    public static Date fecha(){
        Date fecha1 = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String fechaString = hourdateFormat.format(fecha1);
        Date fechaMod= new Date();
        try {
            fechaMod= hourdateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaMod;
    };

    public static Integer contClientesdelActual () {

        Date fecha1 = new Date();
        int cont=0;
        for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
            if(cambiarFormatoFecha(aux.getFecha()).compareTo(cambiarFormatoFecha(fecha1))==0){
               cont++;
            }
        }
        return cont;
    };

    public static Date cambiarFormatoFecha (Date f1){

        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaString = hourdateFormat.format(f1);

        try {
            f1= hourdateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return f1;
    };

   /* public String crearIdEventosNivel(){
        int cantActEventoNivel= listaEventosNivelLiquido.size();
        int nuevoId=cantActEventoNivel++;
        String nId="EvN"+nuevoId;
        return nId;
    };

    public void insetarEventoNivel (String nivel){

        if(!nivel.contentEquals("")){
            //EventoTunelNivleLiquido n1= new EventoTunelNivleLiquido(crearIdEventosNivel(),nivel,fecha());
            //listaEventosNivelLiquido.add(n1);
        }
    };*/
    public static String getModoConexion(){

        return modoConexion;
    }

    public static void setModoConexion(String modoConexion) {
        Main.modoConexion = modoConexion;
    }

    public static Integer contMasc(){
        Integer mas=0;
        for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
            if(aux.getEstadoMascarilla().equalsIgnoreCase("Si")){
                mas++;
            }
        }

        return mas;
    }

    public static Integer sintMasc(){
        Integer sinMas=0;
        for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
            if(aux.getEstadoMascarilla().equalsIgnoreCase("No")){
                sinMas++;
            }
        }

        return sinMas;
    }

    public static void diasUserGraf(){

        Integer lu=0,ma=0,mi=0,ju=0,vi=0,sa=0,dom=0;

        for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {

           if (fechaSemana(aux.getFecha())){
               if(aux.getFecha().getDay()==0){ dom++; }
               if(aux.getFecha().getDay()==1){ lu++; }
               if(aux.getFecha().getDay()==2){ ma++; }
               if(aux.getFecha().getDay()==3){ mi++; }
               if(aux.getFecha().getDay()==4){ ju++; }
               if(aux.getFecha().getDay()==5){ vi++; }
               if(aux.getFecha().getDay()==6){ sa++; }
           }

        }
        System.out.println(lu+"--"+ma+"--"+mi+"--"+ju+"--"+vi+"--"+sa+"--"+dom);
        enviarMensajeAClientesConectados("lu:"+lu);
        enviarMensajeAClientesConectados("ma:"+ma);
        enviarMensajeAClientesConectados("mi:"+mi);
        enviarMensajeAClientesConectados("ju:"+ju);
        enviarMensajeAClientesConectados("vi:"+vi);
        enviarMensajeAClientesConectados("sa:"+sa);
        enviarMensajeAClientesConectados("dom:"+dom);


    }

    public static boolean fechaSemana(Date d1){
        boolean out=false;
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal.getTime());
        cal2.setTime(d1);

        if(cal.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
            out=true;
            System.out.println(cal.getTime());
            System.out.println("Current week of year is : " +cal.get(Calendar.WEEK_OF_YEAR));
        }

        return out;
    }


}
