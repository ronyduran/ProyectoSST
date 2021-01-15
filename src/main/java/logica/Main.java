package logica;

import Servicios.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Calendar;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {


    private static String modoConexion = "";
    public static List<Session> usuariosConectados = new ArrayList<>();
    public static List<EventoTunelClientes> listaEventosClientes = new ArrayList<>();
    private static String mpCryptoPassword = "ProyectoFinalITT";
    private static Integer grafica1=0;
    private static Integer grafica2=0;
    private static Integer mes1=new Date().getMonth();
    private static Integer mes2=new Date().getMonth();
    private static Integer anno1= Calendar.getInstance().get(Calendar.YEAR);
    private static Integer anno2= Calendar.getInstance().get(Calendar.YEAR);
    private static Date fechaprueba= new Date();
    private static Date fechaInicio;
    private static Date fechaFin;


    private static List<Notificaciones>listNoti= new ArrayList();

    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST/");
            javalinConfig.enableCorsForAllOrigins();
        } ).start(7000);

        if(modoConexion.isEmpty()) {
            ConexionDB.getInstance().InciarDB();
            System.out.println("Inicio");
            if(ServiciosUserWeb.getInstancia().todos().isEmpty()){
                UserWeb us= new UserWeb(NumidUsuarioWeb(), "rony", "rony@sst.com", "pucmm","Rony","Duran");
                ServiciosUserWeb.getInstancia().crearObjeto(us);
            }
            if(ServiciosAppCliente.getInstancia().todos().isEmpty()){
                UserAppCliente uapp= new UserAppCliente(idAppUserGenerate(), "Rony Duran", "H", 12, "rony@prueba.com", "pucmm", "rony","N/A");
                ServiciosAppCliente.getInstancia().crearObjeto(uapp);
            }
        }
        ModelWeb mode =new ModelWeb();

        /*for(int i=0;i<50;i++){
            ServicioTunelLiquifo.getIntacia().crearObjeto(new EventoTunelNivleLiquido("55",fecha()));
        }*/
        //JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        //ManejoRemover mr = new ManejoRemover(mode);
        //mr.hilo.start();

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------WebSockect Para la Página WEB-------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------

        
        app.ws("/WSEnvio",ws->{
            ws.onConnect(ctx -> {
                grafica1=0;
                grafica2=0;
                fechaInicio=null;
                fechaFin=null;
                mes1=new Date().getMonth();
                mes2=new Date().getMonth();
                anno1= Calendar.getInstance().get(Calendar.YEAR);
                anno2= Calendar.getInstance().get(Calendar.YEAR);
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
                enviarMensajeAClientesConectados("contGeneral:"+contClientesdelGeneral());
                enviarMensajeAClientesConectados("conMasc:"+contMasc());
                enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                if(!((UserWeb)ctx.sessionAttribute("usuario") ==null)) {
                    enviarMensajeAClientesConectados("usuario:" + ((UserWeb) ctx.sessionAttribute("usuario")).getUserName());
                }
                diasUserGraf1();

            });
            ws.onMessage(ctx -> {
                String data=ctx.message();
                if(data.startsWith("grafica1:")){
                    String recortada=data.substring(9);
                    System.out.println("Mensaje desde grafica 1:"+recortada);
                    System.out.println(anno1);
                    grafica1=Integer.parseInt(recortada);
                    diasUserGraf1();
                }
                if(data.startsWith("Mes1:")){
                    String recortada=data.substring(5);
                    System.out.println("Mes seleccionado en Grafica 1:"+recortada);
                    mes1=Integer.parseInt(recortada);
                    diasUserGraf1();
                }
                if(data.startsWith("Anno1:")){
                    String recortada=data.substring(6);
                    System.out.println("Anno seleccionado en Grafica 1:"+recortada);
                    anno1=Integer.parseInt(recortada);
                    diasUserGraf1();
                }
                if(data.startsWith("grafica2:")){
                    String recortada=data.substring(9);
                    System.out.println("Mensaje desde grafica 2:"+recortada);
                    grafica2=Integer.parseInt(recortada);
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                    System.out.println("Mes Grafica 2:"+mes2);
                }
                if(data.startsWith("Mes2:")){
                    String recortada=data.substring(5);
                    System.out.println("Mes seleccionado en Grafica 2:"+recortada);
                    mes2=Integer.parseInt(recortada);
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                }
                if(data.startsWith("Anno2:")){
                    String recortada=data.substring(6);
                    System.out.println("Anno seleccionado en Grafica 2:"+recortada);
                    anno2=Integer.parseInt(recortada);
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                }
                if(data.startsWith("EliminarApp:")){
                    String recortada=data.substring(12);
                    System.out.println("ID de usuario a borrar es: "+recortada);
                    eliminarUserAPP(recortada);
                }
                if(data.startsWith("EliminarWeb:")){
                    String recortada=data.substring(12);
                    System.out.println("ID de usuario a borrar es: "+recortada);
                    eliminarUserWeb(recortada);
                }
                if(data.startsWith("Inicio:")){
                    String recortada=data.substring(7);
                    System.out.println("Fecha Inicio es "+recortada);
                    if(!recortada.equalsIgnoreCase("null")){
                        fechaInicio=new SimpleDateFormat("dd/MM/yyyy").parse(recortada);
                        System.out.println(fechaInicio);
                    }else{
                        fechaInicio=null;
                    }


                }
                if(data.startsWith("Final:")){
                    String recortada=data.substring(6);
                    System.out.println("Fecha Final es "+recortada);
                    if(!recortada.equalsIgnoreCase("null")){
                        fechaFin=new SimpleDateFormat("dd/MM/yyyy").parse(recortada);
                    }else{
                        fechaFin=null;
                    }

                    System.out.println(fechaFin);

                }
            });

            ws.onClose(ctx -> {
                System.out.println("Conexión Cerrada - "+ctx.getSessionId());
                usuariosConectados.remove(ctx.session);
            });
            ws.onError(ctx -> {
                System.out.println("Ocurrió un error en el WS");
            });

        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------WebSocket para la APP---------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------


        /*app.ws("/AppEnvio", wsHandler -> {
            wsHandler.onConnect(ctx -> {
                ctx.send("Hola este es el web socket");

            });

        });*/

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------EndPoint para la ruta inicio la Página WEB-------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------

        app.routes(() -> {
          //  before(ctx -> );
            path( "/", () -> {
               before("/",ctx -> {
                   UserWeb user = ctx.sessionAttribute("usuario");
                    if(user==null){
                        String usuario = ctx.cookie("usuario");
                        String password = ctx.cookie("password");
                        if(usuario != null  && password !=null){
                            StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
                            decryptor.setPassword(mpCryptoPassword);
                            user  = validar(usuario, decryptor.decrypt(password));
                            if(user != null){
                                ctx.sessionAttribute("usuario", user);
                                ctx.redirect("/index.html");
                            }
                        }
                    }else{
                        ctx.redirect("/index.html");
                    }

                });
                get("/",ctx -> {
                    //ctx.redirect("/index.html");
                   ctx.redirect("login-actualzado.html");
                        });

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------EndPoints para  la APP--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------

                get("/notificacion",ctx -> {

                    List<Notificaciones> listNotiUsario;

                    //Gson g = new Gson();
                    System.out.println("Id Usuario"+ctx.header("idUsuario"));
                    String idUsuario= ctx.header("idUsuario");
                    if(idUsuario!=null|| !idUsuario.equalsIgnoreCase("")){
                        listNotiUsario=NotificacionesIDUsuario(idUsuario);
                        Collections.sort(listNotiUsario, Collections.reverseOrder());
                        Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm:ss aa").create();
                        String res = g.toJson(listNotiUsario);
                        ctx.header("Content-Type","application/json");
                        ctx.result(res);
                    }

                });
                post("/inserNotifi",ctx -> {
                    List<String>userList;

                    String notificacion=ctx.formParam("noti",String.class).get();
                    String idUsuario=ctx.formParam("id",String.class).get();
                    System.out.println(notificacion+"-----"+idUsuario);
                    if(notificacion!=null && idUsuario!=null){
                        if(!idUsuario.equalsIgnoreCase("")&&!notificacion.equalsIgnoreCase("")){
                            if(idUsuario.equalsIgnoreCase("all")){
                                Notificaciones n1= new Notificaciones(idNotificacion(),notificacion,fecha());
                                userList= n1.getListIdUsuario();
                                for (UserAppCliente appCliente:ServiciosAppCliente.getInstancia().todos()) {
                                    if(validarIdClienteNotificacion(appCliente.getIdCliente())){
                                        userList.add(appCliente.getIdCliente());
                                        System.out.println(appCliente.getIdCliente());
                                    }
                                }
                                n1.setListIdUsuario(userList);
                                listNoti.add(n1);
                                System.out.println("Noti:"+n1.getNotificacion()+"----"+"fecha:"+n1.getFecha());
                            }else{
                                Notificaciones n1= new Notificaciones(idNotificacion(),notificacion,fecha());
                                userList= n1.getListIdUsuario();
                                userList.add(idUsuario);
                                n1.setListIdUsuario(userList);
                                listNoti.add(n1);
                                System.out.println("Noti:"+n1.getNotificacion()+"----"+"fecha:"+n1.getFecha());
                            }

                        }
                    }
                });
                post("/deleteNoti",ctx -> {

                    JsonObject convertedObject = new Gson().fromJson(ctx.body(), JsonObject.class);
                    String idNoti=convertedObject.get("idNoti").toString().replace("\"", "");
                    String idUsuario=convertedObject.get("idUsuario").toString().replace("\"", "");
                    if(idUsuario!=null|| !idUsuario.equalsIgnoreCase("")){
                        System.out.println("EL ID a borra es"+idNoti);
                        EliminarNotificacionesIDUsuario(idNoti,idUsuario);
                        System.out.println("Tamam del arreglo de las notificaciones"+listNoti.size());
                    }
                });

                post("valLogigApp", ctx -> {

                    System.out.println(ctx.body());

                    JsonObject convertedObject = new Gson().fromJson(ctx.body(), JsonObject.class);
                    String nuser=convertedObject.get("name").toString().replace("\"", "");
                    String pass= convertedObject.get("pass").toString().replace("\"", "");
                    System.out.println(nuser);
                    System.out.println(pass);
                    List<UserAppCliente> list;

                    UserAppCliente uapp=null;
                    list = ServiciosAppCliente.getInstancia().todos();
                    if(list!=null){
                        for (UserAppCliente aux: list) {
                            if(aux.getUsername().equalsIgnoreCase(nuser)&&aux.getPassword().equalsIgnoreCase(pass)){
                                uapp=aux;
                                System.out.println("encontrado");
                                break;
                            }
                        }

                    }
                    if(uapp==null){
                        ctx.result("1000");
                        ctx.status(500);
                    }else {
                        ctx.result(uapp.getIdCliente());
                        System.out.println(uapp.getIdCliente());
                    }


                } );

                post("inserUserAppp", ctx -> {

                    System.out.println(ctx.body());

                    JsonObject convertedObject = new Gson().fromJson(ctx.body(), JsonObject.class);
                    String nuser=convertedObject.get("name").toString().replace("\"", "");
                    String pass= convertedObject.get("pass").toString().replace("\"", "");
                    String nombrec=convertedObject.get("nombre").toString().replace("\"", "");
                    String email= convertedObject.get("email").toString().replace("\"", "");
                    String sex=convertedObject.get("sexo").toString().replace("\"", "");
                    String id= idAppUserGenerate();
                    //String pass= convertedObject.get("pass").toString().replace("\"", "");
                    System.out.println("id:"+id+"---"+"Username:"+nuser+"-------"+"Contrase:"+pass+"-------"+"email:"+email+"-------"+"sexo:"+sex+"-------"+"Nombre Completo:"+nombrec);
                    //System.out.println(pass);
                    //List<UserAppCliente> list;

                    Integer igual=0;
                    UserAppCliente us= new UserAppCliente(id, nombrec, sex, 12, email, pass, nuser,"N/A");
                    for (UserAppCliente aux: ServiciosAppCliente.getInstancia().todos()) {
                        if(us.getUsername().equalsIgnoreCase(aux.getUsername())&& us.getNombreCompleto().equalsIgnoreCase(aux.getNombreCompleto())&&us.getCorreoElectronico().equalsIgnoreCase(aux.getCorreoElectronico())){
                            System.out.println("usuario ya creado");

                            igual=1;
                            break;
                        }
                    }
                    if(igual==0){
                        ServiciosAppCliente.getInstancia().crearObjeto(us);
                        ctx.result(id);
                    }else{
                        ctx.status(500);
                    }


                } );
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------EndPoints para  la Página WEB-------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------EdnPoints Manejo de Usuarios de la Página WEB----------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

                post("/autenticar",ctx -> {
                    String recordar= ctx.formParam("recordar");
                    String nombreUsuario = ctx.formParam("username");
                    String password = ctx.formParam("pass");
                     System.out.print("\n Nombre de usuario: " + nombreUsuario +"----- "+"pass: " + password);
                    System.out.println("Recordar"+recordar);

                    UserWeb aux = validar(nombreUsuario,password);
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("usuario",modelo);

                   if(aux!=null){
                       if(recordar!=null && recordar.equalsIgnoreCase("on")){
                           System.out.println("Creando cookie...\n");
                           StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                           encryptor.setPassword(mpCryptoPassword);
                           encryptor.encrypt(aux.getPassword());

                           ctx.cookie("usuario", aux.getUserName(), 604800);// se crea la cookie por una semana
                           ctx.cookie("password",   encryptor.encrypt(aux.getPassword()), 604800);
                       }
                       ctx.sessionAttribute("usuario", aux);
                       enviarMensajeAClientesConectados("usuario:"+aux.getUserName());
                       //ctx.redirect("/index.html");
                    }else {
                       //ctx.status(500);
                       ctx.result("Incorrecto, por favor verificar username y/o password!");
                       System.out.println("error enviado");
                       //ctx.redirect("/login-actualzado.html");
                       //modelo.put("error","Please check username & password!");
                       //ctx.render("Web SST/login-actualzado.html",modelo);

                   }
                });
                post("/insertarWebUser",ctx -> {
                    String id=NumidUsuarioWeb();
                    String usuario= ctx.formParam("username");
                    String contrasena = ctx.formParam("pass");
                    String email= ctx.formParam("email");
                    String nombre = ctx.formParam("nombre");
                    String apellido = ctx.formParam("apellido");
                    /*StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                    encryptor.setPassword(mpCryptoPassword);
                    encryptor.encrypt(contrasena);*/
                    Integer val=0;
                    System.out.println(id+"--"+usuario+"--"+contrasena+"--"+email+"--"+nombre+"--"+apellido);
                    if(!usuario.equalsIgnoreCase("")&&!contrasena.equalsIgnoreCase("")&& !email.equalsIgnoreCase("")&& !nombre.equalsIgnoreCase("") && !apellido.equalsIgnoreCase("")){
                        UserWeb us1= new UserWeb(id, usuario, email,  contrasena,nombre,apellido);
                        for (UserWeb aux:ServiciosUserWeb.getInstancia().todos()) {
                            if(usuario.equalsIgnoreCase(aux.getUserName()) && email.equalsIgnoreCase(aux.getCorreoElectronico())){
                                val=1;
                                break;
                            }
                        }
                        if(val==0){
                            ServiciosUserWeb.getInstancia().crearObjeto(us1);
                        } else{
                            ctx.result("Aviso:,ya un usuario tiene estos datos, debe de usar otros datos");
                        }
                        //ctx.redirect("/ListarUsuariosWeb.html");
                    }else{
                        ctx.result("Aviso: Verificar, no se aceptan campos vacios");
                    }
                });
                post("/addUserApp",ctx -> {
                    String id=idAppUserGenerate();
                    String usuario= ctx.formParam("username");
                    String contrasena = ctx.formParam("pass");
                    String email= ctx.formParam("email");
                    String nombre = ctx.formParam("nombre");
                    String sexo = ctx.formParam("sexo");
                    String conContra = ctx.formParam("conpass");
                    String nfc = ctx.formParam("NFC");
                    if(nfc==null){
                      nfc="N/A";

                    }                    /*StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                    encryptor.setPassword(mpCryptoPassword);
                    encryptor.encrypt(contrasena);*/
                    Integer val=0;
                    System.out.println(id+"--"+usuario+"--"+contrasena+"--------"+conContra+"--"+email+"--"+nombre+"--"+sexo);
                    if(!usuario.equalsIgnoreCase("")&&!contrasena.equalsIgnoreCase("")&& !email.equalsIgnoreCase("")&& !nombre.equalsIgnoreCase("")){
                        if(contrasena.equalsIgnoreCase(conContra)){
                            UserAppCliente us= new UserAppCliente(id, nombre, sexo, 12, email, contrasena, usuario,nfc);
                            for (UserAppCliente aux:ServiciosAppCliente.getInstancia().todos()) {
                                if(usuario.equalsIgnoreCase(aux.getUsername()) && email.equalsIgnoreCase(aux.getCorreoElectronico())){
                                    val=1;
                                    break;
                                }
                            }
                            if(val==0){
                                ServiciosAppCliente.getInstancia().crearObjeto(us);
                            }if(val==1){
                                ctx.result("Aviso:Ya un usuario tiene estos datos, debe de usar otros datos");
                            }
                        }else{
                            ctx.result("Aviso: Las claves de acceso no coinciden, favor verificar");

                        }

                        //ctx.redirect("/ListarUsuariosWeb.html");
                    }else{
                        ctx.result("Aviso: Verificar, no se aceptan campos vacios");
                    }

                });
                get("/logout",ctx -> {
                    ctx.sessionAttribute("usuario", null);
                    ctx.removeCookie("usuario");
                    ctx.removeCookie("password");
                    ctx.redirect("/login-actualzado.html");
                });
                get("/showPerfil",ctx -> {
                    // List<UserWeb> list;
                    //list = ServiciosUserWeb.getInstancia().todos();
                    UserWeb user = ctx.sessionAttribute("usuario");
                    //UserWeb u1=null;
                    if(user!=null){
                        System.out.println("entro");
                        /*for (UserWeb aux: list) {
                            if(aux==user){}
                        }*/
                        Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                        String res = g.toJson(user);
                        ctx.header("Content-Type","application/json");
                        ctx.result(res);
                    }
                });

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------EndPoints Manejo de Tablas de la Página WEB------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------

                get("/tableNotiUserWEB",ctx -> {
                    List<EventoTunelClientes> list;
                    HashMap map = new HashMap();
                    list = validarIdClienteAPP();
                    map.put("data", list);
                    Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);
                    //System.out.println(res);
                    //ctx.json(map);

                });
                get("/tableEventosClientes",ctx -> {
                    List<EventoTunelClientes> list;
                    HashMap map = new HashMap();
                    list = ServicioTunelClientes.getInstancia().todos();
                    map.put("data", list);
                    Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);
                    //System.out.println(res);
                    //ctx.json(map);

                });

                get("/tableUserAPP",ctx -> {
                    List<UserAppCliente> list;
                    HashMap map = new HashMap();

                    list = ServiciosAppCliente.getInstancia().todos();
                    map.put("data", list);
                    Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);
                    //System.out.println(res);
                    //ctx.json(map);

                });
                get("/tableUserWEB",ctx -> {
                    List<UserWeb> list;
                    HashMap map = new HashMap();

                    list = ServiciosUserWeb.getInstancia().todos();

                    map.put("data", list);

                    Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);
                });
                get("/tableEventosLiquido",ctx -> {
                    List<EventoTunelNivleLiquido> list;
                    HashMap map = new HashMap();

                    list = ServicioTunelLiquifo.getIntacia().todos();

                    map.put("data", list);

                    Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
                    String res = g.toJson(map);
                    ctx.header("Content-Type","application/json");
                    ctx.result(res);
                    //System.out.println(res);

                    //ctx.json(map);

                });
                /*get("/",ctx -> {
                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("esta",mode.getEstado());
                    modelo.put("numero",mode.getContador());
                    modelo.put("taman",mode.getNivel());
                    modelo.put("notifi",mode.getNotificaciones());

                    ctx.render("/Web SST/index.html", modelo);});

               post("/noti",ctx -> {

                });*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------EndPoint para recepcion de Datos Raspberry PI--------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------


                post("/cliente",ctx -> {
                    String id=ctx.formParam("id",String.class).get();
                    String masc= ctx.formParam("masc",String.class).get();
                    String temp= ctx.formParam("temp",String.class).get();
                    //String nfc= ctx.formParam("nfc",String.class).get();

                    //if(nfc!=null){
                      //  id=UserAPPNFC(nfc);
                   // }

                    System.out.println("Id:"+id+"--"+"Mascarilla:"+masc+"Temperatura:"+temp);
                    ServicioTunelClientes.getInstancia().crearObjeto(new EventoTunelClientes(id,masc,temp,fecha()));


                    enviarMensajeAClientesConectados("cont:"+contClientesdelActual());
                    enviarMensajeAClientesConectados("contGeneral:"+contClientesdelGeneral());
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                    diasUserGraf1();

                });

                post("/addClienteEvento",ctx -> {
                    String id=ctx.formParam("id",String.class).get();
                    String masc= ctx.formParam("masc",String.class).get();
                    String temp= ctx.formParam("temp",String.class).get();
                    String nfc= ctx.formParam("nfc",String.class).get();
                    String fe= ctx.formParam("fecha",String.class).get();
                    Date fnew= new SimpleDateFormat("dd/MM/yyyy").parse(fe);

                    if(nfc!=null && !nfc.equalsIgnoreCase("null")){
                        id=UserAPPNFC(nfc);
                    }

                    System.out.println("Id:"+id+"--"+"Mascarilla:"+masc+"Temperatura:"+temp+"La Fecha es"+fnew);
                    ServicioTunelClientes.getInstancia().crearObjeto(new EventoTunelClientes(id,masc,temp,fnew));


                    enviarMensajeAClientesConectados("cont:"+contClientesdelActual());
                    enviarMensajeAClientesConectados("contGeneral:"+contClientesdelGeneral());
                    enviarMensajeAClientesConectados("conMasc:"+contMasc());
                    enviarMensajeAClientesConectados("sinMasc:"+sintMasc());
                    diasUserGraf1();

                });

                post("/addNivelEvento",ctx -> {
                    String nivel= ctx.formParam("taman",String.class).get();
                    String fe= ctx.formParam("fecha",String.class).get();
                    Date fnew= new SimpleDateFormat("dd/MM/yyyy").parse(fe);

                    ServicioTunelLiquifo.getIntacia().crearObjeto(new EventoTunelNivleLiquido(nivel,fnew));
                    //mode.setNivel(ctx.formParam("taman",Integer.class).get());
                    System.out.println("El nivel es:"+nivel+"----"+"la fecha es:"+fnew);
                    enviarMensajeAClientesConectados("nivel:"+nivel);
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
                /* post("/mascarilla",ctx -> {
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
                post("/contador",ctx -> {
                    mode.setContador(ctx.formParam("numero",String.class).get());
                    System.out.println(ctx.formParam("numero",String.class).get());
                    enviarMensajeAClientesConectados("cont:"+mode.getContador());
                });*/

            });
        });
       // ConexionDB.getInstance().detenerDB();
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

    public static String idNotificacion () {

        int cont=0;
        //cont=ServiciosNotificaciones.getInstancia().todos().size();
        cont=listNoti.size();
        cont++;
        String idFinal= Integer.toString(cont);
        System.out.println("EL id de las Notificaciones es:"+idFinal);
        return idFinal;

    };

    public static Integer contClientesdelGeneral () {

        int cont=0;
        for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                cont++;
        }
        System.out.println("La cantidad general es:"+cont);
        return cont;

    };
    public static String NumidUsuarioWeb () {

        int cont=0;
        String id="";
        cont=ServiciosUserWeb.getInstancia().todos().size();
        cont++;
        id="uWeb-"+cont;
        return id;

    };

    public static String idAppUserGenerate () {
        String id="";
        int cont=0;

        cont=ServiciosAppCliente.getInstancia().todos().size();
        cont++;
        id="capp-"+Integer.toString(cont);
        return id;

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
        if(grafica2==0){
            Date fecha1 = new Date();

            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("Si")){
                    if(cambiarFormatoFecha(aux.getFecha()).compareTo(cambiarFormatoFecha(fecha1))==0){
                        mas++;
                    }
                }
            }
        }
        if(grafica2==1){
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("Si")){
                    cal.setTime(aux.getFecha());
                    if(cal.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
                        mas++;

                    }
                }
            }
        }
        if(grafica2==2){
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("Si")){
                    cal2.setTime(aux.getFecha());
                    if(mes2 == cal2.get(Calendar.MONTH)){

                        mas++;

                    }
                }
            }
        }
        if(grafica2==3){
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("Si")){
                    cal2.setTime(aux.getFecha());
                    if(anno2 == cal2.get(Calendar.YEAR)){
                        mas++;

                    }
                }
            }
        }

        return mas;
    }

    public static Integer sintMasc(){
        Integer sinMas=0;

        if(grafica2==0){
            Date fecha1 = new Date();

            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("No")){
                    if(cambiarFormatoFecha(aux.getFecha()).compareTo(cambiarFormatoFecha(fecha1))==0){
                        sinMas++;
                    }
                }
            }
        }
        if(grafica2==1){
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("No")){
                    cal.setTime(aux.getFecha());
                    if(cal.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
                        sinMas++;

                    }
                }
            }
        }
        if(grafica2==2){
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("No")){
                        cal2.setTime(aux.getFecha());
                    if(mes2 == cal2.get(Calendar.MONTH)){
                        sinMas++;

                    }
                }
            }
        }
        if(grafica2==3){
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            for (EventoTunelClientes aux: ServicioTunelClientes.getInstancia().todos()) {
                if(aux.getEstadoMascarilla().equalsIgnoreCase("No")){
                    cal2.setTime(aux.getFecha());
                    if(anno2 == cal2.get(Calendar.YEAR)){
                        sinMas++;

                    }
                }
            }
        }

        return sinMas;
    }

    public static void diasUserGraf1(){

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
        //System.out.println(lu+"--"+ma+"--"+mi+"--"+ju+"--"+vi+"--"+sa+"--"+dom);
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
        //System.out.println(cal.getTime());
        cal2.setTime(d1);
        if(grafica1==0){
            if(cal.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
                out=true;
                //System.out.println(cal.getTime());
                //System.out.println("Current week of year is : " +cal.get(Calendar.WEEK_OF_YEAR));
            }
        }
        if(grafica1==1){
            //if(cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)){
            if(mes1 == cal2.get(Calendar.MONTH)){
                out=true;
                //System.out.println(cal.getTime());
                System.out.println("Mes Actual: " +cal2.get(Calendar.MONTH));
            }
        }
        if(grafica1==2){
            //if(cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
            if(anno1 == cal2.get(Calendar.YEAR)){
                out=true;
                //System.out.println(cal.getTime());
                //System.out.println("Current week of year is : " +cal.get(Calendar.WEEK_OF_YEAR));
                System.out.println("Año Actual: " +anno1);
            }
        }


        return out;
    }

    public static UserWeb validar(String user, String pass){

            UserWeb u1= null;

        for (UserWeb aux:ServiciosUserWeb.getInstancia().todos()) {
            if(aux.getUserName().equalsIgnoreCase(user)&&aux.getPassword().equalsIgnoreCase(pass)){
                System.out.println("Validado");
                u1=aux;
                break;
            }else {
                System.out.println("No se puede validar");
                u1=null;
            }
        }


            return u1;
    }

    public static List<Notificaciones> NotificacionesIDUsuario(String idUsuario){
        List<Notificaciones> listaN= new ArrayList<Notificaciones>();

        for (Notificaciones aux: listNoti) {
            for (String idU: aux.getListIdUsuario()){
                if(idU.equalsIgnoreCase(idUsuario)){
                    System.out.println("Prueba>>>>>"+aux.getNotificacion());
                    listaN.add(aux);
                }
            }
        }

        return listaN;
    }

    public static void EliminarNotificacionesIDUsuario(String idNot,String idUsuario){

        for (Notificaciones aux: listNoti) {
            if (aux.getIdNoti().equalsIgnoreCase(idNot)) {
                aux.getListIdUsuario().remove(idUsuario);
            }
        }
    }

    public static void eliminarUserAPP(String id){
        UserAppCliente ac =null;
        for (UserAppCliente aux: ServiciosAppCliente.getInstancia().todos()) {
            if(id.equalsIgnoreCase(aux.getIdCliente())){
                ac=aux;
                break;
            }
        }
        if (ac!=null){
            ServiciosAppCliente.getInstancia().EliminarPorID(id);
        }

    }

    public static String UserAPPNFC(String idNFC){
        UserAppCliente ac =null;
        String id=null;
        for (UserAppCliente aux: ServiciosAppCliente.getInstancia().todos()) {
            if(idNFC.equalsIgnoreCase(aux.getIdNFC())){
                ac=aux;
                id=ac.getIdCliente();
                break;
            }
        }
        if (ac==null){
           id="uInv";
        }
        return id;
    }

    public static List<EventoTunelClientes> validarIdClienteAPP(){
        System.out.println("entro 2222");
        List<EventoTunelClientes> listaN= new ArrayList<EventoTunelClientes>();
        if(fechaInicio==null && fechaFin==null){
            System.out.println("entro 111");
            for (EventoTunelClientes auxE: ServicioTunelClientes.getInstancia().todos()) {
                for (UserAppCliente aux:ServiciosAppCliente.getInstancia().todos()) {
                    if(auxE.getIdCliente().equalsIgnoreCase(aux.getIdCliente())){
                            listaN.add(auxE);
                        break;
                    }
                }
            }
        }
        if(fechaInicio!=null && fechaFin!=null){
            System.out.println("entro ");
            for (EventoTunelClientes auxE: ServicioTunelClientes.getInstancia().todos()) {
                for (UserAppCliente aux:ServiciosAppCliente.getInstancia().todos()) {
                    if(auxE.getIdCliente().equalsIgnoreCase(aux.getIdCliente())){
                        if(between(auxE.getFecha(),fechaInicio,fechaFin)){
                            listaN.add(auxE);
                        }


                        break;
                    }
                }
            }
        }

        return listaN;
    }

    public static boolean validarIdClienteNotificacion(String id){
        System.out.println("entro 2222");
        Boolean p =false;
        if(fechaInicio==null && fechaFin==null){
            System.out.println("entro ");
            for (EventoTunelClientes auxE: ServicioTunelClientes.getInstancia().todos()) {
                if(auxE.getIdCliente().equalsIgnoreCase(id)){
                        p=true;
                    break;
                }
            }
        }
        if(fechaInicio!=null && fechaFin!=null){
            System.out.println("entro ");
            for (EventoTunelClientes auxE: ServicioTunelClientes.getInstancia().todos()) {
                    if(auxE.getIdCliente().equalsIgnoreCase(id)){
                        if(between(auxE.getFecha(),fechaInicio,fechaFin)){
                            p=true;
                        }
                        break;
                    }
            }
        }

        return p;
    }



    private static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            return (dateEqualOrAfter(date, dateStart) && dateEqualOrBefore(date, dateEnd));

        }
        return false;
    }

    private static boolean dateEqualOrAfter(Date dateInQuestion, Date date2)
    {
        if (dateInQuestion.equals(date2))
            return true;

        return (dateInQuestion.after(date2));

    }
    private static boolean dateEqualOrBefore(Date dateInQuestion, Date date2)
    {
        if (dateInQuestion.equals(date2))
            return true;

        return (dateInQuestion.before(date2));

    }



    public static void eliminarUserWeb(String id){
        UserWeb aw =null;
        for (UserWeb aux: ServiciosUserWeb.getInstancia().todos()) {
            if(id.equalsIgnoreCase(aux.getIdUserWeb())){
                aw=aux;
                break;
            }
        }
        if (aw!=null){
            ServiciosUserWeb.getInstancia().EliminarPorID(id);
        }

    }

}
