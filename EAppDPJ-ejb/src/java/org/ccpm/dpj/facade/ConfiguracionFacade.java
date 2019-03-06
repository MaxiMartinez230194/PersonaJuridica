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
import org.ccpm.dpj.entity.Configuracion;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class ConfiguracionFacade extends AbstractFacade<Configuracion> implements ConfiguracionFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionFacade() {
        super(Configuracion.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Configuracion> findAll() {
        Query consulta = em.createQuery("select object(o) from Configuracion as o  where o.id = :p1");
        consulta.setParameter("p1", 1);
        return consulta.getResultList();
    }

    
    
    @Override
    public void create(int cantidad) throws Exception {
        try {
            Configuracion configuracionAux = new Configuracion();
            configuracionAux.setTotalDiasReserva(cantidad);
            
            this.create(configuracionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el configuracion");
        }
    }
    
    
    
    @Override
    public void edit(Long idConfiguracion, int cantidad) throws Exception {
        try {
            Configuracion configuracionAux = this.find(idConfiguracion);
            configuracionAux.setTotalDiasReserva(cantidad);
            this.edit(configuracionAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el configuracion");
        }
    }

    

    @Override
    public void remove(Long idConfiguracion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
