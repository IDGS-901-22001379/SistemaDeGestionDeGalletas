package org.utl.dsm.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.bd.ConexionMySQL;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.model.Venta;

public class DAOVenta {

    public int insertarVenta(Venta v, List<DetalleVenta> detalleVenta) throws SQLException, IOException, ClassNotFoundException {
        String query = "{call insertarVenta(?,?,?,?,?,?)}";
        ConexionMySQL connMysql = new ConexionMySQL();

        // Convertir la lista detalleVenta a un JSON String
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        // Convertir cada objeto de la lista en un JsonObject
        for (DetalleVenta detalle : detalleVenta) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id_galleta", detalle.getGalleta().getId_galleta());
            jsonObject.addProperty("cantidad", detalle.getCantidad());
            jsonObject.addProperty("subtotal", detalle.getSubtotal());
            jsonArray.add(jsonObject);
        }

        // Convertir la lista a una cadena JSON
        String detallesJson = gson.toJson(jsonArray);

        // Abrir la conexión
        Connection conn = connMysql.abrirConexion();
        CallableStatement cstm = (CallableStatement) conn.prepareCall(query);

        // Llenar todos los parámetros de la llamada al procedimiento
        cstm.setString(1, v.getDescripcion());
        cstm.setFloat(2, v.getTotal());
        cstm.setString(3, v.getTicket());
        cstm.setString(4, v.getTipo_venta());
        cstm.setString(5, detallesJson);  // Pasar el JSON como un String

        // Registrar el parámetro de salida
        cstm.registerOutParameter(6, Types.INTEGER);

        // Ejecutar la consulta
        cstm.execute();

        // Obtener el id_venta generado
        v.setId_venta(cstm.getInt(6));

        // Cerrar recursos
        cstm.close();
        conn.close();
        connMysql.cerrarConexion(conn);

        return v.getId_venta();
    }

    public List<Venta> getAll() throws SQLException, ClassNotFoundException, IOException {
        List<Venta> listaVentas = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();

        // 1. Crear la sentencia SQL
        String query = "SELECT * FROM ventas;";
        // 2. Se establece la conexión con la BD
        Connection conn = connMySQL.abrirConexion();
        // 3. Se genera el statement para enviar la consulta
        PreparedStatement pstmt = conn.prepareStatement(query);
        // 4. Se prepara un ResultSet para obtener la respuesta de la BD
        ResultSet rs = pstmt.executeQuery();
        // 5. Recorrer el rs y extraer los datos
        while (rs.next()) {
            Venta v = new Venta();
            v.setId_venta(rs.getInt("id_venta"));
            v.setDescripcion(rs.getString("descripcion"));
            v.setTotal(rs.getFloat("total"));
            v.setFecha(rs.getDate("fecha"));
            v.setHora(rs.getTime("hora"));
            v.setTicket(rs.getString("ticket"));
            v.setTipo_venta(rs.getString("tipoVenta"));

            listaVentas.add(v);
        }

        // 6. Cerrar todos los objetos
        rs.close();
        pstmt.close();
        conn.close();
        connMySQL.cerrarConexion(conn);

        // 7. Devolver la información
        return listaVentas;
    }
}
