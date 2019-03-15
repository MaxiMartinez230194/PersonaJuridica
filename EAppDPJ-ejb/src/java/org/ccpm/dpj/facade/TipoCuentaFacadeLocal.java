/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.facade;

import java.util.List;
import javax.ejb.Local;
import org.ccpm.dpj.entity.TipoCuenta;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface TipoCuentaFacadeLocal {

     public void create(TipoCuenta tipoCuenta);

    public void edit(TipoCuenta tipoCuenta);

    public void remove(TipoCuenta tipoCuenta);

    public TipoCuenta find(Object id);

    public List<TipoCuenta> findAll();

    public List<TipoCuenta> findRange(int[] range);

    public int count();

    public List<TipoCuenta> findAll(boolean estado);

    public List<TipoCuenta> findAll(String nombre);

    public void create(String nombre) throws Exception;

    public void remove(Long idTipoCuenta) throws Exception;

    public void edit(Long idTipoCuenta, String nombre) throws Exception;
    
}
