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
import org.ccpm.dpj.entity.Boleta;
import org.ccpm.dpj.entity.Entidad;
import org.ccpm.dpj.entity.EstadoCertificado;
import org.ccpm.dpj.entity.SolicitudCertificado;


@Stateless
public class SolicitudCertificadoFacade extends AbstractFacade<SolicitudCertificado> implements SolicitudCertificadoFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;
    
    @EJB
    private EntidadFacadeLocal entidadFacade;
    @EJB
    private BoletaFacadeLocal boletaFacade;
    @EJB
    private EstadoCertificadoFacadeLocal estadoCertificadoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SolicitudCertificadoFacade() {
        super(SolicitudCertificado.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <SolicitudCertificado> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from SolicitudCertificado as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <SolicitudCertificado> findAll(String nombre) {
        return em.createQuery("select object(o) FROM SolicitudCertificado as o WHERE o.estado = true AND o.nombre LIKE '" + nombre + "%' ORDER BY o.nombre ").getResultList();
    }
    
    @Override
    public void create(Long idEntidad, Long idEstadoCertificado, Long nroBoleta1, Long nroBoleta2) throws Exception {
        try {
//            SolicitudCertificado solicitudAux = new SolicitudCertificado();
//            Entidad entidadAux = this.entidadFacade.find(idEntidad);
//            EstadoCertificado estadoCertificadoAux = this.estadoCertificadoFacade.find(idEstadoCertificado);
//            Boleta boleta1Aux = this.boletaFacade.findAllIn(true, nroBoleta1);
//            Boleta boleta2Aux = this.boletaFacade.findAllIn(true, nroBoleta2);
//            
//            solicitudAux.setEstado(true);
//            solicitudAux.setFecha(new Date());
//            solicitudAux.setEntidad(entidadAux);
//            solicitudAux.setEstadoCertificado(estadoCertificadoAux);
//            solicitudAux.setBoleta1(boleta1Aux);
//            solicitudAux.setBoleta2(boleta2Aux);
//            this.create(solicitudAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la solicitud");
        }
    }
    
    @Override
    public void solicitar(Long idEntidad, Long nroBoleta1, Long nroBoleta2) throws Exception {
        try {
            SolicitudCertificado solicitudAux = new SolicitudCertificado();
            Entidad entidadAux = this.entidadFacade.find(idEntidad);
            EstadoCertificado estadoCertificadoAux = this.estadoCertificadoFacade.find(1L); //EN ESPERA
            
            solicitudAux.setEstado(true);
            solicitudAux.setFecha(new Date());
            solicitudAux.setEntidad(entidadAux);
            solicitudAux.setEstadoCertificado(estadoCertificadoAux);
            solicitudAux.setNroBoleta1(nroBoleta1);
            solicitudAux.setNroBoleta2(nroBoleta2);
            this.create(solicitudAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la solicitud");
        }
    }
    
    @Override
    public void remove(Long idSolicitud) throws Exception {
        try {
            SolicitudCertificado solicitudAux = this.find(idSolicitud);
            solicitudAux.setEstado(false);
            this.edit(solicitudAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la solicitud");
        }
    }
    
    @Override
    public void edit(Long idSolicitud, Long idEntidad, Long idEstadoCertificado, Long idBoleta1, Long idBoleta2) throws Exception {
        try {
//            SolicitudCertificado solicitudAux = this.find(idSolicitud);
//            Entidad entidadAux = this.entidadFacade.find(idEntidad);
//            EstadoCertificado estadoCertificadoAux = this.estadoCertificadoFacade.find(idEstadoCertificado);
//            Boleta boleta1Aux = this.boletaFacade.find(idBoleta1);
//            Boleta boleta2Aux = this.boletaFacade.find(idBoleta2);
//            solicitudAux.setFecha(new Date());
//            solicitudAux.setEntidad(entidadAux);
//            solicitudAux.setEstadoCertificado(estadoCertificadoAux);
//            solicitudAux.setBoleta1(boleta1Aux);
//            solicitudAux.setBoleta2(boleta2Aux);
//            this.edit(solicitudAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la solicitud");
        }
    }
    
}
