package logica;
import  io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig ->{
            javalinConfig.addStaticFiles("/Web SST");
        } ).start(7000);

    }
}
