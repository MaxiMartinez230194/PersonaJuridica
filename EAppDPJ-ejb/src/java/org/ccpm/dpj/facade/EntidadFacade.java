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
import org.ccpm.dpj.entity.Entidad;
import org.ccpm.dpj.entity.Estado;
import org.ccpm.dpj.entity.Municipio;
import org.ccpm.dpj.entity.TipoEntidad;
import org.ccpm.dpj.entity.Configuracion;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class EntidadFacade extends AbstractFacade<Entidad> implements EntidadFacadeLocal {
    @PersistenceContext(unitName = "EAppDPJ-ejbPU")
    private EntityManager em;

    @EJB
    private MunicipioFacadeLocal municipioFacade;
    @EJB
    private EstadoFacadeLocal estadoEntidad;
    @EJB
    private TipoEntidadFacadeLocal tipoEntidad;
    @EJB
    private ConfiguracionFacadeLocal configuracionFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EntidadFacade() {
        super(Entidad.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Entidad> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Entidad as o WHERE o.estado = :p1 ORDER BY o.id desc");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Entidad> findAll(boolean estado, Long tipoEntidad) {
        Query consulta = em.createQuery("select object(o) from Entidad as o WHERE o.estado = :p1 AND o.tipoEntidad.id= :p2 ORDER BY o.id desc");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", tipoEntidad);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List <Entidad> findAll(String nombre,String codigo,Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb,Long idEstadoEntidad, Long idTipoEntidad) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Entidad as o WHERE o.estado = true");
        
        if (!(nombre.equals(""))) {
           query.append(" and o.nombre LIKE '%").append(nombre.toUpperCase()).append("%'");
        } 
        
        if (!(codigo.equals(""))) {
           query.append(" and o.codigo LIKE '%").append(codigo.toUpperCase()).append("%'");
        } 
        
        if (idMunicipio!=0L) {
            query.append(" AND o.municipio.id =").append(idMunicipio);
        }
        
        if (!(direccion.equals(""))) {
           query.append(" and o.direccion LIKE '%").append(direccion.toUpperCase()).append("%'");
        }
        
        if (!(telefono.equals(""))) {
           query.append(" and o.telefono LIKE '%").append(telefono).append("%'");
        }
        
        if (!(correo.equals(""))) {
           query.append(" and o.correo LIKE '%").append(correo).append("%'");
        }
        
        if (!(paginaWeb.equals(""))) {
           query.append(" and o.paginaWeb LIKE '%").append(paginaWeb).append("%'");
        }
        
        if (idEstadoEntidad!=0L) {
            query.append(" AND o.estadoEntidad.id =").append(idEstadoEntidad);
        }
        if (idTipoEntidad!=0L) {
            query.append(" AND o.tipoEntidad.id =").append(idTipoEntidad);
        }
        
        query.append( " ORDER BY o.id desc");
        //System.out.println("facadeEntidad---"+query);
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
        
        
    }
   
    @Override
    public void edit(Long idEntidad, String nombre,String codigo,Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb,Long idEstadoEntidad, Date fechaAlta, Long idTipoEntidad, Date fechaBaja, String dni, String nombreReserva) throws Exception {
        try {
            Estado estadoEntidadAux = this.estadoEntidad.find(idEstadoEntidad);
            TipoEntidad tipoEntidadAux = this.tipoEntidad.find(idTipoEntidad);
            Municipio municipioAux = this.municipioFacade.find(idMunicipio);
            Configuracion configuracionAux = this.configuracionFacade.find(1L);
            Entidad EntidadAux = this.find(idEntidad);
            EntidadAux.setNombre(nombre.toUpperCase());
            EntidadAux.setCodigo(codigo.toUpperCase());
            EntidadAux.setMunicipio(municipioAux);
            EntidadAux.setDireccion(direccion.toUpperCase());
            EntidadAux.setTelefono(telefono);
            EntidadAux.setCorreo(correo);
            EntidadAux.setPaginaWeb(paginaWeb);
            EntidadAux.setFechaAlta(fechaAlta);
            EntidadAux.setFechaBaja(fechaBaja);
            EntidadAux.setDni(dni);
            EntidadAux.setNombreReserva(nombreReserva);
            EntidadAux.setEstadoEntidad(estadoEntidadAux);
            EntidadAux.setConfiguracion(configuracionAux);
            EntidadAux.setTipoEntidad(tipoEntidadAux);
            this.edit(EntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la Entidad");
        }
    }

    @Override
    public void create(String nombre,String codigo,Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb,Long idEstadoEntidad, Date fechaAlta, Long idTipoEntidad, Date fechaBaja,String dni, String nombreReserva) throws Exception {
        try {
            Estado estadoEntidadAux = this.estadoEntidad.find(idEstadoEntidad);
            TipoEntidad tipoEntidadAux = this.tipoEntidad.find(idTipoEntidad);
            Municipio municipioAux = this.municipioFacade.find(idMunicipio);
            Configuracion configuracionAux = this.configuracionFacade.find(1L);
            Entidad EntidadAux = new Entidad();
            EntidadAux.setNombre(nombre.toUpperCase());
            EntidadAux.setCodigo(codigo.toUpperCase());
            EntidadAux.setMunicipio(municipioAux);
            EntidadAux.setDireccion(direccion.toUpperCase());
            EntidadAux.setTelefono(telefono);
            EntidadAux.setCorreo(correo);
            EntidadAux.setFechaAlta(fechaAlta);
            EntidadAux.setPaginaWeb(paginaWeb);
            EntidadAux.setFechaBaja(fechaBaja);
            EntidadAux.setDni(dni);
            EntidadAux.setNombreReserva(nombreReserva.toUpperCase());
            EntidadAux.setEstadoEntidad(estadoEntidadAux);
            EntidadAux.setTipoEntidad(tipoEntidadAux);
            EntidadAux.setConfiguracion(configuracionAux);
            EntidadAux.setEstado(true);
            this.create(EntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la Entidad");
        }
    }
    
    @Override
    public void remove(Long idEntidad) throws Exception {
        try {
            Entidad EntidadAux = this.find(idEntidad);
            EntidadAux.setEstado(false);
            this.edit(EntidadAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la Entidad");
        }
    }
    
    
    public List <Entidad> findCodigo(String nombre,String codigo,Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb,Long idEstadoEntidad, Long idTipoEntidad) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Entidad as o WHERE o.estado = true");
        
        if (!(nombre.equals(""))) {
           query.append(" and o.nombre LIKE '%").append(nombre.toUpperCase()).append("%'");
        } 
        
        if (!(codigo.equals(""))) {
           query.append(" and o.codigo LIKE '").append(codigo.toUpperCase()).append("'");
        } 
        
        if (idMunicipio!=0L) {
            query.append(" AND o.municipio.id =").append(idMunicipio);
        }
        
        if (!(direccion.equals(""))) {
           query.append(" and o.direccion LIKE '%").append(direccion.toUpperCase()).append("%'");
        }
        
        if (!(telefono.equals(""))) {
           query.append(" and o.telefono LIKE '%").append(telefono).append("%'");
        }
        
        if (!(correo.equals(""))) {
           query.append(" and o.correo LIKE '%").append(correo).append("%'");
        }
        
        if (!(paginaWeb.equals(""))) {
           query.append(" and o.paginaWeb LIKE '%").append(paginaWeb).append("%'");
        }
        
        if (idEstadoEntidad!=0L) {
            query.append(" AND o.estadoEntidad.id =").append(idEstadoEntidad);
        }
        if (idTipoEntidad!=0L) {
            query.append(" AND o.tipoEntidad.id =").append(idTipoEntidad);
        }
        
        query.append( " ORDER BY o.id desc");
        //System.out.println("facadeEntidad---"+query);
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
        
        
    }
    
}
