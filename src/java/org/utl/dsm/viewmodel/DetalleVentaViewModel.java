package org.utl.dsm.viewmodel;

import org.utl.dsm.model.Galleta;
import org.utl.dsm.model.Venta;

public class DetalleVentaViewModel {
    private int id; 
    private Venta venta;               
    private Galleta galleta;             
    private int cantidad;                    
    private float subtotal;

    public DetalleVentaViewModel() {
    }

    public DetalleVentaViewModel(int id, Venta venta, Galleta galleta, int cantidad, float subtotal) {
        this.id = id;
        this.venta = venta;
        this.galleta = galleta;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public DetalleVentaViewModel(Venta venta, Galleta galleta, int cantidad, float subtotal) {
        this.venta = venta;
        this.galleta = galleta;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        sb.append("DetalleVentaViewModel{");
        sb.append("id:").append(id);
        sb.append(", venta:").append(venta);
        sb.append(", galleta:").append(galleta);
        sb.append(", cantidad:").append(cantidad);
        sb.append(", subtotal:").append(subtotal);
        sb.append('}');
        return sb.toString();
    }
}
