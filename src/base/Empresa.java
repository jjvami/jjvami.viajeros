package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by juanj on 20/03/2017.
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
        }
    }

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
