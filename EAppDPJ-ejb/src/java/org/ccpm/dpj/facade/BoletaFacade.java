/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.ArrayList;
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
import org.ccpm.dpj.entity.ItemBoleta;
import org.ccpm.dpj.entity.ParteDiario;
import org.ccpm.dpj.entity.TasaServicio;
import org.ccpm.dpj.utilidad.TasaServicioUI;
import org.ccpm.dpj.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class BoletaFacade extends AbstractFacade<Boleta> implements BoletaFacadeLocal {

    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @EJB
    private TasaServicioFacadeLocal tasaFacade;
    @EJB
    private EstadoBoletaFacadeLocal estadoBoletaFacade;
    @EJB
    private ItemBoletaFacadeLocal itemBoletaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BoletaFacade() {
        super(Boleta.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Boleta> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Boleta as o WHERE o.estado = :p1 order by o.fechaEmision asc");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Boleta> findAll(Long nroCorrelativo, Date fechaEmDesde, Date fechaEmHasta,
            Date fechaPagoDesde, Date fechaPagoHasta, Long idEstado) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Boleta as o WHERE o.estado = true");
        if (nroCorrelativo != null) {
            query.append(" and o.nroCorrelativo =").append(nroCorrelativo);
        }
        if (fechaEmDesde != null && fechaEmHasta != null) {
            query.append(" and o.fechaEmision BETWEEN :start AND :end");
        }
        if (fechaPagoDesde != null && fechaPagoHasta != null) {
            query.append(" and o.fechaPago BETWEEN :start AND :end");
        }
        if (idEstado != null) {
            query.append(" and o.estadoBoleta.id =").append(idEstado);
        }
        query.append(" ORDER BY o.fechaEmision DESC");
        Query consulta = em.createQuery(query.toString());
        if (fechaEmDesde != null && fechaEmHasta != null) {
            consulta.setParameter("start", fechaEmDesde, TemporalType.TIMESTAMP);
            consulta.setParameter("end", fechaEmHasta, TemporalType.TIMESTAMP);
        }
        if (fechaPagoDesde != null && fechaPagoHasta != null) {
            consulta.setParameter("start", fechaPagoDesde, TemporalType.TIMESTAMP);
            consulta.setParameter("end", fechaPagoHasta, TemporalType.TIMESTAMP);
        }
        //System.out.println("facade boleta--> "+query);
        return consulta.getResultList();
    }

    @Override
    public List<Long> create(List<TasaServicioUI> lstTasaUI) throws Exception {
        Long nro = 1L;  //devuelve una lista los nroCorrelativos de las tasas cambios 01/06/2017
        List<Long> nro2 = new ArrayList<Long>();

        try {

            if (!lstTasaUI.isEmpty()) {
                int i = 0;

                for (TasaServicioUI tasaServicioUIAux : lstTasaUI) {

                    if (tasaServicioUIAux.isSelect()) {
                        Boleta boletaAux = new Boleta();
                        EstadoBoleta estadoBoletaAux = this.estadoBoletaFacade.find(1L);//1L = estado generada ver BD
                        Long nroCorrelAux = this.generarNroCorrelativo();
                        boletaAux.setEstado(true);
                        boletaAux.setFechaEmision(new Date());
                        boletaAux.setTotal(0.0);
                        boletaAux.setNroCorrelativo(nroCorrelAux);
                        boletaAux.setEstadoBoleta(estadoBoletaAux);
                        TasaServicio tasaAux = this.tasaFacade.find(tasaServicioUIAux.getId());
                        ItemBoleta itemBoletaAux = new ItemBoleta();
                        itemBoletaAux.setEstado(true);
                        itemBoletaAux.setNombreTasa(tasaAux.getNombre());
                        itemBoletaAux.setMonto(tasaAux.getMonto());
                        this.itemBoletaFacade.create(itemBoletaAux);
                        boletaAux.getItems().add(itemBoletaAux);
                        boletaAux.setTotal(boletaAux.getTotal() + itemBoletaAux.getMonto());
                        boletaAux.setTotalLetras(Utilidad.convertir(boletaAux.getTotal().toString(), true));
                        this.create(boletaAux);
                        nro = boletaAux.getNroCorrelativo();
                        nro2.add(nro);
                        //System.out.println("listtt---> "+nro2.get(i).toString());

                    }
                }
            }

        } catch (Exception e) {
            System.out.println("errorrrr--> " + e.getMessage());
            throw new Exception("Error al intentar crear la boleta");
        }
        return nro2;
    }
    
    @Override
    public boolean verificarPago(Long nroCorrelativo1, Long nroCorrelativo2) {
    try {
            Query consulta = em.createQuery("select object(o) from Boleta as o WHERE o.estado = true and o.nroCorrelativo = :p1 ");
            Query consulta1 = em.createQuery("select object(o) from Boleta as o WHERE o.estado = true and o.nroCorrelativo = :p2 ");
            consulta.setParameter("p1", nroCorrelativo1);
            consulta.setParameter("p2", nroCorrelativo2);
            Boleta boletaAux = (Boleta) consulta.getSingleResult();
            Boleta boleta1Aux = (Boleta) consulta1.getSingleResult();
            
            return boletaAux.getEstadoBoleta().getId() == 3 && boleta1Aux.getEstadoBoleta().getId() == 3;
        } catch (Exception ex) {
            return false;
        }
    }

    private Long generarNroCorrelativo() {
        Long nro = 1L;
        try {
            Number result = (Number) em.createQuery("SELECT MAX(b.nroCorrelativo) FROM Boleta as b").getSingleResult();
            if (result != null) {
                nro = result.longValue() + 1;
            }
        } catch (Exception ex) {
        }
        return nro;
    }

    //cambios domingo 28 de mayo 2017 by facu
    @Override
    public Boleta findAllIn(boolean estado, Long idNroCorrelativo) {
        try {
            Query consulta = em.createQuery("select object(o) from Boleta as o WHERE o.estado = :p1 and o.nroCorrelativo = :p2 ");
            consulta.setParameter("p1", estado);
            consulta.setParameter("p2", idNroCorrelativo);
            return (Boleta) consulta.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    
    

    //cambios lunes 05 de junio 2017
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ParteDiario> findAllBoletas(Long idParteDiario) {

        return (List<ParteDiario>) em.createQuery("select object(o) FROM Boleta as o WHERE o.estado = true AND o.parteDiario.id= " + idParteDiario).getResultList();
    }


}
