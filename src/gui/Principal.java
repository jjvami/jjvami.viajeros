package gui;

import base.Empresa;
import base.Viajero;
import util.ActualizarDatos;
import util.ConstantesViajero;
import util.Database;
import util.GenerarFichero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.TableModel;

/**
 * Clase Main del programa.
 */
public class Principal {

    private JPanel panel1;
    private JButton btNuevo;
    private JButton btEditar;
    private JButton btEliminar;
    private JTextField tfBuscar;
    private JTable tableBD;
    private JButton btAnadir;
    private JButton btQuitar;
    private JTable tableFichero;
    private JButton btGenerar;

    public static JMenuBar menuBar;
    public JMenu archivo;
    public JMenuItem conectarBd;
    public JMenuItem datosEmpresa;
    public JMenuItem refrecar;
    public JMenuItem salir;

    public static Database database;
    private Empresa empresa;
    private ActualizarDatos autoActualizar;

    private static DefaultTableModel modeloTablaViajeros;
    private static DefaultTableModel modeloTablaRegistro;

    /**
     * Metodo de carga de la clase y donde estan los listeners.
     */

    public Principal(){
        crearJMenuBar();
        autoActualizar = new ActualizarDatos();
        database = new Database();
        empresa = new Empresa();
        inicializarTablas();
        autoActualizar.execute();

        conectarBd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.conectar();
            }
        });
        datosEmpresa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                datosEmpresa();
            }
        });
        refrecar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rellenarTablas();
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoViajero();
            }
        });
        btEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editiarViajero();
            }
        });
        btAnadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirRegistro();
            }
        });
        btEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarViajero();
            }
        });
        btQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarRegistro();
            }
        });
        btGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFichero();
            }
        });
        tfBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
               buscador();
            }
        });
    }

    /**
     * Metodo que inicializa el menu de navegacion.
     */
    private void crearJMenuBar(){
        menuBar = new JMenuBar();

        archivo = new JMenu("Archivo");
        menuBar.add(archivo);

        conectarBd = new JMenuItem("Configuracion Base de Datos");
        archivo.add(conectarBd);

        refrecar = new JMenuItem("Refrescar");
        archivo.add(refrecar);

        datosEmpresa = new JMenuItem("Datos Empresa");
        salir = new JMenuItem("Salir");
        archivo.add(datosEmpresa);
        archivo.add(salir);
    }

    /**
     * Metodo para inicializar la tablas.
     */
    private void inicializarTablas(){
        modeloTablaRegistro = new DefaultTableModel();
        modeloTablaRegistro.addColumn(ConstantesViajero.ID);
        modeloTablaRegistro.addColumn(ConstantesViajero.NACIONALIDAD);
        modeloTablaRegistro.addColumn(ConstantesViajero.DOCUMENTO);
        modeloTablaRegistro.addColumn(ConstantesViajero.NUMERODOCUMENTO);
        modeloTablaRegistro.addColumn(ConstantesViajero.FECHAEXPEDICION);
        modeloTablaRegistro.addColumn(ConstantesViajero.NOMBRE);
        modeloTablaRegistro.addColumn(ConstantesViajero.APELLIDO1);
        modeloTablaRegistro.addColumn(ConstantesViajero.APELLIDO2);
        modeloTablaRegistro.addColumn(ConstantesViajero.SEXO);
        modeloTablaRegistro.addColumn(ConstantesViajero.FECHANACIMIENTO);
        tableFichero.setModel(modeloTablaRegistro);

        modeloTablaViajeros = new DefaultTableModel();
        modeloTablaViajeros.addColumn(ConstantesViajero.ID);
        modeloTablaViajeros.addColumn(ConstantesViajero.NACIONALIDAD);
        modeloTablaViajeros.addColumn(ConstantesViajero.DOCUMENTO);
        modeloTablaViajeros.addColumn(ConstantesViajero.NUMERODOCUMENTO);
        modeloTablaViajeros.addColumn(ConstantesViajero.FECHAEXPEDICION);
        modeloTablaViajeros.addColumn(ConstantesViajero.NOMBRE);
        modeloTablaViajeros.addColumn(ConstantesViajero.APELLIDO1);
        modeloTablaViajeros.addColumn(ConstantesViajero.APELLIDO2);
        modeloTablaViajeros.addColumn(ConstantesViajero.SEXO);
        modeloTablaViajeros.addColumn(ConstantesViajero.FECHANACIMIENTO);
        tableBD.setModel(modeloTablaViajeros);

        rellenarTablas();
    }

    /**
     * Metodo para actualizar el contenido de las tablas.
     */
    public static void rellenarTablas(){
        List<Viajero> viajeros = null;
        try {
            viajeros = database.getViajeros();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        modeloTablaViajeros.setNumRows(0);
        for (Viajero viajero : viajeros) {
            Object[] fila = new Object[]{
                    viajero.getId(),
                    viajero.getNacionalidad(),
                    viajero.getDocumento(),
                    viajero.getNumero_documento(),
                    String.valueOf(viajero.getFecha_expedicion()),
                    viajero.getNombre(),
                    viajero.getApellido1(),
                    viajero.getApellido2(),
                    viajero.getSexo(),
                    String.valueOf(viajero.getFecha_nacimiento())
            };
            modeloTablaViajeros.addRow(fila);
        }
        modeloTablaRegistro.setNumRows(0);
    }

    /**
     * Metodo donde relleno el objeto de empresa.
     */
    private void datosEmpresa(){
        VentanaEmpresa dialogoEmpresa = new VentanaEmpresa();
        if(dialogoEmpresa.mostrarDialogo() == VentanaEmpresa.Accion.CANCELAR){
            return;
        }
        empresa = dialogoEmpresa.getEmpresa();
    }

    /**
     * Metodo que crea un nuevo viajero en la BD.
     */
    private void nuevoViajero(){
        VentanaViajero dialogoViajero = new VentanaViajero();
        if(dialogoViajero.mostrarDialogo() == VentanaViajero.Accion.CANCELAR){
            return;
        }
        try {
            database.nuevoViajero(dialogoViajero.getViajero());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rellenarTablas();
    }

    /**
     * Metodo que edita un viajero de la BD.
     */
    private void editiarViajero(){

        int row = tableBD.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione un viajero en la tabla viajeros en la base de datos");
        }else{
            Viajero viajero = null;
            try {
                viajero = database.getViajero((Integer) modeloTablaViajeros.getValueAt(row, 0));
                VentanaViajero dialogoViajero = new VentanaViajero(viajero);
                if(dialogoViajero.mostrarDialogo() == VentanaViajero.Accion.CANCELAR){
                    return;
                }
                database.modificarVIajero((Integer) modeloTablaViajeros.getValueAt(row, 0), dialogoViajero.getViajero());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        rellenarTablas();
    }

    /**
     * Metodo que elimina un viajero de la BD.
     */
    private void eliminarViajero(){
        int row = tableBD.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione un viajero en la tabla viajeros en la base de datos");
        }else{
            try {
                database.eliminarViajero((Integer) modeloTablaViajeros.getValueAt(row, 0));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        rellenarTablas();
    }

    /**
     * Metodo que busca viajeros en la BD a partir de su nombre o DNI, y muestra el resultado en una tabla.
     */
    private void buscador(){
        if(tfBuscar.getText().equalsIgnoreCase("")){
            rellenarTablas();
        }else {
            try {
                modeloTablaViajeros.setNumRows(0);
                List<Viajero> viajeros = database.busquedaNombre(tfBuscar.getText().toUpperCase());
                if(viajeros.isEmpty()){
                    viajeros = database.busquedaNDocumento(tfBuscar.getText().toUpperCase());
                }
                for (Viajero viajero : viajeros) {
                    Object[] fila = new Object[]{
                            viajero.getId(),
                            viajero.getNacionalidad(),
                            viajero.getDocumento(),
                            viajero.getNumero_documento(),
                            String.valueOf(viajero.getFecha_expedicion()),
                            viajero.getNombre(),
                            viajero.getApellido1(),
                            viajero.getApellido2(),
                            viajero.getSexo(),
                            String.valueOf(viajero.getFecha_nacimiento())
                    };
                    modeloTablaViajeros.addRow(fila);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Metodo que añade a la tabla de registro un viajero.
     */
    private void anadirRegistro(){
        int row = tableBD.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione un viajero en la tabla viajeros en la base de datos");
        }else{
            Object[] fila = new Object[]{
                    modeloTablaViajeros.getValueAt(row, 0),
                    modeloTablaViajeros.getValueAt(row, 1),
                    modeloTablaViajeros.getValueAt(row, 2),
                    modeloTablaViajeros.getValueAt(row, 3),
                    modeloTablaViajeros.getValueAt(row, 4),
                    modeloTablaViajeros.getValueAt(row, 5),
                    modeloTablaViajeros.getValueAt(row, 6),
                    modeloTablaViajeros.getValueAt(row, 7),
                    modeloTablaViajeros.getValueAt(row, 8),
                    modeloTablaViajeros.getValueAt(row, 9)
            };
            modeloTablaRegistro.addRow(fila);
        }

    }

    /**
     * Metodo que quita de la tabla de registro un viajero.
     */
    private void quitarRegistro(){
        int row = tableFichero.getSelectedRow();
        if(row == -1){
            JOptionPane.showMessageDialog(null, "Selecione un viajero en la tabla registro alta");
        }else{
            modeloTablaRegistro.removeRow(row);
        }
    }

    /**
     * Metodo que genera un fichero con los datos de la empresa y los viajeros de la tabla de registro.
     */
    private void guardarFichero() {
        if (empresa.getNombre() == null || empresa.getCodigo() == null){
            JOptionPane.showMessageDialog(null, "Los datos de la empresa no estan introducidos");
        }else {
            Date date = new Date();
            String fecha = new SimpleDateFormat("yyyyMMdd").format(date);
            String hora = new SimpleDateFormat("hhmm").format(date);
            TableModel tableModel = tableFichero.getModel();
            int cols = tableModel.getColumnCount();
            int rows = tableModel.getRowCount();

            if (rows == 0){
                JOptionPane.showMessageDialog(null, "No hay viajeros en la tabla de registro");
            }else {
                String contenido = "1|" + empresa.getCodigo() + "|" + empresa.getNombre() + "|" + fecha + "|" + hora + "|" + rows + "|\n";
                for(int i=0; i<rows; i++) {
                    contenido += "2|";
                    for(int j=1; j<cols; j++){
                        contenido += tableModel.getValueAt(i,j)+"|";
                    }
                    contenido += "\n";
                }
                new GenerarFichero(contenido);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registro de viajeros");
        frame.setContentPane(new Principal().panel1);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}



