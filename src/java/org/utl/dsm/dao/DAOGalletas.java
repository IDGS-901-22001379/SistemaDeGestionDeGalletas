package org.utl.dsm.dao;

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
import org.utl.dsm.model.Galleta;
import org.utl.dsm.model.InventarioGalleta;

public class DAOGalletas {

    private static final Logger LOGGER = Logger.getLogger(DAOGalletas.class.getName());

    public List<Galleta> getAll() throws SQLException, ClassNotFoundException, IOException {
        List<Galleta> listaGalletas = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();

        // 1. Crear la sentencia SQL
        String query = "SELECT * FROM galletas;";
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
            g.setTipo(rs.getString("tipo"));
            g.setGalleta(rs.getString("galleta"));
            g.setCosto(rs.getFloat("costo"));
            g.setExistencia(rs.getInt("existencia"));
            g.setFecha(rs.getDate("fecha"));
            g.setHora(rs.getTime("hora"));

            listaGalletas.add(g);
        }

        // 6. Cerrar todos los objetos
        rs.close();
        pstmt.close();
        conn.close();
        connMySQL.cerrarConexion(conn);

        // 7. Devolver la información
        return listaGalletas;
    }

    public void disminuirCantidadPorGalleta(String nombreGalleta, int cantidad) throws SQLException, ClassNotFoundException, IOException {
        // Crear una instancia de la conexión
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();

        // Definir la llamada al procedimiento almacenado
        String query = "{CALL disminuirCantidadPorGalleta(?, ?)}";

        // Crear un PreparedStatement para ejecutar el procedimiento
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Establecer los parámetros
        pstmt.setString(1, nombreGalleta);
        pstmt.setInt(2, cantidad);

        // Ejecutar el procedimiento
        pstmt.execute();

        // Cerrar el PreparedStatement y la conexión
        pstmt.close();
        conn.close();
        connMySQL.cerrarConexion(conn);
    }
    // Método para insertar una galleta en la base de datos

    public String insert(Galleta galleta) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        // Validación de datos
        if (galleta.getTipo() == null || galleta.getGalleta() == null || galleta.getFecha() == null || galleta.getHora() == null) {
            throw new Exception("Los valores tipo, galleta, fecha y hora son obligatorios.");
        }
        if (galleta.getCosto() <= 0) {
            throw new Exception("El costo de la galleta debe ser mayor que 0.");
        }
        if (galleta.getExistencia() < 0) {
            throw new Exception("La existencia no puede ser menor que 0.");
        }

        String sql = "INSERT INTO galletas (tipo, galleta, costo, existencia, fecha, hora) VALUES (?, ?, ?, ?, ?, ?)";

        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, galleta.getTipo());
            pstmt.setString(2, galleta.getGalleta());
            pstmt.setFloat(3, galleta.getCosto());
            pstmt.setInt(4, galleta.getExistencia());
            pstmt.setDate(5, galleta.getFecha());
            pstmt.setTime(6, galleta.getHora());

            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            LOGGER.log(Level.INFO, "Galleta insertada correctamente. Filas afectadas: {0}", rowsAffected);
            return "Galleta almacenada correctamente";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al insertar la galleta", e);
            throw new Exception("Error al almacenar la galleta: " + e.getMessage(), e);
        }
    }

    // Método para actualizar una galleta en la base de datos
    public String update(Galleta galleta) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "UPDATE galletas SET tipo = ?, galleta = ?, costo = ?, existencia = ?, fecha = ?, hora = ? WHERE id_galleta = ?";

        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pstmt.setString(1, galleta.getTipo());
            pstmt.setString(2, galleta.getGalleta());
            pstmt.setFloat(3, galleta.getCosto());
            pstmt.setInt(4, galleta.getExistencia());
            pstmt.setDate(5, galleta.getFecha());
            pstmt.setTime(6, galleta.getHora());
            pstmt.setInt(7, galleta.getId_galleta());

            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            LOGGER.log(Level.INFO, "Galleta actualizada correctamente. Filas afectadas: {0}", rowsAffected);
            return "Galleta actualizada correctamente";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar la galleta", e);
            throw new Exception("Error al actualizar la galleta: " + e.getMessage(), e);
        }
    }

    // Método para eliminar una galleta
    public String delete(int idGalleta) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "DELETE FROM galletas WHERE id_galleta = ?";

        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            pstmt.setInt(1, idGalleta);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();

            LOGGER.log(Level.INFO, "Galleta eliminada. Filas afectadas: {0}", rowsAffected);
            return rowsAffected > 0 ? "Galleta eliminada correctamente" : "No se encontró la galleta para eliminar.";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la galleta", e);
            throw new Exception("Error al eliminar la galleta: " + e.getMessage(), e);
        }
    }

    // Método para obtener todas las galletas
    public List<Galleta> getAll_y() throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "SELECT * FROM galletas";
        List<Galleta> listaGalletas = new ArrayList<>();

        try ( PreparedStatement pstmt = conn.prepareStatement(sql);  ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                listaGalletas.add(fill(rs));
            }

            LOGGER.log(Level.INFO, "Número de galletas obtenidas: {0}", listaGalletas.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las galletas: {0}", e.getMessage());
            throw new Exception("Error al obtener la lista de galletas: " + e.getMessage(), e);
        }
        return listaGalletas;
    }

    // Método para obtener una galleta por ID
    public Galleta getById(int idGalleta) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "SELECT * FROM galletas WHERE id_galleta = ?";

        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idGalleta);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return fill(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener la galleta por ID", e);
            throw new Exception("Error al obtener la galleta: " + e.getMessage(), e);
        }
        return null;
    }

    // Método para buscar galletas por nombre
    public List<Galleta> searchByName(String name) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        String sql = "SELECT * FROM galletas WHERE galleta LIKE ?";
        List<Galleta> listaGalletas = new ArrayList<>();

        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    listaGalletas.add(fill(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en searchByName", e);
            throw new Exception("Error al buscar galletas por nombre: " + e.getMessage(), e);
        }
        return listaGalletas;
    }

    // Método para llenar un objeto Galletas a partir de un ResultSet
    private Galleta fill(ResultSet rs) throws SQLException {
        Galleta galleta = new Galleta();
        galleta.setId_galleta(rs.getInt("id_galleta"));
        galleta.setTipo(rs.getString("tipo"));
        galleta.setGalleta(rs.getString("galleta"));
        galleta.setCosto(rs.getFloat("costo"));
        galleta.setExistencia(rs.getInt("existencia"));
        galleta.setFecha(rs.getDate("fecha")); // Convertir a String
        galleta.setHora(rs.getTime("hora"));   // Convertir a String
        return galleta;
    }

    public List<InventarioGalleta> getAllConStock() throws SQLException {
        String query = "SELECT * FROM vista_existencia_galletas";
        List<InventarioGalleta> lista = new ArrayList<>();

        try ( Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            // Depuración: log que muestra que la consulta está siendo ejecutada
            System.out.println("Ejecutando consulta: " + query);

            while (rs.next()) {
                InventarioGalleta inventario = new InventarioGalleta();
                inventario.setId(rs.getInt("id_galleta"));
                inventario.setGalletaId(rs.getInt("id_galleta"));
                inventario.setNombreGalleta(rs.getString("nombre_galleta"));
                inventario.setTipo(rs.getString("tipo"));
                inventario.setRestante(rs.getInt("existencia"));
                inventario.setCosto(rs.getBigDecimal("costo").intValue());  // Asegúrate de convertir correctamente el costo

                // Depuración: muestra los datos que se están obteniendo
                System.out.println("Inventario: " + inventario);

                lista.add(inventario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al obtener datos de la vista_existencia_galletas", e);
        }
        return lista;
    }

    public Galleta actualizarStock(int idGalleta, int cantidad) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.abrirConexion();
        
        String query = "CALL agregarGalletasStock(?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGalleta);
            pstmt.setInt(2, cantidad);
            pstmt.executeUpdate();
            return obtenerPorId(idGalleta);
        }
    }

    public Galleta registrarMerma(int idGalleta, int cantidad, String descripcion) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();
        
        String query = "CALL registrarMermaGalletas(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGalleta);
            pstmt.setInt(2, cantidad);
            pstmt.setString(3, descripcion);
            pstmt.executeUpdate();
            return obtenerPorId(idGalleta);
        }
    }

    public Galleta obtenerPorId(int idGalleta) throws SQLException, ClassNotFoundException {
        ConexionMySQL connMySQL = new ConexionMySQL();  
        Connection conn = connMySQL.abrirConexion();  
        
        String query = "SELECT * FROM galletas WHERE id_galleta = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGalleta);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Galleta galleta = new Galleta();
                    galleta.setId_galleta(rs.getInt("id_galleta"));
                    galleta.setTipo(rs.getString("tipo"));
                    galleta.setGalleta(rs.getString("galleta"));
                    galleta.setCosto(rs.getFloat("costo"));
                    galleta.setExistencia(rs.getInt("existencia"));
                    galleta.setFecha(rs.getDate("fecha"));
                    galleta.setHora(rs.getTime("hora"));
                    return galleta;
                }
            }
        }
        return null;
    }

    private Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
