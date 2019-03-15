/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.ItemBoleta;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface ItemBoletaFacadeLocal {

    void create(ItemBoleta itemBoleta);

    void edit(ItemBoleta itemBoleta);

    void remove(ItemBoleta itemBoleta);

    ItemBoleta find(Object id);

    List<ItemBoleta> findAll();

    List<ItemBoleta> findRange(int[] range);

    int count();
    
}
