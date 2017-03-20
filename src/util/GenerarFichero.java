package util;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by juanjo on 20/03/2017.
 */
public class GenerarFichero extends JFrame{
    public GenerarFichero(String texto){
        try{
            String nombre="eeeeee";
            JFileChooser file = new JFileChooser();
            file.showSaveDialog(this);
            File guarda =file.getSelectedFile();

            if(guarda !=null){
                FileWriter  save = new FileWriter(guarda+".txt");
                save.write(texto);
                save.close();
                JOptionPane.showMessageDialog(null, "El archivo se a guardado Exitosamente", "Información",JOptionPane.INFORMATION_MESSAGE);
            }
        }
            catch(IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Su archivo no se ha guardado", "Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }
}
