package org.utl.dsm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.dao.DAODetalleCompra;
import org.utl.dsm.model.DetalleCompra;

public class ControllerDetalleCompra {
    public List<DetalleCompra> getAllDetalleCompras() throws SQLException, ClassNotFoundException, IOException {
        DAODetalleCompra daodc = new DAODetalleCompra();
        List<DetalleCompra> listaDetalleCompra = daodc.obtenerTodasLosDetallesCompra();

        return listaDetalleCompra;
    }
}
