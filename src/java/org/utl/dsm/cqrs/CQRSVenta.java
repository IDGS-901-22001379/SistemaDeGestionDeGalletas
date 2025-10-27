package org.utl.dsm.cqrs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.dao.DAOVenta;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.model.Venta;

public class CQRSVenta {
    public int insertarVenta(Venta v, List<DetalleVenta> dv) throws SQLException, IOException, ClassNotFoundException {
        // Validación de la venta
        if (v.getDescripcion() == null || v.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la venta es obligatoria.");
        }
        if (v.getTotal() <= 0) {
            throw new IllegalArgumentException("El total de la venta debe ser un valor positivo.");
        }
        if (v.getTicket() == null || v.getTicket().trim().isEmpty()) {
            throw new IllegalArgumentException("El ticket de la venta es obligatorio.");
        }
        if (v.getTipo_venta() == null || v.getTipo_venta().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de venta es obligatorio.");
        }
        // Validación de los detalles de la venta
        for (DetalleVenta detalle : dv) {
            if (detalle.getGalleta().getId_galleta()<= 0) {
                throw new IllegalArgumentException("El ID de la galleta es inválido en uno de los detalles.");
            }
            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad de galletas debe ser un valor positivo.");
            }
            if (detalle.getSubtotal() <= 0) {
                throw new IllegalArgumentException("El subtotal debe ser un valor positivo.");
            }
        }

        // Insertar la venta (llamar al DAO o al procedimiento)
        DAOVenta daoVenta = new DAOVenta();
        int resultado = daoVenta.insertarVenta(v, dv);

        return resultado;
    }

}
