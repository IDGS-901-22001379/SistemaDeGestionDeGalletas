package org.utl.dsm.model;

import java.sql.Date;
import java.sql.Time;

public class Venta {
    private int id_venta;
    private String descripcion;
    private float total;
    private Date fecha;
    private Time hora;
    private String ticket;
    private String tipo_venta;

    public Venta() {
    }

    public Venta(int id_venta, String descripcion, float total, Date fecha, Time hora, String ticket, String tipo_venta) {
        this.id_venta = id_venta;
        this.descripcion = descripcion;
        this.total = total;
        this.fecha = fecha;
        this.hora = hora;
        this.ticket = ticket;
        this.tipo_venta = tipo_venta;
    }

    public Venta(String descripcion, float total, Date fecha, Time hora, String ticket, String tipo_venta) {
        this.descripcion = descripcion;
        this.total = total;
        this.fecha = fecha;
        this.hora = hora;
        this.ticket = ticket;
        this.tipo_venta = tipo_venta;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTipo_venta() {
        return tipo_venta;
    }

    public void setTipo_venta(String tipo_venta) {
        this.tipo_venta = tipo_venta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta{");
        sb.append("id_venta:").append(id_venta);
        sb.append(", descripcion:").append(descripcion);
        sb.append(", total:").append(total);
        sb.append(", fecha:").append(fecha);
        sb.append(", hora:").append(hora);
        sb.append(", ticket:").append(ticket);
        sb.append(", tipo_venta:").append(tipo_venta);
        sb.append('}');
        return sb.toString();
    }
    
    
}
