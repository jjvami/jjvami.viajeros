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
 * Clase donde se gestiona la BD.
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

    /**
     * Si hay archivo de configuracion se autoconecta a la BD.
     */
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

    /**
     * Conecta con la BD
     */
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

    /**
     * Guarda loss datos de configuracion de la BD en un archivo, si el archivo no existe lo crea y si no lo sobreescribe.
     */
    private void guardarConfiguracion(){
        Properties configuracion = new Properties();
        configuracion.setProperty("servidor", this.host);
        configuracion.setProperty("bd", this.combo);
        configuracion.setProperty("usuario", this.usuario);
        configuracion.setProperty("contrasena", this.password);
        try {
            configuracion.store(new FileOutputStream("confifguracionBD.properties"), "Archivo de configuracion");
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Error, no se a encontrado el archivo de configuracion");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la configuracion de la bd a partir del fichero de configuracion, si todo va bien retorna true.
     * @return
     */
    private boolean cargarConfiguracion(){
        Properties configuracion = new Properties();
        try {
            configuracion.load(new FileInputStream("confifguracionBD.properties"));
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

    //VIAJERO

    /**
     * Metodo que guarda un nuevo viajero en la BD.
     * @param viajero
     * @throws SQLException
     */
    public void nuevoViajero(Viajero viajero) throws SQLException{
        String sentenciaSql= "INSERT INTO " +
                ConstantesViajero.TABLA + " (" +
                ConstantesViajero.NACIONALIDAD + ", " +
                ConstantesViajero.DOCUMENTO + ", " +
                ConstantesViajero.NUMERODOCUMENTO + ", " +
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
        sentencia.setString(3, viajero.getNumero_documento());
        sentencia.setDate(4, new Date(viajero.getFecha_expedicion().getTime()));
        sentencia.setString(5, viajero.getNombre());
        sentencia.setString(6, viajero.getApellido1());
        sentencia.setString(7, viajero.getApellido2());
        sentencia.setString(8, viajero.getSexo());
        sentencia.setDate(9, new Date(viajero.getFecha_nacimiento().getTime()));
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }

    }

    /**
     * Metodo que modifica un viajero en la BD.
     * @param id
     * @param viajero
     * @throws SQLException
     */
    public void modificarVIajero(int id, Viajero viajero) throws SQLException{
        String sentenciaSql= " UPDATE " +
                ConstantesViajero.TABLA +
                " SET " +
                ConstantesViajero.NACIONALIDAD + " =?, " +
                ConstantesViajero.DOCUMENTO + " =?, " +
                ConstantesViajero.NUMERODOCUMENTO + " =?, " +
                ConstantesViajero.FECHAEXPEDICION + " =?, " +
                ConstantesViajero.NOMBRE + " =?, " +
                ConstantesViajero.APELLIDO1 + " =?, " +
                ConstantesViajero.APELLIDO2 + " =?, " +
                ConstantesViajero.SEXO + " =?, " +
                ConstantesViajero.FECHANACIMIENTO  +
                " =? WHERE " +
                ConstantesViajero.ID + " =? ";
        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setString(1, viajero.getNacionalidad());
        sentencia.setString(2, viajero.getDocumento());
        sentencia.setString(3, viajero.getNumero_documento());
        sentencia.setDate(4, new Date(viajero.getFecha_expedicion().getTime()));
        sentencia.setString(5, viajero.getNombre());
        sentencia.setString(6, viajero.getApellido1());
        sentencia.setString(7, viajero.getApellido2());
        sentencia.setString(8, viajero.getSexo());
        sentencia.setDate(9, new Date(viajero.getFecha_nacimiento().getTime()));
        //COMPRARADOR
        sentencia.setInt(10, id);
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }
    }

    /**
     * Metodo que elimina un viajero en la BD.
     * @param id
     * @throws SQLException
     */
    public void eliminarViajero(int id) throws SQLException{
        String sentenciaSql = "DELETE FROM " +
                ConstantesViajero.TABLA +
                " WHERE " +
                ConstantesViajero.ID + " =? ";

        PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
        sentencia.setInt(1, id);
        sentencia.executeUpdate();

        if(sentencia != null){
            sentencia.close();
        }

    }

    /**
     * Metodo que devuelve el viajero correspondiente a la id otrogada.
     * @param id
     * @return
     * @throws SQLException
     */
    public Viajero getViajero(int id) throws SQLException{
        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA + " WHERE " + ConstantesViajero.ID + " = " + id;
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        Viajero viajero = null;
        while (resultado.next()){
            viajero = new Viajero();
            viajero.setId(resultado.getInt(1));
            viajero.setNacionalidad(resultado.getString(2));
            viajero.setDocumento(resultado.getString(3));
            viajero.setNumero_documento(resultado.getString(4));
            viajero.setFecha_expedicion(resultado.getDate(5));
            viajero.setNombre(resultado.getString(6));
            viajero.setApellido1(resultado.getString(7));
            viajero.setApellido2(resultado.getString(8));
            viajero.setSexo(resultado.getString(9));
            viajero.setFecha_nacimiento(resultado.getDate(10));
        }
        return  viajero;
    }

    /**
     * Devuelve una arraylist de viajeros con todos los que hay en la BD.
     * @return
     * @throws SQLException
     */
    public ArrayList<Viajero> getViajeros() throws SQLException{
        ArrayList <Viajero> viajeros;

        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA;
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        viajeros = crearListaViajero(resultado);

        if(sentencia != null){
            sentencia.close();
        }
        return viajeros;
    }

    /**
     * Devuelve un arraylist de viajeros a parti del resultado de una sentencia sql
     * @param resultado
     * @return
     * @throws SQLException
     */
    private ArrayList<Viajero> crearListaViajero(ResultSet resultado) throws SQLException{
        ArrayList<Viajero> viajeros = new ArrayList<>();

        Viajero viajero = null;
        while (resultado.next()){
            viajero = new Viajero();
            viajero.setId(resultado.getInt(1));
            viajero.setNacionalidad(resultado.getString(2));
            viajero.setDocumento(resultado.getString(3));
            viajero.setNumero_documento(resultado.getString(4));
            viajero.setFecha_expedicion(resultado.getDate(5));
            viajero.setNombre(resultado.getString(6));
            viajero.setApellido1(resultado.getString(7));
            viajero.setApellido2(resultado.getString(8));
            viajero.setSexo(resultado.getString(9));
            viajero.setFecha_nacimiento(resultado.getDate(10));
            viajeros.add(viajero);
        }
        return viajeros;
    }

    /**
     * Busca en en la BD por nombre y devuelve un array con los viajeros que coinciden en la busqueda.
     * @param nombre
     * @return un array de viajeros
     * @throws SQLException
     */
    public ArrayList<Viajero> busquedaNombre(String nombre) throws SQLException{
        ArrayList <Viajero> viajeros;

        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA + " WHERE nombre LIKE '" + nombre + "%'";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        viajeros = crearListaViajero(resultado);

        if(sentencia != null){
            sentencia.close();
        }
        return viajeros;
    }

    /**
     * Busca en en la BD por numero de documento y devuelve un array con los viajeros que coinciden en la busqueda.
     * @param documento
     * @return un array de viajeros
     * @throws SQLException
     */
    public ArrayList<Viajero> busquedaNDocumento(String documento) throws SQLException{
        ArrayList <Viajero> viajeros;

        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA + " WHERE numero_documento LIKE '" + documento + "%'";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        viajeros = crearListaViajero(resultado);

        if(sentencia != null){
            sentencia.close();
        }
        return viajeros;
    }

    /**
     * Devuelve todos los paises que hay guardados en la BD.
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getPaises() throws SQLException {
        String consulta = "SELECT * FROM paises";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();
        ArrayList<String> paises = new ArrayList<>();
        while (resultado.next()){
            paises.add(resultado.getString(3).toUpperCase());
        }
        return paises;
    }
}
