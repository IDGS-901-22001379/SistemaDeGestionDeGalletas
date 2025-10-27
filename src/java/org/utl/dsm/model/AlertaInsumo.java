/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

/**
 *
 * @author ascen
 */
public class AlertaInsumo {
    private String nombreInsumo;
    private String mensajeAlerta;

    public AlertaInsumo(String nombreInsumo, String mensajeAlerta) {
        this.nombreInsumo = nombreInsumo;
        this.mensajeAlerta = mensajeAlerta;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public String getMensajeAlerta() {
        return mensajeAlerta;
    }
}
