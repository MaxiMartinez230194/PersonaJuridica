/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Configuracion;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface ConfiguracionFacadeLocal {

   public void create(Configuracion configuracion);

    public void edit(Configuracion configuracion);

    public void remove(Configuracion configuracion);

    public Configuracion find(Object id);

    public List<Configuracion> findAll();

    public List<Configuracion> findRange(int[] range);

    public int count();

    

   

    public void create(int cantidad) throws Exception;

    public void remove(Long idConfiguracion) throws Exception;

    public void edit(Long idConfiguracion, int cantidad) throws Exception;
    
}
