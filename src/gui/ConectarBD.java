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
 * Clase en la que se encarga del dialogo para conectar con la base de datos.
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

    /**
     * Metodo en el que relleno el comboBox con las BD que tiene acceso el usario de la BD.
     */
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

    /**
     * //Metodo en el que en casa de que ya tengamos una BD configurada y la queramos cambiar, rellenara los campos con la informcaion de la BD actual.
     * @return
     */
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

    /**
     * Metodo que cierra el dialogo y rellena las variables.
     */
    public void aceptar(){
        host = conectarBDHost.getText();
        usuario = conectarBDUsuario.getText();
        password = conectarBDPassword.getText();
        combo = (String) conectarBDCbBaseDatos.getSelectedItem();

        accion = Accion.ACEPTAR;
        setVisible(false);
    }

    /**
     * Metodo que cierra el dialogo.
     */
    public void cancelar(){
        accion = Accion.CANCELAR;
        setVisible(false);
    }

    /**
     * Metodo para saber la accion final del dialogo(Aceptar o cancelar).
     * @return
     */
    public Accion mostrarDialogo(){
        setVisible(true);
        return  accion;
    }

    // Getters y Setters
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
}
