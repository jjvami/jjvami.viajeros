package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by juanjo on 29/12/2014.
 */
public class ConectarBD extends JDialog {
    private JPanel panel1;
    private JTextField conectarBDHost;
    private JTextField conectarBDUsuario;
    private JTextField conectarBDPassword;
    private JComboBox conectarBDCbBaseDatos;
    private JButton conectarBDBtAceptar;
    private JButton concectarBDBtCancelar;

    private String host;
    private String usuario;
    private String password;
    private String combo;
    public Connection conexion;
    public enum Accion{
        ACEPTAR, CANCELAR
    }

    private Accion accion;

    public ConectarBD(){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        if (rellenarTF()){
            rellenarTF();
        }

        conexion=null;


        conectarBDBtAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }

        });

        conectarBDCbBaseDatos.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                listarCombos();
            }
        });
        concectarBDBtCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    public void listarCombos(){
        if (conectarBDCbBaseDatos.getItemCount()>0){
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + conectarBDHost.getText() + ":3306"
                    , conectarBDUsuario.getText(), String.valueOf(conectarBDPassword.getText()));

            ResultSet combo = conexion.getMetaData().getCatalogs();
            while (combo.next()){
                conectarBDCbBaseDatos.addItem(combo.getString(1));
            }
            combo.close();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private boolean rellenarTF(){
        Properties configuracion = new Properties();
        try {
            configuracion.load(new FileInputStream("confifguracionBD.properties"));
            conectarBDHost.setText(String.valueOf(configuracion.get("servidor")));
            conectarBDUsuario.setText(String.valueOf(configuracion.getProperty("usuario")));
            conectarBDPassword.setText(String.valueOf(configuracion.getProperty("contrasena")));
            conectarBDCbBaseDatos.setSelectedItem(String.valueOf(configuracion.getProperty("bd")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void aceptar(){
        host = conectarBDHost.getText();
        usuario = conectarBDUsuario.getText();
        password = conectarBDPassword.getText();
        combo = (String) conectarBDCbBaseDatos.getSelectedItem();
        accion = Accion.ACEPTAR;
        setVisible(false);
    }
    public void cancelar(){
        accion = Accion.CANCELAR;
        setVisible(false);
    }

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCombo() {
        return combo;
    }
    public void setCombo(String combo) {
        this.combo = combo;
    }
    public Accion mostrarDialogo(){
        setVisible(true);
        return  accion;
    }
}
