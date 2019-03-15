/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.ActionItem;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface ActionItemFacadeLocal {

    public void create(ActionItem actionItem);

    public void edit(ActionItem actionItem);

    public void remove(ActionItem actionItem);

    public ActionItem find(Object id);

    public List<ActionItem> findAll();

    public List<ActionItem> findRange(int[] range);

    public int count();

    public List<ActionItem> findAll(boolean estado);

    public List<ActionItem> findAll(String nombreAccion);

    public List<ActionItem> findAll(Long idMenu);

    public List<ActionItem> findAll(List<Long> actionsId);

    public void create(Long idMenuPadre, String nombre) throws Exception;

    public void remove(Long idAccion) throws Exception;

    public void edit(Long idAccion, String nombreAccion) throws Exception;
    
}
