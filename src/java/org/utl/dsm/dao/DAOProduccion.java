package org.utl.dsm.dao;

import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.Galleta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOProduccion {

    public List<Galleta> obtenerStock() throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();
        
        String query = "SELECT id_galleta, tipo, existencia FROM galletas";
        List<Galleta> stock = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Galleta galleta = new Galleta();
                galleta.setId_galleta(rs.getInt("id_galleta"));
                galleta.setTipo(rs.getString("tipo"));
                galleta.setExistencia(rs.getInt("existencia"));
                stock.add(galleta);
            }
        }
        return stock;
    }

    public void sumarStock(int idGalleta, int cantidad) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();
        
        String query = "UPDATE galletas SET existencia = existencia + ? WHERE id_galleta = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cantidad);
            stmt.setInt(2, idGalleta);
            stmt.executeUpdate();
        }
    }

    public void registrarMermaGalletas(int idGalleta, int cantidad, String descripcion) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();
        
        String sqlInsert = "INSERT INTO mermas_galletas (id_galleta, cantidad, descripcion) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE galletas SET existencia = existencia - ? WHERE id_galleta = ?";

        try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);  PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

            conn.setAutoCommit(false); // Inicia transacción

            // Inserta la merma
            pstmtInsert.setInt(1, idGalleta);
            pstmtInsert.setInt(2, cantidad);
            pstmtInsert.setString(3, descripcion);
            pstmtInsert.executeUpdate();

            // Actualiza el stock
            pstmtUpdate.setInt(1, cantidad);
            pstmtUpdate.setInt(2, idGalleta);
            pstmtUpdate.executeUpdate();

            conn.commit(); // Confirma la transacción
        } catch (SQLException e) {
            throw new SQLException("Error al registrar merma de galletas: " + e.getMessage(), e);
        }
    }

    public void registrarMermaInsumos(int idInsumo, int cantidad, String descripcion) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();
        
        String sqlInsertMerma = "INSERT INTO mermas_insumos (id_insumo, cantidad, descripcion, fecha) VALUES (?, ?, ?, CURDATE())";
        String sqlUpdateStock = "UPDATE insumos SET cantidad = cantidad - ? WHERE id_insumo = ?";

        try (PreparedStatement pstmtMerma = conn.prepareStatement(sqlInsertMerma);  PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock)) {

            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar la merma
            pstmtMerma.setInt(1, idInsumo);
            pstmtMerma.setInt(2, cantidad);
            pstmtMerma.setString(3, descripcion);
            pstmtMerma.executeUpdate();

            // Actualizar el stock
            pstmtStock.setInt(1, cantidad);
            pstmtStock.setInt(2, idInsumo);
            pstmtStock.executeUpdate();

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            throw new SQLException("Error al registrar la merma de insumos: " + e.getMessage(), e);
        }
    }
}
