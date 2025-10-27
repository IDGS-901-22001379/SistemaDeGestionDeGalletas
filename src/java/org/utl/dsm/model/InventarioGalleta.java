package org.utl.dsm.model;

public class InventarioGalleta {
    private int id;
    private int galletaId;
    private String nombreGalleta; // Campo para almacenar el nombre de la galleta
    private int cantidad;
    private int vendido;
    private int restante;
    private String fechaIngreso; // Cambiado de LocalDate a String
    private String fechaVencimiento; // Cambiado de LocalDate a String
    private String tipo; // Agregado para saber si es UNIDAD, MEDIA_CAJA o CAJA
    private int stockTotal; // Campo calculado
    private int costo; // Nuevo campo para almacenar el costo

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGalletaId() {
        return galletaId;
    }

    public void setGalletaId(int galletaId) {
        this.galletaId = galletaId;
    }

    public String getNombreGalleta() {
        return nombreGalleta;
    }

    public void setNombreGalleta(String nombreGalleta) {
        this.nombreGalleta = nombreGalleta;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getVendido() {
        return vendido;
    }

    public void setVendido(int vendido) {
        this.vendido = vendido;
    }

    public int getRestante() {
        return restante;
    }

    public void setRestante(int restante) {
        this.restante = restante;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    // Método toString opcional para depuración
    @Override
    public String toString() {
        return "InventarioGalleta{" +
                "id=" + id +
                ", galletaId=" + galletaId +
                ", nombreGalleta='" + nombreGalleta + '\'' +
                ", cantidad=" + cantidad +
                ", vendido=" + vendido +
                ", restante=" + restante +
                ", fechaIngreso='" + fechaIngreso + '\'' +
                ", fechaVencimiento='" + fechaVencimiento + '\'' +
                ", tipo='" + tipo + '\'' +
                ", stockTotal=" + stockTotal +
                ", costo=" + costo +
                '}';
    }
}
