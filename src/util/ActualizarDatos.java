package util;

import gui.Principal;

import javax.swing.*;

/**
 * Created by sobremesa on 21/03/2017.
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
