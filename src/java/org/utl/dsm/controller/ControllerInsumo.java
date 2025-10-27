package org.utl.dsm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.cqrs.CQRSInsumo;
import org.utl.dsm.dao.DAOInsumo;
import org.utl.dsm.model.AlertaInsumo;
import org.utl.dsm.model.Insumo;

public class ControllerInsumo {
    
    private DAOInsumo insumoDAO;
    private CQRSInsumo insumoCQRS;

    public ControllerInsumo() {
        insumoDAO = new DAOInsumo();
        insumoCQRS = new CQRSInsumo();
    }
    
        //Todo
    public List<Insumo> obtenerInsumos() {
        List<Insumo> libros = new ArrayList<>();
        try {
            libros = insumoDAO.getAllInsumos();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Error al obtener insumos: " + e.getMessage());
        }
        return libros;
    }
    
        //Merma insumo
public void actualizarInsumoMerma(Insumo insumo) {
        try {
            insumoCQRS.MermaInsumo(insumo);
            System.out.println("Insumo y merma actualizados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el insumo y merma: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Buscar descripción de compra por número de orden
    public String buscarDescripcionCompra(String numeroOrden) {
        try {
            // Delegar la búsqueda a la capa CQRS
            return insumoCQRS.buscarDescripcionCompra(numeroOrden);
        } catch (Exception e) {
            System.err.println("Error al buscar la descripción de la compra: " + e.getMessage());
            return null; // Manejar errores
        }
    }
    
    //Actuazlizar insumos agregando lo que se compro
    public void actualizarEstatusCompra(String numeroOrden) {
    try {
        // Intentar actualizar el estatus de la compra
        insumoCQRS.actualizarEstatusCompra(numeroOrden);
        System.out.println("Insumo y merma actualizados correctamente.");
    } catch (SQLException e) {
        // Capturamos excepciones específicas de SQL
        System.err.println("Error de base de datos al actualizar el insumo y merma: " + e.getMessage());
        // Aquí podrías lanzar una excepción personalizada o manejar la respuesta de otra forma
        throw new RuntimeException("Error al actualizar el insumo y merma. Por favor, intente nuevamente.");
    } catch (IOException e) {
        // Capturamos excepciones de IO
        System.err.println("Error de entrada/salida al actualizar el insumo y merma: " + e.getMessage());
        throw new RuntimeException("Error de entrada/salida al actualizar el insumo. Por favor, intente nuevamente.");
    } catch (Exception e) {
        // Capturamos cualquier otra excepción no anticipada
        System.err.println("Error inesperado al actualizar el insumo y merma: " + e.getMessage());
        throw new RuntimeException("Ocurrió un error inesperado. Por favor, intente nuevamente.");
    }
}

    
    //Alertas
public List<AlertaInsumo> verificarAlertas() {
        try {
            return insumoCQRS.verificarAlertasInsumos();
        } catch (Exception e) {
            System.err.println("Error al verificar las alertas de los insumos: " + e.getMessage());
            return null;
        }
    }
    
}