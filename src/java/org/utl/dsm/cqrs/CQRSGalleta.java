package org.utl.dsm.cqrs;

import java.io.IOException;
import java.sql.SQLException;
import org.utl.dsm.dao.DAOGalletas;

public class CQRSGalleta {

    public void disminuirCantidadPorGalleta(String nombreGalleta, int cantidad) throws SQLException, IOException, ClassNotFoundException {
        if (nombreGalleta == null || nombreGalleta.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la galleta no puede estar vacío.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        
        DAOGalletas daog = new DAOGalletas();
        daog.disminuirCantidadPorGalleta(nombreGalleta, cantidad);
    }
}
