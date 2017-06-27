package test;

import base.Viajero;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Database;

import javax.xml.crypto.Data;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by juanj on 06/04/2017.
 */
public class DatabaseTest {
    private Database database = null;
    private Viajero viajero = null;
    public DatabaseTest() throws ClassNotFoundException, SQLException, InstantiationException, IOException, IllegalAccessException {
     database = new Database();
    }

    private Viajero rellenarViajero(){
        Viajero v = new Viajero();
        v.setNombre("Pepito");
        v.setApellido1("Bonaparte");
        v.setNacionalidad("ESPAÑA");
        v.setFecha_expedicion(new Date(System.currentTimeMillis()));
        v.setFecha_nacimiento(new Date(System.currentTimeMillis()));
        v.setDocumento("DNI");
        v.setSexo("HOMBRE");
        v.setNumero_documento("12345679");
        v.setVersion(2);

        return v;
    }

    @Test
    public void nuevoViajero() throws Exception {
        int numeroViajerosInicio = database.getViajeros().size();
        prepararTest();
        int numeroViajerosFin = database.getViajeros().size();
        assertEquals(numeroViajerosInicio +1 , numeroViajerosFin);
        limpiarTest();
    }


    @Test
    public void getViajero() throws Exception {
        prepararTest();
        Viajero viajerobd = database.getViajeroDni(viajero.getNumero_documento());

        assertEquals(viajero.getNombre(), viajerobd.getNombre());
        assertEquals(viajero.getApellido1(), viajerobd.getApellido1());
        assertEquals(viajero.getApellido2(), viajerobd.getApellido2());
        assertEquals(viajero.getDocumento(), viajerobd.getDocumento());
        assertEquals(viajero.getNumero_documento(), viajerobd.getNumero_documento());
        assertEquals(viajero.getSexo(), viajerobd.getSexo());
        assertEquals(viajero.getNacionalidad(), viajerobd.getNacionalidad());

        limpiarTest();
    }


    private void prepararTest() throws SQLException {
        viajero = rellenarViajero();
        database.nuevoViajero(viajero);
    }

    private void limpiarTest() throws SQLException {
        viajero = rellenarViajero();
        int id = database.getViajeroDni(viajero.getNumero_documento()).getId();
        database.eliminarViajero(id);
    }

    @Test
    public void modificarVIajero() throws Exception {
        prepararTest();

        String nuevoNombre =  "Julian";
        String nuevoApellido =  "PEREZ";
        int id = database.getViajeroDni(viajero.getNumero_documento()).getId();

        Viajero viajeroModificado = rellenarViajero();
        viajeroModificado.setNombre(nuevoNombre);
        viajeroModificado.setApellido1(nuevoApellido);
        viajeroModificado.setId(id);

        database.modificarVIajero(id, viajeroModificado);

        assertFalse(viajero.getNombre().equals(viajeroModificado.getNombre()));
        assertFalse(viajero.getApellido1().equals(viajeroModificado.getApellido1()));
        assertEquals(nuevoNombre, viajeroModificado.getNombre());
        assertEquals(nuevoApellido, viajeroModificado.getApellido1());
        assertEquals(viajero.getApellido2(), viajeroModificado.getApellido2());
        assertEquals(viajero.getDocumento(), viajeroModificado.getDocumento());
        assertEquals(viajero.getNumero_documento(), viajeroModificado.getNumero_documento());
        assertEquals(viajero.getSexo(), viajeroModificado.getSexo());
        assertEquals(viajero.getNacionalidad(), viajeroModificado.getNacionalidad());

        limpiarTest();
    }

    @Test
    public void eliminarViajero() throws Exception {
        int numeroViajerosInicio = database.getViajeros().size();
        prepararTest();
        limpiarTest();
        int numeroViajerosFin = database.getViajeros().size();
        assertEquals(numeroViajerosInicio , numeroViajerosFin);
    }

    @Test
    public void busquedaNombre() throws Exception {
        prepararTest();

        ArrayList<Viajero> viajerosBusqueda = database.busquedaNombre(viajero.getNombre());

        assertTrue(viajerosBusqueda.size() > 0);
        viajerosBusqueda.forEach(viajeroLista -> {
                    assertEquals(viajeroLista.getNombre(), viajero.getNombre());
                });

        limpiarTest();
    }

    @Test
    public void busquedaNDocumento() throws Exception {
        prepararTest();

        ArrayList<Viajero> viajerosBusqueda = database.busquedaNDocumento(viajero.getNumero_documento());

        assertTrue(viajerosBusqueda.size() > 0);
        viajerosBusqueda.forEach(viajeroLista -> {
            assertEquals(viajeroLista.getNumero_documento(), viajero.getNumero_documento());
        });

        limpiarTest();
    }
}