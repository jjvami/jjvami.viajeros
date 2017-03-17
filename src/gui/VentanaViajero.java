package gui;

import base.Viajero;
import com.toedter.calendar.JDateChooser;
import util.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

/**
 * Created by sobremesa on 16/03/2017.
 */
public class VentanaViajero extends JDialog {
    private JPanel panel1;
    private JTextField tfNacionalidad;
    private JTextField tfDocumento;
    private JTextField tfNDocumento;
    private JTextField tfNombre;
    private JTextField tfApellido1;
    private JTextField tfApellido2;
    private JDateChooser dcFechaNacimiento;
    private JDateChooser dcFechaExpedicion;
    private JButton btAceptar;
    private JButton btCancelar;
    private JComboBox cbSexo;

    private base.Viajero viajero;

    public enum Accion{
        ACEPTAR, CANCELAR
    }
    private Accion accion;
    private Database database;

    public VentanaViajero(){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);
        this.database = new Database();

        rellenarCombo();

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

    public VentanaViajero(Viajero viajero){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);
        database = new Database();

        setViajero(viajero);

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
    public void setViajero(Viajero viajero){

    }

    private void rellenarCombo(){
        cbSexo.addItem("MASCULINO");
        cbSexo.addItem("FEMENINO");
    }
    private void Aceptar(){
        if(this.viajero == null){
            this.viajero = new base.Viajero();
        }
        this.viajero.setNacionalidad(tfNacionalidad.getText());
        this.viajero.setDocumento(tfDocumento.getText());
        this.viajero.setNumero_documento(tfNDocumento.getText());
        this.viajero.setFecha_expedicion((Date) dcFechaExpedicion.getDate());
        this.viajero.setNombre(tfNombre.getText());
        this.viajero.setApellido1(tfApellido1.getText());
        this.viajero.setApellido2(tfApellido2.getText());
        this.viajero.setSexo(cbSexo.getSelectedItem().toString());
        this.viajero.setFecha_nacimiento((Date) dcFechaNacimiento.getDate());

        accion = Accion.ACEPTAR;
        setVisible(false);
    }
    public void Cancelar(){
        accion = Accion.CANCELAR;
        setVisible(false);
    }
}
