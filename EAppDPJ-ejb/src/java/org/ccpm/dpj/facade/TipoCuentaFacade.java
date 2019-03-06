/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.ccpm.dpj.entity.TipoCuenta;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class TipoCuentaFacade extends AbstractFacade<TipoCuenta> implements TipoCuentaFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoCuentaFacade() {
        super(TipoCuenta.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoCuenta> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoCuenta as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoCuenta> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TipoCuenta as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String nombre) throws Exception {
        try {
            TipoCuenta tipoCuentaAux = new TipoCuenta();
            tipoCuentaAux.setNombre(nombre.toUpperCase());            
            tipoCuentaAux.setEstado(true);
            this.create(tipoCuentaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de cuenta");
        }
    }
    
    @Override
    public void remove(Long idTipoCuenta) throws Exception {
        try {
            TipoCuenta tipoCuentaAux = this.find(idTipoCuenta);
            tipoCuentaAux.setEstado(false);
            this.edit(tipoCuentaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de cuenta");
        }
    }
    
    @Override
    public void edit(Long idTipoCuenta, String nombre) throws Exception {
        try {
            TipoCuenta tipoCuentaAux = this.find(idTipoCuenta);
            tipoCuentaAux.setNombre(nombre.toUpperCase());            
            this.edit(tipoCuentaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de cuenta");
        }
    }
    
}
