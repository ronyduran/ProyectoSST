package logica;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {
    public static List<Session> usuariosConectados = new ArrayList<>();

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST");
        } ).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        ModelWeb mode =new ModelWeb();
        mode.insertNoti("Esperando Notificaciones.....");

        ManejoRemover mr = new ManejoRemover(mode);

       // mr.hilo.start();
        //-----------------------------
        //---------------------------------------------------------------------------------------
        //Prueba WebSockect-----------------------------------------------------------------------
        app.ws("/WSEnvio",ws->{
            ws.onConnect(ctx -> {
                System.out.println("Conexión Iniciada - " + ctx.getSessionId());
                usuariosConectados.add(ctx.session);
                enviarMensajeAClientesConectados("estado:"+mode.getEstado());
                enviarMensajeAClientesConectados("cont:"+mode.getContador());
                enviarMensajeAClientesConectados("nivel:"+Integer.toString(mode.getNivel()));
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
                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("esta",mode.getEstado());
                    modelo.put("numero",mode.getContador());
                    modelo.put("taman",mode.getNivel());
                    modelo.put("notifi",mode.getNotificaciones());

                    ctx.render("/Web SST/index.html", modelo);});

                post("/noti",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.insertNoti(noti);
                    System.out.println(noti);
                    enviarMensajeAClientesConectados("noficiaciones:"+mode.fecha()+"---"+noti);
                });
                post("/mascarilla",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.conMascarilla(noti);
                    System.out.println(noti+"---"+"Cantidad:"+mode.getUserMascarillla().size());
                    enviarMensajeAClientesConectados("noficiaciones:"+mode.fecha()+"---"+noti);
                });post("/sinmascarilla",ctx -> {
                    String noti=ctx.formParam("notfi",String.class).get();
                    mode.sinMascarilla(noti);
                    System.out.println(noti+"---"+"Cantidad:"+mode.getUserSinMascarillla().size());
                    enviarMensajeAClientesConectados("noficiaciones:"+mode.fecha()+"---"+noti);
                });
                post("/nivel",ctx -> {
                    mode.setNivel(ctx.formParam("taman",Integer.class).get());
                    System.out.println(ctx.formParam("taman",Integer.class).get());
                    enviarMensajeAClientesConectados("nivel:"+mode.getNivel());
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


}
