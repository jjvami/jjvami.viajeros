package base;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Objeto en el que se almacena el nombre y codigo de la empresa, en la carga del objeto se busca un fichero,
 * si este existe rellena el objeto con las informacion del fichero.
 */
public class Empresa {

    private String nombre;
    private String codigo;

    public Empresa(){
        Properties configuracion = new Properties();
        try {
            configuracion.load(new FileInputStream("datosEmpresa.properties"));
            nombre = String.valueOf(configuracion.get("nombre"));
            codigo = String.valueOf(configuracion.getProperty("codigo"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Los datos de la empresa no estan introducidos");
        }
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
