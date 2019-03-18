/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.ccpm.dpj.entity.Boleta;
import org.ccpm.dpj.entity.EstadoBoleta;
import org.ccpm.dpj.entity.ParteDiario;
import org.ccpm.dpj.entity.SolicitudCertificado;

/**
 *
 * @author Facundo González
 */
@Stateless
public class ParteDiarioFacade extends AbstractFacade<ParteDiario> implements ParteDiarioFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;
    

    @EJB
    private EstadoBoletaFacadeLocal estadoBoletaFacade;
    @EJB
    private BoletaFacadeLocal boletaFacade;
    @EJB
    private SolicitudCertificadoFacadeLocal solicitudFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParteDiarioFacade() {
        super(ParteDiario.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <ParteDiario> findAll(Date fechaDesde, Date fechaHasta) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ParteDiario as o WHERE o.estado = true");        
        
        if(fechaDesde != null && fechaHasta != null){
            query.append(" and o.fechaPago BETWEEN :start AND :end");
        }
                
        //System.out.println("facade boleta--> "+query);
        query.append(" ORDER BY o.fechaDeposito DESC");
        Query consulta = em.createQuery(query.toString());
        if(fechaDesde != null && fechaHasta != null){
            consulta.setParameter("start", fechaDesde, TemporalType.TIMESTAMP);
            consulta.setParameter("end", fechaHasta, TemporalType.TIMESTAMP);
        }
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <ParteDiario> findAll(String url) {
        return em.createQuery("select object(o) FROM ParteDiario as o WHERE o.estado = true AND o.url="+url ).getResultList();
    }
    
    @Override
    public void create(String url, Date fecha, Date fechaPago, Date fechaDeposito) throws Exception {
        try {
            ParteDiario parteDiarioAux = new ParteDiario();
            parteDiarioAux.setUrl(url.toUpperCase());
            parteDiarioAux.setFecha(fecha);
            parteDiarioAux.setFechaPago(fechaPago);
            parteDiarioAux.setFechaDeposito(fechaDeposito);
            parteDiarioAux.setEstado(true);
            this.create(parteDiarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el parteDiario");
        }
    }
    
    @Override
    public void remove(Long idParteDiario) throws Exception {
        try {
            ParteDiario parteDiarioAux = this.find(idParteDiario);
            parteDiarioAux.setEstado(false);
            this.edit(parteDiarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el parteDiario");
        }
    }
    
    @Override
    public void edit(Long idParteDiario, String url, Date fecha) throws Exception {
        try {
            ParteDiario parteDiarioAux = this.find(idParteDiario);
            parteDiarioAux.setUrl(url.toUpperCase());
            parteDiarioAux.setFecha(fecha);
            parteDiarioAux.setUrl(url);
            this.edit(parteDiarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el parteDiario");
        }
    }
    
    
    //cambios sabado 03 de junio 2017
    public Long generarUltimoIdParteDiario() {
        Long nro = 1L;
        try {
            Number result = (Number) em.createQuery("SELECT MAX(b.id) FROM ParteDiario as b").getSingleResult();
            if (result != null) {
                nro = result.longValue() + 1;
            }
        } catch (Exception ex) {
        }
        return nro;
    }
    
    //cambia el estado de pago de las boletas segun el nro de boleta
    @Override
    public String registrarPagosBoletas(Long nroBoleta){
       String error="";
       
       ParteDiario parteDiarioAux = this.find(this.generarUltimoIdParteDiario()-1);
       try {
           EstadoBoleta estadoBoletaAux=estadoBoletaFacade.find(3L);
           //           System.out.println(generarUltimoIdParteDiario());
           
           Boleta boletaAux = boletaFacade.findAllIn(true,nroBoleta);
           
           
           if (boletaAux != null){
                boletaAux.setEstadoBoleta(estadoBoletaAux);
                boletaAux.setParteDiario(parteDiarioAux); // edita la boleta con el ultimo partediario creado
                boletaAux.setFechaPago(parteDiarioAux.getFechaPago());//fecha del ultimo parte diario
                boletaFacade.edit(boletaAux);
                
           }
       } catch (Exception e) {
            
            return "No se encontró la boleta: "+nroBoleta+"\n";
        }
       
               
           return error;
               
        
        }
    
    //cambios miercoles 07 de junio 2017
    
    
    @Override
    public List <ParteDiario> devuelveParteDiariosPorMes(int mes,int anio) {
         
        Query consulta = em.createQuery("select object(o) from ParteDiario as o WHERE o.estado = true AND EXTRACT(MONTH FROM o.fechaPago)=:p1 AND EXTRACT(YEAR FROM o.fechaPago)=:p2 ");
        consulta.setParameter("p1", mes);
        consulta.setParameter("p2", anio);
        return consulta.getResultList();
        
        
    }
    
    @Override
    public boolean existeParteDiario(String url) {
      Long nro = 1L;
        try {
            Number result =  (Number) em.createQuery("select object(o) from ParteDiario as o WHERE o.estado = true AND o.url = '"+url.toUpperCase()+"'" ).getSingleResult();
        if (result != null) {
            System.out.println("laputaqueteparioTRUE");
                return true;
            }
           
        
        } catch (Exception ex) {
            System.out.println("laputaqueteparioFALSE");
            return false;
        }
         
        
       return false;
        
    }
    
    
  
    public boolean existeParteDiarioConFechaDeposito(Date fecha){
       Query consulta = em.createQuery("select object(o) from ParteDiario as o where o.fechaDeposito = :p1");
       consulta.setParameter("p1", fecha);
        
        return !consulta.getResultList().isEmpty();
    }
    
    
    
    
}
