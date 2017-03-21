package gui;

import base.Viajero;
import com.toedter.calendar.JDateChooser;
import util.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Juan Jose Vallespin Millan on 19/03/2017.
 */
public class VentanaViajero extends JDialog {
    private JPanel panel1;
    private JTextField tfNDocumento;
    private JTextField tfNombre;
    private JTextField tfApellido1;
    private JTextField tfApellido2;
    private JDateChooser dcFechaNacimiento;
    private JDateChooser dcFechaExpedicion;
    private JButton btAceptar;
    private JButton btCancelar;
    private JComboBox cbSexo;
    private JComboBox cbNacionalidad;
    private JComboBox cbDocumento;

    private base.Viajero viajero;

    public enum Accion{
        ACEPTAR, CANCELAR
    }
    private Accion accion;
    private Database database;

    public VentanaViajero(){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);
        this.database = new Database();

        rellenarCombos();

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
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);
        database = new Database();

        rellenarCombos();
        setViajero(viajero);
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
    public void setViajero(Viajero viajero){
        this.viajero = viajero;
    }

    private void rellenarCombos(){

        //Combo Box sexo
        cbSexo.addItem("MASCULINO");
        cbSexo.addItem("FEMENINO");

        //Combo Box paises
        try {
            ArrayList<String> paises = database.getPaises();
            cbNacionalidad.addItem("ESPAÑA");
            for(String pais:paises){
                cbNacionalidad.addItem(pais);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Combo Box documento
        cbDocumento.addItem("Documento nacional de identidad");
        cbDocumento.addItem("Pasaporte");
        cbDocumento.addItem("Permiso de conducir");
        cbDocumento.addItem("Documento de identidad");
        cbDocumento.addItem("Permiso de residencia español");
        cbDocumento.addItem("Permiso de residencia extrangero");
    }
    private void rellenarCampos(){
        cbNacionalidad.setSelectedItem(viajero.getNacionalidad());
        cbDocumento.setSelectedItem(viajero.getDocumento());
        tfNDocumento.setText(viajero.getNumero_documento());
        dcFechaExpedicion.setDate(viajero.getFecha_expedicion());
        tfNombre.setText(viajero.getNombre());
        tfApellido1.setText(viajero.getApellido1());
        tfApellido2.setText(viajero.getApellido2());
        cbSexo.setSelectedItem(viajero.getSexo());
        dcFechaNacimiento.setDate(viajero.getFecha_nacimiento());
    }
    private boolean comprobarCampos(){
        if(tfNDocumento.getText().equalsIgnoreCase("")){
            return false;
        }
        else if(tfNombre.getText().equalsIgnoreCase("")){
            return false;
        }
        else if(tfApellido1.getText().equalsIgnoreCase("")){
            return false;
        }
        else if(dcFechaNacimiento.getDate() == null){
            return false;
        }
        else if(dcFechaExpedicion.getDate() == null){
            return false;
        }
        else {
            return true;
        }
    }
    private void Aceptar(){
        if(comprobarCampos()){
            if(this.viajero == null){
                this.viajero = new Viajero();
            }
            this.viajero.setNacionalidad(cbNacionalidad.getSelectedItem().toString().toUpperCase());
            this.viajero.setDocumento(cbDocumento.getSelectedItem().toString().toUpperCase());
            this.viajero.setNumero_documento(tfNDocumento.getText().toUpperCase());
            java.sql.Date fechaExp = new java.sql.Date(dcFechaExpedicion.getDate().getTime());
            this.viajero.setFecha_expedicion(fechaExp);
            this.viajero.setNombre(tfNombre.getText().toUpperCase());
            this.viajero.setApellido1(tfApellido1.getText().toUpperCase());
            this.viajero.setApellido2(tfApellido2.getText().toUpperCase());
            this.viajero.setSexo(cbSexo.getSelectedItem().toString().toUpperCase());
            java.sql.Date fechaNaci = new java.sql.Date(dcFechaNacimiento.getDate().getTime());
            this.viajero.setFecha_nacimiento(fechaNaci);

            accion = Accion.ACEPTAR;
            setVisible(false);
        }
        else {
            JOptionPane.showMessageDialog(null, "Falta algun campo por rellenar");
        }
    }
    public void Cancelar(){
        accion = Accion.CANCELAR;
        setVisible(false);
    }
    public Accion mostrarDialogo(){
        setVisible(true);
        return  accion;
    }

    public Viajero getViajero() {
        return viajero;
    }
}
