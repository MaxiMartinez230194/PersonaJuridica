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
import org.ccpm.dpj.entity.Banco;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class BancoFacade extends AbstractFacade<Banco> implements BancoFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BancoFacade() {
        super(Banco.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Banco> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Banco as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Banco> findAll(String nombre) {
        return em.createQuery("select object(o) FROM Banco as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(String nombre, String domicilio) throws Exception {
        try {
            Banco bancoAux = new Banco();
            bancoAux.setNombre(nombre.toUpperCase());
            bancoAux.setDomicilio(domicilio);
            bancoAux.setEstado(true);
            this.create(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el banco");
        }
    }
    
    @Override
    public void remove(Long idBanco) throws Exception {
        try {
            Banco bancoAux = this.find(idBanco);
            bancoAux.setEstado(false);
            this.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el banco");
        }
    }
    
    @Override
    public void edit(Long idBanco, String nombre, String domicilio) throws Exception {
        try {
            Banco bancoAux = this.find(idBanco);
            bancoAux.setNombre(nombre.toUpperCase());
            bancoAux.setDomicilio(domicilio);
            this.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el banco");
        }
    }
    
}
