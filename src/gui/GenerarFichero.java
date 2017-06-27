package gui;

import base.Empresa;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

/**
 * Clase que genera un fichero con el texto proporcionado.
 */
public class GenerarFichero extends JFrame{
    public GenerarFichero(String texto, Empresa empresa){
        if (empresa.getContFicehros() > 999)
            empresa.setContFicehros(0);

        FileWriter  save = null;
        try{
            JFileChooser file = new JFileChooser();
            file.setSelectedFile( new File(empresa.getCodigo() + "." + formatNumFicheros(String.valueOf(empresa.getContFicehros()))) );
            file.showSaveDialog(this);
            File guarda = null;
            if (file.CANCEL_OPTION != 1)
                guarda =file.getSelectedFile();

            if(guarda !=null){
                save = new FileWriter(guarda+".txt");
                save.write(texto);
                save.close();
                JOptionPane.showMessageDialog(null, "El archivo se a guardado Exitosamente", "Información",JOptionPane.INFORMATION_MESSAGE);

                empresa.setContFicehros(empresa.getContFicehros()+1);
                guardarDatos(empresa);
            }
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(null, "Su archivo no se ha guardado", "Advertencia",JOptionPane.WARNING_MESSAGE);
        } finally {
            try {
                save.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarDatos(Empresa empresa){
        Properties configuracion = new Properties();
        configuracion.setProperty("nombre", empresa.getNombre().toUpperCase());
        configuracion.setProperty("codigo", empresa.getCodigo().toUpperCase());
        configuracion.setProperty("contFicheros",String.valueOf(empresa.getContFicehros()).toUpperCase());
        try {
            configuracion.store(new FileOutputStream("datosEmpresa.properties"), "Archivo de datos");
        } catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Error, no se a encontrado el archivo de datos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatNumFicheros(String num){

        for (int i = 0; i < 3 ; i++){
            num = "0"+num;
        }

        return num;
    }
}
