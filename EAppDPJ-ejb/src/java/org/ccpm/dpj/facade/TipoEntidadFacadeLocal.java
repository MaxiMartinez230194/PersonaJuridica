/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.TipoEntidad;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface TipoEntidadFacadeLocal {

    void create(TipoEntidad tipoEntidad);

    void edit(TipoEntidad tipoEntidad);

    void remove(TipoEntidad tipoEntidad);

    TipoEntidad find(Object id);

    List<TipoEntidad> findAll();

    List<TipoEntidad> findRange(int[] range);

    int count();

    public List<TipoEntidad> findAll(boolean estado);

    public List<TipoEntidad> findAll(String leyenda);

    public void create(String leyenda) throws Exception;

    public void remove(Long idTipoEntidad) throws Exception;

    public void edit(Long idTipoEntidad, String leyenda) throws Exception;
    
}
