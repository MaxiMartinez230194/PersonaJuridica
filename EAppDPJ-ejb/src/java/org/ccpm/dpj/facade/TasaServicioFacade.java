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
import org.ccpm.dpj.entity.TasaServicio;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class TasaServicioFacade extends AbstractFacade<TasaServicio> implements TasaServicioFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TasaServicioFacade() {
        super(TasaServicio.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TasaServicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from TasaServicio as o WHERE o.estado = :p1 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TasaServicio> findAll(boolean estado, boolean visible) {
        Query consulta = em.createQuery("select object(o) from TasaServicio as o WHERE o.estado = :p1 and o.visible = :p2 ORDER BY o.nombre");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", visible);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <TasaServicio> findAll(String nombre) {
        return em.createQuery("select object(o) FROM TasaServicio as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String nombre, Double monto, boolean visible) throws Exception {
        try {
            TasaServicio tasaAux = new TasaServicio();
            tasaAux.setNombre(nombre.toUpperCase());
            tasaAux.setMonto(monto);
            tasaAux.setEstado(true);
            tasaAux.setVisible(visible);
            this.create(tasaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la tasa de servicio");
        }
    }
    
    @Override
    public void remove(Long idTasaServicio) throws Exception {
        try {
            TasaServicio tasaAux = this.find(idTasaServicio);
            tasaAux.setEstado(false);
            this.edit(tasaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la tasa de servicio");
        }
    }
    
    @Override
    public void edit(Long idTasa, String nombre, Double monto, boolean visible) throws Exception {
        try {
            TasaServicio tasaAux = this.find(idTasa);
            tasaAux.setNombre(nombre.toUpperCase());
            tasaAux.setMonto(monto);
            tasaAux.setVisible(visible);
            this.edit(tasaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la tasa de servicio");
        }
    }
    
}
