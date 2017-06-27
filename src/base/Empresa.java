package base;

import com.sun.media.sound.EmergencySoundbank;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Objeto en el que se almacena el nombre y codigo de la empresa.
 */
public class Empresa {

    private String nombre;
    private String codigo;
    private int contFicehros;

    public Empresa(){
        contFicehros = 0;
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

    public int getContFicehros() {
        return contFicehros;
    }

    public void setContFicehros(int contFicehros) {
        this.contFicehros = contFicehros;
    }
}


