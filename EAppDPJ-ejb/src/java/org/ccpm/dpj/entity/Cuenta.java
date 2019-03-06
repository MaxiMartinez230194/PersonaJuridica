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
@Table(name="cuentas")
public class Cuenta implements Serializable {   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denominacion;
    private String nro;   
    @OneToOne
    private TipoCuenta tipo;
    @ManyToOne
    private Banco banco;
    private boolean estado;//borrado lógico
    
    public Cuenta() {
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Banco getBanco() {
        return banco;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public Long getId() {
        return id;
    }

    public String getNro() {
        return nro;
    }

    public TipoCuenta getTipo() {
        return tipo;
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
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ccpm.dpj.entity.Cuenta[ id=" + id + " ]";
    }
    
}
