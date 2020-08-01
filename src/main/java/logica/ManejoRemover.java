package logica;

import java.util.List;

public class ManejoRemover implements Runnable{
    Thread hilo;
    private ModelWeb modelo;

    public ManejoRemover(ModelWeb m ) {
        hilo = new Thread((Runnable) this);
        modelo=m;
    };


    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == hilo) {
            modelo.eliminarNoti();
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
            }
        }
    }
}
