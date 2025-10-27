package org.utl.dsm.model;

import java.util.Date;

public class Compra {
    private int id_comprasRealizadas;
    private int cantidad;
    private float precio;
    private Date fecha;
    private float peso;
    private String numeroOrden;
    private int estatus;
    private Proveedor proveedor;

    public Compra() {
    }

    public Compra(int id_comprasRealizadas, int cantidad, float precio, Date fecha, float peso, String numeroOrden, int estatus, Proveedor proveedor) {
        this.id_comprasRealizadas = id_comprasRealizadas;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fecha = fecha;
        this.peso = peso;
        this.numeroOrden = numeroOrden;
        this.estatus = estatus;
        this.proveedor = proveedor;
    }

    public int getId_comprasRealizadas() {
        return id_comprasRealizadas;
    }

    public void setId_comprasRealizadas(int id_comprasRealizadas) {
        this.id_comprasRealizadas = id_comprasRealizadas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compra{");
        sb.append("id_comprasRealizadas=").append(id_comprasRealizadas);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", precio=").append(precio);
        sb.append(", fecha=").append(fecha);
        sb.append(", peso=").append(peso);
        sb.append(", numeroOrden=").append(numeroOrden);
        sb.append(", estatus=").append(estatus);
        sb.append(", proveedor=").append(proveedor);
        sb.append('}');
        return sb.toString();
    }
    
}