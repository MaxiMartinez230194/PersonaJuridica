/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="solicitudes")
public class SolicitudCertificado implements Serializable {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    /*ESTAS BOLETAS DEBEN ESTAR PAGADAS PARA EMITIR EL CERTIFICADO*/
    @Column(name="nro_boleta1")
    private Long nroBoleta1; //BOLETA DE TASA CERTIFICACIONES
    @Column(name="nro_boleta2")
    private Long nroBoleta2; //BOLETA DE TASA POR TODO TRAMITE
    @ManyToOne
    private Entidad entidad;
    @OneToOne
    private EstadoCertificado estadoCertificado;
    private boolean estado;//borrado lógico

    public SolicitudCertificado() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }


    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public EstadoCertificado getEstadoCertificado() {
        return estadoCertificado;
    }

    public void setEstadoCertificado(EstadoCertificado estadoCertificado) {
        this.estadoCertificado = estadoCertificado;
    }

    public Long getNroBoleta1() {
        return nroBoleta1;
    }

    public void setNroBoleta1(Long nroBoleta1) {
        this.nroBoleta1 = nroBoleta1;
    }

    public Long getNroBoleta2() {
        return nroBoleta2;
    }

    public void setNroBoleta2(Long nroBoleta2) {
        this.nroBoleta2 = nroBoleta2;
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
        if (!(object instanceof SolicitudCertificado)) {
            return false;
        }
        SolicitudCertificado other = (SolicitudCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.SolicitudCertificado[ id=" + id + " ]";
    }
    
}
