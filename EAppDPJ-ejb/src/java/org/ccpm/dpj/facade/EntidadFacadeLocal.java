/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Entidad;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface EntidadFacadeLocal {

    public void create(Entidad Entidad);

    public void edit(Entidad Entidad);

    public void remove(Entidad Entidad);

    public Entidad find(Object id);

    public List<Entidad> findAll();

    public List<Entidad> findRange(int[] range);

    public int count();

    public List<Entidad> findAll(boolean estado);

    public List<Entidad> findAll(String nombre, String codigo, Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb, Long idEstadoEntidad, Long idTipoEntidad);

    public void edit(Long idEntidad, String nombre, String codigo, Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb, Long idEstadoEntidad, Date fechaAlta, Long idTipoEntidad, Date fechaBaja,String dni, String nombreReserva) throws Exception;

    public void create(String nombre, String codigo, Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb, Long idEstadoEntidad, Date fechaAlta, Long idTipoEntidad, Date fechaBaja,String dni, String nombreReserva) throws Exception;

    public void remove(Long idEntidad) throws Exception;

    public List<Entidad> findAll(boolean estado, Long tipoEntidad);
    
    public List<Entidad> findByCodigo(String codigo);

    public List <Entidad> findCodigo(String nombre,String codigo,Long idMunicipio, String direccion, String telefono, String correo, String paginaWeb,Long idEstadoEntidad, Long idTipoEntidad);
}