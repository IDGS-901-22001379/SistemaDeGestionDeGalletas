package org.utl.dsm.cqrs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.dao.DAOInsumo;
import org.utl.dsm.model.AlertaInsumo;
import org.utl.dsm.model.Insumo;

/**
 *
 * @author ascen
 */
public class CQRSInsumo {
    private DAOInsumo insumoDAO;

    public CQRSInsumo() {
        insumoDAO = new DAOInsumo();
    }
//Merma
public void MermaInsumo(Insumo insumo) throws Exception {
        validarID(insumo.getId_insumo());
        validarCantidad(insumo.getCantidad());
        insumoDAO.actualizarInsumoMerma(insumo.getId_insumo(), insumo.getCantidad());
    }

    private void validarID(int id_insumo) throws Exception {
        if (id_insumo <= 0) {
            throw new Exception("El id del insumo debe ser un número entero positivo.");
        }
        if (id_insumo > 1000) {
            throw new Exception("El id del insumo no puede ser mayor a 1,000.");
        }
    }

    private void validarCantidad(double cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser un número positivo.");
        }
        if (cantidad > 1000) {
            throw new Exception("La cantidad no puede ser mayor a 1,000.");
        }
    }
// /////////////////////////////////////////////////////////////////////////////
//Al hacer la busqueda del orden de compra
public String buscarDescripcionCompra(String numeroOrden) throws Exception {
        // Validar el número de orden antes de buscar
        validarNumeroOrden(numeroOrden);

        // Llamada al DAO para buscar la descripción
        String descripcion = insumoDAO.buscarDescripcionPorNumeroOrden(numeroOrden);
        if (descripcion == null) {
            throw new Exception("No se encontró ninguna compra con el número de orden proporcionado.");
        }

        return descripcion; // Retornar la descripción
    }

    private void validarNumeroOrden(String numeroOrden) throws Exception {
        if (numeroOrden == null || numeroOrden.trim().isEmpty()) {
            throw new Exception("El número de orden no puede estar vacío.");
        }
    }
// /////////////////////////////////////////////////////////////////////////////
    // Para el update del procedimiento almacenado
        public void actualizarEstatusCompra(String numeroOrden) throws Exception {
    // Validación de entrada
    validarNumeroOrde(numeroOrden);

    try {
        // Llamada a la capa de acceso a datos (DAO) para actualizar el estatus de la compra
        insumoDAO.actualizarEstatusCompra(numeroOrden);
    } catch (SQLException e) {
        // Verificamos si el error es por la compra ya registrada (error personalizado en el procedimiento almacenado)
        if (e.getSQLState().equals("45000")) {
            throw new Exception("La compra ya está registrada. " + e.getMessage());
        }
        // Manejo de otros errores relacionados con la base de datos
        throw new Exception("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
    } catch (ClassNotFoundException | IOException e) {
        // Excepciones generales relacionadas con la conexión o el acceso a la base de datos
        throw new Exception("Error de conexión o clase no encontrada: " + e.getMessage());
    }
}

private void validarNumeroOrde(String numeroOrden) throws Exception {
    if (numeroOrden == null || numeroOrden.trim().isEmpty()) {
        throw new Exception("El número de orden no puede estar vacío.");
    }
}

// /////////////////////////////////////////////////////////////////////////////
    //Alertas
    public List<AlertaInsumo> verificarAlertasInsumos() throws Exception {
        List<Insumo> insumos = insumoDAO.obtenerTodosInsumos();
        List<AlertaInsumo> alertas = new ArrayList<>();

        // Verificamos todos los insumos y generamos alertas
        for (Insumo insumo : insumos) {
            double totalInsumo = insumo.getTotal();
            String nombreInsumo = insumo.getNombreInsumo();

            if (totalInsumo <= 5) {
                alertas.add(new AlertaInsumo(nombreInsumo, "El insumo " + nombreInsumo + " tiene que resurtirse."));
            } else if (totalInsumo > 5 && totalInsumo <= 10) {
                alertas.add(new AlertaInsumo(nombreInsumo, "El insumo " + nombreInsumo + " está a punto de acabarse."));
            }
        }

        return alertas;
    }

}