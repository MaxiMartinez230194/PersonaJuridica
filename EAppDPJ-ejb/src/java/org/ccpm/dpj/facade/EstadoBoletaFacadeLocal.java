/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.EstadoBoleta;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface EstadoBoletaFacadeLocal {

    public void create(EstadoBoleta estadoBoleta);

    public void edit(EstadoBoleta estadoBoleta);

    public void remove(EstadoBoleta estadoBoleta);

    public EstadoBoleta find(Object id);

    public List<EstadoBoleta> findAll();

    public List<EstadoBoleta> findRange(int[] range);

    public int count();

    public List<EstadoBoleta> findAll(boolean estado);

    public List<EstadoBoleta> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void remove(Long idEstado) throws Exception;

    public void edit(Long idEstado, String nombre) throws Exception;
    
}
