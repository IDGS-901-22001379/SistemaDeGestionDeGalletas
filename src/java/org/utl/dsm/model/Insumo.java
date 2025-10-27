package org.utl.dsm.model;

import java.util.Date;

public class Insumo {
    private int id_insumo;
    private String nombreInsumo;
    private String unidad;
    private int cantidad;
    private double total;
    private String descripcion;
    private Date fecha;
    private Proveedor proveedor;

    public Insumo() {
    }

    public Insumo(int id_insumo, String nombreInsumo, String unidad, int cantidad, double total, String descripcion, Date fecha, Proveedor proveedor) {
        this.id_insumo = id_insumo;
        this.nombreInsumo = nombreInsumo;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.total = total;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.proveedor = proveedor;
    }

    public Insumo(String nombreInsumo, String unidad, int cantidad, double total, String descripcion, Date fecha, Proveedor proveedor) {
        this.nombreInsumo = nombreInsumo;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.total = total;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.proveedor = proveedor;
    }

    public int getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(int id_insumo) {
        this.id_insumo = id_insumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        sb.append("Insumo{");
        sb.append("id_insumo:").append(id_insumo);
        sb.append(", nombreInsumo:").append(nombreInsumo);
        sb.append(", unidad:").append(unidad);
        sb.append(", cantidad:").append(cantidad);
        sb.append(", total:").append(total);
        sb.append(", descripcion:").append(descripcion);
        sb.append(", fecha:").append(fecha);
        sb.append(", proveedor:").append(proveedor);
        sb.append('}');
        return sb.toString();
    }

   
}
