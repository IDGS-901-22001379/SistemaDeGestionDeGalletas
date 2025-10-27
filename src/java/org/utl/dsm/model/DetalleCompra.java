package org.utl.dsm.model;

public class DetalleCompra {
    private int id_detalleCompra;
    private String descripcion;
    private Compra compra;

    public DetalleCompra() {
    }

    public DetalleCompra(int id_detalleCompra, String descripcion, Compra compra) {
        this.id_detalleCompra = id_detalleCompra;
        this.descripcion = descripcion;
        this.compra = compra;
    }

    public int getId_detalleCompra() {
        return id_detalleCompra;
    }

    public void setId_detalleCompra(int id_detalleCompra) {
        this.id_detalleCompra = id_detalleCompra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Detalle{");
        sb.append("id_detalleCompra=").append(id_detalleCompra);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", compra=").append(compra);
        sb.append('}');
        return sb.toString();
    }
    
}