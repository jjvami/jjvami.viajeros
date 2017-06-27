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

    private Connection conexion = null;
    private String host = null;
    private String usuario = null;
    private String password = null;
    private String combo = null;

    public Database() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        try {
            cargarConfiguracion();
        }catch (IOException e){
            throw (e);
        } finally {
            try {
                if(host == null){
                    conectar();
                }else{
                    autoConectar();
                }
            } catch (SQLException e) {
                throw (e);
            } catch (ClassNotFoundException e) {
                throw (e);
            } catch (InstantiationException e) {
                throw (e);
            } catch (IllegalAccessException e) {
                throw (e);
            } catch (IOException e) {
                throw (e);
            }
        }
    }

    /**
     * Si hay archivo de configuracion se autoconecta a la BD.
     */
    private void autoConectar() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        try {
            cargarConfiguracion();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":3306" + "/" + combo, usuario, password);
        } catch (ClassNotFoundException cnfe) {
            throw (cnfe);
        } catch (SQLException sqle) {
            throw (sqle);
        } catch (InstantiationException ie) {
            throw (ie);
        } catch (IllegalAccessException iae) {
            throw (iae);
        } catch (IOException e) {
            throw (e);
        }
    }

    /**
     * Conecta con la BD
     */
    public void conectar() throws IOException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ConectarBD dialogoConexion = new ConectarBD();
        if (dialogoConexion.mostrarDialogo() == ConectarBD.Accion.CANCELAR){
            return;
        }

        host = dialogoConexion.getHost();
        usuario = dialogoConexion.getUsuario();
        password = dialogoConexion.getPassword();
        combo = dialogoConexion.getCombo();

        try {
            guardarConfiguracion();
        } catch (IOException e) {
            throw(e);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":3306" + "/" + combo, usuario, password);
        } catch (InstantiationException e) {
            throw(e);
        } catch (IllegalAccessException e) {
            throw(e);
        } catch (ClassNotFoundException e) {
            throw(e);
        } catch (SQLException e) {
            throw(e);
        }
    }

    /**
     * Guarda loss datos de configuracion de la BD en un archivo, si el archivo no existe lo crea y si no lo sobreescribe.
     */
    private void guardarConfiguracion() throws IOException{
        Properties configuracion = new Properties();
        configuracion.setProperty("servidor", this.host);
        configuracion.setProperty("bd", this.combo);
        configuracion.setProperty("usuario", this.usuario);
        configuracion.setProperty("contrasena", this.password);
        FileOutputStream fichero = null;

        try {
            configuracion.store(fichero = new FileOutputStream("confifguracionBD.properties"), "Archivo de configuracion");
        } catch (IOException e) {
            throw (e);
        } finally {
            if(fichero != null){
                fichero.close();
            }
        }
    }

    /**
     * Carga la configuracion de la bd a partir del fichero de configuracion.
     */
    private void cargarConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        FileInputStream fichero = null;

        try {
            configuracion.load(fichero = new FileInputStream("confifguracionBD.properties"));
            this.host = String.valueOf(configuracion.get("servidor"));
            this.combo = String.valueOf(configuracion.getProperty("bd"));
            this.usuario = String.valueOf(configuracion.getProperty("usuario"));
            this.password = String.valueOf(configuracion.getProperty("contrasena"));
        } catch (FileNotFoundException e){
            throw (e);
        } catch (IOException e) {
            throw (e);
        } finally {
            if (fichero != null){
                fichero.close();
            }
        }
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
                ConstantesViajero.FECHANACIMIENTO  +  ", " +
                ConstantesViajero.VERSION  +
                " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        sentencia.setInt(10, viajero.getVersion());
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
                ConstantesViajero.FECHANACIMIENTO + " =?, " +
                ConstantesViajero.VERSION  +
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
        sentencia.setInt(10, viajero.getVersion() + 1);
        //COMPRARADOR
        sentencia.setInt(11, id);
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
            viajero.setVersion(resultado.getInt(11));
        }
        return  viajero;
    }

    /**
     * Metodo que devuelve el viajero correspondiente a la id otrogada.
     * @param dni
     * @return
     * @throws SQLException
     */
    public Viajero getViajeroDni(String dni) throws SQLException{
        String consulta = "SELECT * FROM " + ConstantesViajero.TABLA + " WHERE " + ConstantesViajero.NUMERODOCUMENTO + " = " + dni;
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
            viajero.setVersion(resultado.getInt(11));
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
            viajero.setVersion(resultado.getInt(11));
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

    /**
     * Devuelve la version de los datos de un viajero.
     * @param id
     * @return
     * @throws SQLException
     */
    public int getVersionViajero(int id) throws SQLException{
        String consulta = "SELECT "+ ConstantesViajero.VERSION +" FROM " + ConstantesViajero.TABLA + " WHERE " + ConstantesViajero.ID + " = " + id;
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        ResultSet resultado = sentencia.executeQuery();

        int version = -1;
        while (resultado.next()){
            version = resultado.getInt(1);
        }
        return  version;
    }
}
