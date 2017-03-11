package util;

import base.Viajero;
import gui.ConectarBD;


import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Created by juanjo.
 */
public class Database {

    private Connection conexion;
    private String host;
    private String usuario;
    private String password;
    private String combo;

    public Database(){
        cargarConfiguracion();
        if(host == null){
            conectar();
        }else{
            autoConectar();
        }
    }

    private void autoConectar(){
        cargarConfiguracion();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":3306" + "/" + combo, usuario, password);

        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }
    public void conectar() {
        ConectarBD dialogoConexion = new ConectarBD();
        if (dialogoConexion.mostrarDialogo() == ConectarBD.Accion.CANCELAR){
            return;
        }

        host = dialogoConexion.getHost();
        usuario = dialogoConexion.getUsuario();
        password = dialogoConexion.getPassword();
        combo = dialogoConexion.getCombo();
        guardarConfiguracion();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":3306" + "/" + combo, usuario, password);

        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el driver de la Base de Datos");
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "No se ha podido conectar con la Base de Datos");
            sqle.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }
    private void guardarConfiguracion(){
        Properties configuracion = new Properties();
        configuracion.setProperty("servidor", this.host);
        configuracion.setProperty("bd", this.combo);
        configuracion.setProperty("usuario", this.usuario);
        configuracion.setProperty("contrasena", this.password);
        try {
            configuracion.store(new FileOutputStream("configuracionBD"), "Archivo de configuracion");
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Error, no se a encontrado el archivo de configuracion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean cargarConfiguracion(){
        Properties configuracion = new Properties();
        try {
            configuracion.load(new FileInputStream("configuracionBD"));
            this.host = String.valueOf(configuracion.get("servidor"));
            this.combo = String.valueOf(configuracion.getProperty("bd"));
            this.usuario = String.valueOf(configuracion.getProperty("usuario"));
            this.password = String.valueOf(configuracion.getProperty("contrasena"));
        } catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Archivo de configuracion no encontrado");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public int getId(String nombre, String tabla){
        int id = 0;
        try {
            Statement sentencia = conexion.createStatement();
            id = sentencia.executeUpdate("SELECT id from " + tabla + " WHERE nombre = " + nombre);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public String getNombre(int id, String tabla){
        String nombre = null;
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery("SELECT nombre from " + tabla + " WHERE id = " + id);

            while(resultado.next()){
                nombre = resultado.getString(2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }

    //VIAJERO
    public void nuevoViajero(Viajero viajero) throws SQLException{
        String sentenciaSql= "INSERT INTO " +
                ConstantesViajero.TABLA + " (" +
                ConstantesViajero.NACIONALIDAD + ", " +
                ConstantesViajero.DOCUMENTO + ", " +
                ConstantesViajero.FECHAEXPEDICION + ", " +
                ConstantesViajero.NOMBRE + ", " +
                ConstantesViajero.APELLIDO1 + ", " +
                ConstantesViajero.APELLIDO2 + ", " +
                ConstantesViajero.SEXO + ", " +
                ConstantesViajero.FECHANACIMIENTO  +
                " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, viajero.getNacionalidad());
        sentencia.setString(2, viajero.getDocumento());
        sentencia.setDate(3, new Date(viajero.getFecha_expedicion().getTime()));
        sentencia.setString(4, viajero.getNombre());
        sentencia.setString(5, viajero.getApellido1());
        sentencia.setString(6, viajero.getApellido2());
        sentencia.setString(7, viajero.getSexo());
        sentencia.setDate(8, new Date(viajero.getFecha_nacimiento().getTime()));
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }

    }
    public void modificarVIajero(String nombreOriginal, Viajero viajero) throws SQLException{
        String sentenciaSql= " UPDATE " +
                ConstantesViajero.TABLA +
                " SET " +
                ConstantesViajero.NACIONALIDAD + " =?, " +
                ConstantesViajero.DOCUMENTO + " =?, " +
                ConstantesViajero.FECHAEXPEDICION + " =?, " +
                ConstantesViajero.NOMBRE + " =?, " +
                ConstantesViajero.APELLIDO1 + " =?, " +
                ConstantesViajero.APELLIDO2 + " =?, " +
                ConstantesViajero.SEXO + " =?, " +
                ConstantesViajero.FECHANACIMIENTO  +
                " =? WHERE " +
                ConstantesViajero.NOMBRE + " =? ";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, viajero.getNacionalidad());
        sentencia.setString(2, viajero.getDocumento());
        sentencia.setDate(3, new Date(viajero.getFecha_expedicion().getTime()));
        sentencia.setString(4, viajero.getNombre());
        sentencia.setString(5, viajero.getApellido1());
        sentencia.setString(6, viajero.getApellido2());
        sentencia.setString(7, viajero.getSexo());
        sentencia.setDate(8, new Date(viajero.getFecha_nacimiento().getTime()));
        //COMPRARADOR
        sentencia.setString(9, nombreOriginal);
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }
    }
    public void eliminarViajero(String nombre) throws SQLException{
        String sentenciaSql = "DELETE FROM " +
                ConstantesViajero.TABLA +
                " WHERE " +
                ConstantesViajero.NOMBRE + " =? ";

        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, nombre);
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }

    }
    public ArrayList<Viajero> getViajeros() throws SQLException{
        ArrayList <Viajero> viajeros;

        String consulta = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        consulta = "SELECT * FROM " + ConstantesViajero.TABLA;
        sentencia = conexion.prepareStatement(consulta);
        resultado = sentencia.executeQuery();


        viajeros = crearListaViajero(resultado);

        if(sentencia != null){
            sentencia.close();
        }
        return viajeros;
    }
    public Viajero getViajero(String nombre) throws SQLException{
        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA + " WHERE " +
                ConstantesViajero.NOMBRE + " =? ";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombre);
        ResultSet resultado = sentencia.executeQuery();

        resultado.next();

        Viajero viajero = new Viajero();
        viajero.setId(resultado.getInt(1));
        viajero.setDocumento(resultado.getString(2));
        viajero.setFecha_expedicion(resultado.getDate(3));
        viajero.setApellido1(resultado.getString(4));
        viajero.setApellido2(resultado.getString(5));
        viajero.setSexo(resultado.getString(6));
        viajero.setFecha_nacimiento(resultado.getDate(7));

        return viajero;
    }
    private ArrayList<Viajero> crearListaViajero(ResultSet resultado) throws SQLException{
        ArrayList<Viajero> viajeros = new ArrayList<>();

        Viajero viajero = null;
        while (resultado.next()){
            viajero = new Viajero();
            viajero.setId(resultado.getInt(1));
            viajero.setDocumento(resultado.getString(2));
            viajero.setFecha_expedicion(resultado.getDate(3));
            viajero.setApellido1(resultado.getString(4));
            viajero.setApellido2(resultado.getString(5));
            viajero.setSexo(resultado.getString(6));
            viajero.setFecha_nacimiento(resultado.getDate(7));
            viajeros.add(viajero);
        }
        return viajeros;
    }
}
