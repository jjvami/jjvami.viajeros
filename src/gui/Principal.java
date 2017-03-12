package gui;

import util.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Juan Jose Vallespin Millan on 09/03/2017.
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

    public static JMenuBar menuBar;
    public JMenu archivo;
    public JMenuItem conectarBd;
    public JMenuItem datosEmpresa;
    public JMenuItem salir;

    public static Database database;

    public Principal(){
        crearJMenuBar();
        database = new Database();

        conectarBd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.conectar();
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registro de viajeros");
        frame.setContentPane(new Principal().panel1);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void crearJMenuBar(){
        menuBar = new JMenuBar();

        archivo = new JMenu("Archivo");
        menuBar.add(archivo);

        conectarBd = new JMenuItem("Configuracion Base de Datos");
        archivo.add(conectarBd);

        datosEmpresa = new JMenuItem("Datos Empresa");
        salir = new JMenuItem("Salir");
        archivo.add(datosEmpresa);
        archivo.add(salir);
    }
}



