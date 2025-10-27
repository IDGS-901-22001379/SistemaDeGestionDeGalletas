package org.utl.dsm.controller;

import org.utl.dsm.cqrs.CQRSProveedor;
//import DAO.proveedorDAO;
import org.utl.dsm.model.Proveedor;

public class ControllerProveedor {

    private CQRSProveedor cqrsProveedores;
//    private proveedorDAO daoProveedores;

    public ControllerProveedor() {
        cqrsProveedores = new CQRSProveedor();
//        daoProveedores = new proveedorDAO();
    }

    // Insertar un proveedor nuevo
    public void insertarProveedor(Proveedor proveedor) throws Exception {
        try {
            cqrsProveedores.agregarProveedor(proveedor); 
            System.out.println("Proveedor insertado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al insertar el proveedor: " + e.getMessage());
            throw e;
        }
    }

}
