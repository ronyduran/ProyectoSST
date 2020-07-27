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

        app.routes(() -> {
            path( "/", () -> {
                get("/",ctx -> {
                    Map<String,Object> modelo = new HashMap<>();
                    modelo.put("esta","Encendido");
                    modelo.put("numero","3");
                    modelo.put("taman",88);
                    ctx.render("/Web SST/index.html", modelo);});
            });
        });
    }
}
