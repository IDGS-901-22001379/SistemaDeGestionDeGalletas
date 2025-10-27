package org.utl.dsm.model;

public class DetalleVenta {
    private int id_detalle_venta; 
    private Venta venta;               
    private Galleta galleta;             
    private int cantidad;                    
    private float subtotal; 

    public DetalleVenta() {
    }

    public DetalleVenta(int id_detalle_venta, Venta venta, Galleta galleta, int cantidad, float subtotal) {
        this.id_detalle_venta = id_detalle_venta;
        this.venta = venta;
        this.galleta = galleta;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public DetalleVenta(Venta venta, Galleta galleta, int cantidad, float subtotal) {
        this.venta = venta;
        this.galleta = galleta;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId_detalle_venta() {
        return id_detalle_venta;
    }

    public void setId_detalle_venta(int id_detalle_venta) {
        this.id_detalle_venta = id_detalle_venta;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Galleta getGalleta() {
        return galleta;
    }

    public void setGalleta(Galleta galleta) {
        this.galleta = galleta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DetalleVenta{");
        sb.append("id_detalle_venta:").append(id_detalle_venta);
        sb.append(", venta:").append(venta);
        sb.append(", galleta:").append(galleta);
        sb.append(", cantidad:").append(cantidad);
        sb.append(", subtotal:").append(subtotal);
        sb.append('}');
        return sb.toString();
    }
}
