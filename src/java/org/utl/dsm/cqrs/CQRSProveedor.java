package org.utl.dsm.cqrs;

import org.utl.dsm.dao.DAOProveedor;
import org.utl.dsm.model.Proveedor;


public class CQRSProveedor {

    private DAOProveedor proveedorDAO;

    public CQRSProveedor() {
        proveedorDAO = new DAOProveedor();
    }

    public void agregarProveedor(Proveedor proveedor) throws Exception {
        validarNombreProveedor(proveedor.getNombreProveedor());
        proveedorDAO.agregarProveedor(proveedor.getNombreProveedor());
    }


    private void validarNombreProveedor(String nombreProveedor) throws Exception {
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            throw new Exception("El nombre del proveedor es obligatorio.");
        }
        if (nombreProveedor.length() < 3 || nombreProveedor.length() > 100) {
            throw new Exception("El nombre del proveedor debe tener entre 3 y 100 caracteres.");
        }
    }
}
