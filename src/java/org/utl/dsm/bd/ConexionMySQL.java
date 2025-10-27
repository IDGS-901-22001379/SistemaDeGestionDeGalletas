package org.utl.dsm.bd;

import java.sql.Connection;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    String url = "jdbc:mysql://localhost:3306/DonGalleto2";
    String user = "root";
    String password = "mt88xfire";

    public Connection abrirConexion() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection conexion = DriverManager.getConnection(url, user, password);

        return conexion;
    }

    public void cerrarConexion(Connection conexion) throws IOException, SQLException {
        if(conexion!=null){
            conexion.close();
        }   
    }
}
