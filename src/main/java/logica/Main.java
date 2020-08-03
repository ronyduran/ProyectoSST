package logica;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST");
        } ).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        ModelWeb mode =new ModelWeb();
        mode.insertNoti("Esperando Notificaciones.....");

        ManejoRemover mr = new ManejoRemover(mode);

        mr.hilo.start();






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
                    mode.insertNoti(ctx.formParam("notfi",String.class).get());
                    System.out.println(ctx.formParam("notfi",String.class).get());
                });
                post("/nivel",ctx -> {
                    mode.setNivel(ctx.formParam("taman",Integer.class).get());
                    System.out.println(ctx.formParam("taman",Integer.class).get());
                });
                post("/estado",ctx -> {
                    mode.setEstado(ctx.formParam("esta",String.class).get());
                    System.out.println(ctx.formParam("esta",String.class).get());
                });
                post("/contador",ctx -> {
                    mode.setContador(ctx.formParam("numero",String.class).get());
                    System.out.println(ctx.formParam("numero",String.class).get());
                });



            });
        });
    }


}
