package org.utl.dsm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.appservice.DetalleVentaExternaAppService;
import org.utl.dsm.dao.DAODetalleVenta;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.viewmodel.DetalleVentaViewModel;

public class ControllerDetalleVenta {
    public List<DetalleVenta> getAllDetalleVentas() throws SQLException, ClassNotFoundException, IOException {
        DAODetalleVenta daodv = new DAODetalleVenta();
        List<DetalleVenta> listaDetalleVenta = daodv.getAll();

        return listaDetalleVenta;
    }
    
    public List<DetalleVentaViewModel> getAllDetalleVentasPublicos() throws SQLException, ClassNotFoundException, IOException {
        DAODetalleVenta daov = new DAODetalleVenta();
        List<DetalleVenta> ventas = daov.getAll();
        List<DetalleVentaViewModel> lista = new ArrayList<>();

        for (DetalleVenta venta : ventas) {
            DetalleVentaViewModel modelo = new DetalleVentaViewModel(venta.getId_detalle_venta(),
                    venta.getVenta(),
                    venta.getGalleta(),
                    venta.getCantidad(),
                    venta.getSubtotal()
            );
            lista.add(modelo);
        }
        return lista;
    }

    public List<DetalleVentaViewModel> getAllDetallesVentasPublicosTodos() throws SQLException, ClassNotFoundException, IOException {        
        // Instancia del servicio externo
        DetalleVentaExternaAppService veas = new DetalleVentaExternaAppService();

        // Obtener los libros externos de la API
        List<DetalleVentaViewModel> listaExterna = veas.getAll();

        return listaExterna; // Retornar la lista combinada o solo la lista externa
    }
}
