package org.utl.dsm.cqrs;

import org.utl.dsm.model.Compra;
import java.util.List;
import org.utl.dsm.dao.DAOCompra;

public class CQRSCompra {

    private DAOCompra compraDAO;

    public CQRSCompra() {
        compraDAO = new DAOCompra(); 
    }

    // Cambié el nombre del método para evitar confusión con el método de ControllerCompra
    public int agregarCompraConValidacion(Compra compra, String insumosJson) throws Exception {
        // Realiza las validaciones
        validarCompra(compra);

        // Llama a compraDAO para insertar la compra
        return compraDAO.insertarCompra(compra, insumosJson);
    }

    private void validarCompra(Compra compra) throws Exception {
        if (compra == null) {
            throw new Exception("La compra no puede ser nula.");
        }
        
        if (compra.getProveedor() == null) {
            throw new Exception("El proveedor de la compra es obligatorio.");
        }

        if (compra.getCantidad() <= 0) {
            throw new Exception("La cantidad de los insumos debe ser mayor que cero.");
        }

        if (compra.getPrecio() <= 0) {
            throw new Exception("El precio de la compra debe ser mayor que cero.");
        }

        if (compra.getFecha() == null) {
            throw new Exception("La fecha de la compra es obligatoria.");
        }

        if (compra.getEstatus() < 0 || compra.getEstatus() > 1) {
            throw new Exception("El estatus de la compra no es válido.");
        }
    }
    
    // Los métodos de validación siguen siendo útiles y no necesitan cambios.
    private void validarCantidad(int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser un número entero positivo.");
        }
        if (cantidad > 1000) {
            throw new Exception("La cantidad no puede ser mayor a 1,000.");
        }
    }

    private void validarPeso(float peso) throws Exception {
        if (peso <= 0) {
            throw new Exception("El peso debe ser un valor mayor a cero.");
        }

        String pesoString = String.valueOf(peso);

        if (!pesoString.matches("^\\d+(\\.\\d{1,2})?$")) {
            throw new Exception("El peso debe ser un número válido con hasta dos decimales.");
        }
    }

    private void validarPrecio(float precio) throws Exception {
        if (precio <= 0) {
            throw new Exception("El precio debe ser un valor mayor a cero.");
        }

        String precioString = String.valueOf(precio);

        if (!precioString.matches("^\\d+(\\.\\d{1,2})?$")) {
            throw new Exception("El precio debe ser un número válido con hasta dos decimales.");
        }
    }
}
