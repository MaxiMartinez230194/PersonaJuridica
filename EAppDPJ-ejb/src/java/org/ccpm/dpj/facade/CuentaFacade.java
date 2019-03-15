/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.ccpm.dpj.entity.Banco;
import org.ccpm.dpj.entity.Cuenta;
import org.ccpm.dpj.entity.TipoCuenta;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class CuentaFacade extends AbstractFacade<Cuenta> implements CuentaFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    @EJB
    private BancoFacadeLocal bancoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CuentaFacade() {
        super(Cuenta.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Cuenta> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Cuenta as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Cuenta> findAll(String denominacion) {
        return em.createQuery("select object(o) FROM Cuenta as o WHERE o.estado = true AND o.denominacion LIKE '" + denominacion + "%' ORDER BY o.denominacion ").getResultList();
    }
    
    @Override
    public void create(String denominacion, String nro, Long idTipoCuenta, 
    Long idBanco) throws Exception {
        try {
            Banco bancoAux = this.bancoFacade.find(idBanco);
            TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuenta);
            Cuenta cuentaAux = new Cuenta();
            cuentaAux.setDenominacion(denominacion.toUpperCase());
            cuentaAux.setNro(nro);
            cuentaAux.setTipo(tipoCuentaAux);
            cuentaAux.setBanco(bancoAux);
            cuentaAux.setEstado(true);
            this.create(cuentaAux);
            bancoAux.getCuentas().add(cuentaAux);
            this.bancoFacade.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la cuenta");
        }
    }
    
    @Override
    public void remove(Long idCuenta) throws Exception {
        try {
            Cuenta cuentaAux = this.find(idCuenta);
            Banco bancoAux = cuentaAux.getBanco();
            cuentaAux.setEstado(false);
            bancoAux.getCuentas().remove(cuentaAux);
            this.edit(cuentaAux);
            this.bancoFacade.edit(bancoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la cuenta");
        }
    }
    
    @Override
    public void edit(Long idCuenta, String denominacion) throws Exception {
        try {
            Cuenta cuentaAux = this.find(idCuenta);
            cuentaAux.setDenominacion(denominacion.toUpperCase());            
            this.edit(cuentaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la cuenta");
        }
    }
    
}
