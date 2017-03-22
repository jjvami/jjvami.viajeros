package util;

import gui.Principal;

import javax.swing.*;

/**
 * Clase que actualiza en un proceso en background la tabla de viajeros cada 1 minuto.
 */
public class ActualizarDatos extends SwingWorker{
    @Override
    protected Object doInBackground() throws Exception {
        while (true){
            Principal.rellenarTablas();
            Thread.sleep(60000);
        }
    }
}
