package org.utl.dsm.model;

import java.sql.Date;
import java.sql.Time;

public class Galleta {
    private int id_galleta;        
    private String tipo;          
    private String galleta;    
    private float costo;        
    private int existencia;       
    private Date fecha;  
    private Time hora;

    public Galleta() {
    }

    public Galleta(int id_galleta, String tipo, String galleta, float costo, int existencia, Date fecha, Time hora) {
        this.id_galleta = id_galleta;
        this.tipo = tipo;
        this.galleta = galleta;
        this.costo = costo;
        this.existencia = existencia;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Galleta(String tipo, String galleta, float costo, int existencia, Date fecha, Time hora) {
        this.tipo = tipo;
        this.galleta = galleta;
        this.costo = costo;
        this.existencia = existencia;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId_galleta() {
        return id_galleta;
    }

    public void setId_galleta(int id_galleta) {
        this.id_galleta = id_galleta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGalleta() {
        return galleta;
    }

    public void setGalleta(String galleta) {
        this.galleta = galleta;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Galleta{");
        sb.append("id_galleta:").append(id_galleta);
        sb.append(", tipo:").append(tipo);
        sb.append(", galleta:").append(galleta);
        sb.append(", costo:").append(costo);
        sb.append(", existencia:").append(existencia);
        sb.append(", fecha:").append(fecha);
        sb.append(", hora:").append(hora);
        sb.append('}');
        return sb.toString();
    }
    
    
}
