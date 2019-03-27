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
 * @author Alvarenga Angel
 */
@Entity
@Table(name="boletas")
public class Boleta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="fecha_emision")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name="fecha_pago")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Column(name="nro_correlativo")
    private Long nroCorrelativo;
    private Double total;    
    @Column(name="total_letra")
    private String totalLetras;
    @OneToOne
    private EstadoBoleta estadoBoleta;
    @OneToMany
    private List<ItemBoleta> items = new ArrayList<ItemBoleta>();
    
    private boolean estado;//borrado lógico

    public Boleta() {
    }

    
  

    
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setId(Long id) {
        this.id = id;
    }   

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setNroCorrelativo(Long nroCorrelativo) {
        this.nroCorrelativo = nroCorrelativo;
    }

    public void setTotalLetras(String totalLetras) {
        this.totalLetras = totalLetras;
    }

    public void setEstadoBoleta(EstadoBoleta estadoBoleta) {
        this.estadoBoleta = estadoBoleta;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setItems(List<ItemBoleta> items) {
        this.items = items;
    }

    public List<ItemBoleta> getItems() {
        return items;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public EstadoBoleta getEstadoBoleta() {
        return estadoBoleta;
    }

    public String getTotalLetras() {
        return totalLetras;
    }

    public Long getNroCorrelativo() {
        return nroCorrelativo;
    }

    public boolean isEstado() {
        return estado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public Long getId() {
        return id;
    }

    public Double getTotal() {
        return total;
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
        if (!(object instanceof Boleta)) {
            return false;
        }
        Boleta other = (Boleta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.Boleta[ id=" + id + " ]";
    }
    
}
