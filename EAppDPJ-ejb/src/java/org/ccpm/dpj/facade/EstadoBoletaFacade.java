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
import org.ccpm.dpj.entity.EstadoBoleta;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class EstadoBoletaFacade extends AbstractFacade<EstadoBoleta> implements EstadoBoletaFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoBoletaFacade() {
        super(EstadoBoleta.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <EstadoBoleta> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from EstadoBoleta as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <EstadoBoleta> findAll(String nombre) {
        return em.createQuery("select object(o) FROM EstadoBoleta as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String nombre) throws Exception {
        try {
            EstadoBoleta estadoAux = new EstadoBoleta();
            estadoAux.setNombre(nombre.toUpperCase());            
            estadoAux.setEstado(true);
            this.create(estadoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el estado");
        }
    }
    
    @Override
    public void remove(Long idEstado) throws Exception {
        try {
            EstadoBoleta estadoAux = this.find(idEstado);
            estadoAux.setEstado(false);
            this.edit(estadoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el estado");
        }
    }
    
    @Override
    public void edit(Long idEstado, String nombre) throws Exception {
        try {
            EstadoBoleta estadoAux = this.find(idEstado);
            estadoAux.setNombre(nombre.toUpperCase());            
            this.edit(estadoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el estado");
        }
    }
    
}
