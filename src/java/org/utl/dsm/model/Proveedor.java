package org.utl.dsm.model;

public class Proveedor {    
    private int id_proveedor;
    private String nombreProveedor;

    public Proveedor() {
    }

    public Proveedor(int id_proveedor, String nombreProveedor) {
        this.id_proveedor = id_proveedor;
        this.nombreProveedor = nombreProveedor;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proveedor{");
        sb.append("id_proveedor=").append(id_proveedor);
        sb.append(", nombreProveedor=").append(nombreProveedor);
        sb.append('}');
        return sb.toString();
    }

}