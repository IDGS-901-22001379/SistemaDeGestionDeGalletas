package org.utl.dsm.dao;

import com.mysql.cj.jdbc.CallableStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.utl.dsm.bd.ConexionMySQL;

public class DAOProveedor {
    
    public void agregarProveedor(String nombreProveedor) throws SQLException, ClassNotFoundException, IOException {
    ConexionMySQL connMysql = new ConexionMySQL();
    Connection conn = null;
    CallableStatement cstmt = null;

    try {
        // Abrir conexión
        conn = connMysql.abrirConexion();

        // Llamar al procedimiento almacenado
        String procedimiento = "{CALL InsertarProveedor(?)}";
        cstmt = (CallableStatement) conn.prepareCall(procedimiento);
        cstmt.setString(1, nombreProveedor);

        // Ejecutar el procedimiento almacenado
        cstmt.execute();

    } finally {
        // Cerrar todos los recursos en el bloque finally
        if (cstmt != null) {
            cstmt.close();
        }
        if (conn != null) {
            conn.close();
        }
        connMysql.cerrarConexion(conn);
    }
}

}
