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
import org.ccpm.dpj.entity.Municipio;

/**
 *
 * @author matias
 */
@Stateless
public class MunicipioFacade extends AbstractFacade<Municipio> implements MunicipioFacadeLocal {

    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MunicipioFacade() {
        super(Municipio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Municipio as o WHERE o.estado = :p1");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(String leyenda) {
        return em.createQuery("select object(o) FROM Municipio as o WHERE o.estado = true AND o.leyenda LIKE '%" + leyenda + "%' ORDER BY o.nombre ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Municipio> findAll(List<Long> municipioId) {
        String iDes = municipioId.toString().substring(1, municipioId.toString().length() - 1);
        return em.createQuery("select object (m) from Municipio as m where m.id in (" + iDes + ")").getResultList();
    }

//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    @Override
//    public List<Municipio> findAll(String leyenda) {
//        StringBuilder query = new StringBuilder();
//        query.append("select object(o) FROM Municipio as o WHERE o.estado = true");
//        if (!(leyenda.equals(""))) {
//            query.append(" and o.leyenda LIKE '").append(leyenda.toUpperCase()).append("%'");
//        }
//        query.append(" ORDER BY o.nombre");
//        Query consulta = em.createQuery(query.toString());
//        return consulta.getResultList();
//    }

    @Override
    public void create(String leyenda) throws Exception {
        try {
            Municipio municipioAux = new Municipio();
            municipioAux.setLeyenda(leyenda.toUpperCase());
            municipioAux.setEstado(true);
            this.create(municipioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el municipio");
        }
    }

    @Override
    public void remove(Long idMunicipio) throws Exception {
        try {
            Municipio municipioAux = this.find(idMunicipio);
            municipioAux.setEstado(false);
            this.edit(municipioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el municipio");
        }
    }

    @Override
    public void edit(Long idMunicipio, String nombre, Integer ur) throws Exception {
        try {
            Municipio municipioAux = this.find(idMunicipio);
            municipioAux.setLeyenda(nombre.toUpperCase());
            this.edit(municipioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el municipio");
        }
    }

}
