/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author angel
 */
@Entity
@Table(name="configuraciones")
public class Configuracion implements Serializable {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int totalDiasReserva;
    
    

    public Configuracion() {
        
    }

    public int getTotalDiasReserva() {
        return totalDiasReserva;
    }

    public void setTotalDiasReserva(int totalDiasReserva) {
        this.totalDiasReserva = totalDiasReserva;
    }

   
    
    
    


    public void setId(Long id) {
        this.id = id;
    }

    
    public Long getId() {
        return id;
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
        if (!(object instanceof Configuracion)) {
            return false;
        }
        Configuracion other = (Configuracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.Configuracion[ id=" + id + " ]";
    }
    
}

