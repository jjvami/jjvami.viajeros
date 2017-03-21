package gui;

import base.Empresa;
import util.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;

/**
 * Created by sobremesa on 21/03/2017.
 */
public class VentanaEmpresa extends JDialog {
    private JPanel panel1;
    private JTextField tfNombre;
    private JTextField tfCodigo;
    private JButton btCancelar;
    private JButton btAceptar;

    public enum Accion{
        ACEPTAR, CANCELAR
    }
    private Accion accion;
    private Empresa empresa;

    public VentanaEmpresa(){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        empresa = new Empresa();
        rellenarCampos();

        btAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Aceptar();
            }
        });

        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cancelar();
            }
        });
    }
    public Accion mostrarDialogo(){
        setVisible(true);
        return  accion;
    }
    private void guardarDatos(){
        Properties configuracion = new Properties();
        configuracion.setProperty("nombre", tfNombre.getText().toUpperCase());
        configuracion.setProperty("codigo", tfCodigo.getText().toUpperCase());
        try {
            configuracion.store(new FileOutputStream("datosEmpresa.properties"), "Archivo de datos");
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Error, no se a encontrado el archivo de datos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rellenarCampos(){
        Properties configuracion = new Properties();
        try {
            configuracion.load(new FileInputStream("datosEmpresa.properties"));
            tfNombre.setText(String.valueOf(configuracion.get("nombre")));
            tfCodigo.setText(String.valueOf(configuracion.getProperty("codigo")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void Aceptar(){
        guardarDatos();
        this.empresa.setNombre(tfNombre.getText().toUpperCase());
        this.empresa.setCodigo(tfCodigo.getText().toUpperCase());

        accion = Accion.ACEPTAR;
        setVisible(false);
    }
    public void Cancelar(){
        accion = Accion.CANCELAR;
        setVisible(false);
    }

    public Empresa getEmpresa() {
        return empresa;
    }
}
