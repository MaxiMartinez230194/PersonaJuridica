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
import org.ccpm.dpj.entity.TipoEntidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class TipoEntidadFacade extends AbstractFacade<TipoEntidad> implements TipoEntidadFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoEntidadFacade() {
        super(TipoEntidad.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoEntidad> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TipoEntidad as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TipoEntidad> findAll(String leyenda) {
        return em.createQuery("select object(o) FROM TipoEntidad as o WHERE o.estado = true AND o.nombre LIKE '" + leyenda + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String leyenda) throws Exception {
        try {
            TipoEntidad tipoEntidadAux = new TipoEntidad();
            tipoEntidadAux.setLeyenda(leyenda.toUpperCase());            
            tipoEntidadAux.setEstado(true);
            this.create(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el tipo de entidad");
        }
    }
    
    @Override
    public void remove(Long idTipoEntidad) throws Exception {
        try {
            TipoEntidad tipoEntidadAux = this.find(idTipoEntidad);
            tipoEntidadAux.setEstado(false);
            this.edit(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el tipo de entidad");
        }
    }
    
    @Override
    public void edit(Long idTipoEntidad, String leyenda) throws Exception {
        try {
            TipoEntidad tipoEntidadAux = this.find(idTipoEntidad);
            tipoEntidadAux.setLeyenda(leyenda.toUpperCase());            
            this.edit(tipoEntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el tipo de entidad");
        }
    }
    
}
