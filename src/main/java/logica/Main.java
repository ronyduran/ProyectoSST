package logica;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST");
        } ).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        List<String> notis=new ArrayList<>();

        notis.add("Alarma 1");
        notis.add("Alarma 2");
        notis.add("Alarma 3");


        app.routes(() -> {
            path( "/", () -> {
                get("/",ctx -> {


                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("esta","Encendido");
                    modelo.put("numero","3");
                    modelo.put("taman",75);
                    modelo.put("notifi",notis);
                    ctx.render("/Web SST/index.html", modelo);});

                post("/",ctx -> {
                    notis.add(ctx.formParam("notfi",String.class).get());

                    System.out.println(ctx.formParam("notfi",String.class).get());
                });


            });
        });
    }
}
