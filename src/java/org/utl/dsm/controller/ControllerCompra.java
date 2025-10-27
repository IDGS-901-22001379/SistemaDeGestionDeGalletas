package org.utl.dsm.controller;

import java.util.ArrayList;
import org.utl.dsm.model.Compra;
import java.util.List;
import org.utl.dsm.cqrs.CQRSCompra;
import org.utl.dsm.dao.DAOCompra;
import org.utl.dsm.model.Insumo;
import org.utl.dsm.model.Proveedor;

public class ControllerCompra {

    private CQRSCompra cqrsCompras;
    private DAOCompra daoCompras;

    public ControllerCompra() {
        cqrsCompras = new CQRSCompra();
        daoCompras = new DAOCompra();
    }

    public List<Compra> obtenerTodasLasCompras() throws Exception {
        try {
            List<Compra> compras = daoCompras.obtenerTodasLasCompras();

            if (compras == null || compras.isEmpty()) {
                throw new Exception("No se encontraron compras.");
            }

            for (Compra compra : compras) {
                if (compra.getProveedor() == null) {
                    throw new Exception("Una de las compras no tiene proveedor.");
                }
                if (compra.getPrecio() <= 0) {
                    throw new Exception("Una de las compras tiene un precio inválido.");
                }
            }

            return compras;

        } catch (Exception e) {
            System.err.println("Error al obtener las compras: " + e.getMessage());
            throw e;
        }
    }
    
    //Select proveedores
    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        try {
            proveedores = daoCompras.getAllProveedores();
        } catch (Exception e) {
            System.err.println("Error al obtener los proveedores: " + e.getMessage());
        }
        return proveedores;
    }

    //Select insumos segun el proveedor
    public List<Insumo> obtenerInsumos(int idProveedor) {
        List<Insumo> insumos = new ArrayList<>();
        try {
            insumos = daoCompras.getInsumosByProveedor(idProveedor);
        } catch (Exception e) {
            System.err.println("Error al obtener los insumos: " + e.getMessage());
        }
        return insumos;
    }
    
    //Insertar la compra
    public int insertarCompra(Compra compra, String insumosJson) throws Exception {
        try {
            return cqrsCompras.agregarCompraConValidacion(compra, insumosJson);
        } catch (Exception e) {
            System.err.println("Error al realizar el la compra: " + e.getMessage());
            throw e;
        }
    }
}
