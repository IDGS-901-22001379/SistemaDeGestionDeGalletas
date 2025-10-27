package org.utl.dsm.dao;

import java.io.IOException;
import org.utl.dsm.model.Compra;
import org.utl.dsm.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.bd.ConexionMySQL;
import java.util.Set;
import java.util.HashSet;
import org.utl.dsm.model.Insumo;

public class DAOCompra {
    
    public List<Compra> obtenerTodasLasCompras() throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Compra> compras = new ArrayList<>();

        try {
            conn = connMysql.abrirConexion();

            String consulta = "SELECT * FROM VistaComprasHistorial";
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

                compras.add(compra);
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

        
        return compras;
    }
    
    //Lista de proveedores para el select
    public List<Proveedor> getAllProveedores() throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();

        String query = "SELECT * FROM VistaProveedor";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Proveedor> proveedores = new ArrayList<>();
        while (rs.next()) {
            int id_proveedor = rs.getInt("id_proveedor");
            String nombreProveedor = rs.getString("nombreProveedor");

            Proveedor proveedorObj = new Proveedor(id_proveedor, nombreProveedor);
            proveedores.add(proveedorObj);
        }
        rs.close();
        pstmt.close();
        conn.close();
        connMysql.cerrarConexion(conn);
        return proveedores;
    }

    //Lista de insumos dependiendo del ID de proveedor
    //Si el ID es 1 o 2 salen sus insumos, si el ID es 3 o mayor salen todos los insumos disponibles
    public List<Insumo> getInsumosByProveedor(int idProveedor) throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();

        String query;
        PreparedStatement pstmt;
        //Para proveedores fijos
        if (idProveedor == 1 || idProveedor == 2) {
            query = "SELECT i.id_insumo, i.nombreInsumo "
                    + "FROM insumos i "
                    + "WHERE i.id_proveedor = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idProveedor);
        } else {
            //Para proveedores temporales
            query = "SELECT i.id_insumo, i.nombreInsumo "
                    + "FROM insumos i";
            pstmt = conn.prepareStatement(query);
        }

        ResultSet rs = pstmt.executeQuery();
        Set<String> uniqueInsumos = new HashSet<>();  //Set para evitar duplicados
        List<Insumo> insumos = new ArrayList<>();

        while (rs.next()) {
            String nombreInsumo = rs.getString("nombreInsumo");

            //Solo agregar el insumo si no se repite
            if (!uniqueInsumos.contains(nombreInsumo)) {
                uniqueInsumos.add(nombreInsumo);

                Insumo insumo = new Insumo();
                insumo.setNombreInsumo(nombreInsumo);

                int id_insumo = rs.getInt("id_insumo");
                insumo.setId_insumo(id_insumo);

                insumos.add(insumo);
            }
        }

        rs.close();
        pstmt.close();
        conn.close();
        connMysql.cerrarConexion(conn);
        return insumos;
    }

    //Agregar todo el json para mandarlos al procedimeinto almacenado
    //Insertar
    public static int insertarCompra(Compra compra, String insumosJson) throws ClassNotFoundException {
        String query = "{call RegistrarCompras(?,?,?)}";
        ConexionMySQL conMysql = new ConexionMySQL();
        Connection conn = null;
        CallableStatement cstm = null;

        try {
            conn = conMysql.abrirConexion();
            conn.setAutoCommit(false);

            cstm = conn.prepareCall(query);

            cstm.setInt(1, compra.getProveedor().getId_proveedor());
            cstm.setDate(2, new java.sql.Date(compra.getFecha().getTime()));
            cstm.setString(3, insumosJson);

            cstm.executeUpdate();

            conn.commit();
            System.out.println("Compra registrada correctamente.");

            return 1; // Indica éxito
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            System.out.println("Error al registrar la compra: " + e.getMessage());
            return 0; // Indica fallo
        } finally {
            try {
                if (cstm != null) {
                    cstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
