package org.utl.dsm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.appservice.VentaExternaAppService;
import org.utl.dsm.cqrs.CQRSVenta;
import org.utl.dsm.dao.DAOVenta;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.model.Venta;
import org.utl.dsm.viewmodel.VentaViewModel;

public class ControllerVenta {
    public int insertarVenta(Venta venta, List<DetalleVenta> dv) throws IllegalArgumentException, SQLException {
        try {
            CQRSVenta cqrs = new CQRSVenta();
            return cqrs.insertarVenta(venta, dv);
        } catch (IOException | ClassNotFoundException ex) {
            return -1;
        }
    }
    public List<Venta> getAllVentas() throws SQLException, ClassNotFoundException, IOException {
        DAOVenta daov = new DAOVenta();
        List<Venta> listaVentas = daov.getAll();

        return listaVentas;
    }
    
    public List<VentaViewModel> getAllVentasPublicos() throws SQLException, ClassNotFoundException, IOException {
        DAOVenta daov = new DAOVenta();
        List<Venta> ventas = daov.getAll();
        List<VentaViewModel> lista = new ArrayList<>();

        for (Venta venta : ventas) {
            VentaViewModel modelo = new VentaViewModel(venta.getId_venta(),
                    venta.getDescripcion(),
                    venta.getTotal(),
                    venta.getFecha(),
                    venta.getHora(),
                    venta.getTicket(),
                    venta.getTipo_venta()
            );
            lista.add(modelo);
        }
        return lista;
    }

    public List<VentaViewModel> getAllVentasPublicosTodos() throws SQLException, ClassNotFoundException, IOException {        
        // Instancia del servicio externo
        VentaExternaAppService veas = new VentaExternaAppService();

        // Obtener los libros externos de la API
        List<VentaViewModel> listaExterna = veas.getAll();

        return listaExterna; // Retornar la lista combinada o solo la lista externa
    }
}
