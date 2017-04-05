package gui;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que genera un fichero con el texto proporcionado.
 */
public class GenerarFichero extends JFrame{
    public GenerarFichero(String texto){
        try{
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
