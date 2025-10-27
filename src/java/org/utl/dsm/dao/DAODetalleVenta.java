package org.utl.dsm.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.model.Galleta;
import org.utl.dsm.model.Venta;

public class DAODetalleVenta {
    public List<DetalleVenta> getAll() throws SQLException, ClassNotFoundException, IOException {
        List<DetalleVenta> listaDetalleVentas = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();

        // 1. Crear la sentencia SQL
        String query = "SELECT * FROM vista_detalle_venta;";
        // 2. Se establece la conexión con la BD
        Connection conn = connMySQL.abrirConexion();
        // 3. Se genera el statement para enviar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        // 4. Se prepara un ResultSet para obtener la respuesta de la BD
        ResultSet rs = pstmt.executeQuery();
        // 5. Recorrer el rs y extraer los datos
        while (rs.next()) {
            Galleta g = new Galleta();
            g.setId_galleta(rs.getInt("id_galleta"));
            g.setTipo(rs.getString("tipo_galleta"));
            g.setGalleta(rs.getString("galleta"));
            g.setCosto(rs.getFloat("costo"));
            g.setExistencia(rs.getInt("existencia"));
            g.setFecha(rs.getDate("fecha_galleta"));
            g.setHora(rs.getTime("hora_galleta"));
            
            Venta v = new Venta();
            v.setId_venta(rs.getInt("id_venta"));
            v.setDescripcion(rs.getString("descripcion"));
            v.setTotal(rs.getFloat("total"));
            v.setFecha(rs.getDate("fecha_venta"));
            v.setHora(rs.getTime("hora_venta"));
            v.setTicket(rs.getString("ticket"));
            v.setTipo_venta(rs.getString("tipoVenta"));
            
            DetalleVenta dv = new DetalleVenta();
            dv.setId_detalle_venta(rs.getInt("id_detalleVentaGalletas"));
            dv.setCantidad(rs.getInt("cantidad"));
            dv.setSubtotal(rs.getFloat("subtotal"));
            dv.setGalleta(g);
            dv.setVenta(v);
            
            

            listaDetalleVentas.add(dv);
        }

        // 6. Cerrar todos los objetos
        rs.close();
        pstmt.close();
        conn.close();
        connMySQL.cerrarConexion(conn);

        // 7. Devolver la información
        return listaDetalleVentas;
    }
}
