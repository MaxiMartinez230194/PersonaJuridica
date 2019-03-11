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
import org.ccpm.dpj.entity.Leyenda;

@Stateless
public class LeyendaFacade extends AbstractFacade<Leyenda> implements LeyendaFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LeyendaFacade() {
        super(Leyenda.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Leyenda> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Leyenda as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Leyenda findByAnio(String anio){
        Query consulta = em.createQuery("select object(o) from Leyenda as o WHERE o.estado = true and o.anio = :p1");
        consulta.setParameter("p1", anio);
        return (Leyenda) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Leyenda> findAll(String nombre) {
        return em.createQuery("select object(o) FROM Leyenda as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String anio, String nombre) throws Exception {
        try {
            Leyenda leyendaAux = new Leyenda();
            leyendaAux.setAnio(anio);
            leyendaAux.setNombre(nombre);            
            leyendaAux.setEstado(true);
            this.create(leyendaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el estado");
        }
    }
    
    @Override
    public void remove(Long idEstado) throws Exception {
        try {
            Leyenda leyendaAux = this.find(idEstado);
            leyendaAux.setEstado(false);
            this.edit(leyendaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el estado");
        }
    }
    
    @Override
    public void edit(Long idEstado, String anio, String nombre) throws Exception {
        try {
            Leyenda leyendaAux = this.find(idEstado);
            leyendaAux.setAnio(anio);
            leyendaAux.setNombre(nombre);            
            this.edit(leyendaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el estado");
        }
    }
    
}
