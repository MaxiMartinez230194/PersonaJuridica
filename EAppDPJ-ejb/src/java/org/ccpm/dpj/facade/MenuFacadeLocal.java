/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.Menu;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface MenuFacadeLocal {

    public void create(Menu menu);

    public void edit(Menu menu);

    public void remove(Menu menu);

    public Menu find(Object id);

    public List<Menu> findAll();

    public List<Menu> findRange(int[] range);

    public int count();

    public List<Menu> findAll(boolean estado);

    public List<Menu> findAll(String nombreMenu);

    public List<Menu> findAll(List<Long> menuId);

    public List<Menu> findAll(Long id);

    public List<Menu> findAllMenuPadre();

    public void remove(Long idMenu) throws Exception;

    public void create(Long idMenuPadre, String nombre) throws Exception;

    public void edit(Long id, Long idSeleccion, String nombre) throws Exception;
    
}
