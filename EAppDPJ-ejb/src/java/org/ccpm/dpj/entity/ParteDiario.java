/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author angel
 */
@Entity
@Table(name="partes_diario")
public class ParteDiario implements Serializable {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPago; //fecha pago info que viene en el archivo del banco
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeposito; //fecha deposito banco
    @OneToMany
    private List<Boleta> boletas;
    private String url;
   
    
    private boolean estado;//borrado lógico

    public ParteDiario() {
        this.boletas=new ArrayList<Boleta>();
        
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }
    
    

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    
    
    
    public List<Boleta> getBoletas() {
        return boletas;
    }

    public void setBoletas(List<Boleta> boletas) {
        this.boletas = boletas;
    }

    
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   
    
    

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public boolean isEstado() {
        return estado;
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
        if (!(object instanceof ParteDiario)) {
            return false;
        }
        ParteDiario other = (ParteDiario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.ParteDiario[ id=" + id + " ]";
    }
    
}
