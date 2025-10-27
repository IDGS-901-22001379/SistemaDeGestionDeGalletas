package org.utl.dsm.dao;

import com.mysql.cj.jdbc.CallableStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.Insumo;
import org.utl.dsm.model.Proveedor;

/**
 *
 * @author ascen
 */
public class DAOInsumo {

    //Ver todo
    public List<Insumo> getAllInsumos() throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();

        String query = "SELECT * FROM vista_insumos";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<Insumo> insumos = new ArrayList<>();
        while (rs.next()) {
            Insumo insumoObj = new Insumo();

            insumoObj.setId_insumo(rs.getInt("id_insumo"));
            insumoObj.setNombreInsumo(rs.getString("nombreInsumo"));
            insumoObj.setUnidad(rs.getString("unidad"));
            insumoObj.setCantidad(rs.getInt("cantidad"));
            insumoObj.setTotal(rs.getDouble("total"));
            insumoObj.setFecha(rs.getDate("fecha"));

            Proveedor p = new Proveedor();
            p.setId_proveedor(rs.getInt("id_proveedor"));
            p.setNombreProveedor(rs.getString("nombreProveedor"));

            insumoObj.setProveedor(p);
            
            insumos.add(insumoObj);

        }
        rs.close();
        pstmt.close();
        conn.close();
        connMysql.cerrarConexion(conn);
        return insumos;
    }

    //Merma (actualizar la cantidad :v)
    public void actualizarInsumoMerma(int idInsumo, double totalDescuento) throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();
        
        String query = "{CALL actualizarTotalInsumoMerma(?, ?)}";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idInsumo);
            pstmt.setDouble(2, totalDescuento);
            pstmt.executeUpdate();
            System.out.println("Insumo actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el insumo: " + e.getMessage());
            throw e;
        } finally {
            connMysql.cerrarConexion(conn);
        }
    }

//Agregar insumos a travez del orden de Compra
    public void actualizarEstatusCompra(String numeroOrden) throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();

        String query = "{CALL ActualizarEstatusCompra(?)}";
        CallableStatement cstmt = (CallableStatement) conn.prepareCall(query);

        try {
            // Configurar el parámetro de entrada
            cstmt.setString(1, numeroOrden);

            // Ejecutar el procedimiento almacenado
            cstmt.execute();
            System.out.println("Estatus de la compra actualizado correctamente.");

        } catch (SQLException e) {
            // Verifica el código de error y muestra un mensaje adecuado
            if (e.getSQLState().equals("45000")) {
                System.err.println("Error de base de datos: " + e.getMessage());
            } else {
                System.err.println("Error al ejecutar el procedimiento almacenado: " + e.getMessage());
            }
            throw e;
        } finally {
            // Cerrar recursos
            if (cstmt != null) {
                cstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            connMysql.cerrarConexion(conn);
        }
    }

//Buscar orden de compra y regresar descripcción
    public String buscarDescripcionPorNumeroOrden(String numeroOrden) throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();

        String descripcion = null;

        // Consulta para obtener la descripción de la compra según el número de orden
        String query = "SELECT * FROM vista_compras_detalle WHERE numero_orden = ?";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, numeroOrden);

        try ( ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int estatus = rs.getInt("estatus_compra");
                if (estatus == 1) {
                    return "La compra ya está registrada."; // Si ya está registrada
                }
                descripcion = rs.getString("detalle_compra"); // Obtener la descripción de la compra
            } else {
                return "No se encontró ningún resultado para el número de orden proporcionado."; // No hay resultados
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar la descripción: " + e.getMessage());
            throw e; // Propagar error
        } finally {
            pstmt.close();
            conn.close();
            connMysql.cerrarConexion(conn);
        }

        return descripcion; // Retornar la descripción
    }

    //Alertas
    public List<Insumo> obtenerTodosInsumos() throws SQLException, ClassNotFoundException, IOException {
        ConexionMySQL connMysql = new ConexionMySQL();
        Connection conn = connMysql.abrirConexion();
        List<Insumo> listaInsumos = new ArrayList<>();

        String query = "SELECT id_insumo, nombreInsumo, total FROM insumos";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = null;

        try {
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Insumo insumo = new Insumo();
                insumo.setId_insumo(rs.getInt("id_insumo"));
                insumo.setNombreInsumo(rs.getString("nombreInsumo"));
                insumo.setTotal(rs.getDouble("total"));

                listaInsumos.add(insumo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener insumos: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            pstmt.close();
            conn.close();
            connMysql.cerrarConexion(conn);
        }

        return listaInsumos;
    }

}
