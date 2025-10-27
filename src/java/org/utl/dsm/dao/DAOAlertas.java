package org.utl.dsm.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.AlertasInventario;

/**
 * DAO para manejar las operaciones relacionadas con la tabla de alertas de inventario.
 */
public class DAOAlertas {

    private static final Logger LOGGER = Logger.getLogger(DAOAlertas.class.getName());

    // Crear una nueva alerta
    public String crearAlerta(AlertasInventario alerta) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "INSERT INTO alertas_inventario (galleta_id, lote_id, tipo_alerta, descripcion, fecha_alerta, atendida) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alerta.getGalletaId());
            stmt.setInt(2, alerta.getLoteId());
            stmt.setString(3, alerta.getTipoAlerta());
            stmt.setString(4, alerta.getDescripcion());
            stmt.setDate(5, Date.valueOf(alerta.getFechaAlerta()));
            stmt.setBoolean(6, alerta.isAtendida());

            stmt.executeUpdate();
            LOGGER.log(Level.INFO, "Alerta creada correctamente: {0}", alerta.toString());
            return "Alerta creada correctamente.";
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear la alerta.", e);
            throw e;
        }
    }

    // Obtener todas las alertas
    public List<AlertasInventario> obtenerTodasLasAlertas() throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "SELECT * FROM alertas_inventario";
        List<AlertasInventario> alertas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AlertasInventario alerta = llenarAlerta(rs);
                alertas.add(alerta);
            }

            LOGGER.log(Level.INFO, "Total de alertas obtenidas: {0}", alertas.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las alertas.", e);
            throw e;
        }

        return alertas;
    }

    // Obtener alertas por tipo
    public List<AlertasInventario> obtenerAlertasPorTipo(String tipoAlerta) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "SELECT * FROM alertas_inventario WHERE tipo_alerta = ?";
        List<AlertasInventario> alertas = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipoAlerta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AlertasInventario alerta = llenarAlerta(rs);
                    alertas.add(alerta);
                }
            }

            LOGGER.log(Level.INFO, "Total de alertas de tipo {0} obtenidas: {1}", new Object[]{tipoAlerta, alertas.size()});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener alertas por tipo.", e);
            throw e;
        }

        return alertas;
    }

    // Marcar una alerta como atendida
    public String marcarAlertaComoAtendida(int idAlerta) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "UPDATE alertas_inventario SET atendida = TRUE WHERE id_alerta = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAlerta);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Alerta con ID {0} marcada como atendida.", idAlerta);
                return "Alerta marcada como atendida.";
            } else {
                return "No se encontró la alerta con el ID especificado.";
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al marcar la alerta como atendida.", e);
            throw e;
        }
    }

    // Eliminar una alerta
    public String eliminarAlerta(int idAlerta) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "DELETE FROM alertas_inventario WHERE id_alerta = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAlerta);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Alerta con ID {0} eliminada correctamente.", idAlerta);
                return "Alerta eliminada correctamente.";
            } else {
                return "No se encontró la alerta con el ID especificado.";
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la alerta.", e);
            throw e;
        }
    }

    // Método privado para llenar un objeto AlertasInventario desde un ResultSet
    private AlertasInventario llenarAlerta(ResultSet rs) throws SQLException {
        AlertasInventario alerta = new AlertasInventario();
        alerta.setIdAlerta(rs.getInt("id_alerta"));
        alerta.setGalletaId(rs.getInt("galleta_id"));
        alerta.setLoteId(rs.getInt("lote_id"));
        alerta.setTipoAlerta(rs.getString("tipo_alerta"));
        alerta.setDescripcion(rs.getString("descripcion"));
        alerta.setFechaAlerta(rs.getDate("fecha_alerta").toLocalDate());
        alerta.setAtendida(rs.getBoolean("atendida"));

        return alerta;
    }
}
