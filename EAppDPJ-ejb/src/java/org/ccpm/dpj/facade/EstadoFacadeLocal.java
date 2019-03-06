/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Estado;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface EstadoFacadeLocal {

    void create(Estado estado);

    void edit(Estado estado);

    void remove(Estado estado);

    Estado find(Object id);

    List<Estado> findAll();

    List<Estado> findRange(int[] range);

    int count();

    public List<Estado> findAll(boolean estado);

    public List<Estado> findAll(String leyenda);

    public void create(String leyenda) throws Exception;

    public void remove(Long idEstado) throws Exception;

    public void edit(Long idEstado, String leyenda) throws Exception;
    
}
