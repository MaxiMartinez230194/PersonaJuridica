/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Alvarenga Angel
 */
@Entity
@Table(name="items_boletas")
public class ItemBoleta implements Serializable {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre_tasa")
    private String nombreTasa;
    private Double monto;
    private boolean estado;//borrado lógico

    public ItemBoleta() {
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setNombreTasa(String nombreTasa) {
        this.nombreTasa = nombreTasa;
    }

    public boolean isEstado() {
        return estado;
    }

    public Long getId() {
        return id;
    }

    public Double getMonto() {
        return monto;
    }

    public String getNombreTasa() {
        return nombreTasa;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemBoleta)) {
            return false;
        }
        ItemBoleta other = (ItemBoleta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.ItemBoleta[ id=" + id + " ]";
    }
    
}
