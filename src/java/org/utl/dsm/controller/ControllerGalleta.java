package org.utl.dsm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.cqrs.CQRSGalleta;
import org.utl.dsm.dao.DAOGalletas;
import org.utl.dsm.model.Galleta;

public class ControllerGalleta {
    public List<Galleta> getAllGalletas() throws SQLException, ClassNotFoundException, IOException {
        DAOGalletas daog = new DAOGalletas();
        List<Galleta> listaGalletas = daog.getAll();

        return listaGalletas;
    }
    
    public void disminuirCantidadPorGalleta(String nombreGalleta, int cantidad) throws SQLException, ClassNotFoundException, IOException {
        CQRSGalleta cqrs = new CQRSGalleta();
        cqrs.disminuirCantidadPorGalleta(nombreGalleta, cantidad);
    }
}
