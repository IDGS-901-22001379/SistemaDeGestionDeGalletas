package org.utl.dsm.viewmodel;

import java.sql.Date;
import java.sql.Time;

public class VentaViewModel {
    private int id;
    private String descripcion;
    private float total;
    private Date fecha;
    private Time hora;
    private String ticket;
    private String tipo;

    public VentaViewModel() {
    }

    public VentaViewModel(int id, String descripcion, float total, Date fecha, Time hora, String ticket, String tipo) {
        this.id = id;
        this.descripcion = descripcion;
        this.total = total;
        this.fecha = fecha;
        this.hora = hora;
        this.ticket = ticket;
        this.tipo = tipo;
    }

    public VentaViewModel(String descripcion, float total, Date fecha, Time hora, String ticket, String tipo) {
        this.descripcion = descripcion;
        this.total = total;
        this.fecha = fecha;
        this.hora = hora;
        this.ticket = ticket;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VentaViewModel{");
        sb.append("id:").append(id);
        sb.append(", descripcion:").append(descripcion);
        sb.append(", total:").append(total);
        sb.append(", fecha:").append(fecha);
        sb.append(", hora:").append(hora);
        sb.append(", ticket:").append(ticket);
        sb.append(", tipo:").append(tipo);
        sb.append('}');
        return sb.toString();
    }
    
    
}
