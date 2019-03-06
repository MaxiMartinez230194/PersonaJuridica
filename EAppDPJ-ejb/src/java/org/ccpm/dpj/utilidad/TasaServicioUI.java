/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.utilidad;

import org.ccpm.dpj.entity.TasaServicio;

/**
 *
 * @author angel
 */
public class TasaServicioUI {
    private Long id;
    private String nombre;
    private Double monto;
    private boolean select;

    public TasaServicioUI(TasaServicio tasa) {
        this.id= tasa.getId();
        this.nombre = tasa.getNombre();
        this.monto = tasa.getMonto();
        this.select = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Long getId() {
        return id;
    }

    public Double getMonto() {
        return monto;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isSelect() {
        return select;
    }
    
}
