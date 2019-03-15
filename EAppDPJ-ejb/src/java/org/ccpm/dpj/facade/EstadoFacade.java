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
import org.ccpm.dpj.entity.Estado;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class EstadoFacade extends AbstractFacade<Estado> implements EstadoFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoFacade() {
        super(Estado.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Estado> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Estado as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Estado> findAll(String leyenda) {
        return em.createQuery("select object(o) FROM Estado as o WHERE o.estado = true AND o.nombre LIKE '" + leyenda + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String leyenda) throws Exception {
        try {
            Estado tipoEntidadAux = new Estado();
            tipoEntidadAux.setLeyenda(leyenda.toUpperCase());            
            tipoEntidadAux.setEstado(true);
            this.create(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el estado");
        }
    }
    
    @Override
    public void remove(Long idEstado) throws Exception {
        try {
            Estado tipoEntidadAux = this.find(idEstado);
            tipoEntidadAux.setEstado(false);
            this.edit(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el estado");
        }
    }
    
    @Override
    public void edit(Long idEstado, String leyenda) throws Exception {
        try {
            Estado tipoEntidadAux = this.find(idEstado);
            tipoEntidadAux.setLeyenda(leyenda.toUpperCase());            
            this.edit(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el estado");
        }
    }
    
}
