package org.utl.dsm.model;

import java.time.LocalDate;

/**
 * Modelo para representar una alerta de inventario.
 */
public class AlertasInventario {

    private int idAlerta;                // ID único de la alerta
    private int galletaId;              // ID de la galleta relacionada
    private int loteId;                 // ID del lote relacionado
    private String tipoAlerta;          // Tipo de alerta (VENCIMIENTO, MINIMO, EXCESO)
    private String descripcion;         // Descripción de la alerta
    private LocalDate fechaAlerta;      // Fecha de generación de la alerta
    private boolean atendida;           // Indica si la alerta ha sido atendida

    // Constructor por defecto
    public AlertasInventario() {
    }

    // Constructor con parámetros
    public AlertasInventario(int idAlerta, int galletaId, int loteId, String tipoAlerta, String descripcion, LocalDate fechaAlerta, boolean atendida) {
        this.idAlerta = idAlerta;
        this.galletaId = galletaId;
        this.loteId = loteId;
        this.tipoAlerta = tipoAlerta;
        this.descripcion = descripcion;
        this.fechaAlerta = fechaAlerta;
        this.atendida = atendida;
    }

    // Getters y Setters
    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public int getGalletaId() {
        return galletaId;
    }

    public void setGalletaId(int galletaId) {
        this.galletaId = galletaId;
    }

    public int getLoteId() {
        return loteId;
    }

    public void setLoteId(int loteId) {
        this.loteId = loteId;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(LocalDate fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "AlertasInventario{" +
                "idAlerta=" + idAlerta +
                ", galletaId=" + galletaId +
                ", loteId=" + loteId +
                ", tipoAlerta='" + tipoAlerta + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaAlerta=" + fechaAlerta +
                ", atendida=" + atendida +
                '}';
    }
}

