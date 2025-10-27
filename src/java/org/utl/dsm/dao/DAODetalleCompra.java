package org.utl.dsm.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.Compra;
import org.utl.dsm.model.DetalleCompra;
import org.utl.dsm.model.Proveedor;

public class DAODetalleCompra {
    public List<DetalleCompra> obtenerTodasLosDetallesCompra() throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<DetalleCompra> detalleCompra = new ArrayList<>();

        try {
            conn = connMysql.abrirConexion();

            String consulta = "SELECT * FROM VistaDetalleCompras";
            stmt = conn.createStatement();

            rs = stmt.executeQuery(consulta);

            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId_proveedor(rs.getInt("id_proveedor"));
                p.setNombreProveedor(rs.getString("nombreProveedor"));
                
                Compra compra = new Compra();
                compra.setId_comprasRealizadas(rs.getInt("id_comprasRealizadas"));
                compra.setFecha(rs.getDate("fecha"));
                compra.setPrecio(rs.getFloat("precio"));
                compra.setNumeroOrden(rs.getString("numeroOrden"));
                compra.setEstatus(rs.getInt("estatus"));
                compra.setCantidad(rs.getInt("cantidad"));               
                compra.setPeso(rs.getFloat("peso"));
                compra.setProveedor(p);
                
                DetalleCompra dc = new DetalleCompra();
                dc.setId_detalleCompra(rs.getInt("id_detalleCompra"));
                dc.setDescripcion(rs.getString("descripcion"));
                dc.setCompra(compra);

                detalleCompra.add(dc);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            connMysql.cerrarConexion(conn);
        }

        
        return detalleCompra;
    }
}
